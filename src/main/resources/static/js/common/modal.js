const modalScreen = document.querySelector('.modal');
const btnCloseModal = document.querySelector('.modal-close-button');
const btnOpenModal = document.getElementById('modalButton');

const btnLeft = document.querySelector('.left-button');
const btnRight = document.querySelector('.right-button');

btnCloseModal.addEventListener('click', () => {
  modalScreen.style.display = 'none';
  document.body.style.overflow = 'auto';
});

btnOpenModal.addEventListener('click', () => {
  modalScreen.style.display = 'block';
  document.body.style.overflow = 'hidden';
});

btnLeft.addEventListener('click', () => {
  window.alert('왼쪽 버튼 클릭');
});

btnRight.addEventListener('click', () => {
  window.alert('오른쪽 버튼 클릭');
});