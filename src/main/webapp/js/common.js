/*
 * 自定义下拉列表控件
 * 通过ul列表模拟下拉列表样式
 */
var setSelectIdNum=0;
function setSelect(selectobj,fun){
	var html;
	var obj=$(selectobj),option=obj.find('option'),name=setSelectIdNum,w=obj.attr('width');
	obj.attr('ulId',"abc"+name);
	setSelectIdNum++;
	var val=obj.val(),tmpval,tmphead,labValue;
	var isup=$(selectobj).attr("isup");
	/*if(isup==='1'){
		html='<ul class="selectObj" id="abc'+name+'" value="'+val+'" isup="1">';
	}else if(isup==undefined){
		 html='<ul class="selectObj" id="abc'+name+'" value="'+val+'">';
	}*/
	
	if(isup==='1'){
		if(obj.hasClass("disabled")){
			html='<ul class="selectObj disabled" id="abc'+name+'" value="'+val+'" isup="1">';
		}else{
			html='<ul class="selectObj" id="abc'+name+'" value="'+val+'" isup="1">';
		}

	}else if(isup==undefined){
		if(obj.hasClass("disabled")) {
			html='<ul class="selectObj disabled" id="abc'+name+'" value="'+val+'">';
		}else{
			html='<ul class="selectObj" id="abc'+name+'" value="'+val+'">';
		}

	}

	
	//html+='<input type="text l w230" value=""  name="roleIds" />';
	html+='<li class="head">'+option.eq(0).html()+'</li><li class="son"><ul>';
	for(var i=0;i<option.length;i++){
		tmpval=option.eq(i).attr("value");
		if(tmpval==val){
			html+='<li class="selected" value="'+tmpval+'">'+option.eq(i).text()+'</li>';
			tmphead=option.eq(i).text();
		}else{
			html+='<li value="'+tmpval+'">'+option.eq(i).text()+'</li>';
		}
	}
	html+='</ul></li><ul>';
	obj.after(html).hide();
	obj=$("#abc"+name);
	var head=obj.find('.head');
	var li=obj.find('ul li');
	var son=obj.find('.son');
	if(w){
		if(!+[1,]){
			w=parseInt(w)+2;
		}
		obj.width(w);
		son.width(w-2);
		head.css({backgroundPosition:w-20+"px center"});
	}
	if(tmphead)head.html(tmphead);
	//if(!$(selectobj).hasClass('disabled')){
	//	obj.hover(function(){
	//		if($(selectobj).attr('isup')==1){
	//			son.css({top:-son.height(),borderTop:"solid 1px #b9b9b9"});
	//		}
	//		if(!$(selectobj).hasClass('disabled'))son.show();
	//	},function(){
	//		son.hide();
	//	});
	//}

	li.click(function(){
		var tmpval=this.getAttribute('value');
		var tmpstr=$(this).html();
		obj.attr('value',tmpval);
		$(selectobj)[0].value=tmpval;
		head.html(tmpstr);
		if($(selectobj)[0].fireEvent){
			$(selectobj).val(tmpval)[0].fireEvent('onchange');
		}else{
			var event = document.createEvent('HTMLEvents');
			event.initEvent("change", true, true);
			$(selectobj).val(tmpval)[0].dispatchEvent(event);
		}


		li.removeClass('selected');
		$(this).addClass('selected');
		son.hide();
	});


	//重置菜单
	obj.reset=function(){
		$('#'+$(selectobj).attr('ulId')).remove();
		return setSelect(selectobj,fun);
	};
	/*
	 $(selectobj)[0].prototype.reset=function(){
	 obj.remove();
	 return setSelect(selectobj,fun);
	 }
	 */
	return obj;
	/*eg:
	 var s=setSelect('#select');
	 s=s.reset();//刷新菜单
	 */
}
//动态绑定为selectObj添加一个click事件。
$(document).on("click",".selectObj",function(e){
	e.stopPropagation();
	var t=$(this);
	var isUp= t.attr("isup");
	var son= t.find(".son");
	var height= son.height();
	//点击当前selectObj将其它所有的selectObj的son隐藏。
	var selectObj=$(".selectObj").not(t);
	selectObj.find(".son").hide();

	if(!t.hasClass("disabled")){
		if(son.css("display")=="list-item"){
			son.hide();
		}else{
			if(isUp==1){
				son.css({top:-height,borderTop:"solid 1px #b9b9b9"});
				son.find(".son").show();
			}
			son.show();
		}
	}
});
//为模拟select的option项动态绑定click事件，并且阻止事件冒泡。
$(document).on("click",".son li",function(e){
	e.stopPropagation();
	$(this).parents(".son").hide();
});
//在document之中绑定一个click事件,点击之后模拟的option隐藏。
$(document).click(function(e){
	var target=e.target.nodeName;
	//统计页面显示下拉菜单
	if (target=='INPUT') {
		return;
	} else {
		$(".selectObj").find(".son").hide();
	}
});

