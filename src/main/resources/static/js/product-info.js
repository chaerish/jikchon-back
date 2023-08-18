import { checkTokenValid, checkTokenExistence } from './common/jwt_token_check.js';

let fetchData = [];
let price;

/* Header 설정 */
const token = localStorage.getItem('access_token');
var myHeaders = new Headers();
myHeaders.append('Content-Type', 'application/json');

// prod id 받아오기
const urlParams = new URLSearchParams(window.location.search);
const productId = urlParams.get('id');
console.log(productId);

function loadProdData() {
    var url = `/products/${productId}`
    /* 통신용 코드 */
    fetch(url, {
        method: "GET",
        headers: myHeaders,
    })
        .then((response) => response.json())
        .then((data) => {
            let data1 = data.data;
            renderProdData(data1);
            price = data1.price;
        })
        .catch((error) => {
            console.error('An error occurred while loading store data:', error);
        });
}

function renderProdData(data) {
    // prod-info 섹션 요소를 가져옴
    const prodInfoSection = document.getElementById("prod-info");

    // 데이터를 사용하여 섹션 내부의 내용을 변경
    const imgElement = prodInfoSection.querySelector(".prod-img");
    imgElement.src = data.imageUrl;

    const brandElement = prodInfoSection.querySelector(".brands");
    brandElement.textContent = data.storeName;

    const productName1Element = prodInfoSection.querySelector(".prod-name1");
    productName1Element.textContent = data.productName;

    const addressElement = prodInfoSection.querySelector(".address");
    addressElement.textContent = data.address;

    const priceElement = prodInfoSection.querySelector(".price");
    priceElement.textContent = data.price;

    const addCartSection = document.querySelector(".add-cart");

    const productName2Element = addCartSection.querySelector(".prod-name2");
    productName2Element.textContent = data.productName;

    const initSumPrice = addCartSection.querySelector(".sum-price");
    initSumPrice.textContent = data.price;

}

function decreaseQuantity() {
    const downBtn = document.getElementById("decrease");

    downBtn.addEventListener("click", () => {
        const quantityInput = document.querySelector(".quantity-input");
        const currentValue = parseInt(quantityInput.value);
        let sumPrice = document.querySelector(".sum-price");
    
        if (currentValue > 1) {
            quantityInput.value = currentValue - 1;
        }
    
        let sum = price * quantityInput.value;
        sumPrice.textContent = sum;
    })

}

function increaseQuantity() {
    const upBtn = document.getElementById("increase");
    
    upBtn.addEventListener("click", () => {
        const quantityInput = document.querySelector(".quantity-input");
        const currentValue = parseInt(quantityInput.value);
        let sumPrice = document.querySelector(".sum-price");
        
        quantityInput.value = currentValue + 1;
        let sum = price * quantityInput.value;
        sumPrice.textContent = sum;
    })
}


var postHeaders = new Headers();
var teadbear = `Bearer ${token}`
postHeaders.append('Content-Type', 'application/json');
postHeaders.append('Authorization', teadbear);


function buy_postFormData() {
    const buyBtn = document.querySelector(".buy-btn");
    buyBtn.addEventListener("click", () => {
        if (checkTokenExistence()) {
            checkTokenValid();
            // var teadbear = `Bearer ${token}`
            console.log(teadbear);
            const quantityInput = document.querySelector(".quantity-input");
            var postUrl = "/purchases";

            var formData = {
                id: productId,
                quantity: quantityInput.value
            };

            /* 통신용 코드 */
            fetch(postUrl, {
                method: "POST",
                headers: postHeaders,
                body: JSON.stringify(formData)
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("구매 요청이 실패하였습니다.");
                    }
                    else {
                        return response.json();
                    }
                })
                .then(data => {
                    console.log("구매 요청이 성공적으로 전송되었습니다.", data);
                    window.location.href = `../customer/checkorder?id=${quantityInput.value}`;
                })
                .catch(error => {
                    console.error(error);
                });
        }
        else {
            alert("로그인 후 구매해 주세요!");
            window.location.href = "../login";
        }
    })
}

function cart_postFormData() {
    const cartBtn = document.querySelector(".cart-btn");
    cartBtn.addEventListener("click", () => {
        if (checkTokenExistence()) {
            checkTokenValid();
            // var teadbear = `Bearer ${token}`
            console.log(teadbear);
            var postUrl = `/products/${productId}/cart`
            /* 통신용 코드 */
            fetch(postUrl, {
                method: "POST",
                headers: postHeaders,
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("장바구니 추가 요청이 실패하였습니다.");
                    }
                    else {
                        return response.json();
                    }
                })
                .then(data => {
                    console.log("요청이 성공적으로 전송되었습니다.");
                    alert("장바구니에 상품이 성공적으로 담겼습니다!");
                })
                .catch(error => {
                    console.error(error);
                });
        }
        else {
            alert("로그인 후 구매해 주세요!");
            window.location.href = "../login";
        }
    })
}

window.onload = function main() {
    loadProdData();
    // renderProdData(fetchData);
    buy_postFormData();
    cart_postFormData();
    increaseQuantity();
    decreaseQuantity();
}