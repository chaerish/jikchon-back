import { checkTokenValid, checkTokenExistence } from './common/jwt_token_check.js';

let fetchData = [];
let pageNum = 0;
/* 임시데이터
let temporaryData1 = {
  "data": {
    "totalCount": 12,
    "totalPage": 2,
    "itemList": [
      {
        "id": 13,
        "productName": "A++ 한우 꽃등심 160g",
        "category": "소고기",
        "price": 20000,
        "imageUrl": "이미지 경로"
      },
      {
        "id": 12,
        "productName": "삼겹살 320g",
        "category": "돼지고기",
        "price": 20000,
        "imageUrl": "이미지 경로"
      },
      {
        "id": 14,
        "productName": "삼겹살 320g",
        "category": "돼지고기",
        "price": 20000,
        "imageUrl": "이미지 경로"
      },
      {
        "id": 15,
        "productName": "삼겹살 320g",
        "category": "돼지고기",
        "price": 20000,
        "imageUrl": "이미지 경로"
      },
      {
        "id": 16,
        "productName": "삼겹살 320g",
        "category": "돼지고기",
        "price": 20000,
        "imageUrl": "이미지 경로"
      },
      {
        "id": 17,
        "productName": "삼겹살 320g",
        "category": "돼지고기",
        "price": 20000,
        "imageUrl": "이미지 경로"
      },
      {
        "id": 18,
        "productName": "삼겹살 320g",
        "category": "돼지고기",
        "price": 20000,
        "imageUrl": "이미지 경로"
      },
      {
        "id": 19,
        "productName": "삼겹살 320g",
        "category": "돼지고기",
        "price": 20000,
        "imageUrl": "이미지 경로"
      },
      {
        "id": 10,
        "productName": "삼겹살 320g",
        "category": "돼지고기",
        "price": 20000,
        "imageUrl": "이미지 경로"
      },
      {
        "id": 21,
        "productName": "삼겹살 320g",
        "category": "돼지고기",
        "price": 20000,
        "imageUrl": "이미지 경로"
      }
    ]
  }
}
let temporaryData2 = {
  "data": {
    "totalCount": 12,
    "totalPage": 2,
    "itemList": [
      {
        "id": 22,
        "productName": "삼겹살 320g",
        "category": "돼지고기",
        "price": 20000,
        "imageUrl": "이미지 경로"
      },
      {
        "id": 23,
        "productName": "삼겹살 320g",
        "category": "돼지고기",
        "price": 20000,
        "imageUrl": "이미지 경로"
      },
      {
        "id": 24,
        "productName": "삼겹살 320g",
        "category": "돼지고기",
        "price": 20000,
        "imageUrl": "이미지 경로"
      },
      {
        "id": 25,
        "productName": "삼겹살 320g",
        "category": "돼지고기",
        "price": 20000,
        "imageUrl": "이미지 경로"
      },
      {
        "id": 26,
        "productName": "삼겹살 320g",
        "category": "돼지고기",
        "price": 20000,
        "imageUrl": "이미지 경로"
      },
      {
        "id": 12,
        "productName": "삼겹살 320g",
        "category": "돼지고기",
        "price": 20000,
        "imageUrl": "이미지 경로"
      },
      {
        "id": 12,
        "productName": "삼겹살 320g",
        "category": "돼지고기",
        "price": 20000,
        "imageUrl": "이미지 경로"
      },
      {
        "id": 12,
        "productName": "삼겹살 320g",
        "category": "돼지고기",
        "price": 20000,
        "imageUrl": "이미지 경로"
      },
      {
        "id": 12,
        "productName": "삼겹살 320g",
        "category": "돼지고기",
        "price": 20000,
        "imageUrl": "이미지 경로"
      }
    ]
  }
}
*/

/* Header 설정 */
var myHeaders = new Headers();
myHeaders.append('Content-Type', 'application/json');

const urlParams = new URLSearchParams(window.location.search);
const categoryId = urlParams.get('id');
console.log(categoryId);
let url = `/products?category=${categoryId}&page=${pageNum}`

function loadProdData() {
  fetch(url, {
    headers: myHeaders,
    method: 'GET'
  })
    .then((response) => response.json())
    .then((data) => {
      let data1 = data.data;
      console.log(data1);
      renderProdData(data1.itemList);
    })
    .catch((error) => {
      console.error('An error occurred while loading store data:', error);
    });
}

let isLoading = false;

