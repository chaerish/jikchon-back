import { checkTokenValid, checkTokenExistence } from './common/jwt_token_check.js';

let fetchData = [];
// url 부분 백엔드에 질문
var url;

/* Header 정보 */
var myHeaders = new Headers();
myHeaders.append('Content-Type', 'application/json');

function receiptDataLoad() {
    fetch(url, {
        headers: myHeaders,
    })
        .then((response) => response.json())
        .then((data) => {
            let data1 = data.data;
            console.log(data1);
            fetchData = data1;
        })
        .catch((error) => {
            console.error('An error occurred while loading store data:', error);
        });
}

function renderCartData(data) {
    const itemList = data.itemList;

    const receiptContainer = document.getElementById('receipt-container');
    const totalPriceElement = document.getElementById('totalPrice');
    let totalPrice = 0;

    itemList.forEach(product, index => {
        const productDiv = document.createElement('div');
        productDiv.className = 'product';
    
        const productName = document.createElement('p');
        productName.className = 'product-name';
        productName.textContent = index+1 + product.productName;
    
        const productQuantity = document.createElement('p');
        productQuantity.className = 'product-quantity';
        productQuantity.textContent = product.quantity;
    
        const productPrice = document.createElement('p');
        productPrice.className = 'product-price';
        productPrice.textContent = product.price;
    
        productDiv.appendChild(productName);
        productDiv.appendChild(productQuantity);
        productDiv.appendChild(productPrice);
    
        receiptContainer.appendChild(productDiv);
    
        // 결제 금액 누적 계산
        const priceNumber = parseInt(product.price.replace(',', ''));
        totalPrice += priceNumber * product.quantity;
    });
    totalPriceElement.textContent = totalPrice.toLocaleString() + '원';    
}

function backToHome() {
    window.location.href = "../html/main-home1.html";
}