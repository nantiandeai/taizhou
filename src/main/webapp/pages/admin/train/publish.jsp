<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath(); request.setAttribute("basePath", basePath); %>
<%request.setAttribute("resourceid", request.getParameter("rsid"));%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>培训审核列表</title>
<%@include file="/pages/comm/header.jsp"%>
<script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/ueditor.all.min.js"></script>
<script type="text/javascript" charset="utf-8" src="${basePath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
<script>
var winform = new WhuiWinForm("#form",{height:250});
var winform1 = new WhuiWinForm("#check_edit",{height:250});
/** 预览事件处理 */
function preview(idx){
	alert(JSON.stringify(WHdg.getRowData(idx)));
}
/**
 * 设置富文本不可编辑
 */
function setDisabled() {
    UE.getEditor('traeditor').setDisabled('fullscreen');
    UE.getEditor('catalog').setDisabled('fullscreen');
}
/** 发布事件处理 */
function publish(index){
	var row = WHdg.getRowData(index);
	var traid = row.traid;
	var trastate = row.trastate;
	//alert(JSON.stringify(traids));
	$.messager.confirm('确认对话框', '您确认将所选择的发布吗？', function(r){
		if (r){
			$.ajax({
				type: "POST",
				url: getFullUrl('/admin/trainManage/passTrain'),
				data: {traid : traid,trastate: trastate},
				success: function(data){
					if(data.success == '0'){
						$('#checklistDG').datagrid('reload');
					   }else{
						   $.messager.alert('提示', '操作失败。原因：'+data.errmsg, 'error');
					   }
				}
			});
		}
	});
}
/** 取消发布事件处理 */
function cancelp(index){
	var row = WHdg.getRowData(index);
	var traid = row.traid;
	var trastate = row.trastate;
	$.ajax({
		type : "post",
		url : getFullUrl('/admin/trainManage/canNoPublish'),
		data : {traid : traid},
		success :  function(data){
			
			if (data != 0 && data > 0) {
				$.messager.alert("提示","此培训已经在配置中启用，不能取消！");
				return ;
			}else{
				$.messager.confirm('确认对话框', '确定取消发布？', function(r){
					if (r){
						$.ajax({
							type: "POST",
							url: getFullUrl('/admin/trainManage/goBack'),
							data: {traid : traid,trastate : trastate},
							success: function(data){
								if(data.success == '0'){
									$('#checklistDG').datagrid('reload');
								   }else{
									   $.messager.alert('提示', '操作失败。原因：'+data.errmsg, 'error');
								}
							}
						});
					}
				}); 
			}
		}
	})
}
/** 取消发布事件处理 */
/* function cancelp(index){
	var row = WHdg.getRowData(index);
	var traid = row.traid;
	var trastate = row.trastate;
	
		$.messager.confirm('确认对话框', '确定取消发布？', function(r){
			if (r){
				$.ajax({
					type: "POST",
					url: getFullUrl('/admin/trainManage/goBack'),
					data: {traid : traid,trastate : trastate},
					success: function(data){
						if(data.success == '0'){
							$('#checklistDG').datagrid('reload');
						   }else{
							   $.messager.alert('提示', '操作失败。原因：'+data.errmsg, 'error');
							}
						}
				});
			}
		}); 
} */
/**
 * 查看详情
 */
 function look(index){
		var row = WHdg.getRowData(index);
		winform1.openWin();
		winform1.getWinFooter().find("a:eq(0)").hide();
		winform1.setWinTitle("查看详情");
		setDisabled()
		//显示富文本的值
		UE.getEditor('traeditor').setContent(row.tradetail);
		UE.getEditor('catalog').setContent(row.tracatalog);
		_showImg(row);
		var frm = winform1.getWinBody().find('form').form({
			url : getFullUrl('/admin/trainManage/goBack'),
			onSubmit : function(param) {
				trastate : param.trastate;
				//设置富文本的内容
				$('#tradetail').val(UE.getEditor('traeditor').getContent());
				$('#tracatalog').val(UE.getEditor('catalog').getContent());
				return $(this).form('enableValidation').form('validate');
			},
			success : function(data) {
				//alert(JSON.stringify(data));
				var Json = $.parseJSON(data);
		 		if(Json && Json.success == '0'){
		 			$.messager.alert('提示', '操作成功！');
					$('#checklistDG').datagrid('reload');
					
					winform1.closeWin();
				} else {
					$.messager.alert('提示', '操作失败！');
				}
			}
		});
		//格式化日期
		var _data = $.extend({}, row,
				{
			trasdate : datetimeFMT(row.trasdate),
			traedate : datetimeFMT(row.traedate),
			traenrolstime : datetimeFMT(row.traenrolstime),	
			traenroletime : datetimeFMT(row.traenroletime),
		});
		frm.form("clear");
		frm.form("load", _data);
		winform1.setFoolterBut({onClick:function(){
			frm.submit();
		}});
	}
 

