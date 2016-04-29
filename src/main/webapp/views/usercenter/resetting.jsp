<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../include/head.jsp" />
<div class="container theme-showcase" role="main">
	<div class="page-header">
		<h1>重置密码</h1>
	</div>
	<div class="row">
		<div class="col-md-4">
			<table class="table">
				<tbody>
					<tr>
						<td colspan="3"><h4>注册邮箱为：${email}</h4></td>
					</tr>
					<c:choose>
						<c:when test="${canReset}">
							<tr>
								<td><h4>
										<span class="label label-default">密码：</span>
									</h4></td>
								<td colspan="2"><input type="password" class="form-control"
									id="password"></td>
							</tr>
							<tr>
								<td><h4>
										<span class="label label-default">确认密码：</span>
									</h4></td>
								<td colspan="2"><input type="password" class="form-control"
									id="confirmPassword"></td>
							</tr>
							<tr>
								<td colspan="3"><button id="btn_reset"
										class="btn btn-default navbar-right" onclick="reset()">重置密码</button></td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr>
								<td><span class="label label-warning">验证失败，请稍后重置密码</span></td>
							</tr>
						</c:otherwise>
					</c:choose>
					<tr>
						<td colspan="3"><span class="label label-warning"
							id="reset_tip"></span></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
<jsp:directive.include file="../include/footer.jsp" />
<script type="text/javascript">
	function reset() {
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
					email : '${email}',
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
	}
	$(document).keydown(function(event) {
		if (event.keyCode == 13) {
			reset();
		}
	})
</script>