	//统计分析和排行榜获取屏幕高度显示条数
	function autoHeight(){
		//alert($(window).height());
		//alert($('.form').height());
		var screenHeight = parseInt($(window).height()) - 140 - 70 - 36;
		//alert(screenHeight);
		var ss = Math.floor(screenHeight/36);
		//alert(ss);
		return ss;
	}