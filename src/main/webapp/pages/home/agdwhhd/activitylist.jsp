<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<%@include file="/pages/comm/agdhead.jsp"%>
<title>台州文化云-文化活动列表</title>
<link href="${basePath }/static/assets/css/activity/activitylist.css" rel="stylesheet">
<link href="${basePath }/static/assets/js/plugins/oiplayer-master/css/oiplayer.css" rel="stylesheet">
<script src="${basePath }/static/assets/js/activity/activitylist.js"></script>
<script src="${basePath }/static/assets/js/plugins/oiplayer-master/plugins/flowplayer-3.2.6.min.js"></script>
<script src="${basePath }/static/assets/js/plugins/oiplayer-master/js/jquery.oiplayer.js"></script>
<script type="text/javascript">
var _param={};

//获取查找的条件
function setParam(param){
	if (!$.isPlainObject) return;
	//合并并覆盖 数据
	$.extend(_param,param);
}


//处理选择项样式
function _active(active){
	if (!active || active.size()==0) return;
	active.addClass("active");
	//siblings 遍历同级其它类型
	active.siblings(".active").removeClass("active");
}

//处理 活动活动类型查找
function setactType(active,type) {
	var val=$("#title").val();
	setParam({actvshorttitle:val});
	setParam({etype:type});
	_active($(active).parent());
	loadData();
}

//处理 区域查找
function setactArea(active,type) {
	var val=$("#title").val();
	setParam({actvshorttitle:val});
	setParam({areaid:type});
	_active($(active).parent());
	loadData();
}

//处理 艺术类型查找
/* function setactArt(active,type) {
	var val=$("#title").val();
	setParam({actvshorttitle:val});
	setParam({arttype:type});
	_active($(active).parent());
	loadData();
} */

//处理 时间查找
function setactDate(active,type) {
	var val=$("#title").val();
	setParam({actvshorttitle:val});
	setParam({statemdfdate:type});
	_active($(active).parent());
	loadData();
}

//处理 标题搜索
function setactTitle(){
	var val=$("#title").val();
	setParam({actvshorttitle:val});
	loadData();
}

/**点更多进入 数据加载 */
function loadData(page, rows){
	_param.page = page || 1;
	_param.rows = rows || 8;

	$.ajax({
		type: 'post',
		url: '${basePath }/agdwhhd/activityload',
		data: _param,		
		success: function(data){
			//
			if (!data) return;
			var actlist = data.actlist;
			var total = data.total;
			showData(actlist);
			
			//加载分页工具栏
			genPagging('artPagging', _param.page, _param.rows, total, loadData);
		}
	});
}

/** 展示数据 */
function showData(data){
	$('.DataUrl').html('');
	$(".DataUrl").parent().find("div").remove();
	if(data.length > 0){
	    $.each(data,function(index,value){
            var $li = setCard(value);
            $(".DataUrl").append($li);
        });
	}else{
		$(".DataUrl").parent().append("<div class='public-no-message'></div>");
	}
}

function setCard(item) {
    var $li = $("<li>");
    var $a = $("<a>");
    $a.attr("href","${basePath }/agdwhhd/activityinfo?actvid=" + item.id);
    var $img = $("<img width='280' height='210'>");
    $img.attr("src","${imgServerAddr}"+WhgComm.getImg750_500(item.imgurl));
    $img.appendTo($a);
    $a.append($("<div class='mask'>"));
    $a.appendTo($li);
    var $detail = $("<div class='detail'>");
    $detail.append("<h2>" + item.name + "</h2>");
    $detail.append('<p><span class="tt">地点：</span><span class="desc">'+item.address+'</span></p>');
    $detail.append('<p><span class="tt">时间：</span><span class="desc">'+dateFMT(item.starttime)+'</span></p>');
    $detail.appendTo($li);
    var $button = $("<div class='button clearfix'>");
    var endTime = item.endtime;
    var nowDate = new Date();
    endTime = dateFMT(endTime);
    var nowTime = dateFMT(nowDate);
    if(endTime < nowTime ){
        $button.append('<a href="javascript:void(0);" style="background-color: #A6BBCE">活动已结束</a>');
    }else {
        if(item.sellticket != 1){
            $button.append('<a href="${basePath }/agdwhhd/activityinfo?actvid='+item.id+'">马上预定</a>');
        }else{
            $button.append('<a href="${basePath }/agdwhhd/activityinfo?actvid='+item.id+'">查看详情</a>');
        }
    }
    $button.append('<div class="collection on"><i></i><span>'+item.shoucang+'</span></div><div class="likes on"><i></i><span>'+item.dianzan+'</span></div>');
    $button.appendTo($li);
    return $li;
}

