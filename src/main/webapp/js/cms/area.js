$(function(){
	getProvinces('#province0');
	var city = $("#city");
});

var s0;
function getProvinces(self){
	  var tmpstr="";
	  $.ajax({
	       url:config.contextPath+"/area/getProvince.html",
	       dataType:"json",
	       success:function(data){
		   for(i in data){ 
			  if(i)tmpstr+=" <option value='"+i+"'>"+data[i]+"</option>";
		   }
				$(self).append(tmpstr);
				s0=setSelect(self);
				
	       }
	   });
}

//城市
function getCitys(self,id){
	  var tmpstr="<option value=''>市</option>";
	  var provinceId = id;
	  $.ajax({
		   url:config.contextPath+"/area/getCity.html/"+provinceId,
	       dataType:"json",
	       success:function(data){
	    	   for(i in data){ 
				  if(i)tmpstr+=" <option value='"+i+"'>"+data[i]+"</option>";
			   }
			   var cityObj=$(self).next().next();
			   cityObj.next().remove();
			   $(cityObj).html('').append(tmpstr);
			   c0=setSelect(cityObj);			   
			   var areas=cityObj.next().next();
			   areas.html("<option value=''>区</option>");
			   areas.next().remove();
			   q0=setSelect(areas);
		    }
	   });
}

//地区
function getAreas(self,id){
	  var tmpstr="<option value=''>区</option>";
	  var provinceId = id;	  
	  $.ajax({
		   url:config.contextPath+"/area/getArea.html/"+provinceId,
	       dataType:"json",
	       success:function(data){
	    	   for(i in data){ 
				  if(i)tmpstr+=" <option value='"+i+"'>"+data[i]+"</option>";
			   }
			   var cityObj=$(self).next().next();
			   cityObj.next().remove();
			   $(cityObj).html('').append(tmpstr);
			   q0=setSelect(cityObj);
		    }
	   });
}

