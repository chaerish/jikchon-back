import { checkTokenValid, checkTokenExistence, checkUserRole } from './common/jwt_token_check.js';
let fetchData = [];
let pageNum = 0;
let urls = "/members/products";

/* Header 설정 */
const token = localStorage.getItem('access_token');
var myHeaders = new Headers();
myHeaders.append('Authorization', `Bearer ${token}`);
myHeaders.append('Content-Type', 'application/json');

function loadProdManageData() {
    /* 통신용 코드 */
    if (checkTokenExistence()) {
        if (checkUserRole() === 'seller') {
            fetch(urls, {
                headers: myHeaders,
                method: 'GET'
            })
                .then((response) => response.json())
                .then((data) => {
                    let data1 = data.data;
                    renderProdManageData(data1.itemList);
                })
                .catch((error) => {
                    console.error('An error occurred while loading store data:', error);
                });
        }
    }
}


// function loadMoreItems() {
//     // if (fetchData.totalPage > pageNum) {
//     //     pageNum++;
//     //     console.log("pageNum: ", pageNum);
//     // }
//     let nextPageData = [];

//     fetch(urls, {
//         headers: myHeaders,
//     })
//         .then((response) => response.json())
//         .then((data) => {
//             let data1 = data.data;
//             renderProdManageData(data1.itemList);
//         })
//         .catch((error) => {
//             console.error('An error occurred while loading store data:', error);
//         });
// }

// function ProdInfinityScroll() {
//     window.addEventListener("scroll" , function() {
//       const SCROLLED_HEIGHT = window.scrollY;
//       const WINDOW_HEIGHT = window.innerHeight;
//       const DOC_TOTAL_HEIGHT = document.body.offsetHeight;
//       const IS_END = (WINDOW_HEIGHT + SCROLLED_HEIGHT > DOC_TOTAL_HEIGHT - 10);
  
//       if (IS_END && fetchData.totalPage > pageNum) {
//         pageNum++;
//         loadMoreItems();
//       }
//     })
//   }

let indexOfClickBtn = 0;

function deleteProduct(productId) {
    let url = `/products/${productId}`
    /* 통신용 코드 */
    fetch(url, {
        method: 'delete',
        headers: myHeaders,
    })
        .then((response) => {
            response.json()
            const deletedItem = document.getElementById(`${productId}`);
            if (deletedItem) {
                window.alert("상품이 삭제되었습니다!");
                deletedItem.remove();
            }
        })
        .catch((error) => {
            console.error('An error occurred while loading store data:', error);
        });
}

function renderProdManageData(data) {
    const orderList = document.getElementById('orderList');

    data.forEach(item => {
        const listItem = document.createElement('li');
        listItem.className = 'order-comp';
        listItem.id = item.productId;

        const image = document.createElement('img');
        image.src = item.imageUrl;
        image.alt = '';

        const orderInfoBox = document.createElement('div');
        orderInfoBox.className = 'order-info-box';

        const orderInfo = document.createElement('div');
        orderInfo.className = 'order-info';

        const prodName = document.createElement('p');
        prodName.className = 'prod-name';
        prodName.textContent = item.productName;

        const price = document.createElement('p');
        price.className = 'price';
        price.textContent = item.price + "원";

        const inventoryQuantity = document.createElement('p');
        inventoryQuantity.className = 'inventory-quantity';
        inventoryQuantity.textContent = '수량 ' + item.quantity;

        orderInfo.appendChild(prodName);
        orderInfo.appendChild(price);
        orderInfo.appendChild(inventoryQuantity);

        const btnBox = document.createElement('div');
        btnBox.className = 'btn-box';

        const changeBtn = document.createElement('button');
        changeBtn.className = 'change-btn';
        changeBtn.textContent = '수정하기';
        changeBtn.addEventListener('click', () => {
            const clickedItemId = item.productId; // 클릭된 버튼의 항목 ID 가져오기
            window.location.href = `../product/modify?id=${clickedItemId}`
        });

        const deleteBtn = document.createElement('button');
        deleteBtn.className = 'delete-btn';
        deleteBtn.textContent = '삭제하기';
        deleteBtn.addEventListener('click', () => {
            const clickedItemId = item.productId; // 클릭된 버튼의 항목 ID 가져오기
            deleteProduct(clickedItemId);
            listItem.remove();
        });

        btnBox.appendChild(changeBtn);
        btnBox.appendChild(deleteBtn);

        orderInfoBox.appendChild(orderInfo);
        orderInfoBox.appendChild(btnBox);

        listItem.appendChild(image);
        listItem.appendChild(orderInfoBox);

        orderList.appendChild(listItem);
    });
}


window.onload = function main() {
    loadProdManageData();
}