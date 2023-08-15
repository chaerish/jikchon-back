// Description: jwt 토큰 관련 함수들을 모아놓은 파일입니다.

/*
  import 사용법:

import {checkTokenExistence, checkUserRole, checkTokenValid, logOut} from './common/jwt_token_check.js';

*/

/** 
 * (페이지 이동 시) 초기 토큰 확인 함수
 * 
 * 로컬 스토리지에 토큰이 없으면 로그인 페이지로 이동합니다.
 * valid 여부는 확인하지 않습니다.
 * 
 * @returns {boolean} true: 토큰이 존재함
 * @returns {boolean} false: 토큰이 존재하지 않음
 */
export function checkTokenExistence() {
  return localStorage.getItem("access_token") !== null ? true : false;
}

/**
 * 유저 역할 구별(검사) 함수
 * 
 * localStorage의 유저 역할을 확인하고, 반환합니다.
 * 
 * @returns {string} seller: 판매자
 * @returns {string} customer: 구매자
 */
export function checkUserRole() {
  return localStorage.getItem("user_role");
}

/** 
 * access 토큰 유효성 검사 함수
 * 
 * access 토큰의 유효성을 검사합니다.
 * 검사 결과 access 토큰이 유효하면 아무 작업도 하지 않고,
 * 유효하지 않으면 refresh 토큰으로 기존 localstorage의 access 토큰을 갱신합니다.
 * 
 * @returns none
*/
export function checkTokenValid() {
  const now = new Date().getTime()
  const tokenExpiresIn = Number(localStorage.getItem("expires_in"));

  if (tokenExpiresIn < now) {
    fetch('http://jikchon.ap-northeast-2.elasticbeanstalk.com/members/refresh', {
      method: 'POST',
      headers: {
        'Content-Type': "application/json",
      },
    })
    .then(response => {
      if (response.status === 200) return response.json();
      else throw new Error(response.message);
    })
    .then(response => {
        localStorage.setItem("access_token", response.data.access_token);
        localStorage.setItem("expires_in", response.data.expires_in);
    })
    .catch(error => {
      console.log(error);
    });
  }
}

/**
 * 로그아웃 함수
 * 
 * 로컬 스토리지의 토큰을 삭제하고, 메인 페이지로 이동합니다.
 */
export function logOut() {
  localStorage.removeItem("access_token");
  localStorage.removeItem("expires_in");
  localStorage.removeItem("user_role");
  window.alert("로그아웃 되었습니다.");
  window.location.href = "./main-home1.html";
}



// !!! 예시 - 장바구니 조회 함수
function inquireCart() {
  checkTokenValid();

  fetch("https://api.example.com/cart", {
    method: "GET",
    headers: {
      'Content-Type': "application/json",
      'Authorization': `Bearer ${localStorage.getItem("access_token")}`,
    },
  })
  .then(response => response.json())
  .then(response => {
    console.log(response.data); // 가져온 데이터 처리
  });
}