/** 报名事件处理 */
function enrollp(idx){
	alert(JSON.stringify(WHdg.getRowData(idx)));
}
/**
 * 打回到未审核状态
 */
function goCheck(index){
	var row = WHdg.getRowData(index);
	var traid = row.traid;
	var trastate = row.trastate;
	$.messager.confirm('确认对话框', '确定打回未审核状态？', function(r){
		if (r){
			$.ajax({
				type: "POST",
				url: getFullUrl('/admin/trainManage/goBack'),
				data: {traid : traid,trastate : trastate},
				success: function(msg){
				$('#checklistDG').datagrid('reload');
				}
			});
		}
	}); 
}

//处理编辑时显示图片
function _showImg(data){
	$(".img_div").remove();
		
	var imgDiv = '<div class="row img_div">'
		+'<div></div>'
		+'<div><img width="200" height="150"> </div> '
		+'</div>';
		
	if (data.trapic){
		var imgrow = $("[name$='trapic_up']").parents(".row");
		imgrow.after(imgDiv);
		imgrow.next(".img_div").find("div img").attr("src",getFullUrl(data.trapic));	
	}
	if (data.trabigpic){
		var imgrow = $("[name$='trabigpic_up']").parents(".row");
		imgrow.after(imgDiv);
		imgrow.next(".img_div").find("div img").attr("src",getFullUrl(data.trabigpic));	
	}
	if (data.trapersonfile){
		var imgrow = $("[name$='trapersonfile_up']").parents(".row");
		imgrow.after(imgDiv);
		imgrow.next(".img_div").find("div img").attr("src",getFullUrl(data.trapersonfile));	
	}
	if (data.trateamfile){
		var imgrow = $("[name$='trateamfile_up']").parents(".row");
		imgrow.after(imgDiv);
		imgrow.next(".img_div").find("div img").attr("src",getFullUrl(data.trateamfile));	
	}
}



/**
 * 上首页
 * @param index
 * @returns
 */
function goPage(index){
	
	winform.openWin();
	winform.setWinTitle("设置上首页");
	$("#form").form('clear');
	var row = WHdg.getRowData(index);
	var traid = row.traid;
	if (row.traitmghp == "0") {
		$("#traitmidx").numberspinner('enable');
	}
	$('#traitmghp').combobox({
		onChange: function(){
			if($("#traitmghp").combobox('getValue') == "1"){
				$('#traitmidx').numberspinner('enable');
			}else {
				$('#traitmidx').numberspinner('disable');
			}
		}
	});
	var frm = winform.getWinBody().find('form').form({
		url : getFullUrl("/admin/trainManage/goPage"),
		onSubmit : function(param) {
			param.traid = row.traid;
		},
		success : function(data) {
			var Json = $.parseJSON(data);
			//alert(JSON.stringify(data));
			if(Json && Json.success == "0"){
				$('#checklistDG').datagrid('reload');
				$.messager.alert('提示', '操作成功!');
				winform.closeWin();
			   }else{
				   $.messager.alert('提示', '操作失败!');
			   }
		}
	});
	//alert(JSON.stringify(row));
	winform.setFoolterBut({onClick:function(){
		frm.submit();
	}});
}


