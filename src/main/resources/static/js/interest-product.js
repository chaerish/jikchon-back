import { checkTokenValid, checkTokenExistence } from './common/jwt_token_check.js';

let prodList = [
    {
        imgUrl: '../images/apple.jpg',
        productName: '사과',
        category: '과일',
        categoryNum: 101
    },
    {
        imgUrl: '../images/eggs.jpg',
        productName: '계란',
        category: '닭/오리/알류',
        categoryNum: 203
    },
    {
        imgUrl: '../images/pig.jpg',
        productName: '돼지고기',
        category: '돼지',
        categoryNum: 202
    },
    {
        imgUrl: '../images/fish.jpg',
        productName: '고등어',
        category: '생선류',
        categoryNum: 301
    },
    {
        imgUrl: '../images/laver.jpg',
        productName: '김',
        category: '김/해조류    ',
        categoryNum: 303
    },
    {
        imgUrl: '../images/milk.png',
        productName: '우유',
        category: '유제품',
        categoryNum: 403
    },
    {
        imgUrl: '../images/redpepper_paste.jpg',
        productName: '고추장',
        category: '양념류',
        categoryNum: 401
    },
    {
        imgUrl: '../images/rice.jpg',
        productName: '쌀',
        category: '곡물',
        categoryNum: 104
    },
    {
        imgUrl: '../images/shellfish.jpg',
        productName: '조개',
        category: '해산물/어패류',
        categoryNum: 304
    },
    {
        imgUrl: '../images/leek.png',
        productName: '파',
        category: '야채',
        categoryNum: 102
    },
    {
        imgUrl: '../images/lemon.png',
        productName: '레몬',
        category: '과일',
        categoryNum: 101
    },
    {
        imgUrl: '../images/apple.png',
        productName: '사과',
        category: '과일',
        categoryNum: 101
    },
    {
        imgUrl: '../images/eggs.png',
        productName: '계란',
        category: '닭/오리/알류',
        categoryNum: 203
    },
    {
        imgUrl: '../images/mushroom.png',
        productName: '버섯',
        category: '버섯',
        categoryNum: 103
    },
    {
        imgUrl: '../images/milk.png',
        productName: '우유',
        category: '유제품',
        categoryNum: 403
    },
    {
        imgUrl: '../images/recommendLIst-3.jpg',
        productName: '바나나',
        category: '과일',
        categoryNum: 101
    },
    {
        imgUrl: '../images/sausages.jpg',
        productName: '소세지',
        category: '육가공류',
        categoryNum: 204
    },
    {
        imgUrl: '../images/tuna_can.jpg',
        productName: '참치캔',
        category: '수산가공물',
        categoryNum: 305
    },
    {
        imgUrl: '../images/seaweed.jpg',
        productName: '김',
        category: '김/해조류',
        categoryNum: 303
    },
    {
        imgUrl: '../images/redpepper_paste.jpg',
        productName: '고추장',
        category: '양념류',
        categoryNum: 401
    },
    {
        imgUrl: '../images/leek.png',
        productName: '파',
        category: '야채',
        categoryNum: 102
    }
];

let interestProd = {
    interestCategory: []
}

const token = localStorage.getItem('token');
var myHeaders = new Headers();
myHeaders.append('Authorization', 'Bearer ' + token);
myHeaders.append('Content-Type', 'application/json');

let prodStartIndex = 0;
let prodEndIndex = 9;


function loadInterestedList() {
    const recommendList = document.getElementById('recommendList');

    // 처음부터 9개의 항목만 가져오기
    if (prodEndIndex > prodList.length) {
        prodEndIndex = prodList.length;
    }
    const firstNineItems = prodList.slice(prodStartIndex, prodEndIndex);

    firstNineItems.forEach(item => {
        const listItem = document.createElement('li');

        const image = document.createElement('img');
        image.src = item.imgUrl;
        image.alt = '';
        image.className = 'prod-img';

        const prodName = document.createElement('p');
        prodName.className = 'prod-name';
        prodName.textContent = item.productName;

        listItem.appendChild(image);
        listItem.appendChild(prodName);
        listItem.addEventListener('click', () => {
            const categoryName = item.category;
            const categoryNum = item.categoryNum;
            if (interestProd.interestCategory.includes(categoryName)){
                listItem.style.border = "2px solid white";
                interestProd.interestCategory = interestProd.interestCategory.filter(name => name !== categoryName);
            }
            else {
                interestProd.interestCategory.push(categoryName);
                listItem.style.border = "2px solid gray";
            }

            console.log('category: ' + categoryName);
            console.log('categoryNum: ' + categoryNum);
            console.log(interestProd);
        })

        recommendList.appendChild(listItem);
    });
    prodStartIndex += 9;
    prodEndIndex += 9;
}

function reloadInterestedList() {
    const recommendList = document.getElementById('recommendList');
    const reload = document.querySelector(".reload");
    
    reload.addEventListener('click', () => {    
        while (recommendList.firstChild) {
            recommendList.removeChild(recommendList.firstChild);
        }
        loadInterestedList();        
    })
}

function postInterestCategory() {
    var url = "/members/interest";
    const saveBtn = document.getElementById('save-btn');

    saveBtn.addEventListener('click', () => {  
        /* 통신용 코드 */
        fetch(url, {
            method: "POST",
            headers: myHeaders,
            body: JSON.stringify(interestProd)
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

window.onload = function () {
    if (checkTokenExistence()) {
        loadInterestedList();
        reloadInterestedList();
        postInterestCategory();
    }
    else {
        window.location.href = "../html/login.html";
    }
};