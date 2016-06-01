<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../include/head.jsp" />
<div class="container theme-showcase" role="main">
	<div class="jumbotron">
		<h2>注册使用的邮箱未验证</h2>
		<p>
			<button class="btn btn-success" data-loading-text="请等待..."
				id="auth_btn">现在验证</button>
			<!-- <a class="btn btn-default"
				href="/jeadmin/space">稍后验证</a> -->
		</p>
		<span class="label label-warning" id="auth_tip"></span>
	</div>
</div>
<jsp:directive.include file="../include/footer.jsp" />
<script type="text/javascript">
$('#auth_btn').on('click', function() {
	var btn = $(this).button('loading');
	setTimeout(function() {
		btn.button('reset');
	}, 5000);

	$.ajax({
		type : 'post',
		url : './auth.json',
		success : function(json) {
			if (json.status) {
				$('#auth_tip').html("认证邮件已发送，请按提示验证邮箱");
			} else {
				if (json.obj == "EMAIL_ERROR") {
					$('#auth_tip').html("登录邮箱已验证");
				} else {
					$('#auth_tip').html("请稍后验证");
				}
			}
		},
		error : function() {

		}
	});

})
</script>