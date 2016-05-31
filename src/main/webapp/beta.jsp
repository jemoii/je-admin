<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--Let browser know website is optimized for mobile-->
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<!-- Material Design fonts -->
<link rel="stylesheet" type="text/css"
	href="//fonts.useso.com/css?family=Roboto:300,400,500,700">
<link rel="stylesheet" type="text/css"
	href="//fonts.useso.com/icon?family=Material+Icons">
<!-- Bootstrap -->
<link href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css"
	rel="stylesheet">
<!-- Bootstrap Material Design -->
<link
	href="//fezvrasta.github.io/bootstrap-material-design/dist/css/bootstrap-material-design.css"
	rel="stylesheet">
<link
	href="//fezvrasta.github.io/bootstrap-material-design/dist/css/ripples.min.css"
	rel="stylesheet">
<style type="text/css">
a.segoe {
	font-family: "Segoe Script"
}
</style>
<title>Theme Beta</title>
</head>
<body>
	<div class="navbar navbar-default">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-responsive-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand segoe" href="/">Jeadmin</a>
			</div>
			<div class="navbar-collapse collapse navbar-responsive-collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="javascript:void(0)">Link</a></li>
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown">Dropdown<b class="caret"></b>
					</a>
						<ul class="dropdown-menu">
							<li><a href="javascript:void(0)">Action</a></li>
							<li><a href="javascript:void(0)">Another action</a></li>
							<li><a href="javascript:void(0)">Something else here</a></li>
							<li class="divider"></li>
							<li class="dropdown-header">Dropdown header</li>
							<li><a href="javascript:void(0)">Separated link</a></li>
							<li><a href="javascript:void(0)">One more separated link</a></li>
						</ul></li>
				</ul>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="row">
			<div class="col-md-6">
				<div class="well bs-component">
					<ul class="nav nav-tabs" style="margin-bottom: 15px;">
						<li class="active"><a href="#login" data-toggle="tab">登录</a></li>
						<li><a href="#register" data-toggle="tab">注册</a></li>
					</ul>
					<div class="tab-content">
						<div class="tab-pane fade active in" id="login">
							<form class="form-horizontal">
								<div class="form-group">
									<label for="username" class="col-md-2 control-label">邮箱</label>
									<div class="col-md-8">
										<input type="email" class="form-control" id="username">
									</div>
								</div>
								<div class="form-group">
									<label for="password" class="col-md-2 control-label">密码</label>
									<div class="col-md-8">
										<input type="password" class="form-control" id="password">
									</div>
								</div>
								<div class="form-group">
									<div class="col-md-8 col-md-offset-1">
										<button type="submit" class="btn btn-raised btn-primary">登录</button>
										<a href="#reset-dialog" data-toggle="modal">忘记密码？ </a>
									</div>
								</div>
							</form>
						</div>
						<div class="tab-pane fade" id="register">
							<form class="form-horizontal">
								<div class="form-group">
									<label class="col-md-2 control-label">身份</label>
									<div class="col-md-10">
										<div class="radio radio-primary">
											<label> <input type="radio" name="user_level"
												value="1" checked="">学生
											</label> <label> <input type="radio" name="user_level"
												value="2">教师
											</label> <label> <input type="radio" name="user_level"
												value="3">管理员
											</label>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label for="rg_username" class="col-md-2 control-label">邮箱</label>
									<div class="col-md-8">
										<input type="email" class="form-control" id="rg_username">
									</div>
								</div>
								<div class="form-group">
									<label for="rg_password" class="col-md-2 control-label">密码</label>
									<div class="col-md-8">
										<input type="password" class="form-control" id="rg_password">
									</div>
								</div>
								<div class="form-group">
									<div class="col-md-8 col-md-offset-1">
										<button type="submit" class="btn btn-raised btn-primary">注册</button>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="reset-dialog" class="modal fade" tabindex="-1">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title">重置密码</h4>
				</div>
				<div class="modal-body">
					<div class="form-group label-floating">
						<form class="bs-component">
							<div class="input-group">
								<label class="control-label" for="rs_username">请输入注册使用的邮箱</label>
								<input type=email id="rs_username" class="form-control">
								<span class="input-group-btn">
									<button type="button" class="btn btn-fab btn-fab-mini">
										<i class="material-icons">send</i>
									</button>
								</span>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
<script src="//cdn.bootcss.com/jquery/1.10.2/jquery.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

<script
	src="//fezvrasta.github.io/bootstrap-material-design/dist/js/material.min.js"></script>
<script
	src="//fezvrasta.github.io/bootstrap-material-design/dist/js/ripples.min.js"></script>
<script>
	$(function() {
		$.material.init();
		$(".dropdown-menu li a").bind("click", function() {
			var $this = $(this);
			var $a = $this.parent().parent().prev()
			$a.text($this.text());
			$a.append("<span class='caret'>");
		});
	})
</script>
</html>