<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../include/head.jsp" />
<div class="container theme-showcase" role="main">
	<div class="page-header">
		<h1>登录</h1>
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
						<td><a href="./reset">忘记密码？</a></td>
						<td><a href="./register">未注册？</a></td>
						<td colspan="3"><button class="btn btn-default navbar-right"
								onclick="login()">登录</button></td>
					</tr>
					<tr>
						<td colspan="3"><span class="label label-warning"
							id="login_tip"></span></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
<!-- /container -->
<jsp:directive.include file="../include/footer.jsp" />
<script type="text/javascript">
	function login() {
		$('#login_tip').html("");
		if (!/^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/.test($("#email").val())) {
			$('#login_tip').html("邮箱地址不合法");
			return;
		}
		if ($('#email').val().trim() != "" && $('#password').val().trim() != "") {
			$.ajax({
				type : 'post',
				url : './login.json',
				data : {
					status : $('input[name="user_status"]:checked').val(),
					email : $('#email').val().trim(),
					password : $('#password').val().trim(),
				},
				success : function(json) {
					if (json.status) {
						location.href = './space';
					} else {
						$('#login_tip').html("邮箱地址或密码错误");
					}
				},
				error : function() {

				}
			});

		}
	}
	$(document).keydown(function(event) {
		if (event.keyCode == 13) {
			login();
		}
	})
</script>