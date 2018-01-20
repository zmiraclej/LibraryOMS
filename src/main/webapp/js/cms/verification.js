
/*--------------表单验证----------------*/
var regexps={	
	empty:function(v){//是否为空
		if(!v)return "不能为空！";
		return true;
	},
	isnum:function(v){
		
	}
}

function form(formObj,submitFun){
	var formItem=$(formObj+' .formItem');
	var tmpClass="label"+parseInt(Math.random()*1000);
	$(formObj).attr('labelObj',tmpClass).submit(function(){return false;});
	formItem.attr('pd',1).each(function(i){		
		$(this).attr('Index',i).addClass('Index'+i);
		if(""!=$(this).val())$(this).attr('pd',0);
		var idName='label'+i;
		var offset=$(this).offset();
		var w=$(this).width();
		var h=$(this).height();
		$('body').append('<label id="'+idName+'" Index="'+i+'" class="formlabel '+tmpClass+'"></label>');//生成提示层		
		$('#'+idName).css({
			left:offset.left,top:offset.top,width:w,height:h
		}); 
		if($(this).attr('msg') && !$(this).val()){			
			$('#'+idName).show().html($(this).attr('msg'));
		}
	}).blur(function(){
		var dataType=$(this).attr('dataType'),pd;		
		if(!dataType)return;		
		if(this.tagName!='div' && this.tagName!='DIV'){
			pd=regexps[dataType](this.value,this);
		}else{
			pd=regexps[dataType]($(this).attr('value'),this);			
		}
		if(pd!=true){			
			$(this).val('').attr('pd',1);
			$(this).attr('msg',pd);
			if(!$(this).attr('fun')){				
				var idStr='#label'+$(this).attr('Index');			
				setTimeout(function(){$(idStr).show().html(pd);},100);
			}else{
				new Function($(this).attr('fun'))();
			}
		}else{			 
			$(this).attr('pd',0);			
		}
	});
	
	formItem.focus(function(){
		//获得焦点时全选文本框内容
		if($(this).attr('type')!=("button" || "submit")){			
			selectText(this,0,$(this).val().length);
			/*window.tmpId=$('#label'+$(this).attr('Index'));		
			setTimeout(function(){
				window.tmpId.hide();
			},800);*/
		}
	}).keypress(function(){
		$('#label'+$(this).attr('Index')).hide();
	});
	
	function submitForm(){		
		if(isSubmit())$(formObj).submit();
	}
	
	//是否含有表单提交处理函数
	if(!submitFun){
		/*有就判断表单是否完全通过验证，验证通过再执行submitFun,返回true时再提交表单*/
		$(formObj+' input.btn[type=submit]').bind({keypress:function(){
			if(isSubmit())if(submitFun())$(formObj).submit();			
		},click:function(){
			if(isSubmit())if(submitFun())$(formObj).submit();
		}});
	}else{
		$(formObj+' input.btn[type=submit]').bind({keypress:submitForm,click:submitForm});		
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
		var num=0,tmpObj;	
		$(formObj+' .formItem').each(function(){			
			var type=$(this).attr('type');			
			if(type!=('button') && type !='submit'){
				tmpObj=this;
				num+=parseInt($(this).attr('pd'));			
				if($(this).attr('pd')==1){				
					this.focus();
					return false;
				}
			}
		});
		document.body.focus();
		tmpObj.focus();
		if(num==0)return true; else return false;
	}
	
	return{
		//显示消息
		msg:function(obj,str){
			var i=$(obj).attr('Index');
			$(obj).focus().val('').attr('pd',1);
			$('#label'+i).show().eq(i).html(str);
		},
		isSubmit:isSubmit,
		reset:function(){
			$('label.'+$(formObj).attr('labelObj')).remove();
			form(formObj,submitFun);
		}
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

/*
 * 
//小写转换为大写
function changeCapital(obj){
	obj=$(obj);
	var tmpstr=obj.val();
	obj.val(tmpstr.toLocaleUpperCase());
}

//特殊字符
function isSpecial(self){
	self.value=self.value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'');
}

//只能输入英文
function En(self){
	self.value=self.value.replace(/[^\a-\z\A-\Z]/g,'');	
}

*/