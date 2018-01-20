var vcity = { 11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古",
    21: "辽宁", 22: "吉林", 23: "黑龙江", 31: "上海", 32: "江苏",
    33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南",
    42: "湖北", 43: "湖南", 44: "广东", 45: "广西", 46: "海南", 50: "重庆",
    51: "四川", 52: "贵州", 53: "云南", 54: "西藏", 61: "陕西", 62: "甘肃",
    63: "青海", 64: "宁夏", 65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外"
}

checkCard=function(value){
    var str = true;
    var card = value;
    //是否为空
    if (card === ''){str = "身份证号";}else
    //校验长度，类型
    if (isCardNo(card) === false){str = "身份证号错误！";}else
    //检查省份
    if (checkProvince(card) === false){str = "身份证号错误！";}else
    //校验生日
    if (checkBirthday(card) === false){str = "身份证号错误！";}else
    //检验位的检测
    if (checkParity(card) === false) {str = "身份证号错误！";}
    return str;
}

//检查号码是否符合规范，包括长度，类型
isCardNo = function(card) {
    //身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
    var reg = /(^\d{15}$)|(^\d{17}(\d|X)$)/;
    if (reg.test(card) === false) {
        return false;
    }
    return true;
}

//取身份证前两位,校验省份
checkProvince = function(card) {
    var province = card.substr(0, 2);
    if (vcity[province] == undefined) {
        return false;
    }
    return true;
}

//检查生日是否正确
checkBirthday = function(card) {
    var len = card.length;
    //身份证15位时，次序为省（3位）市（3位）年（2位）月（2位）日（2位）校验位（3位），皆为数字
    if (len == '15') {
        var re_fifteen = /^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/;
        var arr_data = card.match(re_fifteen);
        var year = arr_data[2];
        var month = arr_data[3];
        var day = arr_data[4];
        var birthday = new Date('19' + year + '/' + month + '/' + day);
        return verifyBirthday('19' + year, month, day, birthday);
    }
    
    //身份证18位时，次序为省（3位）市（3位）年（4位）月（2位）日（2位）校验位（4位），校验位末尾可能为X
    if (len == '18') {

        var Wi = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
        ArrCheckSum = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2');

        Re18Digital = /(\d{1})(\d{1})(\d{1})(\d{1})(\d{1})(\d{1})(\d{1})(\d{1})(\d{1})(\d{1})(\d{1})(\d{1})(\d{1})(\d{1})(\d{1})(\d{1})(\d{1})([0-9xX]{1})/;
        Arr = Re18Digital.exec(card);
        if (Arr == null) {
            return false;
        }
        Sum = 0;
        for (i = 0; i <= 16; i++)Sum += Arr[i + 1] * Wi[i];

        strCheckSum = ArrCheckSum[Sum % 11];
        if (!(strCheckSum == Arr[18].toLocaleUpperCase()))
            return false;

        var re_eighteen = /^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X|x)$/;
        var arr_data = card.match(re_eighteen);
        var year = arr_data[2];
        var month = arr_data[3];
        var day = arr_data[4];
        var birthday = new Date(year + '/' + month + '/' + day);
        return verifyBirthday(year, month, day, birthday);
    }
    return false;
}

//校验日期
verifyBirthday = function(year, month, day, birthday) {
    var now = new Date();
    var now_year = now.getFullYear();
    //年月日是否合理
    if (birthday.getFullYear() == year && (birthday.getMonth() + 1) == month && birthday.getDate() == day) {
        //判断年份的范围（3岁到100岁之间)
        var time = now_year - year;
        if (time >= 3 && time <= 100) {
            return true;
        }
        return false;
    }
    return false;
}

//校验位的检测
checkParity = function(card) {
    //15位转18位
    card = changeFivteenToEighteen(card);
    var len = card.length;
    if (len == '18') {
        var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
        var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2');
        var cardTemp = 0, i, valnum;
        for (i = 0; i < 17; i++) {
            cardTemp += card.substr(i, 1) * arrInt[i];
        }
        valnum = arrCh[cardTemp % 11];
        if (valnum == card.substr(17, 1)) {
            return true;
        }
        return false;
    }
    return false;
}

