/**
 * 持卡用户登录
 */
(function(){
	var $from = $("#c-login");
	var $cardNumber = $from.find("input[name='cardNumber']");
	var $deptCode = $from.find("input[name='deptCode']")
	var $cardPasssword = $from.find("input[name='cardPasssword']");
	$deptCode.keypress(function(e){
		var key = window.event ? e.keyCode : e.which;
		$deptCode.val($deptCode.val().toUpperCase());
		if(key=="13"){
			if(valiDeptCode($deptCode.val().toUpperCase())){
				$cardNumber.focus();
			}
			return false;
		}
	});
	$cardNumber.keypress(function(e){
		var key = window.event ? e.keyCode : e.which;
		if(key=="13"){
			if(valiCardNumber($cardNumber.val())){
				$cardPasssword.focus();
			}
			return false;
		}
	});
	$cardPasssword.keypress(function(e){
		var key = window.event ? e.keyCode : e.which;
		if(key=="13"){
			if(valiCardPasssword($cardPasssword.val())){
				return true;
			}
			return false;
		}
		return true;
	});
	
	$from.submit(function(){
		$cardNumber.attr("tipMsg","");
		var deptCode = $.trim($deptCode.val());//加盟店编号
		var cardNumber = $.trim($cardNumber.val());//卡号
		var cardPasssword = $.trim($cardPasssword.val());//密码
		if(!valiDeptCode(deptCode)||!valiCardNumber(cardNumber)||!valiCardPasssword(cardPasssword)){
			return false;
		}
		cardPasssword = $.base64Encode(cardPasssword);
		var p = {
			"deptCode":deptCode,
			"cardNumber":cardNumber,
			"cardPasssword":cardPasssword
		}
		$.ajaxSetup({async:false});
		$.post($(this).attr("action"),p,function(data){
			$.ajaxSetup({async:true});
			if(data==1){
				showErrorTipMsg($deptCode,"平台号不存在");
				return false;
			}
			if(data==2){
				showErrorTipMsg($cardNumber,"用户名错误");
				showErrorTipMsg($cardPasssword,"密码错误");
				$deptCode.select();
				return false;
			}
			window.location.href = config.contextPath+"/user/index.html";
		});
		return false;
	});
	/**
	 * 验证平台号
	 */
	function valiDeptCode(deptCode){
		if(!deptCode||deptCode==""){
			showErrorTipMsg($deptCode,"平台号不为空");
			return false;
		}
		if(!/^[A-Z]{4}$/g.test(deptCode)){
			showErrorTipMsg($deptCode,"平台号错误");
			return false;
		}
		var isCheck = true;
		$.ajaxSetup({async:false});
		$.post(config.contextPath+"/login/checkDeptcode.do",{"deptCode":deptCode},function(data){
			$.ajaxSetup({async:true});
			if(!data){
				isCheck = false;
			}
		})
		if(!isCheck){
			showErrorTipMsg($deptCode,"平台号错误");
			return false;
		}
		return true;
	}
	/**
	 * 验证卡号
	 */
	function valiCardNumber(cardNumber){
		if(!cardNumber||cardNumber==""){
			showErrorTipMsg($cardNumber,"卡号不能为空");
			return false;
		}
		if(cardNumber.length<6){
			var appendLenght = 6-cardNumber.length;
			var appendStr = ""
			for(var i=0;i<appendLenght;i++){
				appendStr +="0";
			}
			cardNumber = appendStr + cardNumber;
			$cardNumber.val(cardNumber);
		}
		return true;
	}
	function valiCardPasssword(cardPasssword){
		if(!cardPasssword||cardPasssword==""){
			showErrorTipMsg($cardPasssword,"密码不能为空");
			return false;
		}
		return true;
	}
})($);