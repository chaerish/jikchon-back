import { checkTokenExistence, checkTokenValid, checkUserRole } from "../js/common/jwt_token_check.js";

const pCustomerName = document.getElementById('customer-name');
const pCustomerContact = document.getElementById('customer-contact');
const pCustomerAddress = document.getElementById('customer-address');

const pProductName = document.getElementById('product-name');
const pProductQuantity = document.getElementById('product-quantity');
const pProductPrice = document.getElementById('product-price');

const pTotalPrice = document.getElementById('total-price');

const purchaseID = window.location.href.split('=')[1].split('=')[1];

function getSellerRecieptInfo() {
  checkTokenValid();
  fetch(`/seller/receipt/${purchaseID}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem('access_token')}`
    },
  })
    .then(response => {
      if (response.status === 200) {
        return response.json();
      } else throw new Error(response.status);
    })
    .then(response => {
      return setSellerRecieptInfo(response.data)
    })
    .catch(error => {
      console.log(error);
      window.alert('영수증 조회에 실패했습니다.');
      window.location.href = '/seller/checkorder';
    });
}

function setSellerRecieptInfo(data) {
  pCustomerName.innerText = data.customerDto.name;
  pCustomerContact.innerText = data.customerDto.phone;
  pCustomerAddress.innerText = data.customerDto.address;

  pProductName.innerText = `${data.productDto.productName}(${data.productDto.id})`;
  pProductPrice.innerText = Number(data.productDto.price) / Number(data.productDto.quantity);
  pProductQuantity.innerText = data.productDto.quantity;

  pTotalPrice.innerText = `${data.productDto.price}원`;
}

if (checkTokenExistence()) {
  if (checkUserRole() !== 'seller') {
    window.alert('잘못된 접근입니다.');
    window.location.href = '/';
  } else {
    getSellerRecieptInfo();
  }
} else {
  window.alert('로그인 후 이용해주세요.');
  window.location.href = '/login';
}