<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${functionName}管理</title>
<link href="${r"${res}"}/assets/css/chosen.css" rel="stylesheet">
<style type="text/css">
div.area_menu>ul {
	height: 100%;
	padding-top: 10px;
	padding-bottom: 40px;
}
div.area_menu>ul>li:first-child {
	float: left;
}

div.area_menu>ul>li {
	width: 195px;
	overflow-x: auto;
	list-style: outside none none;
	height: 100%;
}

div.area_menu>ul>li:last-of-type {
	position: absolute;
	top:10px;
	right: 0px;
	bottom: 0px;
}

div.area_menu>ul>li:last-of-type>div:HOVER {
	background-color: #8AAFCE;
	cursor: pointer;
}

div.area_menu {
	width: 400px;
}

.roles-chosen label {
	padding-left: 20px;
}
#roles_chosen{
	width: 100% !important;
}
.btn_postion{padding-left:36%;
  margin-top: 20px;
  margin-bottom: 20px;}
</style>
<script type="text/javascript" src="${r"${res}"}/assets/js/bootstrap-tag.js"></script>
<script type="text/javascript" src="${r"${res}"}/js/cms/classNumber.json"></script>
<script type="text/javascript">
	$(function() {
		initValidate();
	});
	function initValidate() {//初始化表单验证
		$.ajaxSetup({
			async : false
		});
		$('#form').lineFeedValidate();
		$('#form').validate({
		 	//debug: true, //调试模式取消submit的默认提交功能   
            focusInvalid: true, //当为false时，验证无效时，没有焦点响应  
            onkeyup: false,   
			rules:{
           		 "code":{
                    "required":true
                },
                startClassNumber:{
                    required:true
                },
                endClassNumber:{
                    required:true
                },
                "layerNumber":{
                	 "required":true,
                    "max":10,
                    "digits":true
                },
                "maxStockNum":{
                	 "required":true,
                    "max":200,
                    "digits":true
                }
			},
			messages : {
				"code" : {
					"required":"排架号不能为空"
				},
				"startClassNumber":{
					"required":"开始分类号不能为空"
				},
				"endClassNumber":{
					"required":"结束分类号不能为空"
				},
				"layerNumber":{
					"required":"请输入正确的货架数"
				},"maxStockNum":{
					"required":"请输入正确的货架存放数"
				}
			},
			"errorPlacement" : function(error, element) {
				showErrorTipMsgs(element, error.text());
			}
			
		});
	}
	
	function showErrorTipMsgs(element,msg){
		element.val("");
		element.inputDefault("setTipMsg",msg);
	}
	
	function backHostory(){
		window.history.back(-1);
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<legend>${functionName}</legend>
			</div>
		</div>
		<div class="row-fluid context">
			<div class="span12">
				<form class="form-horizontal" id="form"
					action="${res}/warehouse/save.do" method="post"
					autocomplete="off"  onsubmit="false">
					<fieldset>
						<div class="form-group">
							<div>
								<label class="control-label col-xs-12 col-lg-4 no-padding-right"
									for="code">排架号:</label>
								<div class="col-xs-12 col-lg-4">
									<div class="clearfix">
										<div class="input_panel">
											<input type="text"  tipMsg="" name="code"
												id="code" class="col-xs-12 col-sm-12" 
												value='${((wh.code)!)?html}'/>
										</div>
									</div>
								</div>
							</div>
						</div>
					<div class="form-group">
							<div>
								<label class="control-label col-xs-12 col-lg-4 no-padding-right"
									for="code">架数:</label>
								<div class="col-xs-12 col-lg-4">
									<div class="clearfix">
										<div class="input_panel">
											<input type="text"  tipMsg="" name="layerNumber"
												id="layerNumber" class="col-xs-12 col-sm-12" 
												value='${((wh.layerNumber)!)?html}'/>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div>
								<label class="control-label col-xs-12 col-lg-4 no-padding-right"
									for="code">架存放数:</label>
								<div class="col-xs-12 col-lg-4">
									<div class="clearfix">
										<div class="input_panel">
											<input type="text"  tipMsg="" name="maxStockNum"
												id="maxStockNum" class="col-xs-12 col-sm-12" 
												value='${((wh.maxStockNum)!)?html}'/>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="btn_postion" >
						
							<button class="btn btn-prev btn-success" id="submitsssss" type="submit">
								提交 <i class="glyphicon glyphicon-ok"></i>
							</button>
					
							<button class="btn btn-next"  id="reverts"  type="button" onclick="backHostory()">
								返回 <i class="glyphicon glyphicon-remove"></i>
							</button>
						
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