/**
 * 课程表
 * @param index
 * @returns
 */
function tokecheng(index){
	var rows = $('#checklistDG').datagrid('getRows');
	var row = rows[index];//WHdg2.getRowData(index);
	var traid = row.traid;
	var sdate = datetimeFMT(row.trasdate);
	var edate = datetimeFMT(row.traedate);
	$('#dd iframe').attr('src', getFullUrl('/admin/train/kecheng?tra_s_date='+sdate+'&tra_e_date='+edate+'&traid='+traid+'&type='));
	$('#dd').dialog({    
	    title: '课程表',  
	    modal:true,
	    maximizable: true,
	    maximized: true,
	    width: 400,
	    height: 200   
	}); 
}

/**
 * 添加资源
 * @param index
 * @returns
 */
function addzy(index){
	var rows = $('#checklistDG').datagrid('getRows');
	var row = rows[index];//WHdg2.getRowData(index);
	var traid = row.traid;
	
	$('#addzy iframe').attr('src', getFullUrl("/admin/ent/entsrc?reftype=2016101400000051&refid="+traid));
	$('#addzy').dialog({    
	    title: '查看资源',  
	    modal:true,
	    maximizable: true,
	    maximized: true,
	    width: 400,
	    height: 200   
	}); 
}

/**
 * 一键发布
 */
function publishAll(){
	var rows={};
	rows = $("#checklistDG").datagrid("getSelections");
	if (rows == "" || rows == null) {
		$.messager.alert('提示', '请选择要操作的数据！');
		return;
	}
	var traids = _split = "";//id1,id2,id3
	for (var i = 0; i<rows.length; i++){
		traids += _split+rows[i].traid;
		_split = ",";
	}
	//trastate = rows[0].trastate;
	$.messager.confirm('确认对话框', '您确认将所选择的进行发布吗？', function(r){
		if (r){
			$.ajax({
				type: "POST",
				url: getFullUrl('/admin/trainManage/checkTrain'),
				data: {traid : traids,fromstate: 2, tostate:3},
				success: function(data){
					if(data.success=="success"){
						$.messager.alert("提示", data.msg);
						$('#checklistDG').datagrid('reload');
					   }else{
						   $.messager.alert('提示', '操作失败。原因：'+data.errmsg, 'error');
					   }
				}
			});
		}
	});
}
/**
 * 一键取消
 */
function noPublish(){
	var rows={};
	rows = $("#checklistDG").datagrid("getSelections");
	if (rows == "" || rows == null) {
		$.messager.alert('提示', '请选择要操作的数据！');
		return;
	}
	var traids = _split = "";//id1,id2,id3
	for (var i = 0; i<rows.length; i++){
		traids += _split+rows[i].traid;
		_split = ",";
	}
	//trastate = rows[0].trastate;
	$.messager.confirm('确认对话框', '您确认将所选择的取消发布吗？', function(r){
		if (r){
			$.ajax({
				type: "POST",
				url: getFullUrl('/admin/trainManage/checkTrain'),
				data: {traid : traids,fromstate: 3, tostate:2},
				success: function(data){
					if(data.success=="success"){
						$.messager.alert("提示", data.msg);
						$('#checklistDG').datagrid('reload');
					   }else{
						   $.messager.alert('提示', '操作失败。原因：不满足条件！');
					   }
				}
			});
		}
	});
}
/**
 * 一键打回审核
 */
function goallCheck(){
	var rows={};
	rows = $("#checklistDG").datagrid("getSelections");
	if (rows == "" || rows == null) {
		$.messager.alert('提示', '请选择要操作的数据！');
		return;
	}
	var traids = _split = "";//id1,id2,id3
	for (var i = 0; i<rows.length; i++){
		traids += _split+rows[i].traid;
		_split = ",";
	}
	//trastate = rows[0].trastate;
	$.messager.confirm('确认对话框', '您确认将所选择的打回审核吗？', function(r){
		if (r){
			$.ajax({
				type: "POST",
				url: getFullUrl('/admin/trainManage/checkTrain'),
				data: {traid : traids,fromstate: 2, tostate:1},
				success: function(data){
					if(data.success=="success"){
						$.messager.alert("提示", data.msg);
						$('#checklistDG').datagrid('reload');
					   }else{
						   $.messager.alert('提示', '操作失败。原因：'+data.errmsg, 'error');
					   }
				}
			});
		}
	});
}

