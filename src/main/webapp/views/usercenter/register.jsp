<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../include/head.jsp" />
<div class="container theme-showcase" role="main">
	<div class="page-header">
		<h1>注册</h1>
	</div>
	<div class="row">
		<div class="col-md-4">
			<table class="table">
				<tbody>
					<tr>
						<td><span><input type="radio" name="user_status"
								value="student" checked="checked" /> 学生</span></td>
						<td><span><input type="radio" name="user_status"
								value="teacher" /> 教师</span></td>
						<td><span><input type="radio" name="user_status"
								value="admin" /> 管理员</span></td>
					</tr>
					<tr>
						<td><h4>
								<span class="label label-default">昵称：</span>
							</h4></td>
						<td colspan="2"><input type="text" class="form-control"
							id="username"></td>
					</tr>
					<tr>
						<td><h4>
								<span class="label label-default">邮箱：</span>
							</h4></td>
						<td colspan="2"><input type="text" class="form-control"
							id="email"></td>
					</tr>
					<tr>
						<td><h4>
								<span class="label label-default">密码：</span>
							</h4></td>
						<td colspan="2"><input type="password" class="form-control"
							id="password"></td>
					</tr>
					<tr>
						<td><a href="./login">已注册？</a></td>
						<td colspan="3"><button class="btn btn-default navbar-right"
								onclick="register()">注册</button></td>
					</tr>
					<tr>
						<td colspan="3"><span class="label label-warning"
							id="register_tip"></span></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
<!-- /container -->
<jsp:directive.include file="../include/footer.jsp" />
<script type="text/javascript">
	function register() {
		$('#register_tip').html("");
		if (!/^(\w{5,9})$/.test($("#username").val())) {
			$('#register_tip').html("昵称由5~9个大小写字母、数字组成");
			return;
		}
		if (!/^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/.test($("#email").val())) {
			$('#register_tip').html("邮箱地址不合法");
			return;
		}
		if ($('#username').val().trim() != "" && $('#email').val().trim() != ""
				&& $('#password').val().trim() != "") {
			$.ajax({
				type : 'post',
				url : './register.json',
				data : {
					status : $('input[name="user_status"]:checked').val(),
					username : $("#username").val().trim(),
					email : $('#email').val().trim(),
					password : $('#password').val().trim(),
				},
				success : function(json) {
					if (json.status) {
						location.href = './unauthenticated';
					} else {
						$('#register_tip').html("邮箱地址已注册");
					}

				},
				error : function() {

				}
			});

		}
	}
	$(document).keydown(function(event) {
		if (event.keyCode == 13) {
			register();
		}
	})
</script>