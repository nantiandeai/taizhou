/**
 * 数字展馆
 */
$(function(){
	//艺术分类
	$('.categoryChange >.row >.adrList > span> a').each(function(idx){
		$(this).unbind('click.sz').bind('click.sz', function(e){
			e.preventDefault();
			//当前样式的类型
			var one = $(this).parent('span').hasClass("active");
			//不等于  
			if (!one) {
				//点击的事件加载样式,其他删除
				$(this).parent('span').addClass("active").siblings().removeClass('active');
				//加载数据
				loadArtContent();
			}
		});
	});//艺术分类结束
	
	//搜索
	$('#selectBtn').unbind('click.sz').bind('click.sz', function(e){
		e.preventDefault();
		loadArtContent();
	});
	
	//回车事件
	$("body").keydown(function() {
		var isFocus = $("#select").is(":focus");
		var _val = $("#select").val();
        if (event.keyCode == "13" && isFocus) {
        	//加载默认数据...
        	loadArtContent();
        }
    });
	
	//加载服务器内容
	loadArtContent();
});


/**
 * 加载数字展馆
 */
function loadArtContent(page, rows){
	var page = page || 1;//分页加载第一页 
	var rows = rows || 5;//每页5条记录
	
	//url 请求
	var _url = getFullUrl('./art/srchjpwhz?p=1');
	
	//艺术分类 得到艺术分类的id
	var arttyp = '';
	$('.categoryChange >.row >.adrList > span> a').each(function(idx){
		if($(this).parent('span').hasClass('active')){
			arttyp = $(this).attr('arttyp');
		}
	});
	
	//搜索条件
	var condition = $("#select").val();
	
	//url
	if(arttyp != ''){
		_url += '&arttyp='+arttyp;
	}
	
	if(condition != ''){
		_url += '&title='+encodeURIComponent(condition);
	}
	
	//请求服务器数据
	$.ajax({
		type: "POST",
		url: _url+'&page='+page+'&rows='+rows,
		success: function(data){
			var dataRows = data.rows;
			var total = data.total || 0;
			//加载页面数据
			if(total > 0){
				showList(dataRows);
			}else{
				showList([]);
			}
			
			//加载分页工具栏
			genPagging('artPagging', page, rows, total, loadArtContent);
		}
	});
}

/**
 * 生成列表数据
 * @param dataList
 * @returns
 */
function showList(dataRows){
	var _html = '';
	if (!dataRows.length){
		$('#cience').siblings(".public-no-message").show();
	}else{
		$('#cience').siblings(".public-no-message").hide();
	}
	
	for(var i=0; i<dataRows.length; i++){
		_html += '<div class="item clearfix">';
		_html += '<div class="img"> <a href="'+getFullUrl('agdszzg/zglist?id='+dataRows[i].exhid)+'"><img src="'+getFullUrl(dataRows[i].exhpic)+'" width="300" height="190"></a> </div>';
		_html += '<div class="info">';
		_html += '<h2><a href="'+getFullUrl('agdszzg/zglist?id='+dataRows[i].exhid)+'">'+dataRows[i].exhtitle+'</a></h2>';
		_html += '<p>时间 :<span>'+dateFMT(dataRows[i].exhstime)+'</span></p>';
		_html += '<p> '+dataRows[i].exhdesc+'</p>';
		_html += '</div>';
		_html += '</div>';
	}
	$('#cience').find('div.item').remove();
	$('#cience').prepend(_html);
}