//解决IE8 report模块不识别@media Query的问题。
$(document).ready(function(){
	function setIquery(){
		if(!+[1,]) {
			var width = document.body.clientWidth;
			var head = $("head");
			var link = document.createElement("link");
			var rel = document.createAttribute("rel");
			var node=document.createAttribute("class");
			node.value="add";
			link.setAttributeNode(node);
			rel.value="stylesheet";
			rel.value = "stylesheet";

			var href = document.createAttribute("href");
			if(width>1480){
				href.value="css/ie/report/ie_style.css";

				link.setAttributeNode(href);
				link.setAttributeNode(rel);
				head.append(link);
			} else if (width > 1200 && width < 1479) {
				href.value = "css/ie/report/ie1479_style.css";

				link.setAttributeNode(href);
				link.setAttributeNode(rel);
				head.append(link);
			} else if (width > 980 && width < 1199) {
				href.value = "css/ie/report/ie1199_style.css";

				link.setAttributeNode(href);
				link.setAttributeNode(rel);
				head.append(link);
			} else if (width > 768 && width < 979) {
				href.value = "css/ie/report/ie979_style.css";

				link.setAttributeNode(href);
				link.setAttributeNode(rel);
				head.append(link);
			} else if(width<768){
				href.value="css/ie/report/768_style.css";

				link.setAttributeNode(href);
				link.setAttributeNode(rel);
				head.append(link);
			}
		}
	}
	setIquery();
	$(window).resize(function(){
		$("link.add").remove();
		setIquery();
	})
})




