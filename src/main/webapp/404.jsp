<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap core CSS -->
<link href="http://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
<!-- Bootstrap theme -->
<link href="http://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" rel="stylesheet">
<!-- Custom styles for this template -->
<link href="http://7u2pdv.com1.z0.glb.clouddn.com/example.theme.css" rel="stylesheet">
<link href="/static/bootstrap/theme.css" rel="stylesheet">
<title>页面不存在...</title>
</head>
<body role="document">
	<div class="container theme-showcase" role="main">
		<div class="row">
			<div class="col-sm-6">
				<div class="panel panel-warning">
					<div class="panel-body">
						<p>
							<span class="label label-warning" id="time">3</span> 秒之后跳转至首页...</br>
							页面未自动跳转？请点击 <a href="/jeadmin/">这里</a>
						</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<!-- Bootstrap core JavaScript -->
<script src="http://cdn.bootcss.com/jquery/1.10.2/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(function() {
		jump();
	});
	var delay = 3;
	function jump() {
		if (delay == 0) {
			location.href = "/jeadmin/";
		} else {
			$("#time").html(delay);
			delay--;
			window.setTimeout("jump()", 1000);
		}
	}
</script>
</html>