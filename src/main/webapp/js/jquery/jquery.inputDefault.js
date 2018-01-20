/*
Name		输入框提示
Version		V1.0
*/

(function($){
	$.fn.inputDefault = function(options,o){
		if($.type(options)=="string"){
			if(options=="setColor"){
				var $label = this.next('label');
				$label.css("color",o);
			}else if(options=="show"){
				this.each(function(){
					var $label = $(this).next('label');
					$label.show();
				});
			}else if(options=="setTipMsg"){
				this.each(function(){
					var $this = $(this);
					var $label = $this.next('label');
					$label.text(o);
					$this.attr(options.attrName,o)
				});
			}else if(options=="hide"){
				var $label = $(this).next('label');
				$label.hide();
			}
		}else{
			var defaults = {attrName: 'tipMsg', size:0, bold: false, italic:false, color:'#CCC'};
			var options = $.extend(defaults, options);
			this.each(function(){
				var $this = $(this);
				var text = $this.attr(options.attrName);
			
				var offset = $this.position();
				
				var outerWidth = $this.outerWidth();
				var outerHeight = $this.outerHeight();
				
				var innerWidth = $this.innerWidth();
				var innerHeight = $this.innerHeight();
				
				var plusLeft = (outerWidth - innerWidth)/2;
				var plusTop = (outerHeight - innerHeight)/2;
				var textAlign = $this.css("textAlign");
				var paddingTop = parseInt($this.css('paddingTop'));
				var paddingRight = parseInt($this.css('paddingRight'));
				var paddingBottom = parseInt($this.css('paddingBottom'));
				var paddingLeft = parseInt($this.css('paddingLeft'));
				var width = $this.width();
				var height = $this.height();
				/*if(window.navigator.userAgent.indexOf("Chrome")=="-1"){
				}else{
					var width = innerWidth - paddingRight;
					var height = innerHeight - paddingBottom;
				}*/
				
				var top = offset.top + plusTop;
				var left = offset.left + plusLeft;
				
				var lineHeight = $this.css('lineHeight');
				var display = $this.val() ? 'none' : 'block';
				
				var fontSize = options.size ? options.size : $this.css('fontSize');
				var fontStyle = options.italic ? 'italic' : '';
				var fontWeight = options.bold ? '700' : $this.css('fontWeight');
				var css = {position:'absolute', fontSize: fontSize, fontWeight:fontWeight, fontStyle:fontStyle, lineHeight:lineHeight, display:display,paddingTop:paddingTop, paddingBottom:paddingBottom,cursor:'text', width:innerWidth,height:innerHeight,top:top, left:left, color:options.color, overflow:'hidden',textAlign:textAlign,"zIndex":99};
				var lable = $("<label></label>").text(text).css(css).click(function(){
					$(this).hide();
					$(this).prev().focus();
				});
				$this.next("label").remove();
				$this.after(lable);
			}).focus(function(){
				var $this = $(this);
				var $label = $(this).next('label');
				$label.hide();
			}).blur(function(){
				var $this = $(this);
				var $label = $(this).next('label');
				if(!$this.val()) $label.show();
			}).keydown(function(){
				var $this = $(this);
				var $label = $(this).next('label');
				$label.hide();
			});
		}
	}
	$(function(){
		var $input = $("input[tipMsg],select[tipMsg]");
		window.setTimeout(function(){
			$input.inputDefault();
		}, 700);
		$(window).resize(function(){
			$input.inputDefault();
		});	
	});
})(jQuery);

function initTipMsg(){
	var $input = $("input[tipMsg],select[tipMsg]");
	window.setTimeout(function(){
		$input.inputDefault();
	}, 700);
	$(window).resize(function(){
		$input.inputDefault();
	});	
}