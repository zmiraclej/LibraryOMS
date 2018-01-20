<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>${functionName}管理</title>
<style type="text/css">

div.area_menu > ul{
	height: 100%;
	padding-top: 10px;
	padding-bottom: 40px;
}
div.area_menu > ul >li:first-child{
	float: left;
}
div.area_menu > ul >li{
	width: 195px;
	overflow-x:auto;
	list-style: outside none none;
	height: 100%;
}
div.area_menu >ul >li:last-of-type{
	position:absolute;
	right:0px;
	bottom: 0px;
}
div.area_menu >ul >li:last-of-type > div:HOVER{
	background-color: #8AAFCE;
	cursor: pointer;
}
div.area_menu{
	position:absolute;
	z-index:10;
	width: 400px;
}
.widget-box>.table-responsive{
	overflow: visible;
}

</style>
<script type="text/javascript">
	$(function() {
	});
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
    	return false;
    }
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box widget-color-blue2 gray-theme-border">
					<div class="widget-header gray-theme-bkg">
						<h6 class="widget-title">
							${functionName}管理
						</h6>
						<div class="widget-toolbar" >
							<a href="#" data-action="reload" title="刷新" class="but_refurbish">
								<i class="ace-icon fa fa-refresh bigger-140"></i>
							</a>
 							 <a href="${r"${res}"}/${urlPrefix}/form" id="but_editAndSave" data-action="reload" title="新增">
								<i class="glyphicon glyphicon-plus bigger-140 white"></i>
							</a>
						</div>
					</div>
 					<div class="table-responsive" > 
 						<form action="${r"${res}"}/${urlPrefix}/list.do" id="searchForm"> 
 							<input id="pageNo" name="pageNo" type="hidden" value="${r"${page.pageNo}"}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${r"${page.pageSize}"}"/>
 							<div class="toolbar"> 
 								<input tipMsg="${functionName}"  type="text" name="search" value="${r"${(className.name!)?html}"}"> 
 								<button type="submit" class="btn btn-purple btn-sm but-search"> 
 									搜索 <i class="glyphicon glyphicon-search bigger-110"></i> 
 								</button> 
 							</div> 
 						</form> 
 					</div> 
					<div class="widget-body">
						<div class="table-responsive">
							<table class="table table-bordered" ul="warehouse">
								<thead>
									<tr>
										<th width="10%">序号</th>
										<th width="10%">操作</th>
									</tr>
								</thead>
								<tbody>
									<#list page.list as oo>
										<tr tid="${"${"+className+".id}"}">
										<td><a href="${r"${ctx}"}/${urlPrefix}/form?id=${"${"+className+".id}"}">${"${"+className+".name}"}</a></td>
											<td>
												<a class="blue"
													href="${r"${ctx}"}/${urlPrefix}/form?id=${"${"+className+".id}"}" title="编辑">
														<i class="ace-icon fa fa-pencil bigger-130"></i>
												</a>&nbsp; <a class="red" href="#"
													onclick="del(this)" title="删除">
														<i class="ace-icon fa fa-trash-o bigger-130"></i>
												</a>
											</td>
										</tr>
									</#list>	
								</tbody>
							</table>
						</div>
						<div align="center">${r"${page}"}</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>