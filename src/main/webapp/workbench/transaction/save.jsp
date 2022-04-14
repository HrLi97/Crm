<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Set" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="u" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

HashMap<String,String> pMap = (HashMap<String,String>) application.getAttribute("pMap");
Set<String> keys = pMap.keySet();
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>

	<script type="text/javascript">

		var json = {

			<%

				for (String key:keys){
					String value = pMap.get(key);

			//json={"":"","":"",...}
			%>

			"<%=key%>":<%=value%>,

			<%
			}
			%>


		}

		$(function (){

			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "down-left"
			})
			//自动补全
			$("#create-customerName").typeahead({
				source: function (query, process) {
					$.get(
						"workbench/transaction/getCustomerName.do",
						{ "name" : query },
						function (data) {
							process(data);
						},
						"json"
					);
				},
				delay: 1500
			});
			//绑定阶段可能性
			$("#create-transactionStage").change(function (){
				var stage = $("#create-transactionStage").val();
				var possibility = json[stage];

				$("#create-possibility").val(possibility);

			})
			//绑定活动源
			$("#searchActivity").keydown(function (event){
				if (event.keyCode==13){
					var aname = $("#searchActivity").val();
					$.ajax({
						url:"workbench/transaction/searchActivity.do",
						data: {"aname": aname},
						type:"get",
						dataType:"json",
						success:function (data){
							//data为活动信息
							var html="";
							$.each(data,function (i,n){

								html +='<tr>';
								html +='<td><input type="radio" name="xz" value="'+n.id+'"/></td>';
								html +='<td>'+n.name+'</td>';
								html +='<td>'+n.startDate+'</td>';
								html +='<td>'+n.endDate+'</td>';
								html +='<td>'+n.owner+'</td>';
								html +='</tr>';

							})
							$("#activityBody").html(html);
						}
					})
					return false;
				}
			})
			//为选定活动按钮绑定事件
			$("#searchActivityBtn").click(function (){
				var $xz = $("input[name=xz]:checked")
				if ($xz==0){
					alert("没有选定")
				}else {
					var AId = $($xz[0]).val();
					$("#hideActivityId").val(AId);

					$.ajax({
						url:"workbench/transaction/addActivityToUser.do",
						data:{"AId":AId},
						type:"get",
						dataType:"json",
						success:function (data){
							//data只是一个活动的name,但是id也需要传进来放到隐藏域
							$.each(data,function (i,n){
								$("#create-activitySrc").val(n.name)
							})
						},
					})
				}
				$("#findMarketActivity").modal("hide");
			})
			//绑定联系人
			$("#searchCon").keydown(function (event){
				if (event.keyCode==13){
					var cname = $.trim($("#searchCon").val());

					$.ajax({
						url:"workbench/transaction/searchContacts.do",
						data:{
							"cname":cname
						},
						type:"get",
						dataType:"json",
						success:function (data){
							var html=""
							$.each(data,function (i,n){
								html+='<tr>';
								html+='<td><input type="radio" name="xz" value="'+n.id+'"/></td>';
								html+='<td>'+n.fullname+'</td>';
								html+='<td>'+n.email+'</td>';
								html+='<td>'+n.mphone+'</td>';
								html+='</tr>';
							})
							$("#contactsBody").html(html)
						},
					})
					return false;
				}
			})
			//为选定联系人按钮绑定事件
			$("#searchConBtn").click(function (){
				var $xz = $("input[name=xz]:checked")
				if ($xz==0){
					alert("没有选定")
				}else {
					var CId = $($xz[0]).val();
					$("#hideContactId").val(CId);

					$.ajax({
						url:"workbench/transaction/addContactToTran.do",
						data:{"CId":CId},
						type:"get",
						dataType:"json",
						success:function (data){
							//data只是一个活动的name,但是id也需要传进来放到隐藏域
							$.each(data,function (i,n){
								$("#create-contactsName").val(n.fullname)
							})
						},
					})
					$("#findContacts").modal("hide");
				}

			})
			//绑定表单的提交事件
			$("#saveBtn").click(function (){
				$("#form").submit();
			})
		})
	</script>

</head>
<body>



	<!-- 查找市场活动 -->	
	<div class="modal fade" id="findMarketActivity" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" style="width: 300px;" id="searchActivity" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable3" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
							</tr>
						</thead>
						<tbody id="activityBody">

						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="searchActivityBtn">确定</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 查找联系人 -->	
	<div class="modal fade" id="findContacts" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找联系人</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" style="width: 300px;" id="searchCon" placeholder="请输入联系人名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>邮箱</td>
								<td>手机</td>
							</tr>
						</thead>
						<tbody id="contactsBody">

						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="searchConBtn">确定</button>
				</div>
			</div>
		</div>
	</div>
	
	
	<div style="position:  relative; left: 30px;">
		<h3>创建交易</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
			<button type="button" class="btn btn-default" id="cancelBtn">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form" id="form" action="workbench/transaction/saveTran.do" style="position: relative; top: -30px;">
		<input type="hidden" value="" id="hideActivityId" name="activityId">
		<input type="hidden" value="" id="hideContactId" name="contactId" >
		<div class="form-group">
			<label for="create-transactionOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionOwner" name="owner">
				  <u:forEach var="u" items="${userList}">
					  <option value="${u.id}" ${user.id eq u.id ? "selected" : ""}>${u.name}</option>
				  </u:forEach>
				</select>
			</div>
			<label for="create-amountOfMoney" class="col-sm-2 control-label">金额</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-amountOfMoney" name="money">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-transactionName" name="name">
			</div>
			<label for="create-expectedClosingDate" class="col-sm-2 control-label">预计成交日期<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control time" id="create-expectedClosingDate" name="expectedDate">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-accountName" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-customerName" name="customerName" id="create-accountName" placeholder="支持自动补全，输入客户不存在则新建">
			</div>
			<label for="create-transactionStage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
			  <select class="form-control" id="create-transactionStage" name="stage">
			  	<u:forEach items="${stage}" var="s">
					<option value="${s.value}">${s.text}</option>
				</u:forEach>
			  </select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionType" class="col-sm-2 control-label">类型</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionType" name="type">
				  <c:forEach items="${transactionType}" var="t">
					  <option value="${t.value}">${t.text}</option>
				  </c:forEach>
				</select>
			</div>
			<label for="create-possibility" class="col-sm-2 control-label">可能性</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-possibility" >
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-clueSource" class="col-sm-2 control-label">来源</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-clueSource" name="source">
				  <c:forEach items="${source}" var="o">
					  <option value="${o.value}">${o.text}</option>
				  </c:forEach>
				</select>
			</div>
			<label for="create-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#findMarketActivity"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-activitySrc" name="">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#findContacts"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-contactsName" name="conName">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-describe" name="description"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-contactSummary" name="contactSummary"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control time" id="create-nextContactTime" name="nextContactTime">
			</div>
		</div>
		
	</form>
</body>
</html>