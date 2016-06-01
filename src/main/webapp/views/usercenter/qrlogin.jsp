<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../include/head.jsp" />
<div class="container theme-showcase" role="main">
	<div class="page-header">
		<h1>扫描二维码登录</h1>
	</div>
	<div class="row">
		<div class="col-md-6">
			<div class="form-group col-md-8">
				<label for=password class="control-label">密码</label> <input
					type="password" class="form-control" id="password"
					data-toggle="popover" data-trigger="hover" data-placement="top"
					data-content="填写密码登录成功后，桌面端将自动登录">
			</div>
			<div class="form-group col-md-8">
				<button class="btn btn-success navbar-right"
					data-loading-text="正在登录..." id="login_btn">登录</button>
				<span class="label label-warning" id="login_tip"></span>
			</div>
		</div>
	</div>
</div>
<!-- /container -->
<jsp:directive.include file="../include/footer.jsp" />
<script type="text/javascript">
$(function() {
	$('[data-toggle="popover"]').popover()
})

$('#login_btn').on('click', function() {
	var btn = $(this).button('loading');
	setTimeout(function() {
		btn.button('reset');
	}, 1000);

	$('#login_tip').html("");
	if ($('#password').val().trim() != "") {
		$.ajax({
			type : 'post',
			url : './qrlogin.json',
			data : {
				username : '${param.u}',
				password : $('#password').val().trim(),
				token : '${param.tk}',
			},
			success : function(json) {
				if (json.status) {
					if (json.obj == "EMAIL_ERROR") {
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
})
</script>