$(function(){
	/** 回车事件 */
	$("body").keydown(function() {
		if (event.keyCode == "13"){
			 setactTitle();
		}
	});
	
	$.extend(_param,{actvtype:"${actvtype}"});
	loadData();
	
	/* _param.index = 1 ;
	_param.size = 8 ;
	$.ajax({
		type: 'post',
		url: '${basePath }/api/activity/actList',
		data: _param,		
		success: function(data){
			alert(111);
		}
	});  */ 
	
})
</script>

</head>
<body>
<!-- 公共头部开始 -->
<%@include file="/pages/comm/agdtop.jsp"%>
<!-- 公共头部结束-END -->

<!--主体开始-->
<div id="content">
    <div class="categoryChange">
        <div class="row clearfix">
            <div class="title">区域</div>
            <div class="adrList qrtype">
                <span class="item active"><a href="javascript:void(0)" onClick="setactArea(this,'')">全部</a></span>
                <c:if test="${qrtype !=null}">
	            	<c:forEach items="${qrtype}" var="item">
	               		<span class="item"><a href="javascript:void(0)"  onClick="setactArea(this,${item.id})">${item.name}</a></span>
	            	</c:forEach>
                </c:if>
            </div>
        </div>
        
        <div class="row clearfix">
            <div class="title">活动类型</div>
            <div class="adrList">
                <span class="item ${empty actvtype ?'active':''}"><a href="javascript:void(0)" onClick="setactType(this,'')">全部</a></span>
           	  	<c:if test="${acttype != null}">
            		<c:forEach items="${acttype}" var="item">
             	 		<span class="item ${actvtype eq item.id? 'active':'' }"><a href="javascript:void(0)" onClick="setactType(this,${item.id})">${item.name}</a></span>
             		</c:forEach>
            	</c:if>
            </div>
        </div>
        
       <%--  <div class="row clearfix">
            <div class="title">艺术类型</div>
            <div class="adrList">
                <span class="item active"><a href="javascript:void(0)" onClick="setactArt(this,'')">全部</a></span>
                <c:if test="${ystype !=null}">
	                <c:forEach items="${ystype}" var="item"> 
	               		 <span class="item"><a href="javascript:void(0)" onClick="setactArt(this,${item.id})">${item.name}</a></span>
	             	</c:forEach>
             	</c:if>
            </div>
        </div> --%>
        
        <div class="row border-none clearfix">
            <div class="title">排序</div>
            <div class="adrList adrList1">
                <span class="item active"><a href="javascript:void(0)" onClick="setactDate(this,'')">智能排序</a></span>
                <span class="item"><a href="javascript:void(0)" onClick="setactDate(this,'1')">即将开始</a></span>
                <span class="item"><a href="javascript:void(0)" onClick="setactDate(this,'2')">正在进行</a></span>
                <span class="item"><a href="javascript:void(0)" onClick="setactDate(this,'3')">已结束</a></span>
                <span class="item"><a href="javascript:void(0)" onClick="setactDate(this,'4')">活动推荐</a></span>
            </div>
            <%--<div class="searchCont">
                <input  id="title" placeholder="搜点什么...">
                <button type="submit" onClick="setactTitle()"></button>
            </div>--%>
        </div>
    </div>
    
    <div class="active-list container">
        <div class="con">
        
        	<!--list 数据加载 start -->
            <ul class="clearfix DataUrl">
            </ul>
            <!-- list 数据加载  end  -->
            
        </div>
    </div>
    
    <!--分页开始 -->
        <div class="green-black" id="artPagging" style="margin-bottom: 40px"></div>
   	<!--分页结束-->
</div>
<!--主体结束-->

<!--公共主底部开始-->
<%@include file="/pages/comm/agdfooter.jsp"%>
<!--公共主底部结束-END-->
</body>
</html>