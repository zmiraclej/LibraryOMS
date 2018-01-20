/**
 * 公共引用
 */
var config = {
		contextPath:"/library"
}
var web_res = config.contextPath;
/**
 * 表单取消，不提交表单
 * @param url
 * @returns {Boolean}
 */
function cancelSubmit(url){
	 location.href =url;
	 return false;
}
$(function(){
	$(".uppercase").keypress(function(e){
		var $this = $(this);
		var key =  parseInt(e.keyCode||e.which)
		if(key&&key>=97&&key>=122){
			$this.val($this.val().toUpperCase());
		}
	});
	$(".bookimg img,.booklist_1_bookimg img,.bookimg_1 img,.simi_bookimg img").each(function(){
		   this.onerror = function(){
			   this.src = config.contextPath+"/images/nopic.jpg";
		       this.onerror = null;
		   }
	});
});
function AddFavorite(sURL, sTitle) {
	try {
		window.external.addFavorite(sURL, sTitle);
	} catch (e) {
		try {
			window.sidebar.addPanel(sTitle, sURL, "");
		} catch (e) {
			alert("加入收藏失败，请使用Ctrl+D进行添加");
		}
	}
}

function SetHome(obj, vrl) {
	try {
		obj.style.behavior = 'url(#default#homepage)';
		obj.setHomePage(vrl);
	} catch (e) {
		if (window.netscape) {
			try {
				netscape.security.PRivilegeManager
						.enablePrivilege("UniversalXPConnect");
			} catch (e) {
				alert("此操作被浏览器拒绝！\n请在浏览器地址栏输入'about:config'并回车\n然后将[signed.applets.codebase_principal_support]设置为'true'");
			}
			var prefs = Components.classes['@mozilla.org/preferences-service;1']
					.getService(Components.interfaces.nsIPrefBranch);
			prefs.setCharPref('browser.startup.homepage', vrl);
		}
	}
}
function showErrorTipMsg(element,msg){
	element.val("");
	//element.inputDefault("setColor","red");
	element.inputDefault("setTipMsg",msg);
	element.focus();
	element.inputDefault("show");
}