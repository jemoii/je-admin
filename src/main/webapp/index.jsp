<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="./views/include/head.jsp" />
<div class="container theme-showcase" role="main">
	<div class="jumbotron">
		<h1>Hello, world</h1>
		<a class="github-button" href="https://github.com/jemoii/je-admin"
			data-style="mega" aria-label="Watch jemoii/je-admin on GitHub">Watch</a>
		<a class="github-button"
			href="https://github.com/jemoii/je-admin/fork" data-style="mega"
			aria-label="Fork jemoii/je-admin on GitHub">Fork</a>
	</div>
</div>
<jsp:directive.include file="./views/include/footer.jsp" />
<script src="/jeadmin/static/bootstrap/jquery.bootstrap.min.js"></script>
<!-- Github buttons -->
<script async defer id="github-bjs"
	src="/jeadmin/static/github/buttons.js"></script>
<script type="text/javascript">
	$(function() {
		if ('${sessionScope.pritk == null}'
				&& '${cookie.jeadmin_user.value != null}') {
			$.messager.popup("你好，欢迎回来！");
		}
	});
</script>