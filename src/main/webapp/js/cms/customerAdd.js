var testUrl = "";
function del(tid,id){
	$.post(config.contextPath+"/cms/customer/delLib.do?id="+id,function(data){
		$("#"+tid).remove();
		var num=0;
		$("#libraryTab tr").each(function(i){
			if(i!=0)$(this).find('td').eq(0).html(++num);
		})
		
	});
	
}

function sub(action){
	//单位名称			
	var name = $("input[name='name']").val();
	//地址
	var address = $("input[name='address']").val();
	//管理人
	var contactPersonAdmin = $("input[name='users[0].userName']").val();
	//座机
	var telAdmin = $("input[name='users[0].tel']").val();
	//省市区
	var province = $("#province0 option:selected").text();
	var city = $("#city0 option:selected").text();
	var area = $("#area0 option:selected").text();
	//馆别
	var libraryLevel = $("#libraryLevel option:selected").text();
	//馆数
	var libraryNumber = $("input[name='libraryNumber']").val();
	//分配馆号
	var assignCode = $("input[name='assignCode']").val();
	if(province!="省"&&city!="市"&&area!="区"&&libraryLevel!="请选择馆别"&&libraryNumber!=""&&name!=""&&address!=""&&contactPersonAdmin!=""&&telAdmin!=""&&sendPsw==true){
		document.getElementById("form2").submit();
		updataLibInfo($("#libraryIdTwo").val());
		$(".tswords").text("");
	}else if(province=="省"&&city=="市"&&area=="区"&&libraryLevel=="请选择馆别"&&libraryNumber==""&&name!=""&&address!=""&&contactPersonAdmin!=""&&telAdmin!=""){
		document.getElementById("form2").submit();
		$(".tswords").text("保存成功!");
	}else if(action==0){
		document.getElementById("form2").submit();
		if(province!="省"&&city!="市"&&area!="区"&&libraryLevel!="请选择馆别"){
			updataLibInfo($("#libraryIdTwo").val());
		}
		$(".tswords").text("保存成功!");
	}else if(sendPsw==false&&action==1){
		$(".tswords").text("您还未发送短信密码");
	}else{
		$(".tswords").text("您的资料未完善");
	}
}

function updataLibInfo(data){
	//省市区
	var province = $("#province0 option:selected").text();
	var city = $("#city0 option:selected").text();
	var area = $("#area0 option:selected").text();
	var provinceval=$('.select_s0').val();
	var cityval=$('.select_c0').val();
	var areaval=$('.select_q0').val();
	
	//馆别
	var libraryLevel = $("#libraryLevel option:selected").text();
	//馆数
	var libraryNumber = $("input[name='libraryNumber']").val();
	//分配馆号
	var assignCode = $("input[name='assignCode']").val();
	//收费标准
	var chargeStandard = $("input[name='chargeStandard']").val();
	//收费金额
	var chargeMoney = $("input[name='chargeMoney']").val();
	//缴费时间
	var chargeStartDate = $("input[name='chargeStartDate']").val();
	var chargeEndDate = $("input[name='chargeEndDate']").val();
	//合同时间
	var contractStartDate = $("input[name='contractStartDate']").val();
	var contractEndDate = $("input[name='contractEndDate']").val();
	var attachementFile = $("input[name='attachementFile']").val();
	
	//所要更新的图书馆
	if(data!=""){
		$("#libraryTab tr ").each(function(i){
			if(i!=0){
				//找到对应的class tr
				if($(this).hasClass('lib'+data))
				{
					//找到对应的tr的id
					var row = $(this).find('td').eq(0).text();
					//得到服务器返回值
					var bufer = getIfream();
					var arr = bufer.split(",");
					
					var str = "";
					str += "<td>"+row+"</td>";
					str += "<td class='tl' title='' s='"+provinceval+"' c='"+cityval+"' q='"+areaval+"'>"+province+"-"+city+"-"+area+"</td>";
					str += "<td>"+libraryLevel+"</td>";
					str += "<td>"+libraryNumber+"</td>";
					str += "<td>"+assignCode+"</td>";
					str += "<td>"+chargeStartDate+" - "+chargeEndDate+"</td>";
					str += "<td>"+contractStartDate+" - "+contractEndDate+"</td>";
					str += "<input type='hidden' value='"+chargeStandard+"'></input>";
					str += "<input type='hidden' value='"+chargeMoney+"'></input>";
					if(arr[2]!=""||arr[2]!=null)
						str += "<td><a href='"+testUrl+"/cms/customer/download.do?fileName="+attachementFile+"'><img src='"+testUrl+"/images/download.png'/></a></td>";
					else 
						str += "<td><a href='#'><img src='"+testUrl+"/images/download_1.png'/></a></td>";
					str += "<td><a href='javascript:void(0)' onclick=\"editLibrary('"+arr[1]+"')\"><img src='"+testUrl+"/images/yun_back_img2.gif'/></a>"
					str += "<a href='javascript:void(0)' onclick=\"del('tt"+row+"',"+arr[1]+");return false;\" ><img src='"+testUrl+"/images/yun_back_img3.gif'/></a></td>"
					
					$(this).html(str);
				}
			}
			
		})
	}
}

