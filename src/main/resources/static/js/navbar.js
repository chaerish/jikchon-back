import {checkTokenExistence, checkUserRole} from './common/jwt_token_check.js';

const btnGoBack = document.getElementById('back-button');
const btnGoCart = document.getElementById('cart-button');
const btnGoMyPage = document.getElementById('mypage-button');

btnGoBack.addEventListener('click', () => {
  history.back();
})

btnGoCart.addEventListener('click', () => {
  if (checkTokenExistence()) {
    window.location.href = './cart.html';
  } else {
    window.alert('로그인이 필요한 서비스입니다. 로그인 화면으로 이동합니다.');
    window.location.href = './login.html';
  }
});

btnGoMyPage.addEventListener('click', () => {
  if (checkTokenExistence()) {
    if (checkUserRole() === 'seller') {
      window.location.href = './mypage_seller.html';
    } else if (checkUserRole() === 'customer') {
      window.location.href = './mypage_customer.html';
    }
  } else {
    window.alert('로그인이 필요한 서비스입니다. 로그인 화면으로 이동합니다.');
    window.location.href = './login.html';
  }
});
