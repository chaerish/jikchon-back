import { checkTokenValid, checkTokenExistence, checkUserRole } from './common/jwt_token_check.js';

let fetchdata = []; // Initialize fetchdata array
let pageNum = 0;
let initSum = 0;

/* Header 설정 */
const token = localStorage.getItem('access_token');
var myHeaders = new Headers();
myHeaders.append('Authorization', 'Bearer ' + token);
myHeaders.append('Content-Type', 'application/json');

function loadCartData() {
    if (checkTokenExistence()){
        checkTokenValid();
        let url = `/members/cart?page=${pageNum}`;
        /* 통신용 코드 */
        fetch(url, {
            headers: myHeaders,
            method: "GET"
        })
            .then((response) => response.json())
            .then((data) => {
                let data1 = data.data;
                fetchdata = data.data.itemList;
                renderCartData(data1.itemList);
            })
            .catch((error) => {
                console.error('An error occurred while loading store data:', error);
            });
    }
    else {
        window.alert("로그인이 필요한 서비스입니다. 로그인 페이지로 이동합니다!");
        window.location.href = '/login';
    }    
}


/* 통신 data로 cart rendering */
function renderCartData(data) {
    const productList = document.getElementById("cart-li");
    data.forEach(product => {
        const li = document.createElement("li");

        li.innerHTML = `
            <img src="${product.imageUrl}" alt="" class="prod-img">
            <div class="cart-component">
                <div class="row-dir-box">
                    <p class="brands">${product.storeName}</p>
                    <div class="amount-box">
                        <button class="quantity-down-btn"">-</button>
                        <input type="text" class="quantity-input" value="1">
                        <button class="quantity-up-btn"">+</button>
                    </div>
                </div>
                <p class="prod-name">${product.productName}</p>
                <p class="address">${product.address}</p>
                <p class="price">${product.price}</p>
            </div>
        `;
        productList.appendChild(li);
        initSum += product.price;
    });
}

let liIndex;

function getCartListIndex() {
    const cartList = document.getElementById("cart-li");
    const amountBox = document.querySelectorAll(".amount-box");

    amountBox.forEach(btn => {
        btn.addEventListener("click", function (event) {
            const clickedLi = event.target.closest("li");
            if (clickedLi) {
                // 클릭한 li 요소의 인덱스를 찾아서 사용
                liIndex = Array.from(cartList.children).indexOf(clickedLi);
                console.log(liIndex);
            }
        });
    })
}

function decreaseQuantity() {
    console.log("down");
    var selectComp = `#cart-li li:nth-child(${liIndex + 1}) .quantity-input`

    const quantityInput = document.querySelector(selectComp);
    const currentValue = parseInt(quantityInput.value);
    const totalPrice =document.querySelector(".total-price-box input");

    if (currentValue > 1) {
        quantityInput.value = currentValue - 1;
        initSum -= fetchdata[liIndex].price;
        totalPrice.value = initSum;
    }
}

const decreaseButton = document.querySelectorAll(".quantity-down-btn");
decreaseButton.forEach(product => {
    product.addEventListener("click", decreaseQuantity);
})



function increaseQuantity() {
    console.log("up");
    var selectComp = `#cart-li li:nth-child(${liIndex + 1}) .quantity-input`

    const quantityInput = document.querySelector(selectComp);
    const currentValue = parseInt(quantityInput.value);
    const totalPrice =document.querySelector(".total-price-box input");

    quantityInput.value = currentValue + 1;
    initSum += fetchdata[liIndex].price;
    totalPrice.value = initSum;
}

const increaseButton = document.querySelectorAll(".quantity-up-btn");
increaseButton.forEach(product => {
    product.addEventListener("click", increaseQuantity);
})

let formData = { 
    cartList: []
}

function payCart() {
    const cartBtn = document.querySelector(".cart-btn");
    cartBtn.addEventListener("click", () => {
        const cartItems = document.querySelectorAll("#cart-li li");
        const postUrl = "/purchases/cart";

        cartItems.forEach((item, index) => {
            const quantityInput = item.querySelector(".quantity-input");
            const quantityValue = quantityInput.value;
            let cart = {
                id: fetchdata[index].id,
                quantity: quantityValue
            }
            formData.cartList.push(cart);
            // formData.append(`item[${index}][quantity]`, quantityValue);
            // formData.append(`item[${index}][quantity]`, quantityValue);
        });

        fetch(postUrl, {
            method: "POST",
            headers: myHeaders,
            body: JSON.stringify(formData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("관심카테고리 선택 실패");
                }
                return response.json();
            })
            .then(data => {
                console.log("관심카테고리 선택 성공.", data);
            })
            .catch(error => {
                console.error(error);
            });
    })

}

function returnMainHome() {
    const buyBtn = document.querySelector(".buy-btn");

    buyBtn.addEventListener("click", () => {
        window.location.href = "../html/main-home1.html";
    })
}

window.onload = function main() {
    loadCartData();
    // renderCartData();
    payCart();
    getCartListIndex()
    returnMainHome();
}

