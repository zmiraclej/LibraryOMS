function changeSelect(self){
		var par = $(self).find("option:selected").text();
		if(flag == "false"){
			var sheng = $('.n1').find("option:selected").text();var shi = $('.n2').find("option:selected").text();
			par = sheng+="-"+shi+"-"+par;
		}
		par = encodeURI(encodeURI(par,"UTF-8"));
		$.post(url+"/report/vLibraryBook/getMore.do?area="+par+"&t="+Math.random(),function(data){
			$(".select3").html("");
			$(".select2").html("");
			var str ="<option value=''></option>";
			var str2 ="";
			data=JSON.parse(data);
			
			var arrys = data.lib;
			if(arrys.length>0){
				str2 ="<option></option>";
				for (var i = 0; i < arrys.length; i++) {
					str2 += "<option>"+arrys[i]+"</option>";
				}
			}else{
				str2 ="<option value=''></option>";
			}
			$(".select2").html(str2);
			select2=select2.reset();
			//select2.find('.head').html($('.select2').val());
			
			var arr = data.hall;
			for (var i = 0; i < arr.length; i++) {
				str += "<option value='"+arr[i].split("-")[0]+"'>"+arr[i]+"</option>";;
			}
			/*$(".select3").html(str);
			select3=select3.reset();
			select3.find('.head').html($('.select3').val());*/
		});
	}
