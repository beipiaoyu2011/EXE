/**
 * Created by wz on 2015/7/24.
 */

//case1
//window.onload=initAll;//窗口完成加载时 调用函数initAll()
//function initAll(){
//    for(var i= 0;i<24;i++){
//        var newNum=Math.floor(Math.random()*75)+1;//Math.random()获得随机数  Math.floor()函数取整
//        document.getElementById('square'+i).innerHTML=newNum;
//    }
//}


//case2
//window.onload=initAll;
//function initAll(){
//    for(var i=0;i<24;i++){
//        setSquare(i);
//    }
//}
//function setSquare(thisSquare){
//    var currSquare='square'+thisSquare;
//    var newNum=Math.floor(Math.random()*75+1);
//    document.getElementById(currSquare).innerHTML=newNum;
//}


//case 3 探测对象 是否支持

//window.onload=initAll;
//function initAll(){
//    if(document.getElementById){
//        for(var i=0;i<24;i++){
//            setSquare(i);
//        }
//    }else{
//        alert("Sorry, your browser doesn't support this script");
//    }
//}
//
//function setSquare(thisSquare){
//    var currSquare='square'+thisSquare;
//    var newNum=Math.floor(Math.random()*75)+1;
//    document.getElementById(currSquare).innerHTML=newNum;
//}


//case4 B 1-15 I 16-30 N 31-45 ...... 有重复的数字
//window.onload=initAll;
//function initAll(){
//    for(var i=0;i<24;i++){
//        setSquare(i);
//    }
//}
//
//function setSquare(thisSquare){
//    var currSquare='square'+thisSquare;
//    var colPlace=new Array(0,0,0,0,0,1,1,1,1,1,2,2,2,2,3,3,3,3,3,4,4,4,4,4);
//    var basicPlace=colPlace[thisSquare]*15;
//    var newNum=basicPlace+Math.floor(Math.random()*15)+1;
//    document.getElementById(currSquare).innerHTML=newNum
//}
//

//case5  return 从函数返回一个值

//window.onload=initAll;
//function initAll(){
//    for(var i=0;i<24;i++){
//        setSquare(i);
//    }
//}
//
//function setSquare(thisSquare){
//    var currSquare='square'+thisSquare;
//    var colPlace=new Array(0,0,0,0,0,1,1,1,1,1,2,2,2,2,3,3,3,3,3,4,4,4,4,4);
//    var basic=colPlace[thisSquare]*15;
//    var newNum=basic+getNewNum()+1;
//    document.getElementById(currSquare).innerHTML=newNum;
//}
//
//function getNewNum(){
//    return Math.floor(Math.random()*15)
//}
//case6 出现空格

//window.onload=initAll;
//var userNum=new Array(76);
//function initAll(){
//    for(var i=0;i<24;i++){
//        setSquare(i);
//    }
//}
//
//function setSquare(thisSquare){
//    var currSquare='square'+thisSquare;
//    var colPlace=new Array(0,0,0,0,0,1,1,1,1,1,2,2,2,2,3,3,3,3,3,4,4,4,4,4);
//    var basic=colPlace[thisSquare]*15;
//    var newNum=basic+getNewNum()+1;
//    if(!userNum[newNum]){
//        userNum[newNum]=true;
//        document.getElementById(currSquare).innerHTML=newNum;
//    }
//}
//
//function getNewNum(){
//    return Math.floor(Math.random()*15)
//}


//case6 do while

//window.onload=initAll;
//var userNum=new Array(76);
//function initAll(){
//    for(var i=0;i<24;i++){
//        setSquare(i);
//    }
//}
//
//function setSquare(thisSquare){
//    var currSquare='square'+thisSquare;
//    var colPlace=new Array(0,0,0,0,0,1,1,1,1,1,2,2,2,2,3,3,3,3,3,4,4,4,4,4);
//    var basic=colPlace[thisSquare]*15;
//    var newNum;
//    do{
//        newNum=basic+Math.floor(Math.random()*15)+1
//    }while(userNum[newNum]);
//    userNum[newNum]=true;
//    document.getElementById(currSquare).innerHTML=newNum;
//}
//
//function getNewNum(){
//    return Math.floor(Math.random()*15)
//}

//case7  用户自己操作 更新卡片

//window.onload=initAll;
//var userNum=new Array(76);
//function initAll(){
//        document.getElementById("reload").onclick=antherCard;
//        newCard();
//}
//function newCard(){
//    for(var i=0;i<24;i++){
//        setSquare(i)
//    }
//}
//
//function setSquare(thisSquare){
//    var currSquare='square'+thisSquare;
//    var colPlace=new Array(0,0,0,0,0,1,1,1,1,1,2,2,2,2,3,3,3,3,3,4,4,4,4,4);
//    var basic=colPlace[thisSquare]*15;
//    var newNum;
//    do{
//        newNum=basic+getNewNum()+1;
//    }while(userNum[newNum]);
//    userNum[newNum]=true;
//    document.getElementById(currSquare).innerHTML=newNum;
//}
//
//function getNewNum(){
//    return Math.floor(Math.random()*15)
//}
//
//function antherCard(){
//    for(var i=0;i<userNum.length;i++){
//        userNum[i]=false;
//    }
//    newCard();//创建新的卡片
//    return false;//禁止进入href中的链接
//}
//


//case8  通过Javascript创建一个类 调用css

window.onload=initAll;
var userNum=new Array(76);
function initAll(){
        document.getElementById("reload").onclick=antherCard;
        newCard();
}
function newCard(){
    for(var i=0;i<24;i++){
        setSquare(i)
    }
}

function setSquare(thisSquare){
    var currSquare='square'+thisSquare;
    var colPlace=new Array(0,0,0,0,0,1,1,1,1,1,2,2,2,2,3,3,3,3,3,4,4,4,4,4);
    var basic=colPlace[thisSquare]*15;
    var newNum;
    do{
        newNum=basic+getNewNum()+1;
    }while(userNum[newNum]);
    userNum[newNum]=true;
    document.getElementById(currSquare).innerHTML=newNum;
    document.getElementById(currSquare).className='';
    document.getElementById(currSquare).onmousedown=toggleColor;
}

function getNewNum(){
    return Math.floor(Math.random()*15)
}

function antherCard(){
    for(var i=0;i<userNum.length;i++){
        userNum[i]=false;
    }
    newCard();//创建新的卡片
    return false;//禁止进入href中的链接
}

function toggleColor(evt){
    if(evt){
        var thisSquare=evt.target;
    }else{
        var thisSquare=window.event.srcElement;
    }
    if(thisSquare.className==''){
        thisSquare.className='pickedBg';
    }else{
        thisSquare.className='';
        //thisSquare.classList="";
    }
}