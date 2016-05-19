<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../include/head.jsp" />
<div class="container theme-showcase" role="main">
	<div class="page-header">
		<h1>扫描二维码登录</h1>
	</div>
	<div class="row">
		<div class="col-md-4">
			<table class="table">
				<tbody>
					<tr>
						<td colspan="3">
							<div class="alert alert-success" role="alert">填写密码验证成功后，桌面端将自动登录</div>
						</td>
					</tr>
					<tr>
						<td><h4>
								<span class="label label-default">密码：</span>
							</h4></td>
						<td colspan="2"><input type="password" class="form-control"
							id="password"></td>
					</tr>
					<tr>
						<td colspan="2"><button class="btn btn-default navbar-right"
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
		if ($('#password').val().trim() != "") {
			$.ajax({
				type : 'post',
				url : './qrlogin.json',
				data : {
					status : '${param.status}',
					email : '${param.email}',
					password : $('#password').val().trim(),
					token : '${param.token}',
				},
				success : function(json) {
					if (json.status) {
						if (json.obj == "邮箱未验证") {
							$('#login_tip').html("邮箱未验证，扫码登录失败");
						} else {
							location.href = './qrlogined';
						}
					} else {
						$('#login_tip').html("扫码登录失败");
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