//开放时间统计查询使用
function changeSelectLightLib(self){
	var par = $(self).find("option:selected").text();
	if(flag == "false"){
		var sheng = $('.n1').find("option:selected").text();var shi = $('.n2').find("option:selected").text();
		par = sheng+="-"+shi+"-"+par;
	}
	par = encodeURI(encodeURI(par,"UTF-8"));
	$.post(url+"/report/vLibraryBook/getMore.do?area="+par+"&t="+Math.random(),function(data){
//		$(".select3").html("");
		$(".select2").html("");
//		var str ="<option value=''></option>";
		var str2 ="";
		data=JSON.parse(data);
		
		var arrys = data.lib;
		if(arrys.length>0){
			str2 ="<option></option>";
			for (var i = 0; i < arrys.length; i++) {
				str2 += "<option>"+arrys[i]+"</option>";
			}
		}else{
			str2 ="<option value=''></option>";
		}
		$(".select2").html(str2);
		select2=select2.reset();
		select2.find('.head').html($('.select2').val());
		
//		var arr = data.hall;
//		for (var i = 0; i < arr.length; i++) {
//			str += "<option value='"+arr[i].split("-")[0]+"'>"+arr[i]+"</option>";;
//		}
		/*$(".select3").html(str);
		select3=select3.reset();
		select3.find('.head').html($('.select3').val());*/
	});
}
//开放时间同价使用查询管号
function changeSelectHall(self){
	var par = $(".select1").find("option:selected").text();
	if(flag == "false"){
		par = $(".select9").find("option:selected").text();
		var sheng = $('.n1').find("option:selected").text();var shi = $('.n2').find("option:selected").text();
		par = sheng+="-"+shi+"-"+par;
	}
	var lib = $(self).find("option:selected").text();
	par = encodeURI(encodeURI(par,"UTF-8"));
	lib = encodeURI(encodeURI(lib,"UTF-8"));
	$.post(url+"/report/vLibraryBook/getMore.do?area="+par+"&lib="+lib+"&t="+Math.random(),function(data){
		$(".select3").html("");
		var str ="<option value=''></option>";
//		var str2 ="";
		data=JSON.parse(data);
//		var arrys = data.lib;
//		if(arrys.length>0){
//			str2 ="<option></option>";
//			for (var i = 0; i < arrys.length; i++) {
//				str2 += "<option>"+arrys[i]+"</option>";
//			}
//		}else{
//			str2 ="<option value=''></option>";
//		}
//		$(".select3").html(str2);
//		select3 = select3.reset();
		//select2.find('.head').html($('.select2').val());
		
		var arr = data.hall;
		for (var i = 0; i < arr.length; i++) {
			str += "<option value='"+arr[i].split("-")[0]+"'>"+arr[i].split("-")[0]+"</option>";;
		}
		$(".select3").html(str);
		select3 = select3.reset();
		select3.find('.head').html($('.select3').val());
	});
}
	
	/*$(".select2").on("change",function(){
		var par = $(".select1").find("option:selected").text();
		if(flag == "false"){
			var sheng = $('.n1').find("option:selected").text();var shi = $('.n2').find("option:selected").text();
			var qu = $('.n3').find("option:selected").text();
			par = sheng+="-"+shi+"-"+qu;
		}
		par = encodeURI(encodeURI(par,"UTF-8"));
		var lib = encodeURI(encodeURI($(this).val(),"UTF-8"));
		$.post(url+"/report/vLibraryBook/getMore.do?area="+par+"&lib="+lib+"&t=new Date()",function(data){
			$(".select3").html("");
			var str ="<option value=''></option>";
			data=JSON.parse(data);
			var arr = data.hall;
			if(arr.length>0){
				for (var i = 0; i < arr.length; i++) {
					str += "<option value='"+arr[i].split("-")[0]+"'>"+arr[i]+"</option>";
				}
			}
			$(".select3").html(str);
			select3=select3.reset();
			select3.find('.head').html($('.select3').val());
		});
	});*/
	
	var s0,c0,q0;
	function getProvinces(self){
		var tmpstr="";
		  $.ajax({
		       url:config.contextPath+"/area/getProvince.html",
		       dataType:"json",
		       success:function(data){
				   for(i in data){ 
					  if(i)tmpstr+=" <option value='"+i+"'>"+data[i]+"</option>";
				   }
				    $(self).next().remove();
					$(self).append(tmpstr);
					s0=setSelect(self);	
		       }
		   });
	}
	
	
	
	//城市
	function getCitys2(self,areas,id){
		//$('.select2').html("<option value=''></option>");
		//$('.select3').html("<option value=''></option>");
		//select2.reset();
		//select3.reset();
		  var tmpstr="<option value=''>市</option>";
		  var provinceId = id;
		  $.ajax({
			   url:config.contextPath+"/area/getCity.html/"+provinceId,
		       dataType:"json",
		       success:function(data){
		    	   for(i in data){ 
					  if(i)tmpstr+=" <option value='"+i+"'>"+data[i]+"</option>";
				   }
				   var cityObj=$(self);
				   cityObj.next().remove();
				   $(cityObj).html('').append(tmpstr);
				   c0=setSelect(cityObj);
				   areas.html("<option value=''>区</option>");
				   areas.next().remove();
				   q0=setSelect(areas);
			    }
		   });
	}

	//地区
	function getAreas2(self,id){
		 //$('.select2').html("<option value=''></option>");
		 //$('.select3').html("<option value=''></option>");
		// select2.reset();
		 //select3.reset();
		  var tmpstr="<option value=''>区</option>";
		  var cityId = id;
		  $.ajax({
			   url:config.contextPath+"/area/getArea.html/"+cityId,
		       dataType:"json",
		       success:function(data){
		    	   for(i in data){ 
					  if(i)tmpstr+=" <option value='"+i+"'>"+data[i]+"</option>";
				   }
				   var areasObj=$(self);
				   areasObj.next().remove();
				   $(areasObj).html('').append(tmpstr);
				   q0=setSelect(areasObj);
			    }
		   });
	}
	
	
	//城市
	function getCitys3(self,areas,id,val){
		//$('.select2').html("<option value=''></option>");
		//$('.select3').html("<option value=''></option>");
		//select2.reset();
		//select3.reset();
		  var tmpstr="<option value=''>市</option>";
		  var provinceId = id;
		  $.ajax({
			   url:config.contextPath+"/area/getCity.html/"+provinceId,
		       dataType:"json",
		       success:function(data){
		    	   for(i in data){ 
					  if(i)tmpstr+=" <option value='"+i+"'>"+data[i]+"</option>";
				   }
				   var cityObj=$(self);
				   cityObj.next().remove();
				   $(cityObj).html('').append(tmpstr);
				   c0=setSelect(cityObj);
				   $('.select8').val(val);
				   c0.reset();
				   getAreas3($('.select9'),val,$.cookie('select9'));
				   //getAreas3()
			    }
		   });
	}
	
	//地区
	function getAreas3(self,id,val){
		 //$('.select2').html("<option value=''></option>");
		 //$('.select3').html("<option value=''></option>");
		// select2.reset();
		 //select3.reset();
		  var tmpstr="<option value=''>区</option>";
		  var cityId = id;
		  $.ajax({
			   url:config.contextPath+"/area/getArea.html/"+cityId,
		       dataType:"json",
		       success:function(data){
		    	   for(i in data){ 
					  if(i)tmpstr+=" <option value='"+i+"'>"+data[i]+"</option>";
				   }
				   var areasObj=$(self);
				   areasObj.next().remove();
				   $(areasObj).html('').append(tmpstr).val(val);
				   q0=setSelect(areasObj);
			    }
		   });
	}
