import { checkTokenExistence, checkTokenValid,checkUserRole } from "./common/jwt_token_check.js";
var files = [];
var idValue = '';

document.addEventListener("DOMContentLoaded", function() {
  idValue = getQueryParamValue('id');
  enrollItem();
  getData();
});
function getQueryParamValue(paramName){
  var urlParams = new URLSearchParams(window.location.search);
  return urlParams.get(paramName);
}
function enrollItem(){
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
}
function getData(){
  const url = '/products/'+idValue;
    var myHeaders = new Headers();
    var data = {
    //   productId:4,
    //   productName : "해바라기씨",
    //   subcategory : "곡물",
    //   price : 2000,
    //   quantity : 50,
    //   intro : "우리집 햄스터 애착품",
    //   imageUrl:["https://elasticbeanstalk-ap-northeast-2-502124422422.s3.ap-northeast-2.amazonaws.com/product/d6fb88d4-cad9-4925-9caf-30f6e4d4222f_KakaoTalk_Photo_2023-06-11-16-24-21.jpeg"],
    };

    const token = localStorage.getItem('token');
    myHeaders.append('Authorization',`Bearer ${token}`); 
    fetch(url,{
        headers:myHeaders,
        method:"GET",
    })
    .then((response)=>{
        return response.json();
    })
    .then(date => {
        if(data.httpStatus==='OK'){
            data = data;
        } else {
            console.error("데이터 가져오기 실패");
        }
    })
    .catch((error)=>{
        console.error("오류발생",error);
    });
    
    setData(data);
}

function setData(data){
  var imageContainer = document.getElementById("image-container");
  imageContainer.innerHTML = '';
  imageContainer.style.background = "none";

  var img = document.createElement("img");
  img.classList.add('item-img');
  img.setAttribute("src", data.imageUrl[0]);
  img.style.width = "10rem";
  img.style.height ="10rem";
  imageContainer.appendChild(img);

  setCategory(data);
  document.getElementById("item-name").value = data.productName;
  document.getElementById("item-price").value = data.price;
  document.getElementById("item-amount").value = data.quantity;
  document.getElementById("item-detailed").value = data.intro;
}
function setCategory(data){
  var bigCategorySelect = document.getElementById("item-big-category");
  var smallCategorySelect = document.getElementById("item-small-category");
    
    // bigCategorySelect의 변경에 따라 smallCategorySelect 옵션을 설정하는 함수
  bigCategorySelect.addEventListener("change", function() {
    var selectedValue = bigCategorySelect.value;
    smallCategorySelect.innerHTML = "";

    if(checkInclude(data.category,["과일", "채소", "버섯","곡물","건농산물"])){
      bigCategorySelect.value = "농산물";
      smallCategorySelect.Value = data.subcategory
    }
    else if(checkInclude(data.category,["소", "돼지", "닭/오리/알류","육가공륙"])){
      bigCategorySelect.value = "축산물";
      smallCategorySelect.Value = data.subcategory
    }
    else if(checkInclude(data.category,["생선류", "건어물", "김/해조류","해산물/어패류","수산가공물"])){
      bigCategorySelect.value = "수산물";
      smallCategorySelect.Value = data.subcategory
    }
    else if(checkInclude(data.category,["앙념류", "반찬류", "유제품"])){
      bigCategorySelect.value = "가공식품";
      smallCategorySelect.Value = data.subcategory
    }
    
    
    if (selectedValue === "농산물") {
      populateSmallCategory(["과일", "채소", "버섯","곡물","건농산물"]);
    } else if (selectedValue === "축산물") {
      populateSmallCategory(["소", "돼지", "닭/오리/알류","육가공륙"]);
    }else if (selectedValue === "수산물") {
      populateSmallCategory(["생선류", "건어물", "김/해조류","해산물/어패류","수산가공물"]);
    }else if (selectedValue === "가공식품") {
      populateSmallCategory(["앙념류", "반찬류", "유제품"]);
    }

  });
  
  function populateSmallCategory(categories) {
    categories.forEach(function(category) {
      var option = document.createElement("option");
      option.value = category;
      option.text = category;
      smallCategorySelect.appendChild(option);
    });
  }
  function checkInclude(inputValue,categoryList){
    return(categoryList.includes(inputValue))
  }
}
document.getElementById("item-image").addEventListener("change", function (event){
  loadFiles(event);
})

//이미지 여러장으로 받아내기!
function loadFiles(event) {
  var imageContainer = document.getElementById("image-container");
  imageContainer.innerHTML = '';
  imageContainer.style.background = "none";

  files = event.target.files;
  for (var i = 0; i < files.length; i++) {
    var reader = new FileReader();
    reader.onload = function(event) {
      var img = document.createElement("img");
      img.classList.add('item-img');
      img.setAttribute("src", event.target.result);
      img.style.width = 10/i+"rem";
      img.style.height = 10/i+"rem";
      img.style.margin = '0.1rem';
      imageContainer.appendChild(img);
    };
    reader.readAsDataURL(files[i]);
  }
}
document.getElementById('submit-button').addEventListener("click",()=>{
  submit();
})

  function submit(){
    var productName = document.getElementById('item-name').value;
    var smallCategory = document.getElementById('item-small-category').value;
    var price = document.getElementById('item-price').value;
    var quantity = document.getElementById('item-amount').value;
    var intro = document.getElementById('item-detailed').value;

    const itemRequest = {
        'productName': productName,
        'price' : price,
        'quantity' : quantity,
        'smallCategory':smallCategory,
        'intro' : intro,
    }

    const formData = new FormData();
    for (var i = 0; i < files.length; i++) {
      formData.append('productImageList', files[i]);
    }
    formData.append(
      'productRequestDto',
        new Blob([JSON.stringify(itemRequest)],{
            type:'application/json'
        })
    );

    console.log(formData);
    var myHeaders = new Headers();
    const url = "/products/"+idValue;
    const token = localStorage.getItem('token');
    myHeaders.append('Authorization',token);
    myHeaders.append('Content-Type','application/json');

    fetch(url,{
        headers: myHeaders,
        body:formData,
        method: "PUT"
    })
    .then((Response)=>Response.json())
    .then((result)=>console.log(result))
    .catch((error)=>{
        console.error(error);
    })
    console.log("전송완료 : ",files)
    moveChangePage();
  }

  function moveChangePage(){
    alert("수정이 완료되었습니다.");
    window.location.href = "http://jikchon.ap-northeast-2.elasticbeanstalk.com/product/manage";
}