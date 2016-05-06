<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../include/head.jsp" />
<%@ page import="me.voler.admin.usercenter.dto.UserInfo"%>
<div class="container theme-showcase" role="main">
	<div class="row">
		<div class="col-md-8">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h1>
						个人信息
						<c:if test="${sessionScope.midtk != null}">
							<a class="badge" href="./group">信息列表>></a>
						</c:if>
					</h1>
				</div>
				<div class="panel-body">
					<div class="col-md-10">
						<table class="table">
							<tbody>
								<tr>
									<td><label>昵称：</label></td>
									<td><span id="name">${info.username}</span></td>
								</tr>
								<tr>
									<td><label>邮箱：</label></td>
									<td><span id="email">${info.email}</span></td>
								</tr>
								<tr>
									<td><label>手机号：</label></td>
									<td><span id="telephone">${info.telephone}</span></td>
								</tr>
								<tr>
									<td colspan="2"><span class="label label-warning"
										id="space_tip"></span></td>
								</tr>
							</tbody>
						</table>
					</div>
					<button class="btn btn-primary" id="button" onclick="edit()">编辑</button>
					<button class="btn btn-default" style="display: none" id="cancel"
						onclick="cancel()">取消</button>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:directive.include file="../include/footer.jsp" />
<script>
	var initialInfo = new Array();
	function edit() {
		span2input('name');
		span2input('telephone');
		$('#button').html("保存");
		$('#button').attr("onclick", "save()");
		$('#cancel').css("display", "inline-block");
	}
	function save() {
		$('#space_tip').html("");
		if (!/^(\w{5,9})$/.test($('#nameInput').val())) {
			$('#space_tip').html("昵称由5~9个大小写字母、数字组成");
			return;
		}
		if ($('#telephoneInput').val().trim() != ""
				&& !/^(\d{11})$/.test($('#telephoneInput').val())) {
			$('#space_tip').html("手机号不合法");
			return;
		}
		$.ajax({
			type : 'put',
			url : './space.json',
			contentType : 'application/json; charset=UTF-8',
			data : JSON.stringify({
				userId : '${info.userId}',
				username : $('#nameInput').val().trim(),
				status : '${info.status}',
				email : $('#email').html(),
				telephone : $('#telephoneInput').val().trim(),
			}),
			success : function(json) {
				if (json.status) {
					$('#name').html(json.obj.username);
					$('#telephone').html(json.obj.telephone);
				} else {
					cancel();
				}

			},
			error : function() {
				cancel();
			}
		});
		$('#button').html("编辑");
		$('#button').attr("onclick", "edit()");
		$('#cancel').css("display", "none");
	}
	function span2input(spanId) {
		var inputElement = $('<input></input>');
		inputElement.addClass('form-control');
		inputElement.attr("value", $('#' + spanId).html());
		inputElement.attr("id", spanId + "Input");
		$('#' + spanId).html(inputElement);
		initialInfo.push($('#' + spanId + 'Input').val());
	}
	function cancel() {
		$('#space_tip').html("");
		$('#name').html(initialInfo[0]);
		$('#telephone').html(initialInfo[1]);
		initialInfo = new Array();
		$('#button').html("编辑");
		$('#button').attr("onclick", "edit()");
		$('#cancel').css("display", "none");
	}
</script>