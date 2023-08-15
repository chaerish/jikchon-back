const header = document.querySelector('header');

addEventListener('scroll', () => {
  if (window.scrollY > 70 || window.pageYOffset > 70) {
    header.style.top = `${70 - window.pageYOffset}px`;
  } else {
    header.style.top = 0;
  }
})