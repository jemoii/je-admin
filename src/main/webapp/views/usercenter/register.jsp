<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../include/head.jsp" />
<div class="container theme-showcase" role="main">
	<div class="page-header">
		<h1>注册</h1>
	</div>
	<div class="row">
		<div class="col-md-6">
			<div class="form-group col-md-8">
				<label for="user_level">身份</label>
				<div class="radio">
					<label style="width: 25%;"><input type="radio"
						name="user_level" value="1" checked="checked" /> 学生</label> <label
						style="width: 25%;"><input type="radio" name="user_level"
						value="2" /> 教师</label> <label style="width: 45%;"><input
						type="radio" name="user_level" value="3" /> 管理员</label>
				</div>
			</div>
			<div class="form-group col-md-8">
				<label for="username">邮箱</label> <input type="email"
					class="form-control" id="username"> <a href="./login">已注册？</a>
			</div>
			<div class="form-group col-md-8">
				<label for=password class="control-label">密码</label> <input
					type="password" class="form-control" id="password">
			</div>
			<div class="form-group col-md-8">
				<button class="btn btn-default navbar-right"
					data-loading-text="正在注册..." id="register_btn">注册</button>
				<span class="label label-warning" id="register_tip"></span>
			</div>
		</div>
	</div>
</div>
<!-- /container -->
<jsp:directive.include file="../include/footer.jsp" />
<script type="text/javascript">
$('#register_btn').on('click',
function() {
	var btn = $(this).button('loading');
	setTimeout(function() {
		btn.button('reset');
	},
	1000);
	
	$('#register_tip').html("");
	//if (!/^(\w{5,9})$/.test($("#nickname").val())) {
	//	$('#register_tip').html("昵称由5~9个大小写字母、数字组成");
	//	return;
	//}
	if (!/^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/.test($("#username").val())) {
		$('#register_tip').html("邮箱地址不合法");
		return;
	}
	if ($('#username').val().trim() != "" && $('#password').val().trim() != "") {
		$.ajax({
			type: 'post',
			url: './register.json',
			data: {
				level: $('input[name="user_level"]:checked').val(),
				username: $("#username").val().trim(),
				password: $('#password').val().trim(),
			},
			success: function(json) {
				if (json.status) {
					location.href = './unauthenticated';
				} else {
					$('#register_tip').html("邮箱地址已注册");
				}

			},
			error: function() {

			}
		});

	}
})
</script>