function loadMoreItems() {
  isLoading = true;

  fetch(`/products?category=${categoryId}&page=${pageNum}`, {
    headers: myHeaders,
  })
    .then((response) => response.json())
    .then((data) => {
      let data1 = data.data;
      renderProdData(data1.itemList);
    })
    .catch((error) => {
      console.error('An error occurred while loading store data:', error);
    });
}


function ProdInfinityScroll() {
  const container = document.querySelector(".container");

  container.addEventListener("scroll", function () {
    const scrollHeight = container.scrollHeight;
    const scrollTop = container.scrollTop;
    const clientHeight = container.clientHeight;
    const IS_END = ((scrollHeight - scrollTop) <= clientHeight - 10);

    if (IS_END && fetchData.totalPage > pageNum) {
      pageNum++;
      loadMoreItems();
    }
  })
}

function renderSubCategoryBtn() {
  const categoryDetails = document.getElementById("category-details");
  if (categoryId == 1) {
    categoryDetails.innerHTML = `
      <button class="fruits" type="button" id="101"><span>과일</span></button>
      <button class="vegetables" type="button" id="102"><span>채소</span></button>
      <button class="mushrooms" type="button" id="103"><span>버섯</span></button>
      <button class="grains" type="button" id="104"><span>곡물</span></button>
      <button class="driedProduce" type="button" id="105"><span>건농산물</span></button>
    `;
  }
  else if (categoryId == 2) {
    categoryDetails.innerHTML = `
      <button class="fruits" type="button" id="201"><span>소</span></button>
      <button class="vegetables" type="button" id="202"><span>돼지</span></button>
      <button class="mushrooms" type="button" id="203"><span>닭/오리/알류</span></button>
      <button class="grains" type="button" id="204"><span>육가공류</span></button>
    `;
  }
  else if (categoryId == 3) {
    categoryDetails.innerHTML = `
      <button class="fruits" type="button" id="301"><span>생선류</span></button>
      <button class="vegetables" type="button" id="302"><span>건어물</span></button>
      <button class="mushrooms" type="button" id="303"><span>김/해조류</span></button>
      <button class="grains" type="button" id="304"><span>해산물/어패류</span></button>
      <button class="driedProduce" type="button" id="305"><span>수산가공물</span></button>
    `;
  }
  else if (categoryId == 4) {
    categoryDetails.innerHTML = `
      <button class="fruits" type="button" id="401"><span>양념류</span></button>
      <button class="vegetables" type="button" id="402"><span>반찬류</span></button>
      <button class="mushrooms" type="button" id="403"><span>유제품</span></button>
    `;
  }
  else {
    categoryDetails.innerHTML = `
    <button class="fruits" type="button" id="101"><span>과일</span></button>
    <button class="vegetables" type="button" id="102"><span>채소</span></button>
    <button class="mushrooms" type="button" id="103"><span>버섯</span></button>
    <button class="grains" type="button" id="104"><span>곡물</span></button>
    <button class="driedProduce" type="button" id="105"><span>건농산물</span></button>
  `;
  }
}

function renderProdData(productsData) {
  const productList = document.getElementById("product-list");
  productsData.forEach(product => {
    const li = document.createElement("li");

    li.innerHTML = `
          <img src="${product.imageUrl}" alt="${product.productName}" class="prod-img">
          <div class="prod-info">
            <div class="product">
              <div class="brands">
                <p>${product.storeName}</p>
                <img src="../images/cart_icon.svg" class="cart-img" />
              </div>
              <div class="prod-name">
                <p>${product.productName}</p>
              </div>
            </div>
            <div class="price">${product.price}</div>
          </div>
        `;

    // li click 시 상세페이지로 이동
    li.addEventListener("click", () => {
      const clickedItemId = product.productId;
      window.location.href = `../product/info?id=${product.productId}`;
    })

    productList.appendChild(li);
  })
  isLoading = false;
}

function categoryFiltering() {
  const categorybtn = document.querySelectorAll(".category-details button");
  const prodList = document.getElementById("product-list");

  categorybtn.forEach(btn => {
    btn.addEventListener("click", () => {
      prodList.innerHTML = "";
      const categoryID = btn.id;
      url = `/products?category=${categoryID}&page=0`;
      pageNum = 0;

      fetch(url, {
        headers: myHeaders,
        method: "GET"
      })
        .then((response) => response.json())
        .then((data) => {
          let data1 = data.data;
          renderProdData(data1.itemList);
        })
        .catch((error) => {
          console.error("loading fail")
        });

    })
  })
}

window.onload = function main() {
  loadProdData();
  renderSubCategoryBtn();
  categoryFiltering();
  ProdInfinityScroll();
}