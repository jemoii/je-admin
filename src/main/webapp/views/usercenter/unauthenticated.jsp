<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../include/head.jsp" />
<div class="container theme-showcase" role="main">
	<div class="jumbotron">
		<h2>注册使用的邮箱未验证</h2>
		<p>
			<a id="btn_auth" class="btn btn-success" href="javascript:void(0)"
				onclick="auth()">现在验证</a><a class="btn btn-default"
				href="/duobei/space">稍后验证</a>
		</p>
		<span class="label label-warning" id="auth_tip"></span>
	</div>
</div>
<jsp:directive.include file="../include/footer.jsp" />
<script type="text/javascript">
	function auth() {
		$("#btn_auth").attr("disabled", "disabled");
		$.ajax({
			type : 'post',
			url : './auth.json',
			success : function(json) {
				if (json.status) {
					$('#auth_tip').html("认证邮件已发送，请按提示验证邮箱");
				} else {
					$('#auth_tip').html("请稍后验证");
				}
			},
			error : function() {

			}
		});

	}
</script>