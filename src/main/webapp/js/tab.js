/**
 * 
 */
(function($){
	$(function(){
		var close_but = $("<span class='glyphicon glyphicon-remove-sign click'></span>");
		close_but.click(function(event){
			event.stopPropagation();
			$(this).parents(".tabs-panel").tabs("close",$.trim($(this).parent("a").text()));
			return false;
		});
		$(".nav-tabs>li>a[close='true']").append(close_but);
	});
	var idNum=0;
	$.fn.extend({
		tabs:function(fun,o){
			var r_o;
			this.each(function(){
				var $this = $(this);
				if(fun=="add"){
					var tabs = $this.children(".nav-tabs");
					var tab_html = "<a data-toggle='tab'  ></a>";
					var li = $(tab_html);
					li.text(o.title);
					var id = "tb_"+(idNum++);
					li.attr("href","#"+id);
					if(o.close=="true"||o.close==true){
						li.attr("close","true");
						var but_close = $("<span class='glyphicon glyphicon-remove-sign click'></span>");
						but_close.click(function(event){
							event.stopPropagation();
							var t = $(this).parent("a").text();
							$this.tabs("close",t);
							return false;
						});
						li.append(but_close);
					}
					tabs.append($("<li></li>").append(li));
					var tabContents = $this.children(".tab-content");
					var content = $("<div id='"+id+"' class='tab-pane fade'></div>");
					content.append(o.content);
					tabContents.append(content);
				}else if(fun=="close"){
					var title = o;
					$this.find(".nav-tabs>li:has(a:contains('"+title+"'))").each(function(){
						var $t = $(this);
						if($.trim($t.text())==title){
							var isAction = $t.hasClass("active");
							var $a;
							if(isAction){
								$a = $t.prevAll("li:last").find("a");
							}
							var tp_id = $t.find("a").attr("href");
							$(tp_id).remove();
							$t.remove();
							if(isAction){
								$a.tab("show");
							}
						};
					});
				}else if(fun=="show"){
					$this.find(".nav-tabs>li>a:contains('"+o+"')").each(function(){
						var $t = $(this);
						if($.trim($t.text())==o){
							$t.tab("show");
						}
					});
				}else if(fun=="getTab"){
					$this.find(".nav-tabs>li:has(a:contains('"+o+"'))").each(function(){
						var $t = $(this);
						if($.trim($t.text())==o){
							r_o = $t;
							return;
						}
					});
				}else if(fun="closeAll"){
					$this.find(".nav-tabs>li").remove();
					$this.find(".tab-content>div").remove();
				}
			});
			return r_o;
		}
	});
})(jQuery);
