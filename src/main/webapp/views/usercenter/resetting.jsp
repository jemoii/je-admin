<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../include/head.jsp" />
<div class="container theme-showcase" role="main">
	<div class="page-header">
		<h1>重置密码</h1>
	</div>
	<div class="row">
		<div class="col-md-6">
			<h4>注册邮箱：${loginname}</h4>
			<c:choose>
				<c:when test="${canReset}">
					<div class="form-group col-md-8">
						<label for=password class="control-label">密码</label> <input
							type="password" class="form-control" id="password">
					</div>
					<div class="form-group col-md-8">
						<label for=confirmPassword class="control-label">确认密码</label> <input
							type="password" class="form-control" id="confirmPassword">
					</div>
					<div class="form-group col-md-8">
						<button class="btn btn-default navbar-right"
							data-loading-text="请等待..." id="reset_btn">重置密码</button>
						<span class="label label-warning" id="reset_tip"></span>
					</div>
				</c:when>
				<c:otherwise>
					<div class="form-group col-md-8">
						<span class="label label-warning">验证失败，请稍后重置密码</span>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>
<jsp:directive.include file="../include/footer.jsp" />
<script type="text/javascript">
$('#reset_btn').on('click', function() {
	var btn = $(this).button('loading');
	setTimeout(function() {
		btn.button('reset');
	}, 1000);

	$('#reset_tip').html("");
	if ($('#password').val().trim() != $('#confirmPassword').val().trim()) {
		$('#reset_tip').html("两次输入的密码不一致");
		return;
	}
	if ($('#password').val().trim() != "") {
		$.ajax({
			type : 'post',
			url : './resetting.json',
			data : {
				username : '${loginname}',
				password : $('#password').val().trim()
			},
			success : function(json) {
				if (json.status) {
					location.href = './login';
				} else {
					$('#reset_tip').html("请稍后重置密码");
				}
			},
			error : function() {

			}
		});
	}
})
</script>