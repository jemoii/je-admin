<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="./views/include/head.jsp" />
<div class="container theme-showcase" role="main">
	<div class="jumbotron">
		<h1>Hello, world</h1>
	</div>
</div>
<jsp:directive.include file="./views/include/footer.jsp" />
<script src="http://7u2pdv.com1.z0.glb.clouddn.com/jquery.bootstrap.min.js"></script>
<script type="text/javascript">
	$(function() {
		if ('${sessionScope.pritk == null and cookie.jeadmin_user.value != null}' == 'true') {
			$.messager.popup("你好，欢迎回来！");
		}
	});
</script>