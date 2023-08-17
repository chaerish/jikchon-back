import { checkTokenValid, checkTokenExistence } from './common/jwt_token_check.js';

let fetchdata = []; // Initialize fetchdata array
var url = "/home/products"

/* Header 설정 */
const token = localStorage.getItem('access_token');
var myHeaders = new Headers();
myHeaders.append('Authorization', 'Bearer ' + token);
myHeaders.append('Content-Type', 'application/json');

// 추천 상품 load
function loadRecommendList() {
    if (checkTokenExistence()) {
        const head = document.querySelector(".today-recommend");
        fetch(url, {
            headers: myHeaders,
            method: 'GET'
        })
            .then((response) => response.json())
            .then((data) => {
                let data1 = data.data;
                fetchdata = data1;
            })
            .catch((error) => {
                console.error('An error occurred while loading store data:', error);
            });

        renderRecommendList(fetchdata);
    }
    else {
        const recommendList = document.querySelector(".recommend-list");
        recommendList.innerHTML = `
            <div class="login-text">
                <p id="login-intend">로그인 하고 맞춤 상품 추천 받기!</p>
                <a href="../html/login.html"><button>로그인</button></a>
            </div>
        `
    }
}

// 추천 상품 render
function renderRecommendList(products) {
    const productList = document.getElementById("product-list");

    // 데이터를 기반으로 제품 목록을 생성
    products.forEach(product => {
        const li = document.createElement("li");
        li.innerHTML = `
            <img src="${product.imgUrl}" alt="${product.productName}" class="prod-img">
            <div class="prod-info">
                <div class="product">
                    <div class="brands">
                        <p>${product.brand}</p>
                        <img src="../images/cart_icon.svg" class="cart-img" />
                    </div>
                    <div class="prod-name"><p>${product.productName}</p></div>
                </div>
                <div class="price">${product.price}</div>
            </div>
        `;

        li.addEventListener('click', () => {
            const clickedItemId = product.id;
            window.location.href = `../product/info?id=${clickedItemId}`;
        })
        productList.appendChild(li);
    })
}

// click 시 해당 카테고리 페이지(main-home2로 이동)
function attachMenuClickEvent() {
    const categoryElems = document.querySelectorAll('.category-elem');
    let pageNum = 0;
    console.log("attach func 진입");

    categoryElems.forEach((elem, index) => {
        elem.addEventListener('click', () => {
            if (index === 0) {
                console.log(0);
                // 농산물 페이지인거 main-home2.js에 넘겨주기 -> 1
                window.location.href = `../product?id=${index + 1}`;
            }
            else if (index === 1) {
                console.log(1);
                // 축산물 페이지인거 main-home2.js에 넘겨주기 -> 2
                window.location.href = `../product?id=${index + 1}`;
            }
            else if (index === 2) {
                console.log(2);
                // 해산물 페이지인거 main-home2.js에 넘겨주기 -> 3
                window.location.href = `../product.html?id=${index + 1}`;
            }
            else if (index === 3) {
                console.log(3);
                // 가공식품 페이지인거 main-home2.js에 넘겨주기 -> 4
                window.location.href = `../product.html?id=${index + 1}`;
            }
        });
        pageNum++;
    });
}


window.onload = function main() {
    checkTokenValid();
    loadRecommendList();
    renderRecommendList(fetchdata);
    attachMenuClickEvent();
}


