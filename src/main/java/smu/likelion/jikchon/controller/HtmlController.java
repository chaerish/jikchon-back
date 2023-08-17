package smu.likelion.jikchon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HtmlController {

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/cart")
    public String getCart() {
        return "cart";
    }

    @GetMapping("/interest-product")
    public String getInterestProduct() {
        return "interest-product";
    }

    @GetMapping
    public String getMainHome() {
        return "main-home1";
    }

    @GetMapping("/product")
    public String getProduct() {
        return "main-home2";
    }

    @GetMapping("/customer/checkorder")
    public String getCustomerCheckOrder() {
        return "mypage_con_checkOrder";
    }

    @GetMapping("/customer/recommend")
    public String grtCustomerRecommend() {
        return "mypage_con_recommend";
    }

    @GetMapping("/mypage/customer")
    public String getCustomerMypage() {
        return "mypage_customer";
    }

    @GetMapping("/mypage/seller")
    public String getSellerMypage() {
        return "mypage_seller";
    }

    @GetMapping("/seller/checkorder")
    public String getSellerCheckorder() {
        return "mypage_sell_checkOrder";
    }

    @GetMapping("/seller/enrollItem")
    public String getSellerEnrollItem() {
        return "mypage_sell_enrollItem";
    }


    @GetMapping("/payment/completed")
    public String getPaymentCompleted() {
        return "payment-completed";
    }

    @GetMapping("/product/info")
    public String getProductInfo() {
        return "product-info";
    }

    @GetMapping("/product/manage")
    public String getProductManage() {
        return "product-manage";
    }

    @GetMapping("/signup/customer")
    public String getRegisterCustomer() {
        return "register_customer";
    }

    @GetMapping("/signup/edit/customer")
    public String getRegisterEditCustomer() {
        return "register_edit_customer";
    }

    @GetMapping("/signup/edit/seller")
    public String getRegisterEditSeller() {
        return "register_edit_seller";
    }

    @GetMapping("/signup/seller")
    public String getRegisterSeller() {
        return "register_seller";
    }

    @GetMapping("/receipt/customer")
    public String getShopReceiptCustomer() {
        return "shop_receipt_customer";
    }

    @GetMapping("/receipt/seller")
    public String getShopReceiptSeller() {
        return "shop_receipt_seller";
    }
}
