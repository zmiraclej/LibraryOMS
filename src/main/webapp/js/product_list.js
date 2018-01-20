/**
 * 
 */
$(function() {
		/**
		 * 下拉选择跳转页面
		 */
		$(".droplist li a[href]").click(function() {
			var o = $.getUrlParamObject($(this).attr("href"));
			goToPate(1, o);
			return false;
		});
		/**
		 * 显示出当前下拉选择的内容
		 */
		$(".inner li[action]").each(function(i){
			var $this =  $(this);
			$this.parents("li").find("div.price a").text($this.text());
			$this.remove();
		});
		$(".price a").click(function(){return false;});
		$('.inner ul li').hover(function(){
			if($(this).find('ul.droplist').is(':hidden')){
				$(this).find('ul.droplist').show();
			$(this).siblings().find('ul.droplist').hide();
				}
			else if(!$(this).find('ul.droplist').is(':hidden')){
				$(this).find('ul.droplist').hide();
				}
			
		})
	})