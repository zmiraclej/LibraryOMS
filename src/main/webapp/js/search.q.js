/**
 * 快速搜索
 */
$(function() {
	$.widget( "custom.catcomplete", $.ui.autocomplete, {//将商品分类分类显示
	    _create: function() {
	        this._super();
	        this.widget().menu( "option", "items", "> :not(.ui-autocomplete-category)" );
	     },
	    _renderMenu: function( ul, items ) {
	      var that = this,
	        currentCategory = "";
	      $.each( items, function( index, item ) {
	        if ( item.category != currentCategory ) {
	          ul.append( "<li class='ui-autocomplete-category'>" + item.category + "</li>" );
	          currentCategory = item.category;
	        }
	        that._renderItemData( ul, item );
	      });
	    }
	  });
	var $st = $(".search_textbox,.dept_search_textbox");
	var ksUrl = $st.attr("ks");
	$st.catcomplete({
      select:function(event, ui){
    	  var form = $("#search_form");
    	  form.find(".search_textbox").val(ui.item.label);
    	  if(ui.item.dept){
    		  form.attr("action",web_res+"/search/dept.html");
    	  }
    	 form.submit();
    	  return;
      },
      source: function(request, response){
    	$.getJSON(ksUrl,{"k":request.term},function(data){
    		response(data);
    	});
      }
    });
  });