$(function(){
	//云图书馆页面顶部搜索条
	$('.main2 input.text').attr('alt',0).focus(function(){
		if($(this).attr('alt')==0){
			$(this).val('').css({color:'#000'}).attr('alt',1);	
		}
	}).blur(function(){
		if($(this).val()==''){
			$(this).val('图书、图书馆').attr('alt',0).css({color:'#999'});
		}
	});
	
	//各图书馆页面顶部搜索条
	$('.search2 input.text').attr('alt',0).focus(function(){
		if($(this).attr('alt')==0){
			$(this).val('').css({color:'#000'}).attr('alt',1);	
		}
	}).blur(function(){
		if($(this).val()==''){
			$(this).val('图书名称').attr('alt',0).css({color:'#999'});
		}
	});
});


function setDisabledById(id){
	$("#"+id).find('input.text,.selectObj,.rolelistdiv').addClass("disabled").attr("disabled","disabled");
}
function removeDisabledById(id){
	$("#"+id).find('input.text,.selectObj,.rolelistdiv').removeClass("disabled").removeAttr("disabled");
}

function isEmpty(formId){		
	var list=$(formId).find('input.text,input.hidden,select');
	var tmpobj,is=true;
	for(var i=0;i<list.length;i++){
		tmpobj=list.eq(i);
		var rq =tmpobj.attr("rq");
		if(rq!=undefined)continue;
		if(tmpobj.val()==""||tmpobj.val()==null){				
			is=tmpobj[0];
			break;
		}
	}
	return is;
}

//加载地图api
//document.write('<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=EmWvece0GHuKBjnfjSm2DwOO"></script>');
//显示地图
setMap=function(option){
	/*
		 option={
		 	point:[104.711182,31.541041],
		 	title:'西科大图书馆',
		 	address:'地址：四川省绵阳市涪城区青龙大道中段59号'
		 }
	*/
	if($('.mapdiv').length==0){
		var html='<div class="mapdiv">\
		<img src="'+config.contextPath+'/images/yun_back_img5.gif" class="close" onclick="$(\'.mapdiv,.bg\').hide();"/>\
		<div class="maptitle"></div>';
		html+='<div id="allmap"></div></div><div class="bg"></div>';
		$('body').append(html);
	}
	$('.mapdiv').show().find('.close').show();
	$('.bg').show();
	$('.mapdiv .maptitle').hide();
	
	var point=new BMap.Point(option.point[0],option.point[1]);
	var map = new BMap.Map("allmap");    // 创建Map实例
	var marker = new BMap.Marker(point);  // 创建标注
	map.centerAndZoom(point, 17);  // 初始化地图,设置中心点坐标和地图级别
	map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
	//map.setCurrentCity("北京");          // 设置地图显示的城市 此项是必须设置的
	map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放			
	map.addOverlay(marker);		
	var opts = {
	  width : 150,     // 信息窗口宽度
	  height: 80,     // 信息窗口高度
	  title : option.title // 信息窗口标题
	}
	var infoWindow = new BMap.InfoWindow(option.address, opts);  // 创建信息窗口对象
	if(option.address || option.title){
		marker.addEventListener("click", function(){
			map.openInfoWindow(infoWindow,point); //开启信息窗口
		});
	}
}

function pullSelect($obj){
	if($obj.tagName=='SELECT'){
		$($obj).next().find('.son').show();
		return false;
	}
}

function resetSelect(id){
	$('#'+id).next().remove();
	setSelect('#'+id);
}


