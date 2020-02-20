$(function(){
    var name = sessionStorage.getItem('username');
    $("#adminName").text(name);
})