/**设为模板*/
function toTraintpl(index){
	var row = WHdg.getRowData(index);
	//alert(JSON.stringify(row));
	row.trasdate = datetimeFMT(row.trasdate);
	row.traedate = datetimeFMT(row.traedate);
	row.traenrolstime = datetimeFMT(row.traenrolstime);
	row.traenroletime = datetimeFMT(row.traenroletime);
	$.messager.confirm('确认对话框', '您确认将所选择的设置为模板吗？', function(r){
		if (r){
			$.ajax({
				type: "POST",
				url: getFullUrl('/admin/trainManage/setTratpl'),
				data: row,
				success: function(data){
					//alert(JSON.stringify(data));
					if(data.success=="0"){
						
						$.messager.alert("提示", '操作成功！');
						$('#checklistDG').datagrid('reload');
					   }else{
						   $.messager.alert('提示', '操作失败!','error');
					   }
				}
			});
		}
	});
}

$(function(){
	//定义表格参数
	var options = {
		title: '培训发布',			
		url: getFullUrl('/admin/trainManage/selectTrain'),
		queryParams: {stateArray:"2,3"},
		rownumbers:true,
		singleSelect:false,
		columns: [[
			{field:'ck',checkbox:true},
			{field:'tratitle',title:'标题',width:80, sortable:true},
			{field:'tratyp',title:'培训类型',width:80, sortable:true, formatter: tratypFMT},
			{field:'traarttyp',title:'艺术类型',width:80, sortable:true, formatter: arttypFMT},
			{field:'traagelevel',title:'适合年龄',width:80, sortable:true, formatter:agelevelFMT},    
			{field:'tralevel',title:'课程级别',width:80, sortable:true, formatter:tralevelFMT},
			{field:'trapic',title:'列表图',width:80, formatter:imgFMT},
			{field:'trabigpic',title:'详细页图片',width:80, formatter:imgFMT},
			{field:'trasdate',title:'开始日期',width:200, sortable:true, formatter :datetimeFMT,},
			{field:'traedate',title:'结束日期',width:200, sortable:true, formatter :datetimeFMT,},
			
			{field:'trateacher',title:'授课老师',width:80, sortable:true},
			
			{field:'trastate',title:'状态',width:80, sortable:true, formatter :traStateFMT},
			
			
		//	{field:'traitmghp',title:'是否上首页',width:80, sortable:true, formatter:yesNoFMT},
		//	{field:'traitmidx',title:'上首页排序值',width:80, sortable:true},
			{field:'opt', title:'操作', formatter: WHdg.optFMT, optDivId:'checklistOPT'}
		]]
	};
	
	//初始表格
	$("#checklistTB").css('visibility','visible');
	
	WHdg.init('checklistDG', 'checklistTB', options);
	
	//初始富文本
	UE.getEditor('traeditor');
	UE.getEditor('catalog');
	//新建表单
	winform.init();
	winform1.init();
});
</script>
</head>
<body>
	<!-- datagrid div -->
	<div id="checklistDG"></div>
	
	<!-- datagrid toolbar -->
	<div id="checklistTB" class="grid-control-group" style="display: none;">
		<div>
			<shiro:hasPermission name="${resourceid}:publish"><a href="javascript:void(0)" class="easyui-linkbutton" size='small' onclick="publishAll()">批量发布</a></shiro:hasPermission>
			<shiro:hasPermission name="${resourceid}:publishoff"><a href="javascript:void(0)" class="easyui-linkbutton" size='small' onclick="noPublish()">批量取消发布</a></shiro:hasPermission>
			<shiro:hasPermission name="${resourceid}:checkoff"><a href="javascript:void(0)" class="easyui-linkbutton" size='small' onclick="goallCheck()">批量打回</a></shiro:hasPermission>
		</div>
		
		<div style="padding-top: 5px">
			培训类型:
			<input class="easyui-combobox" name="tratyp" data-options="valueField:'typid',textField:'typname',url:'${basePath}/comm/whtyp?type=2'"/>
			艺术类型: 
			<input class="easyui-combobox" name="arttyp" data-options="valueField:'typid',textField:'typname',url:'${basePath}/comm/whtyp?type=0'"/>
			适合年龄: 
			<input class="easyui-combobox" name="agelevel" data-options="valueField:'typid',textField:'typname',url:'${basePath}/comm/whtyp?type=3'"/>
			课程级别: 
			<input class="easyui-combobox" name="tralevel" data-options="valueField:'typid',textField:'typname',url:'${basePath}/comm/whtyp?type=4'"/>
		</div>
		<div style="padding-top: 5px">
			培训标题:
			<input class="easyui-textbox" name="tratitle" data-options="validType:'length[1,30]'"/>
			开始时间:
			<input class="easyui-datebox" name="sdate_s" data-options=""/>到<input class="easyui-datebox" name="sdate_e" data-options=""/>
			状态:
			<select class="easyui-combobox" name="trastate" >
				<option value="">选择状态</option>
				<option value="2">已审核</option>
				<option value="3">已发布</option>
			</select>
			<a href="#" class="easyui-linkbutton search" iconCls="icon-search">查询</a>
			
			 
		</div>
	</div>
	
	<!-- 操作按钮 -->
	<div id="checklistOPT" class="none" style="display: none">
		<shiro:hasPermission name="${resourceid}:publish"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" validKey="trastate" validVal="2" method="publish">发布</a></shiro:hasPermission> 
		<shiro:hasPermission name="${resourceid}:publishoff"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" validKey="trastate" validVal="3" method="cancelp">取消</a></shiro:hasPermission>
		<%-- <shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" validKey="trastate" validVal="3" method="enrollp">报名</a></shiro:hasPermission> --%>
		<shiro:hasPermission name="${resourceid}:checkoff"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" validKey="trastate" validVal="2" method="goCheck">打回</a></shiro:hasPermission>
	<!--<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" validKey="trastate" validVal="3" method="goPage">上首页</a>   -->
		<shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" validKey="trastate" validVal="3"  method="toTraintpl">设为模板</a></shiro:hasPermission>
		<shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true"  method="look">查看详情</a></shiro:hasPermission>
		<shiro:hasPermission name="${resourceid}:view"><a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" method="addzy">查看资源</a></shiro:hasPermission>   
		<shiro:hasPermission name="${resourceid}:view"><a href="#" class="easyui-linkbutton" data-options="plain:true" method="tokecheng">课程表</a></shiro:hasPermission>	
	</div>
	
	<!--培训资源dialog  -->
		<div id="addzy" style="display: none">
			<iframe  style="width:100%; height:100%;border:0px"></iframe>
		</div>
		
		<!-- 课程表dialog -->
		<div id="dd" style="display: none">
			<iframe  style="width:100%; height:100%;border:0px"></iframe>
		</div>
	
	<!-- 送审预览 -->
	<div id="check_edit" class="none" style="display: none" data-options=" fit:true">
			 <form id="check_ff" method="post" enctype="multipart/form-data" >
				<!-- 隐藏域  -->
				<input type="hidden" id="tratplid" name="tratplid" value="" />
				<input type="hidden" id="traid" name="traid" value="" />
		    	<input type="hidden" id="teacherid" name="trateacherid" value="123" />
		    	<input type="hidden" id="tradetail" name="tradetail" value="" />
		    	<input type="hidden" id="tracatalog" name="tracatalog" value="" />
		    	<input type="hidden" id="trapic" name="trapic" value=""/>
				<input type="hidden" id="trabigpic" name="trabigpic" value=""/>
				<input type="hidden" id="trapersonfile" name="trapersonfile" value=""/>
				<input type="hidden" id="trateamfile" name="trateamfile" value=""/>
				<input type="hidden" id="trastate" name="trastate" value=""/>
				
				<div class="main">
					<div class="row">
		    			<div>培训类型:</div>
		    			<div>
		    				<input id="tratyp" class="easyui-combobox" missingMessage="培训类型必须填写" name="tratyp" style="width:100%;height:32px;" data-options="readonly:true,required:true, valueField:'typid',textField:'typname',url:'${basePath}/comm/whtyp?type=2'"/>
		    			</div>
		    		</div>
		    		<div class="row">
		    			<div>艺术类型:</div>
		    			<div>
		    				<input id="traarttyp" class="easyui-combobox" name="traarttyp" style="width:100%;height:32px;" data-options="readonly:true,required:true, valueField:'typid',textField:'typname',url:'${basePath}/comm/whtyp?type=0'"/>
		    			</div>
		    		</div>
		    		<div class="row">
		    			<div>课程标题:</div>
		    			<div>
							<input id="tratitle" class="easyui-textbox" name="tratitle" style="width:100%;height:32px;" data-options="readonly:true"/>
		    			</div>
		    		 </div>
		    		<div class="row">
		    			<div>短标题:</div>
		    			<div>
							<input id="trashorttitle" class="easyui-textbox" name="trashorttitle" style="width:100%;height:32px;" data-options="readonly:true"/>
		    			</div>
		    		 </div>
		    		<div class="row">
		    			<div>区域:</div>
		    			<div>
		    				<input id="traarea" class="easyui-combobox" name="traarea" style="width:100%;height:32px" 
									data-options="readonly:true,editable:false,required:true, valueField:'typid',textField:'typname',url:'${basePath}/comm/whtyp?type=8'"/>
		    			</div>
		    		</div>
		    		<div class="row">
		    			<div>列表页图:</div>
		    			<div>
		    				<a name="trapic_up" ></a>
		    			</div>
		    		 </div>
		    		 <div class="row">
		    			<div>详细页图片：</div>
			    		<div> 
			    			<a name="trabigpic_up" ></a>
			    		</div>
		    		 </div>
		    		 <div class="row">
		    			<div>培训开始日期:</div>
		    			<div><input id="trasdate" name="trasdate" type="text" class="easyui-datetimebox" data-options="readonly:true" style="height: 35px; width: 100%"></input></div>
		    		</div>
		    		<div class="row">
		    			<div>培训结束日期:</div>
		    			<div><input id="traedate" name="traedate" type="text" class="easyui-datetimebox" data-options="readonly:true" style="height: 35px; width: 100%"></input></div>
		    		</div>
		    		 <div class="row">
		    			<div>适合年龄:</div>
			    		<div>
							<input id="traagelevel" class="easyui-combobox" name="traagelevel" style="width:100%;height:32px;" data-options="readonly:true,editable:false,required:true, valueField:'typid',textField:'typname',url:'${basePath}/comm/whtyp?type=3'"/>
			    		</div>
		    		 </div>
		    		<div class="row">
		    			<div>课程级别:</div>
		    			<div>
		    				<input id="tralevel" class="easyui-combobox" name="tralevel" style="width:100%;height:32px;" data-options="readonly:true,editable:false,required:true, valueField:'typid',textField:'typname',url:'${basePath}/comm/whtyp?type=4'"/>
		    			</div>
		    		</div>
		    		<div class="row">
		    			<div>详细地址：</div>
		    			<div>
		    				<input class="easyui-textbox" id="traaddress" name="traaddress" data-options="readonly:true" style="width:100%;height:50px;">
		    			</div>
		    		</div>
		    		<div class="row">
		    			<div>标签:</div>
		    			<div>
							<input id="tratags" class="easyui-combobox" name="tratags" multiple="true" style="width:100%;height:32px;" data-options="readonly:true, valueField:'id',textField:'name',url:'${basePath}/comm/whtag?type=2016101400000020', multiple:true"/>
		    			</div>
		    		 </div>
		    		 <div class="row">
		    			<div>关键字:</div>
			    		<div>
							<input id="trakeys" class="easyui-combobox" name="trakeys" multiple="true" style="width:100%;height:32px;" data-options="readonly:true, valueField:'id',textField:'name',url:'${basePath}/comm/whkey?type=2016101400000023', multiple:true"/>
			    		</div>
		    		 </div>
		    		 <div class="row">
		    			<div>联系人：</div>
		    			<div>
		    				<input class="easyui-textbox" id="tracontact" name="tracontact" data-options="validType:'length[0,10]',multiline:true " style="width:100%;height:32px;">
		    			</div>
		    		</div>
		    		<div class="row">
		    			<div>联系电话：</div>
		    			<div>
		    				<input class="easyui-textbox" id="traphone" name="traphone" data-options="validType:'isPhone[\'traphone\']'" style="width:100%;height:32px;">
		    			</div>
		    		</div>
		    		 <div class="row">
		    			<div>授课老师:</div>
			    		<div>
							<input id="trateacher" class="easyui-textbox" name="trateacher" style="width:100%;height:32px;" data-options="readonly:true"/>
			    		</div>
		    		 </div>
		    		<div class="row">
		    			<div>老师介绍:</div>
		    			<div>
		    				<input id="trateacherdesc" class="easyui-textbox" name="trateacherdesc" style="width:100%;height:100px;" data-options="readonly:true"/>
		    			</div>
		    		</div>
		    		<div class="row">
		    			<div>必需登录点评:</div>
		    			<div>
		    				<select class="easyui-combobox radio" name="traislogincomment" data-options="readonly:true" style="width:100%;height:32px;">
			    					<option value="1">是</option>
			    					<option value="0">否</option>
							</select>
		    			</div>
		    		</div>
		    		<div class="row">
		    			<div>是否需要报名:</div>
		    			<div>
		    				<select id="traisenrol" class="easyui-combobox combobox_isenroldata"
								name="traisenrol" style="height: 35px; width: 100%"
								data-options="readonly:true">
								<option value="1">是</option>
								<option value="0">否</option>
							</select>	
		    			</div>
		    		</div>
		    		<div class="row">
		    			<div>报名介绍:</div>
		    			<div>
		    				<input id="traenroldesc" class="easyui-textbox" name="traenroldesc" style="width:100%;height:100px;" data-options="readonly:true"/>
		    			</div>
		    		</div>
		    		<div class="row">
		    			<div>报名开始时间:</div>
		    			<div>
		    				<input id="traenrolstime" name="traenrolstime" type="text" data-options="readonly:true" class="easyui-datetimebox"  style="height: 35px; width: 100%"></input>
		    			</div>
		    		</div>
		    		<div class="row">
		    			<div>报名结束时间:</div>
		    			<div>
		    				<input id="traenroletime" name="traenroletime" type="text" data-options="readonly:true" class="easyui-datetimebox"  style="height: 35px; width: 100%"></input>
		    			</div>
		    		</div>
		    		<div class="row">
		    			<div>报名人数限制:</div>
		    			<div>
		    				<input id="traenrollimit" name="traenrollimit" class="easyui-numberspinner"   data-options="readonly:true" style="height: 35px; width: 100%"></input>  
		    			</div>
		    		</div>
		    		<div class="row">
		    			<div>报名是否需要审核:</div>
		    			<div>
							<select id="traisenrolqr" class="easyui-combobox"
								name="traisenrolqr" style="height: 35px; width: 100%"
								data-options="readonly:true">
								<option value="0">否</option>
								<option value="1">是</option>
							</select>
		    			</div>
		    		 </div>
		    		 <div class="row">
		    			<div>是否需要面试通知:</div>
		    			<div>
							<select id="traisnotic" class="easyui-combobox"
								name="traisnotic" style="height: 35px; width: 100%"
								data-options="readonly:true">
								<option value="1">是</option>
								<option value="0">否</option>
							</select>
		    			</div>
		    		 </div>
		    		<div class="row">
		    			<div>必须实名报名:</div>
		    			<div>
							<select id="traisrealname" class="easyui-combobox" name="traisrealname" data-options="readonly:true" style="width:100%;height:32px;">
			    					<option value="1">是</option>
			    					<option value="0">否</option>
							</select>
		    			</div>
		    		 </div>
		    		 <div class="row">
		    			<div>是否需要完善资料:</div>
			    		<div>
							<select id="traisfulldata" class="easyui-combobox" name="traisfulldata" data-options="readonly:true" style="width:100%;height:32px;">
			    					<option value="1">是</option>
			    					<option value="0">否</option>
							</select>
			    		</div>
		    		 </div>
		    		 <div class="row">
		    			<div>1人1项限制:</div>
			    		<div>
							<select id="traisonlyone" class="easyui-combobox" name="traisonlyone" data-options="readonly:true" style="width:100%;height:32px;">
			    					<option value="1">是</option>
			    					<option value="0">否</option>
							</select>
			    		</div>
		    		 </div>
		    		 <div class="row">
		    			<div>需上传附件:</div>
		    			<div>
							<select class="easyui-combobox" id="traisattach" name="traisattach" data-options="readonly:true" style="width:100%;height:32px;">
			    					<option value="1">是</option>
			    					<option value="0">否</option>
							</select>
		    			</div>
		    		 </div>
		    		<div class="row">
		    			<div>允许个人报名:</div>
		    			<div>
		    				<select id="tracanperson" class="easyui-combobox" name="tracanperson" data-options="readonly:true" style="width:100%;height:32px;">
			    					<option value="1">是</option>
			    					<option value="0">否</option>
							</select>
		    			</div>
		    		</div>
		    		
		    		<div class="row">
		    			<div>个人附件:</div>
			    		<div>
			    			<a name="trapersonfile_up" ></a>
			    		</div>
		    		 </div>
		    		 <div class="row">
		    			<div>允许团队报名:</div>
		    			<div>
		    				<select id="tracanteam" class="easyui-combobox" name="tracanteam" data-options="readonly:true" style="width:100%;height:32px;">
			    					<option value="1">是</option>
			    					<option value="0">否</option>
							</select>
		    			</div>
		    		</div>
		    		 <div class="row">
		    			<div>团队附件:</div>
			    		<div>
			    			<a name="trateamfile_up" ></a>
			    		</div>
		    		 </div>
		    		<div class="row">
		    			<div>课程简介：</div>
		    			<div>
		    				<input class="easyui-textbox" id="traintroduce" name="traintroduce" data-options="readonly:true" style="width:100%;height:50px;">
		    			</div>
		    		</div>
		    		 <div class="row">
		    			<div>培训大纲：</div>
			    		<div>
							<script id="catalog" type="text/plain" style="width:100%; height:300px;"></script>
			    		</div>
		    		 </div>
		    		<div class="row">
		    			<div>培训详情:</div>
		    			<div>
		    				<script id="traeditor" type="text/plain" style="width:100%; height:300px;"></script>
		    			</div>
		    		</div>
				</div>
			</form>
		</div>	
	
	
	<!-- 上首页 -->
		<div id="form" class="none" style="display: none">
			<form method="post">
				<div class="main">
					<div class="row">
						<div>
							<label>是否上首页:</label>
						</div>
						<div>
							<select id="traitmghp" style="width:90%;height:35px;" class="easyui-combobox radio" name="traitmghp" >
			    					<option value="1">是</option>
			    					<option value="0">否</option>
							</select>
						</div>
					</div>
					<div class="row">
						<div>
							<label>上首页排序值:</label>
						</div>
						<div>
							<input id="traitmidx" style="width:90%;height:35px;" name="traitmidx" class="easyui-numberspinner"  data-options="min:1,max:100,editable:true"></input>
						</div>
					</div>
				</div>
			</form>
		</div>	
</body>
</html>