//15位转18位身份证号
changeFivteenToEighteen = function(card) {
    if (card.length == '15') {
        var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
        var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2');
        var cardTemp = 0, i;
        card = card.substr(0, 6) + '19' + card.substr(6, card.length - 6);
        for (i = 0; i < 17; i++) {
            cardTemp += card.substr(i, 1) * arrInt[i];
        }
        card += arrCh[cardTemp % 11];
        return card;
    }
    return card;
}



/*--------------表单验证----------------*/
var regexps={
	IDCard:checkCard,//身份证号检测
	psw:function(v,o){//密码验证
		if(v==""){
			if($(o).attr('alt')!=""){
				return $(o).attr('alt');
			}else{
				return "密码不能为空！";
			}
		}
		return true;
	},
	pswn:function(v,o){//密码验证
		if(v==""){
			if($(o).attr('alt')!=""){
				return $(o).attr('alt');
			}else{
				return "新密码不能为空！";
			}
		}
		if(v.length!=6)return "密码为6位数字！";
		return true;
	},
	psw2:function(v){//旧密码验证
		if(v==""){
			if($(o).attr('alt')!=""){
				return $(o).attr('alt');
			}else{
				return "旧密码不能为空！";
			}
		}
		return true;
	}, 
	pswempty2:function(v,o){//新密码一致性验证
		var psw=$('.pswtext');
		if(v==""){
			if($(o).attr('alt')!=""){
				return $(o).attr('alt');
			}else{
				return "新密码不一致！";
			}
		}
		if(v.length==6 && psw[0].value==psw[1].value){
			return true;
		}else{
			return "新密码不一致！";
		}
	},
	year:function(v){//年份验证 只能为公元1000到当前年份	
		if(v!=""){
			var tmpDate=new Date();
			y=tmpDate.getFullYear();
			if(v.length==4){
				if(v<1000 || v>y){
					 return "年份错误！";
				}
			}else return "年份错误！";
		}
		return true;
	},
	isbn:function(v){//新密码一致性验证
		if(v!=""){
			if(v.length==13)return true;else return "书号错误！";
		}
		return true;
	},
	guanhao:function(v){//馆号验证
		if(v!=""){
			if(v.length==4)return true;else return "馆号错误！";
		}
		return true;
	},
	libraryName:function(v){//馆名验证
		if(v!=""){
			var r=/^[\u2E80-\u9FFF]+$/;
			if(!r.test(v))return "馆名错误";
		}
		return true;
	},
	empty:function(v){//新密码一致性验证
		if(v==""){
			return "不能为空！";
		}
		return true;
	}
}

