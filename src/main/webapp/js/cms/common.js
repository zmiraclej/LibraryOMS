/**
 * 
 */

$(function(){
	/*$(".widget-box .widget-header a[data-action='collapse']").click(function(){
		var $this = $(this);
		$this.parents(".widget-box").toggleClass("collapsed").find(".widget-body").slideToggle(200);
		return false;
	});*/
	$("#submit").click(function(){
		$("#submit").submit();
	});
	$(".but_refurbish").click(function(){
		$.reloadDocument(true);
	});
	$(".navbar-bottom>.pagination a[href]").click(function(){
		var href = $(this).attr("href");
		var o = $.getUrlParamObject(href);
		var url = $.setUrlParam(o);
		$.reloadDocument(url);
		return false;
	});
	$(".uppercase").keyup(function(e){
		var $this = $(this);
		var key =  parseInt(e.keyCode||e.which)
		if(key&&key>=97&&key>=122){
			$this.val($this.val().toUpperCase());
		}
	});
	var $pagerBut = $(".pagination input[type='button']");
	$pagerBut.click(function(){
		goToPate($(this).prev("input").val());
			return false;
		});
	$pagerBut.prev("input").keypress(function(e){
		var key =  e.keyCode||e.which;
		if(key=="13"){
			goToPate($(this).val());
			return false;
		}
	});
	$("td a[href='#']").click(function(e){
		return false;
	});
});
var _t = {
		log:function(msg){
			console.log(msg);
		}
}
function goToPate(pageNum,param){
	pageNum = $.trim(pageNum);
	pageNum = parseInt(pageNum);
	if(pageNum<1||isNaN(pageNum)){
		pageNum=1;
	}
	
	var pageNumTatli = $(".pagination li").length-3;
	if (pageNum>pageNumTatli) {
		pageNum = pageNumTatli;
	}
	param = $.extend(param,{"pageNum":pageNum});
	url = $.setUrlParam(param);
	$.reloadDocument(url);
}

var config = {
		contextPath:"/library",
		regexps:{
			identificationCard:new RegExp("(^[0-9]{15}$)|(^[0-9]{18}$)|(^[0-9]{17}([0-9]|X|x)$)","i"),
			phone:new RegExp("(^([0-9]{11})|^(([0-9]{7,8})|([0-9]{4}|[0-9]{3})-([0-9]{7,8})|([0-9]{4}|[0-9]{3})-([0-9]{7,8})-([0-9]{4}|[0-9]{3}|[0-9]{2}|[0-9]{1})|([0-9]{7,8})-([0-9]{4}|[0-9]{3}|[0-9]{2}|[0-9]{1}))$)"),
			fax:new RegExp("^(\d{3,4}-)?\d{7,8}$"),
			age:new RegExp("^[0-9]{2}$"),
			userName:new RegExp("^[a-z0-9_]{3,16}$","i"),
			userPassword:new RegExp("^[^*\]{6,20}$"),
			price:new RegExp("^[0-9]+(\.[0-9]{2})?$")//价格
		}
}

function onlyNum(event){ 
	event=window.event || event;	
    if(event.keyCode = 86 || (event.keyCode>=48 && event.keyCode<=57) || (event.keyCode>=96 && event.keyCode<=105) || (event.keyCode>=37 && event.keyCode<=40) || event.keyCode==8 || event.keyCode==13 || event.keyCode==46||event.keyCode==110){
    	return true;
    }else{
    	return false;
    }
}


function checkMobile(str) {
   var re = /^1\d{10}$/
   if (re.test(str)) {
       return true;
   } else {
	   return false;;
   }
}

function setButtonDisabled(id){
	var $obj =$("#"+id);
	$obj.removeAttr("disabled");

}
function showErrorTipMsg(element,msg){
	element.val("");
	element.focus();
	element.inputDefault("setTipMsg",msg);
	window.setTimeout(function(){
		element.inputDefault("show");
	}, 50);
}
function del(t){
		var $this = $(t);
		var $tr = $this.parents("tr");
		var ul = $tr.parents("table").attr("ul");
		var name = $tr.children("td[t]").text();
		var id = $tr.attr("tid");
//		Showbo.Msg.confirm("您将要删除 ["+name+"],确认删除?",function(b){
//			if(b=='yes'){
				ZENG.msgbox.show("删除中...", 6);
				$.post(config.contextPath+"/cms/"+ul+"/del/"+id+".do",function(data){
					ZENG.msgbox.hide();
					if (!data.success) {
						if(data.msg){
							ZENG.msgbox.show(data.msg, 5);
						}else{
							ZENG.msgbox.show("删除失败,请联系管理员!", 5);
						}
					}else{
//						$.reloadDocument(true);
						$(".listmsg").show();
						document.location.reload();
					}
				});
//			}
//		});
	}
function sotp(t){
		var $this = $(t);
		var $tr = $this.parents("tr");
		var ul = $tr.parents("table").attr("ul");
		var name = $tr.children("td[t]").text();
		var id = $tr.attr("tid");
//		Showbo.Msg.confirm("您将要停用 ["+name+"] ?",function(b){
//			if(b=='yes'){
				ZENG.msgbox.show("停用中...", 6);
				$.post(config.contextPath+"/cms/"+ul+"/stop/"+id+".do",function(data){
					if (!data.success) {
					ZENG.msgbox.show("停用失败,请联系管理员!", 5);
					}else{
						document.location.reload();
					}
				});
//			}
//		});
}
function start(t){
	var $this = $(t);
	var $tr = $this.parents("tr");
	var ul = $tr.parents("table").attr("ul");
	var name = $tr.children("td[t]").text();
	var id = $tr.attr("tid");
//	Showbo.Msg.confirm("您将要启用 ["+name+"] ?",function(b){
//		if(b=='yes'){
			ZENG.msgbox.show("启用中...", 6);
			$.post(config.contextPath+"/cms/"+ul+"/start/"+id+".do",function(data){
				if (!data.success) {
					ZENG.msgbox.show("启用失败,请联系管理员!", 5);
				}
				$.reloadDocument(true);
			});
//		}
//	});
}

function formatToFixed(id,max){
	var value = $("#"+id).val();
	if(parseFloat(value)>max){
		 showErrorTipMsg($("#"+id),"超限值!");
		 return false;
	}
	if(value=='')return;
	$("#"+id).val(parseFloat(value).toFixed(2));
} 

function setDisabled(id){
	$("#"+id).attr("disabled","disabled");
}

function removeDisabled(id){
	$("#"+id).removeAttr("disabled");
}

/***验证***/




