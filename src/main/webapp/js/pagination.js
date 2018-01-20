/**
 * 分页
 */
(function($){
	$(function(){
		$(".pager .pagination a[href]").click(function(){
  			var href = $(this).attr("href");
  			var o = $.getUrlParamObject(href);
  			var url = $.setUrlParam(o);
  			$.reloadDocument(url);
  			return false;
  		});
		var $pagerBut = $(".pager .pagination input[type='button']");
		$pagerBut.click(function(){
			goToPate($(this).prev("input").val());
  			return false;
  		});
		$pagerBut.prev("input").keypress(function(e){
			var key =  e.keyCode||e.which;
			if(key=="13"){
				goToPate($(this).val());
				return false;
			}
		});
		$(".page span.pageleft a").click(function() {
			goToPate(parseInt($(".pager li.action").text()) - 1);
			return false;
		});
		$(".page span.pageright a").click(function() {
			goToPate(parseInt($(".pager li.action").text()) + 1);
			return false;
		});
	});
})(jQuery);
function goToPate(pageNum,param){
	pageNum = $.trim(pageNum);
	pageNum = parseInt(pageNum);
	if(pageNum<1||isNaN(pageNum)){
		pageNum=1;
	}
	
	var pageNumTatli = $(".pager .pagination li").length-3;
	if (pageNum>pageNumTatli) {
		pageNum = pageNumTatli;
	}
	param = $.extend(param,{"pageNum":pageNum});
	url = $.setUrlParam(param);
	$.reloadDocument(url);
}