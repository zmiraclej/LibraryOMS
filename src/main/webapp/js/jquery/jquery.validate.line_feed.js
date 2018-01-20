/**
 * 
 */
(function($){
	$.fn.lineFeedValidate = function(fun){
		$.validator.setDefaults({
			onkeyup:false,
			onclick:false,
			Onubmit:false,
			onfocusout:false
		});
		this.each(function(){
			$(this).find("input[index],select[index],textarea[index]").keypress(function(e){
				var $form = $(this).parents("form");
				var maxIndex = $form.find("input[index],textarea[index],select[index]").not(":disabled").length;
			
				var key =  e.keyCode||e.which;
				if(key=="13"||key=="9"){
					var $input = $(this);
					if($input.valid()){
						var index =parseInt($input.attr("index"));
						do {
							var nextIndex = (index+1);
							var $nextInput = $form.find("input[index='"+nextIndex+"'],textarea[index='"+nextIndex+"'],select[index='"+nextIndex+"']");
							index = nextIndex;
							if(fun){
									var r = fun(index,$nextInput);
									if(r==false){
										return false;
									}
							}
						} while (($nextInput.length==0&&nextIndex<maxIndex)||$nextInput.attr("disabled")||$nextInput.attr("readonly"));
						if($nextInput.length>=1){
							if($nextInput.is("select")){
								//$nextInput.focus();
								pullSelect($nextInput);
							}else if($nextInput.is("input[type='submit']")){
								$nextInput.focus();
							}else if($nextInput.is("input[type='button']")){
								$nextInput.focus();
							}else{
								$nextInput.select();
							}
							return false;
						}
						return true;
					}else{
						if($input.inputDefault){
							$input.inputDefault("show");
						}
						return false;
					}
					return true;
				}
			});
		});
	}
})($);