function loadHtml(){
	var cal = document.getElementById('iframeBox').contentWindow.document.body.innerHTML
	if(cal == ""){
		return;
	}
	var arr = cal.split(" split");
	$(".inHtml").html(arr[0]);
	$("#customerId").val(arr[1]);
}
//获取里面的值
function getIfream(){
	return document.getElementById('iframeBox').contentWindow.document.body.innerHTML
}

function loadown(uu){
	testUrl = uu;
	var cal = document.getElementById('iframeBox').contentWindow.document.body.innerHTML
	//第一次加载进来，阻止进行向下执行
	if(cal==""){
		return;
	}
	
	//省市区
	var province = $("#province0 option:selected").text();
	var city = $("#city0 option:selected").text();
	var area = $("#area0 option:selected").text();
	var provinceval=$('.select_s0').val();
	var cityval=$('.select_c0').val();
	var areaval=$('.select_q0').val();
	
	//馆别
	var libraryLevel = $("#libraryLevel option:selected").text();
	//馆数
	var libraryNumber = $("input[name='libraryNumber']").val();
	//分配馆号
	var assignCode = $("input[name='assignCode']").val();
	//收费标准
	var chargeStandard = $("input[name='chargeStandard']").val();
	//收费金额
	var chargeMoney = $("input[name='chargeMoney']").val();
	//缴费时间
	var chargeStartDate = $("input[name='chargeStartDate']").val();
	var chargeEndDate = $("input[name='chargeEndDate']").val();
	//合同时间
	var contractStartDate = $("input[name='contractStartDate']").val();
	var contractEndDate = $("input[name='contractEndDate']").val();
	
	var arr = cal.split(",");
	//得到这个列数
	var row = $("#libraryTab tr").length;
	var str = "";
	str += "<tr id=tt"+row+" class='lib"+arr[1]+" li'><td>"+row+"</td>";
	str += "<td class='tl' title='' s='"+provinceval+"' c='"+cityval+"' q='"+areaval+"'>"+province+"-"+city+"-"+area+"</td>";
	str += "<td>"+libraryLevel+"</td>";
	str += "<td>"+libraryNumber+"</td>";
	str += "<td>"+assignCode+"</td>";
	str += "<td>"+chargeStartDate+" - "+chargeEndDate+"</td>";
	str += "<td>"+contractStartDate+" - "+contractEndDate+"</td>";
	str += "<input type='hidden' value='"+chargeStandard+"'></input>";
	str += "<input type='hidden' value='"+chargeMoney+"'></input>";
	str += "<td><a href='"+uu+"/cms/customer/download.do?fileName="+arr[2]+"'><img src='"+uu+"/images/download.png'/></a></td>"
	str += "<td><a href='javascript:void(0)' onclick=\"editLibrary('"+arr[1]+"')\"><img src='"+uu+"/images/yun_back_img2.gif'/></a>"
	str += "<a href='javascript:void(0)' onclick=\"del('tt"+row+"',"+arr[1]+");return false;\" ><img src='"+uu+"/images/yun_back_img3.gif'/></a></td></tr>"
	//如果图书馆别为空的话，就是没有添加图书馆
	if(arr[1] != "" && arr[3]=="true" ){
		resetLibrary()
		$("#libraryTab").append(str);
		$("#masterId").val(arr[0]);
		$("#customerId").val(arr[0]);
		$("#customerContactId").val(arr[2]);
		$("#userId").val(arr[5]);
		$("#customerContactId").val(arr[4]);
		
	}else{
		resetLibrary();
		str="";
	}
	
}

function resetLibrary(){
	 $(".formLibary").find("input[type='text']").val("");
	 $(".select_s0 option").removeAttr('selected').eq(0).attr('selected','selected');
	 $(".select_c0").html("<option value=''>市</option>");
	 $(".select_q0").html("<option value=''>区</option>");
	 $(".select_gb option").removeAttr('selected').eq(0).attr('selected','selected');
	  s0.reset();
	  c0.reset();
	  q0.reset();
	  gb.reset();
	  $("#customerId").val(customerId);
	  $("#libraryId").val("");
	  $("#libraryEdit").hide();
	  $("#librarySave").show();
	  delfile($(".filename"));
	 // sendPsw =false;
	removeDisabled('librarySave'); 
	$("input[name='libraryNumber']").removeAttr("readonly").removeClass("disabled");
}


