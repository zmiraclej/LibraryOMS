/**
 * 高级搜索
 */
$(function() {
		var g = "gscurrentin";
		if($(".searchin .gslistin ul li .gscurrentin").length==0){
			g = "gscurrent";
		}
		//选项卡切换
		$(".searchin .gslistin ul li,.searchinfo .gslist ul li").click(
				function() {
					var $this = $(this);
					$(".searchin .gslistin ul li .gscurrentin,.searchinfo .gslist ul li .gscurrent").removeClass(
							g);
					$this.children("span").addClass(g);
					var index = $this.attr("index") || 0;
					$(".gsboxs .gsbox,.gsboxs .gstext").hide();
					$(".gsboxs .gsbox:eq("+index+"),.gsboxs .gstext:eq("+index+")").show();
				});
		//价钱范围选择
		var $pricetype = $("input[name='pricetype']");
		$pricetype.filter("input:not(:checked)").closest("td").siblings("td").find("input").attr("disabled","disabled");
		$pricetype.change(function(){
			$pricetype.filter("input:not(:checked)").closest("td").siblings("td").find("input").val("").attr("disabled","disabled");
			$(this).closest("td").siblings("td").find("input").removeAttr("disabled");
		});
		
		initFromValidate();
		initDateSelect();
		//$(".ui-select").selectmenu();
		initAreaSelect();
		$(".gsboxs .gsbox,.gsboxs .gstext").hide();
		var index = $(".searchin .gslistin ul li:has(span.gscurrentin),.searchinfo .gslist ul li:has(span.gscurrent)").attr("index");
		$(".gsboxs .gsbox:eq("+index+"),.gsboxs .gstext:eq("+index+")").show();
	});
	//form表单验证
	function initFromValidate(){
		jQuery.validator.addMethod("compare",function(value, element,o){
			var targetValue = o.target.val()||"";
			value = value||"";
			var compare = o.isRall?">":"<";
			if(value==""||targetValue==""){
				return true;
			}
			var $this = $(element);
			targetValue = parseFloat(targetValue);
			value = parseFloat(value);
			return eval("value"+compare+"targetValue");
		});
		var $form = $("form");
		$form.lineFeedValidate();
		$form.each(function(){
			$(this).validate({
				rules:{
					"price_low":{
						"compare":{
							"target":$(this).find("input[name='price_tall']"),
							"isRall":false
						}
					}
				},onkeyup:false,onfocusout:false,onclick:false,
				errorPlacement:function(error,element){
					//showErrorTipMsg(element,error.html());
					alert(error.html());
				},messages:{
					"price_low":{
						"compare":"价钱范围输入不合法！请重新输入！"
					}
				}
			});
		});
		$("form").submit(function(){
			var $this = $(this);
			var low = $this.find("select[name='discount_low']").val()||"";
			var tall = $this.find("select[name='discount_tall']").val()||"";
			var low = parseFloat(low);
			var tall = parseFloat(tall);
			if(low>tall){
				alert("折扣选择不合法！请重新选择！");
				return false;
			}
			
			var ylow = parseFloat($this.find("select[name='ylow']").val());
			var mlow = parseFloat($this.find("select[name='mlow']").val());
			var ytall = parseFloat($this.find("select[name='ytall']").val());
			var mtall = parseFloat($this.find("select[name='mtall']").val());
			if(((!ylow||ylow=="")&&(mlow&&mlow!=""))||((!ytall||ytall=="")&&(mtall&&mtall!=""))){
				alert("时间范围选择不合法!请重新选择！");
				return false;
			}
			if(ylow>ytall||(ylow<=ytall&&mlow>mtall)){
				alert("时间范围选择不合法!请重新选择！");
				return false;
			}
			return true;
		});
		$("input[name='price_low']").keyup(filterPrice);
		$("input[name='price_tall']").keyup(filterPrice);
	}
	//商品价格验证
	function filterPrice() {
	  var $this = $(this);
	  var price = $this.val();
	  var re = /^[^0-9]/i;
	  if (re.test(price)) {
		  $this.val('');
	    return false;
	  }
	  price = $this.val();
	  if (price.length > 15) {
	    var new_value = price.substr(0, 15);
	    $this.attr('value', new_value);
	  };
	  re = /[^\d|\.]/i;
	  price = $this.val();
	  if (re.test(price)) {
	    var new_value = price.substr(0, price.search(re));
	    $this.val(new_value);
	  }
	  re = /\.[0-9]{3}/i;
	  price = $this.val();
	  if (re.test(price)) {
	    var new_value = price.substr(0, price.indexOf('.') + 3);
	    $this.val(new_value);
	    return false;
	  }
	  re = /\.[0-9]*\.+/i;
	  price = $this.val();
	  if (re.test(price)) {
	    var new_value = price.substr(0, price.indexOf('.') + 1);
	    if (!isNaN(price.substr(price.indexOf('.') + 1, 1))) {
	      new_value = price.substr(0, price.indexOf('.') + 2);
	      if (!isNaN(price.substr(price.indexOf('.') + 2, 1))) {
	        new_value = price.substr(0, price.indexOf('.') + 3);
	      }
	    }
	    $this.val(new_value);
	    return false;
	  }
	  return false;
	}
	//区域选择
	function initAreaSelect(){
		var $sf = $("#tb_area_sel select:eq(0)");
		if($sf.length==0) return;
		var sfId = $sf.attr("sel")||"";
		$sf.FillOptions(web_res+"/area/loadInfo/.do", {
			"datatype" : "json",
			"textfield" : $.trim("name"),
			"valuefiled" : "id"
		});
		$sf.AddOption("省", "", true, 0)
		$sf.val(sfId);
		//$sf.val("11111111");
		$sf.change(
				function() {
					var $this = $(this);
					var sid = $this.val() || "";
					var $cs = $("#tb_area_sel select:eq(1)");
					var $qy = $("#tb_area_sel select:eq(2)");
					$qy.html("");
					if (sid == "") {
						$cs.html("");
					} else {
						$cs.FillOptions(web_res+"/area/loadInfo/" + $.trim($this.val())
								+ ".do", {
							"datatype" : "json",
							"textfield" : "name",
							"valuefiled" : "id"
						});
					}
					$cs.AddOption("市", "", true, 0)
					$qy.AddOption("区", "", true, 0)
				});
		$sf.change();
		var $cs = $("#tb_area_sel select:eq(1)");
		var csId = $cs.attr("sel")||"";
		$cs.val(csId);
		$cs.change(
				function() {
					var $this = $(this);
					var sid = $this.val() || "";
					var $qy = $("#tb_area_sel select:eq(2)");
					if (sid == "") {
						$qy.html("");
					} else {
						$qy.FillOptions(web_res+"/area/loadInfo/" + $this.val()
								+ ".do", {
							"datatype" : "json",
							"textfield" : "name",
							"valuefiled" : "id"
						});
					}
					$qy.AddOption("区", "", true, 0)
				});
		$cs.change();
		var $qy = $("#tb_area_sel select:eq(2)");
		var qyId = $qy.attr("sel")||"";
		$qy.val(qyId);
	}
	//时间选择
	function initDateSelect(){
		var $discount_scope = $(".discount_scope select");
		$discount_scope.each(function() {
			var $this = $(this);
			var num= parseInt($this.attr("sel"));
			for (var i = 1; i <= 9; i++){
				$this.AddOption(i + "折", i*10, num==i, i);
			}
		});
		var $y_sel = $(".y_td select");
		var date = new Date();
		var year = date.getFullYear();
		$y_sel.each(function() {
			var $this = $(this);
			var j = 0;
			var num= parseInt($this.attr("sel"));
			for (var i = year; i >= 1970; i--) {
				j++;
				$this.AddOption(i, i, num==i, j);
			};
		});
		var $m_sel = $(".m_td select");
		$m_sel.each(function() {
			var $this = $(this);
			var num= parseInt($this.attr("sel"));
			for (var i = 1; i <= 12; i++) {
				$this.AddOption(i, i,num==i, i);
			}
		});
	}