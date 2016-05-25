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
						<td><h4>
								<span class="label label-default">邮箱：</span>
							</h4></td>
						<td colspan="2"><input type="text" class="form-control"
							id="username"></td>
					</tr>
					<tr>
						<td colspan="3"><button id="btn_reset"
								class="btn btn-default navbar-right" onclick="reset()">重置密码</button></td>
					</tr>
					<tr>
						<td colspan="3"><span class="label label-warning"
							id="reset_tip"></span></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
<!-- /container -->
<jsp:directive.include file="../include/footer.jsp" />
<script type="text/javascript">
	function reset() {
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
	}
	$(document).keydown(function(event) {
		if (event.keyCode == 13) {
			reset();
		}
	})
</script>