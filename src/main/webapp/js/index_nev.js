/**
 * 导航菜单生成
 * @param $
 */
(function($){
	var nav_cfg=[{
		"text":"基础管理",
		"icon":"fa-caret-right",
		"itmes":[{
			"text":"图书",
			"url":"http://www.qq.com"
		}]
	},{
		"text":"加盟店管理",
		"icon":"fa-caret-right",
		"itmes":[{
			"text":"列表",
			"url":config.contextPath+"/cms/dept/list.do"
		},{
			"text":"添加",
			"url":config.contextPath+"/cms/dept/add.do"
		}]
	},{
		"text":"角色管理",
		"icon":"fa-caret-right",
		"itmes":[{
			"text":"加盟店管理",
			"url":"role/manage.html"
		}]
	},{
		"text":"区域管理",
		"icon":"fa-caret-right",
		"itmes":[{
			"text":"管理",
			"url":config.contextPath+"/cms/area/area.do"
		}]
	}
	];

	var nav = $("#navigation");
	var nav_html = "<ul class='nav nav-list'>";
	function appendNavigation(itmes,root){
		if(!root){
			nav_html +="<b class='arrow'></b><ul class='submenu'>";
		}
		for(var i=0;i<itmes.length;i++){
			var itme = itmes[i];
			nav_html += "<li><a href='javascript:void(0)' ";
			if(itme.url) nav_html += "url='"+itme.url+"' "
			if(itme.itmes)nav_html +="class='dropdown-toggle'";//加上这个class表示有下级
			nav_html +=" ><i class='menu-icon fa ";
			if(itme.icon)nav_html += itme.icon;
			//else itmeHtml +="fa-caret-right"
			nav_html += "'></i><span class='menu-text'>"+itme.text+"</span>";
			if(itme.itmes)nav_html +="<b class='arrow fa fa-angle-down'></b>";
			nav_html += "</a>";
			if(itme.itmes){
				appendNavigation(itme.itmes);
			}
			nav_html +="</li>";
		}
		nav_html +="</ul>"
	}
	appendNavigation(nav_cfg, true);
	document.write(nav_html);
	
	
	nav.find("a[url]").click(function(){
		var $this = $(this);
		var title = $(this).text();
		var $tabs = $("#tabs");
		title = $this.parents("li").parents("li").children("a").text()+" / "+title;
		var tab = $tabs.tabs("getTab",title);
		alert($this.attr("url"));
		if(tab){
			$tabs.tabs("show",title);
		}else{
			$tabs.tabs("add",{
				"title":title,
				"close":true,
				"content":"<iframe style='border: 0px;' width='100%' height='99%' src="+$this.attr("url")+"></iframe>"
			});
			$tabs.tabs("show",title);
		}
	});
})($||jQuery);