/*选择多个角色 start*/
$('html').click(function(){
	$('.checkboxlist').hide();
});	
function setrolelist(obj){
	var datainput=$(obj).find('.hidden');
	var list=$(obj).find('.checkboxlist');
	list.width($(obj).width());
	$(obj).css({backgroundPosition:$(obj).width()-20+"px center"});
	$(obj).mouseover(function(){
		if(!$(this).hasClass("disabled"))list.show();
	});
	
	function setlist(){
		var str="",str2="",pd=true;
		list.find('input').each(function(){
			if(this.checked==true && pd){				
				str=$(this).parent()[0].innerText;
				str2=$(this).val();
				pd=false;
			}
			
//			if(this.checked==true){
//				str+=$(this).parent()[0].innerText+',';
//				str2+=$(this).val()+",";
//			}
		});	
		$(obj).find('div').html(str);
		datainput.val(str2);
		//str=str.substr(0,str.length-1);
		//str2=str2.substr(0,str2.length-1);
		//$(obj).find('div').html(str);
		//datainput.val(str2);
	}
	
	list.find('li').click(function(event){
		event.stopPropagation();
		var tmpObj=$(this).find('input');
		tmpObj.click();
		
	}).find('input').change(function(){
		setTimeout(function(){
			setlist();
			list.hide();
			
		},100);
	}).click(function(event){
		event.stopPropagation();
	});
	setlist();
	return {reset:setlist};
}



function setCheckboxList(obj){
	var datainput=$(obj).find('.hidden');
	var list=$(obj).find('.checkboxlist');
	list.width($(obj).width());
	$(obj).css({backgroundPosition:$(obj).width()-20+"px center"});
//	$(obj).mouseover(function(){
//		if(!$(this).hasClass("disabled"))list.show();
//	});
	$(obj).click(function(e){
		e.stopPropagation();
		if(list.is(":visible")){
			if(!$(this).hasClass("disabled"))list.hide();
		}else{
			if(!$(this).hasClass("disabled"))list.show();
		}

	});
	function setlist(){
		var str="",str2="";
		list.find('input').each(function(){
			if(this.checked==true){				
				str+=$(this).parent()[0].innerText+',';
				str2+=$(this).val()+',';
				pd=false;
			}
		});	
		str=str.substr(0,str.length-1);
		str2=str2.substr(0,str2.length-1);
		$(obj).find('div').html(str);
		datainput.val(str2);
	}
	
	list.find('li').click(function(event){
		event.stopPropagation();
		var tmpObj=$(this).find('input');
		tmpObj.click();
		
	}).find('input').change(function(){
		setTimeout(function(){
			setlist();
		},100);
	}).click(function(event){
		event.stopPropagation();
	});
	setlist();
	return {reset:setlist};
}

function stopMP(e){
	e=e || widnow.e;
	e.stopPropagation();		
}

/*选择多个角色 end*/	

//验证手机号
function validatePhone(tel){
	var reg = /^0?1[3|4|5|7|8][0-9]\d{8}$/;
	if(reg.test(tel))return true;else return false;
}
function getwinHeight(){
    var winHeight=0;
    if (window.innerHeight)
        winHeight = window.innerHeight;
    else if ((document.body) && (document.body.clientHeight))
        winHeight = document.body.clientHeight;
    //对body进行检测，获取浏览器窗口高度
    if (document.documentElement && document.documentElement.clientHeight)
        winHeight = document.documentElement.clientHeight;
	return winHeight;
}
function actionDo(divId, heights, eachheights,hrefs){
	var windowHeight = getwinHeight();
	 var pageSize = Math.floor((windowHeight - heights)/eachheights);
	 if(pageSize <= 4) pageSize = 4;
	 if(hrefs.indexOf("?")!=-1) $(divId).attr('href', hrefs + '&pageSize=' + pageSize);
	 else $(divId).attr('href', hrefs + '?pageSize=' + pageSize);
}
function subimitDo(divId, heights, eachheights,hrefs){
	var windowHeight = getwinHeight();
	 var pageSize = Math.floor((windowHeight - heights)/eachheights);
	 if(pageSize <= 4) pageSize = 4;
	 $("#pageSize").val(pageSize);
	 $(divId).attr('action', hrefs);
	 //if(hrefs.indexOf("?")!=-1) $(divId).attr('action', hrefs + '&pageSize=' + pageSize);	 else 

}