var ispd=false;
function form(formObj,submitFun){
	var formItem=$(formObj+' .form-item');
	formItem.attr('pd',1).each(function(i){			
		$(this).attr('Index',i).addClass('Index'+i);
		var idName='label'+i;
		var offset=$(this).offset();
		var w=$(this).width();
		var h=$(this).height();
		$('body').append('<label id="'+idName+'" Index="'+i+'" class="formlabel"></label');//生成提示层		
		$('#'+idName).css({
			left:offset.left,top:offset.top,width:w,height:h
		});
		if($(this).attr('alt')!=''){
			$('#'+idName).show().html($(this).attr('alt'));
		}		
	}).blur(function(){
		var fun=$(this).attr('validate'),pd;
		if(!fun)return;		
		pd=regexps[fun](this.value,this);
		if(ispd==true)return;
		if(pd!=true){
			$(this).val('').attr('pd',1);			
			var idStr='#label'+$(this).attr('Index');			
			$(idStr).show().html(pd);			
		}else{
			$(this).attr('pd',0);
		}
	});
	formItem.focus(function(){
		//获得焦点时全选文本框内容
		if($(this).attr('type')!=("button" || "submit")){
			selectText(this,0,$(this).val().length);		
		}
	});
	
	formItem.keyup(function(event){//模拟回车
		ispd=false;
		$('#label'+$(this).attr('Index')).hide();//隐藏提示
		var fun=$(this).attr('validate'),pd;
		event=event || window.event;		
		if(event.keyCode==13|| event.keyCode==40){
			if(fun){
				ispd=true;
				pd=regexps[fun](this.value,this);		
				if(pd!=true){
					$(this).val('').attr('pd',1);
					var offset=$(this).offset();
					var w=$(this).width();
					var h=$(this).height();
					var idStr='#label'+$(this).attr('Index');			
					$(idStr).show().html(pd);
					return;
				}else{
					$(this).attr('pd',0);
				}
			}
			var index=parseInt($(this).attr('Index'))+1;
			if(index>=formItem.length)index=0;
			$('.Index'+index).show().focus();
		}else if(event.keyCode==38){
			if(fun){
				ispd=true;
				pd=regexps[fun](this.value,this);		
				if(pd!=true){
					$(this).val('').attr('pd',1);
					var offset=$(this).offset();
					var w=$(this).width();
					var h=$(this).height();
					var idStr='#label'+$(this).attr('Index');			
					$(idStr).show().html(pd);
					return;
				}else{
					$(this).attr('pd',0);
				}
			}
			var index=parseInt($(this).attr('Index'))-1;
			if(index<0)index=formItem.length-1;
			$('.Index'+index).show().focus();
		}
	});
	
	function submitForm(){
		if(isSubmit()){
			$(formObj).submit();
		}
	}
	
	//是否含有表单提交处理函数
	if(submitFun){
		/*有就判断表单是否完全通过验证，验证通过再执行submitFun,返回true时再提交表单*/
		$(formObj+' .submit-btn').bind({keypress:function(){
			if(isSubmit())if(submitFun())$(formObj).submit();			
		},click:function(){
			if(isSubmit())if(submitFun())$(formObj).submit();
		}});
	}else{
		$(formObj+' .submit-btn').bind({keypress:submitForm,click:submitForm});
	}
	
	//点击提示语
	$('.formlabel').click(function(){
		$(this).hide();
		var obj=formItem.eq($(this).attr('index'));		
		var type=obj.attr('type');
		if(type!=('button') && type !='submit'){
			obj.focus();
		}
	});
	//判断表单元素是否全部通过验证
	function isSubmit(){
		var num=0;
		formItem.each(function(){
			var type=$(this).attr('type');
			if(type!=('button') && type !='submit'){
				num+=parseInt($(this).attr('pd'));
			}
		});
		if(num==0)return true; else return false;
	}
	
	return {
		//显示消息
		msg:function(obj,str){
			var i=$(obj).attr('Index');
			$(obj).focus().val('').attr('pd',1);
			$('#label'+i).show().eq(i).html(str);
		},
		isSubmit:isSubmit
	}
}



//判断输入的数字
function iscard(event){
	var e=window.event || event;
	var keyNum=e.keyCode;
	if((keyNum>=96 && keyNum<=105) ||keyNum==13 ||keyNum==8 ||keyNum==9 ||keyNum==46 ||keyNum==88|| (keyNum>=48 && keyNum<=57)|| (keyNum>=37 && keyNum<=40)){
		if(event.shiftKey==1)return false;
		return true;
	}else{
		return false;
	}
}

//判断输入的数字
function isnum(event){	
	var e=window.event || event;
	var keyNum=e.keyCode;
	if((keyNum>=96 && keyNum<=105) ||keyNum==13 ||keyNum==8 ||keyNum==9 ||keyNum==46 || (keyNum>=48 && keyNum<=57)|| (keyNum>=37 && keyNum<=40)){
		if(event.shiftKey==1)return false;
		return true;
	}else{
		return false;
	}
}

//选中文本
function selectText(textbox,startIndex,stopIndex){	
    if(textbox.setSelectionRange){
	       textbox.setSelectionRange(startIndex,stopIndex); 
	}else if(textbox.createTextRange){
     	var range=textbox.createTextRange();
		range.collapse(true);
		range.moveStart('character',startIndex);
		range.moveEnd('character',stopIndex-startIndex);
		range.select();
	}
}

//小写转换为大写
function changeCapital(obj){
	obj=$(obj);
	var tmpstr=obj.val();
	obj.val(tmpstr.toLocaleUpperCase());
}

//特殊字符
function isSpecial(self){
	self.value=self.value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'')
}

//只能输入英文
function En(self){
	self.value=self.value.replace(/[^\a-\z\A-\Z]/g,'');	
}

