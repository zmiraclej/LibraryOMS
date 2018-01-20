function readMac(){
	var locator = new ActiveXObject ("WbemScripting.SWbemLocator");
	var service = locator.ConnectServer(".");
	var properties = service.ExecQuery("SELECT * FROM win32_networkadapterconfiguration");
	var e = new Enumerator (properties);
	for (;!e.atEnd();e.moveNext ())
      {
            var p = e.item ();
            if (p.IPAddress==null) {
                 continue;
            }
			return p.MACAddress;
    }
}
var hallCode,userName,password,valid;
$.ajaxSetup({
	async: false
})
$(function(){
	var $form = $("form");
	$hallCode = $form.find("input[name='hallCode']");
	$userName = $form.find("input[name='userName']");
	$password = $form.find(".pwd");

	 hallCode = $hallCode.val();
	 userName = $userName.val();
	 password = $password.val();

	$mac = $form.find("input[name='mac']");
	$form.lineFeedValidate();
	$form.validate({
		errorPlacement: function(error, element) {
			showErrorTipMsg(element,error.html());
		},rules:{
			"hallCode":{
				"required":true,
				"regex":new RegExp("^[a-z]{4}$","i"),
				"remote":{
					"url":config.contextPath+"/common/checkDeptcode.html",
					"type":"post",
					"dataType":"json"
				}
			},"userName":{
				"required":true
			},"password":{
				"required":true
			}
		},messages:{
			"hallCode":{
				"required":"馆号不能为空",
				"regex":"馆号错误",
				"remote":"馆号不存在"
			},"userName":{
				"required":"用户名不能为空"
			},"password":{
				"required":"密码错误"
			}
		},submitHandler:function(from){
			ZENG.msgbox.show("登录中....", 6);
			$form.ajaxSubmit(function(data){
				var json = JSON.parse(data);
				ZENG.msgbox.hide();
				if(json.data=="0"){
					window.location.href = config.contextPath+"/common/index.html";
				}else if(json.data=="2"){
					showErrorTipMsg($userName,"用户不存在!");
				}else if(json.data=="3"){
					showErrorTipMsg($password,"密码错误!");
				}else if(json.data=="4"){
					showErrorTipMsg($hallCode,"图书馆未启用!");
				}else if(json.data=="5"){
					showErrorTipMsg($userName,"当前用户没有登录权限!");
				}else if(json.data=="6"){
					showErrorTipMsg($userName,"用户未启用!");
				}else if(json.data=="7"){
					showErrorTipMsg($userName,"用户没有权限在当前电脑登录!");
				}else if(json.data=="8"){
					showErrorTipMsg($hallCode,"重复登录,是否强制登录?");
					showErrorTipMsg($userName,"重复登录,是否强制登录?");
					showErrorTipMsg($password,"重复登录,是否强制登录?");
					$("#login").hide();
					$("#relogin").show();
					$("#confirmBtn").focus();
				}else if(json.data=="9"){
					if(json.validCode){
						$("#validDiv").show();
						initTipMsg();
					}
					setTimeout(function(){
						showErrorTipMsg($("#validCode"),"验证码错误!");
					}, 200);
					$("#validDiv").focus();
				}else{
					showErrorTipMsg($hallCode,"系统错误请联系管理员!");
				}
			});
			return false;
		}	
		});
});

function setValue(){
	var $form = $("form");
	$hallCode = $form.find("input[name='hallCode']");
	$userName = $form.find("input[name='userName']");
	$password = $form.find(".pwd");
	 hallCode = $hallCode.val();
	 userName = $userName.val();
	 password = $password.val();
}

$("#validCode").keyup(function(){
	var k = window.event.keyCode;
	if((9 == k || 13 == k) && 0 == this.value.length){
		showErrorTipMsg($(this),config.errorMsg.nullError);
	}
	if( (9 == k || 13 == k) && 0 !=  this.value.length){
		$("#loginBtn").focus();
	}
});

$("#confirmBtn").keyup(function(){
	var k = window.event.keyCode;
	if(37 == k || 39 == k){
		$("#cancelBtn").focus();
	}
});
$("#cancelBtn").keyup(function(){
	var k = window.event.keyCode;
	if(37 == k || 39 == k){
		$("#confirmBtn").focus();
	}
});
