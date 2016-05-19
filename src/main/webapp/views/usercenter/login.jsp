<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../include/head.jsp" />
<div class="container theme-showcase" role="main">
	<div class="page-header">
		<h1>登录</h1>
	</div>
	<div class="row">
		<div class="col-md-6">
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
						<td><a href="./register">未注册？</a></td>
					</tr>
					<tr>
						<td><h4>
								<span class="label label-default">密码：</span>
							</h4></td>
						<td colspan="2"><input type="password" class="form-control"
							id="password"></td>
						<td><a href="./reset">忘记密码？</a></td>
					</tr>
					<tr>
						<td><a href="javascript:onclick=qrlogin()">扫描二维码登录</a></td>
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
	<div class="hide">
		<div id="qrloginwrap">
			<input type="hidden" id="qrlogin_token" value="" />
			<form id="qrloginform" role="form">
				<img id="qrlogin_img" src="" />
			</form>
		</div>
	</div>
</div>
<!-- /container -->
<jsp:directive.include file="../include/footer.jsp" />
<script src="./static/bootstrap/jquery.bootstrap.min.js"></script>
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
							status : $('input[name="user_status"]:checked')
									.val(),
							email : $('#email').val().trim(),
							password : $('#password').val().trim(),
						},
						success : function(json) {
							if (json.status) {
								if (json.obj == "邮箱未验证") {
									location.href = './unauthenticated';
								} else {
									location.href = '${param.from == null || param.from eq "" ? "./space" : param.from}';
								}
							} else {
								$('#login_tip').html("邮箱地址或密码错误");
							}
						},
						error : function() {

						}
					});
		}
	}
	var delay = 3;
	function qrlogin() {
		$.messager.model = {
			ok : {
				text : "确定",
				classed : "btn-success",
			},
			cancel : {
				text : "取消",
				classed : "btn-default",
			}
		};
		if (navigator.userAgent.match(/mobile/i)) {
			$.messager.alert("移动端不支持扫码登录");
			return;
		}
		$('#login_tip').html("");
		if ($('#email').val().trim() == "") {
			$('#login_tip').html("移动端正在开发中，当前扫码登录需要填写邮箱地址");
			return;
		}
		if (!/^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/.test($("#email").val())) {
			$('#login_tip').html("邮箱地址不合法");
			return;
		}
		$('#qrlogin_token').val(Math.random());
		$('#qrlogin_img').attr(
				"src",
				'./login.json?j=' + $('#qrlogin_token').val() + '&status='
						+ $('input[name="user_status"]:checked').val()
						+ '&email=' + $('#email').val().trim());
		$("#qrloginwrap").dialog({
			title : "扫描二维码登录",
			dialogClass : "modal-sm",
		});
		delay = 3;
		qrlogin_check();
	}
	
	function qrlogin_check() {
		if (delay == 6) {
			$('#qrlogin_img').dialog("close");
			$.messager.alert("扫码登录失败");
		} else {
			$.ajax({
						type : 'get',
						url : './qrlogin.json',
						data : {
							status : $('input[name="user_status"]:checked')
									.val(),
							email : $('#email').val().trim(),
							token : $('#qrlogin_token').val(),
						},
						success : function(json) {
							if (json.status) {
								location.href = '${param.from == null || param.from eq "" ? "./space" : param.from}';
							} else {
								delay++;
								window.setTimeout("qrlogin_check()", 1000 * 30);
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