import {checkTokenExistence, checkUserRole, logOut} from './common/jwt_token_check.js';

const btnLogOut = document.getElementById('logout-button');

// 로그인 확인부
if (!checkTokenExistence()) {
  window.alert('로그인이 필요한 서비스입니다. 로그인 화면으로 이동합니다.');
  window.location.href = './login.html';
} else { // 로그인이 되어있는 경우
  // 유저 역할 확인부 (구매자만 접근 가능)
  if (checkUserRole() !== 'customer') {
    window.alert('잘못된 접근입니다.');
    window.location.href = './main-home1.html';
  }
}

btnLogOut.addEventListener('click', logOut);