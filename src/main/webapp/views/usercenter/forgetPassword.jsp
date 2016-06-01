<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../include/head.jsp" />
<div class="container theme-showcase" role="main">
	<div class="page-header">
		<h1>重置密码</h1>
	</div>
	<div class="row">
		<div class="col-md-6">
			<div class="form-group col-md-8">
				<label for="username">邮箱</label> <input type="email"
					class="form-control" id="username">
			</div>
			<div class="form-group col-md-8">
				<button class="btn btn-default navbar-right"
					data-loading-text="请等待..." id="reset_btn">重置密码</button>
				<span class="label label-warning" id="reset_tip"></span>
			</div>
		</div>
	</div>
</div>
<!-- /container -->
<jsp:directive.include file="../include/footer.jsp" />
<script type="text/javascript">
$('#reset_btn').on('click', function() {
	var btn = $(this).button('loading');
	setTimeout(function() {
		btn.button('reset');
	}, 1000);

	$('#reset_tip').html("");
	if (!/^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/.test($("#username").val())) {
		$('#reset_tip').html("邮箱地址不合法");
		return;
	}
	if ($('#username').val().trim() != "") {
		$.ajax({
			type : 'post',
			url : './reset.json',
			data : {
				username : $('#username').val().trim()
			},
			success : function(json) {
				if (json.status) {
					$("#btn_reset").attr("disabled", "disabled");
					$('#reset_tip').html("邮件已发送，请按提示找回登录密码");
				} else {
					$('#reset_tip').html("邮箱地址错误");
				}
			},
			error : function() {

			}
		});
	}
})
</script>