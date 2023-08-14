package smu.likelion.jikchon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smu.likelion.jikchon.base.PageResult;
import smu.likelion.jikchon.domain.Cart;
import smu.likelion.jikchon.domain.Order;
import smu.likelion.jikchon.domain.Product;
import smu.likelion.jikchon.domain.Purchase;
import smu.likelion.jikchon.domain.member.Member;
import smu.likelion.jikchon.dto.order.OrderRequestDto;
import smu.likelion.jikchon.dto.order.OrderResponseDto;
import smu.likelion.jikchon.dto.purchase.PurchaseRequestDto;
import smu.likelion.jikchon.exception.CustomNotFoundException;
import smu.likelion.jikchon.exception.ErrorCode;
import smu.likelion.jikchon.repository.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final LoginService loginService;

    private final PurchaseRepository purchaseRepository;
    private final CartRepository cartRepository;

    public OrderResponseDto.Simple purchaseProduct(PurchaseRequestDto purchaseRequestDto) {
        return OrderResponseDto.Simple.of(createOrder(Collections.singletonList(purchaseRequestDto)));
    }

    @Transactional
    public OrderResponseDto.Simple purchaseCart(OrderRequestDto.CartOrder orderRequestDto) {
        Map<Long, Cart> cartMap = cartRepository.findAllById(
                        orderRequestDto.getCartList().stream().map(PurchaseRequestDto::getId).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(Cart::getId, cart -> cart));

        List<PurchaseRequestDto> cartOrderList = new ArrayList<>();

        for (PurchaseRequestDto purchaseRequestDto : orderRequestDto.getCartList()) {
            Cart cart = cartMap.get(purchaseRequestDto.getId());
            if (cart == null) {
                throw new CustomNotFoundException(ErrorCode.NOT_FOUND_CART);
            }

            cartOrderList.add(PurchaseRequestDto.builder()
                    .id(cart.getProduct().getId())
                    .quantity(purchaseRequestDto.getQuantity())
                    .build());

            cartRepository.delete(cart);
        }

        return OrderResponseDto.Simple.of(createOrder(cartOrderList));
    }

    @Transactional
    public Order createOrder(List<PurchaseRequestDto> purchaseRequestDtoList) {
        Order order = Order.builder()
                .member(Member.builder().id(loginService.getLoginMemberId()).build())
                .build();

        orderRepository.save(order);

        for (PurchaseRequestDto purchaseRequestDto : purchaseRequestDtoList) {
            Product product = productRepository.findById(purchaseRequestDto.getId()).orElseThrow(() -> {
                throw new CustomNotFoundException(ErrorCode.NOT_FOUND);
            });

            Purchase purchase = Purchase.builder()
                    .member(Member.builder().id(loginService.getLoginMemberId()).build())
                    .order(order)
                    .product(product)
                    .price(product.getPrice())
                    .quantity(purchaseRequestDto.getQuantity())
                    .build();

            purchaseRepository.save(purchase);

            product.reduceQuantity(purchaseRequestDto.getQuantity());
        }

        return order;
    }

    @Transactional(readOnly = true)
    public PageResult<OrderResponseDto.BriefForCustomer> getMyOrderList(Pageable pageable) {
        return PageResult.ok(
                orderRepository.findAllByMemberId(loginService.getLoginMemberId(), pageable)
                        .map(OrderResponseDto.BriefForCustomer::of));
    }
}
