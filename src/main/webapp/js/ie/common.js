//解决IE8不识别@media Query的问题。

$(document).ready(function(){
	function setIquery(){
	if(!+[1,]){
		var width=document.body.clientWidth;
		var head=$("head");
		var link=document.createElement("link");
		var rel=document.createAttribute("rel");
		var node=document.createAttribute("class");
		node.value="add";
		link.setAttributeNode(node);
		rel.value="stylesheet";

		var href=document.createAttribute("href");
		if(width>1720){
			href.value="../../css/ie/ie_style.css";

			link.setAttributeNode(href);
			link.setAttributeNode(rel);
			head.append(link);
		}else if(width>1580&&width<1720){
			href.value="../../css/ie/ie1720_style.css";

			link.setAttributeNode(href);
			link.setAttributeNode(rel);
			head.append(link);
		}
		else if(width>1480&&width<1580){
			href.value="../../css/ie/ie1580_style.css";

			link.setAttributeNode(href);
			link.setAttributeNode(rel);
			head.append(link);
		} else if(width>1480&&width<1719){
			href.value="../../css/ie/ie1719_style.css";

			link.setAttributeNode(href);
			link.setAttributeNode(rel);
			head.append(link);
		}else if(width>1380&&width<1480){
			href.value="../../css/ie/ie1479_style.css";

			link.setAttributeNode(href);
			link.setAttributeNode(rel);
			head.append(link);
		} else if(width>1200&&width<1380){
			href.value="../../css/ie/ie1380_style.css";

			link.setAttributeNode(href);
			link.setAttributeNode(rel);
			head.append(link);
		}else if(width>980&&width<1199){
			href.value="../../css/ie/ie1199_style.css";

			link.setAttributeNode(href);
			link.setAttributeNode(rel);
			head.append(link);
		}else if(width>768&&width<979){
			href.value="../../css/ie/ie979_style.css";

			link.setAttributeNode(href);
			link.setAttributeNode(rel);
			head.append(link);
		}else if(width<768){
			href.value="../../css/ie/ie768_style.css";

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





