var list
var types
$(document).ready(function () {
    list=new Vue({
        el:"#list-table",
        data:{
            files:[
            ],
            remains:[]
        }
    })
    types=new Vue({
        el:"#filter-list",
        data:{
            types:[
            ]
        }
    })
})
function analyze() {
    showLoading()
    var dat=document.getElementById("input-address").value
    var send={}
    send["address"]=dat

    $.ajax({
        url:"/analyze?address="+dat,
        type:"GET",
        success:function (result) {
            try{
                var resultJ=JSON.parse(result)
                var arr=resultJ["items"]
                list.files=arr
                var map={}
                for (var i=0;i<arr.length;i++){
                    map[arr[i]["type"]]=1;
                }
                types.types=[]
                for (var m in map){
                    var type={}
                    type["name"]=m
                    type["selected"]=true
                    types.types.push(type)
                }
            }catch (e) {
                alert(e)
            }
            hideLoading()
        }
    })
}
function selectAll() {
    for (var i=0;i<list.files.length;i++){
        list.files[i]["checked"]=true
    }
}
function deselectAll() {
    for (var i=0;i<list.files.length;i++){
        list.files[i]["checked"]=false
    }
}
function reverseSelect(){
    for (var i=0;i<list.files.length;i++){
        list.files[i]["checked"]=!list.files[i]["checked"]
    }
}
function toggleFilter(ele) {
    var id=ele.getAttribute("data-toggle")
    var l=document.getElementById(id)
    if (l.style.display=="none"){
        l.style.display="block"
    }else{
        l.style.display="none"
    }
}
function onFilterChange(el) {
    var typ=el.getAttribute("data-bind")
    if (el.checked){
        var toFiles=list.remains.filter(function (value) {
            if (value["type"]==typ){
                return value
            }
        })
        var remains=list.remains.filter(function (value) {
            if (value["type"]!=typ){
                return value
            }
        })
        list.files=list.files.concat(toFiles)
        list.remains=remains
    }else{
        var toRemains=list.files.filter(function (value) {
            if (value["type"]==typ){
                return value
            }
        })
        var files=list.files.filter(function (value) {
            if (value["type"]!=typ){
                return value
            }
        })
        list.remains=list.remains.concat(toRemains)
        list.files=files
    }
}
function selectDirectory() {
    
}
function showDownloading(){
    document.getElementById("download-dialog").style.display="block"
}
function hideLoading(){
    document.getElementById("loading-dialog").style.display="none"
}
function hideDownloading() {
    document.getElementById("download-dialog").style.display="none"
}
function showLoading() {
    document.getElementById("loading-dialog").style.display="block"
}