import { checkTokenExistence, checkTokenValid,checkUserRole } from "./common/jwt_token_check.js";
document.addEventListener("DOMContentLoaded", function() {
    sell_checkOrders();
    getData();
});
function sell_checkOrders(){
    if(!checkTokenExistence()){
        window.alert('로그인이 필요한 서비스입니다. 로그인 화면으로 이동합니다.');
        window.location.href = 'http://jikchon.ap-northeast-2.elasticbeanstalk.com/login';
    }else {
        if (checkUserRole() !== 'seller') {
          window.alert('잘못된 접근입니다.');
          window.location.href = 'http://jikchon.ap-northeast-2.elasticbeanstalk.com/';
          return;
        }
    }
    checkTokenValid();
    fetch("/seller/purchases", {
        method: "GET",
        headers: {
          'Content-Type': "application/json",
          'Authorization': `Bearer ${localStorage.getItem("token")}`,
        },
      })
      .then(response => response.json())
      .then(response => {
        console.log(response.data); // 가져온 데이터 처리
      });
}
function getData(){
    var data = {
        // totalCount: 12,
        // itemList:[
        //     {
        //         orderId:20,
        //         purchasedId :13,
        //         productName : "A++ 한우 꽃등심 160g",
        //         quantity : 3,
        //         totalPrice : 60000,
        //         orderAt : "2023.12,04",
        //         imageUrl : "../images/meat.jpg",
        //         purchaseCustomer : {
        //             id :2,
        //             name : "김직촌",
        //             phoneNumber : "010-1234-5678"
        //         }
        //     },
        //     {
        //         orderId:19,
        //         purchasedId :12,
        //         productName : "A++ 한우 꽃등심 160g",
        //         quantity : 3,
        //         totalPrice : 60000,
        //         orderAt : "2023.12,04",
        //         imageUrl : "../images/meat.jpg",
        //         purchaseCustomer : {
        //             id :2,
        //             name : "김직촌",
        //             phoneNumber : "010-1234-5678"
        //         }
        //     }
        // ]
    }  
    const url = '/seller/purchases';
    var myHeaders = new Headers();
    const token = localStorage.getItem('token');
    myHeaders.append('Authorization','Bearer'+token);  
    fetch(url,{
        headers:myHeaders,
        method:"GET",
    })
    .then((response)=>{
        return response.json();
    })
    .then(date => {
        if(data.httpStatus==='OK'){
            data=data;
        } else {
            console.error("데이터 가져오기 실패");
        }
    })
    .catch((error)=>{
        console.error("오류발생",error);
    });
    
    setOrderList(data);
}

function setOrderList(data){
    var orderList= document.getElementById("order-list");

    var itemLists = data.itemList;
    itemLists.forEach(function(itemList){
        purchaseCustomer = itemList.purchaseCustomer;
        var orderBox = document.createElement("div");
        orderBox.classList.add("order-box");

        var leftBox = document.createElement("div");
        leftBox.classList.add("order-left-box");

        var leftBox2 = document.createElement('div');
        leftBox2.classList.add("order-left-box2")

        var orderDate = document.createElement("p");
        orderDate.classList.add("order-date");
        orderDate.textContent = itemList.orderAt;

        var leftBox1 = document.createElement('div');
        leftBox1.classList.add("order-left-box1")
        
        var itemImage = document.createElement("img");
        itemImage.classList.add("order-img");
        itemImage.src = itemList.imageUrl;

        var orderName = document.createElement("p");
        orderName.classList.add("order-name");
        orderName.textContent = itemList.productName;

        var orderNum = document.createElement("p");
        orderNum.classList.add("order-num");
        orderNum.textContent = itemList.orderID;

        leftBox2.appendChild(orderName);
        leftBox2.appendChild(orderNum);
        leftBox1.appendChild(itemImage);
        leftBox1.appendChild(leftBox2);
        leftBox.appendChild(orderDate);
        leftBox.appendChild(leftBox1);

        var rightBox = document.createElement("div");
        rightBox.classList.add("order-right-box");

        var conName = document.createElement("p");
        conName.classList.add("order-con-name");
        conName.textContent = "주문자명 : " + purchaseCustomer.name;

        var orderPrice = document.createElement("p");
        orderPrice.classList.add("order-price");
        orderPrice.textContent = itemList.totalPrice;

        var orderDetail = document.createElement("a");
        orderDetail.classList.add("order-detail");
        orderDetail.textContent = "자세히보기";
        orderDetail.href = 'http://jikchon.ap-northeast-2.elasticbeanstalk.com/receipt/seller/?id='+itemList.purchasedId;

        rightBox.appendChild(conName);
        rightBox.appendChild(orderPrice);
        rightBox.appendChild(orderDetail);

        orderBox.appendChild(leftBox);
        orderBox.appendChild(rightBox);
        orderList.appendChild(orderBox);

    });
}
