import { checkTokenExistence, checkTokenValid, checkUserRole } from "../js/common/jwt_token_check.js";

const pCustomerName = document.getElementById('customer-name');
const pCustomerContact = document.getElementById('customer-contact');
const pCustomerAddress = document.getElementById('customer-address');

const pProductName = document.getElementById('product-name');
const pProductQuantity = document.getElementById('product-quantity');
const pProductPrice = document.getElementById('product-price');

const pTotalPrice = document.getElementById('total-price');

function getSellerRecieptInfo() {
  checkTokenValid();

  const purchaseID = window.location.href.split('=')[1];

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
    });
}

function setSellerRecieptInfo(data) {
  pCustomerName.innerText = data.customerDto.name;
  pCustomerContact.innerText = data.customerDto.phoneNumber;
  pCustomerAddress.innerText = data.customerDto.address;

  pProductName.innerText = `${data.purchaseDto.productName}(${data.purchaseDto.id})`;
  pProductPrice.innerText = Number(data.purchaseDto.price) / Number(data.purchaseDto.quantity);
  pProductQuantity.innerText = data.purchaseDto.quantity;

  pTotalPrice.innerText = `${data.purchaseDto.price}원`;
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