/**
 *  Ajax Form plugins
 *  
 *  Copyright (c) gaoyang 2011
 *  Dual licensed under the MIT and GPL licenses:
 *  http://www.opensource.org/licenses/mit-license.php
 *  http://www.gnu.org/licenses/gpl.html
 *	@author gaoyang（高阳）
 *	@email yunnysunny@gmail.com
 *	@version 0.9
 */

/**
* get JSON object from the given str
* 
* @param Unknown str The string to transform or just the json object
* @return Object str The Object you get
*/
 function getObjectFromStr(str) {
	return (typeof(str) == "object") ? str : eval('(' + str + ')');
}

 /**
 * Set a url string with random data to enforces refreshing the request of ajax
 * @param String url The original url
 * @return String The random url
 */
function getRandUrl(url) {
	var newUrl = '';
	if (url.indexOf('?') == -1) {
		newUrl = url + '?rand=' + Math.random();
	} else {
		newUrl = url + '&rand' + Math.random();
	}
	return newUrl;
}
/**
* Set the form's data requested from server side via ajax
*
* @param String fromUrl The address you wanna request
* @param String toUrl The new action attribute of the form(can be null)
* @param String useJson The json's name form the server(can be null) 
* @param Object options The addition data you can add to form's data(can be null)
*/
jQuery.fn.setWebData = function(fromUrl,toUrl,useJson,options) {
	var formNow = $(this);
	var inputs = $(this).find('input,select,radio,textarea');
	var len = inputs.length;
	$.post(getRandUrl(fromUrl),{},function(webData) {
		webData = getObjectFromStr(webData);
		if (options) {
			$.extend(webData,options);
		}
		//alert(webData[useJson]);
		for (var i=0;i<len;i++) {
			var objectNow = $(inputs[i]);
			if (objectNow.attr('type') != 'hidden') {
				objectNow.val('');
			}
			var keyNow = objectNow.attr('name');//
			if (useJson && webData[useJson]) {//有json对象
				var element = eval('webData.' + keyNow);
				if (element) {
					objectNow.val(element);
					//alert(keyNow);
				}
			}
			if (webData[keyNow]) {
				objectNow.val(webData[keyNow]);
				//alert(keyNow);
			}
			
		}
		//showInput();//自定义函数，可删除
		$(inputs[0]).focus();
		if (toUrl) {
			formNow.attr('action',getRandUrl(toUrl));
		}
	});
	
	return false;
}
/**
* Send the form's data via ajax
*
* @param Function callback The response function called when ajax request is success
* @param Object options The options add to ajax's request parameters(can be null)
* @param String type Ajax's request type,can be 'xml','text','json'(can be null)
*/
jQuery.fn.submit = function(callback,options,type) {	
	//var formNow = $(this).get(0).form;
	var formNow = $(this).get(0);
	var url = $(this).attr('action');
	var inputs = $(formNow).find('input,select,radio,textarea');
	var len = inputs.length;
	var data = {};
	
	if (len > 0 || options)
	{
		for (var i=0;i<len;i++)
		{
			var inputNow = $(inputs[i]);
			var typeNow = inputNow.attr('type');
			if (typeNow != 'button' && typeNow != 'submit' && typeNow != 'reset')
			{
				var name = inputNow.attr('name');//name属性
				var value = inputNow.val();
				data[name] = value;
				//alert(name + ':' + value);
			}				
		}
		if (options)
		{			
			$.extend(data,options);
		}

		url = getRandUrl(url);

		if (type)
		{
			$.post(url,data,callback,type);
		}
		else 
		{
			$.post(url,data,callback);
		}		
	}	
	return false;
}
