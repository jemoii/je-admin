<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../include/head.jsp" />
<div class="container theme-showcase" role="main">
	<div class="page-header">
		<h1>登录</h1>
	</div>
	<div class="row">
		<div class="col-md-6">
			<ul class="nav nav-tabs">
				<li class="active"><a href="#login" data-toggle="tab">普通登录
				</a></li>
				<li><a href="#qrlogin" data-toggle="tab">扫码登录</a></li>
			</ul>
			<div class="tab-content">
				<div class="tab-pane fade active in" id="login">
					<div class="form-group col-md-8">
						<label for="username">邮箱</label> <input type="email"
							class="form-control" id="username"> <a href="./register">未注册？</a>
					</div>
					<div class="form-group col-md-8">
						<label for=password class="control-label">密码</label> <input
							type="password" class="form-control" id="password"> <a
							href="./reset">忘记密码？</a>
					</div>
					<div class="form-group col-md-8">
						<button class="btn btn-success navbar-right"
							data-loading-text="正在登录..." id="login_btn">登录</button>
						<span class="label label-warning" id="login_tip"></span>
					</div>
				</div>
				<div class="tab-pane fade" id="qrlogin">
					<div class="">
						<div class="form-group col-md-8">
							<label for="qr_username">邮箱</label> <input type="email"
								class="form-control" id="qr_username">
						</div>
						<div class="form-group col-md-8">
							<button class="btn btn-success navbar-right"
								data-loading-text="正在获取..." id="qrlogin_btn">获取登录二维码</button>
							<span class="label label-warning" id="qrlogin_tip"></span>
						</div>
					</div>
					<div class="hide">
						<div class="form-group col-md-8">
							<input type="hidden" id="qrlogin_token" value="" /> <img
								id="qrlogin_img" src="" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- /container -->
<jsp:directive.include file="../include/footer.jsp" />
<script type="text/javascript">
$('#login_btn').on('click',
function() {
	var btn = $(this).button('loading');
	setTimeout(function() {
		btn.button('reset');
	},
	1000);

	$('#login_tip').html("");
	if (!/^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/.test($("#username").val())) {
		$('#login_tip').html("邮箱地址不合法");
		return;
	}
	if ($('#username').val().trim() != "" && $('#password').val().trim() != "") {
		$.ajax({
			type: 'post',
			url: './login.json',
			data: {
				username: $('#username').val().trim(),
				password: $('#password').val().trim(),
			},
			success: function(json) {
				if (json.status) {
					location.href = '${param.from == null || param.from eq "" ? "./space" : param.from}';
				} else {

					if (json.obj == "EMAIL_ERROR") {
						location.href = './unauthenticated';
					} else if (json.obj == "LEVEL_ERROR") {
						$('#login_tip').html("当前权限下，账号不存在");
					} else {
						$('#login_tip').html("邮箱地址或密码错误");
					}
				}
			},
			error: function() {

			}
		});
	}
})

var delay = 3;
$('#qrlogin_btn').on('click',
function() {
	var btn = $(this).button('loading');
	setTimeout(function() {
		btn.button('reset');
	},
	1000);

	$('#qrlogin_tip').html("");
	if (navigator.userAgent.match(/mobile/i)) {
		$('#qrlogin_tip').html("移动端不支持扫码登录");
		return;
	}
	$('#qrlogin_tip').html("");
	if ($('#qr_username').val().trim() == "") {
		$('#qrlogin_tip').html("当前扫码登录需要填写邮箱地址");
		return;
	}
	if (!/^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/.test($("#qr_username").val())) {
		$('#qrlogin_tip').html("邮箱地址不合法");
		return;
	}
	$('#qrlogin_token').val(Math.random());
	$('#qrlogin_img').attr("src", './login.json?j=' + $('#qrlogin_token').val() + '&username=' + $('#qr_username').val().trim());
	$('#qrlogin').children().each(function(i, n) {
		var div = $(n);
		if (div.attr('class').indexOf("hide") == -1) {
			div.attr('class', 'hide');
		} else {
			div.attr('class', '');
		}
	});
	delay = 3;
	qrlogin_check();
})

function qrlogin_check() {
	if (delay == 8) {
		$('#qrlogin').children().each(function(i, n) {
			var div = $(n);
			if (div.attr('class').indexOf("hide") == -1) {
				div.attr('class', 'hide');
			} else {
				div.attr('class', '');
			}
		});
		$('#qrlogin_tip').html("扫码登录失败");
	} else {
		$.ajax({
			type: 'get',
			url: './qrlogin.json',
			data: {
				username: $('#qr_username').val().trim(),
				token: $('#qrlogin_token').val(),
			},
			success: function(json) {
				if (json.status) {
					location.href = '${param.from == null || param.from eq "" ? "./space" : param.from}';
				} else {
					delay++;
					window.setTimeout("qrlogin_check()", 1000 * 30);
				}
			},
			error: function() {

			}
		});
	}
}
</script>