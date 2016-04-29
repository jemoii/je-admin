<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../include/head.jsp" />
<div class="container theme-showcase" role="main">
	<div class="jumbotron">
		<c:choose>
			<c:when test="${isAuth}">
				<span class="label label-success">验证成功</span>
			</c:when>
			<c:otherwise>
				<span class="label label-warning">验证失败，请稍后重新验证</span>
			</c:otherwise>
		</c:choose>
	</div>
</div>
<jsp:directive.include file="../include/footer.jsp" />