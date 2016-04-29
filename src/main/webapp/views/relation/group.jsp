<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../include/head.jsp" />
<div class="container theme-showcase" role="main">
	<div class="row">
		<div class="col-md-8">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h1>信息列表</h1>
				</div>
				<div class="panel-body">
					<table id="dg" class="easyui-datagrid"
						style="width: 100%; height: auto"
						data-options="
				singleSelect:true,
                toolbar: '#tb',
                url: './group.json',
                method: 'get'">
						<thead>
							<tr>
								<th
									data-options="field:'status',width:80,
						formatter:function(value,row){
							if(row.status == 'student'){
                            	return '学生';
                            } else {
                            	return '教师';
                            }
                        }">身份</th>
								<th data-options="field:'userId',width:100">编号</th>
								<th data-options="field:'username',width:100">昵称</th>
								<th data-options="field:'email',width:200">邮箱地址</th>
								<th data-options="field:'telephone',width:150">手机号</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="tb" style="height: auto">
	<p>
		<a href="javascript:void(0)" class="btn btn-success"
			onclick="addUser()">添加</a> <a href="javascript:void(0)"
			class="btn btn-danger" onclick="removeUser()">删除</a> <a
			href="javascript:void(0)" class="btn btn-primary"
			onclick="editUser()">编辑</a>
	</p>
</div>

<div id="add-dlg" class="easyui-dialog"
	style="width: 400px; height: auto; padding: 10px 20px" closed="true"
	buttons="#add-dlg-buttons">
	<form id="add-fm">
		<table class="table">
			<tbody>
				<tr>
					<td><span><input type="radio" name="add_user_status"
							value="student" checked="checked" /> 学生</span></td>
					<c:if test="${sessionScope.sentk != null}">
						<td><span><input type="radio" name="add_user_status"
								value="teacher" /> 教师</span></td>
					</c:if>
				</tr>
				<tr>
					<td><h4>
							<span class="label label-default">昵称：</span>
						</h4></td>
					<td colspan="2"><input type="text" class="form-control"
						id="add_username"></td>
				</tr>
				<tr>
					<td><h4>
							<span class="label label-default">邮箱：</span>
						</h4></td>
					<td colspan="2"><input type="text" class="form-control"
						id="add_email"></td>
				</tr>
				<tr>
					<td><h4>
							<span class="label label-default">密码：</span>
						</h4></td>
					<td colspan="2"><input type="password" class="form-control"
						id="add_password"></td>
				</tr>
				<tr>
					<td colspan="3"><span class="label label-warning"
						id="add_register_tip"></span></td>
				</tr>
			</tbody>
		</table>
	</form>
</div>
<div id="add-dlg-buttons">
	<a href="javascript:void(0)" class="btn btn-success"
		onclick="confirmAddUser()">添加</a> <a href="javascript:void(0)"
		class="btn btn-default"
		onclick="javascript:$('#add-dlg').dialog('close')">取消</a>
</div>

<div id="edit-dlg" class="easyui-dialog"
	style="width: 400px; height: auto; padding: 10px 20px" closed="true"
	buttons="#edit-dlg-buttons">
	<form id="edit-fm">
		<table class="table">
			<tbody>
				<tr>
					<td><h4>
							<span class="label label-default">昵称：</span>
						</h4></td>
					<td colspan="2"><input type="text" class="form-control"
						id="edit_username"></td>
				</tr>
				<tr>
					<td><h4>
							<span class="label label-default">手机号：</span>
						</h4></td>
					<td colspan="2"><input type="text" class="form-control"
						id="edit_telephone"></td>
				</tr>
				<tr>
					<td colspan="3"><span class="label label-warning"
						id="edit_group_tip"></span></td>
				</tr>
			</tbody>
		</table>
	</form>
</div>
<div id="edit-dlg-buttons">
	<a href="javascript:void(0)" class="btn btn-primary"
		onclick="confirmEditUser()">修改</a> <a href="javascript:void(0)"
		class="btn btn-default"
		onclick="javascript:$('#edit-dlg').dialog('close')">取消</a>
</div>
<jsp:directive.include file="../include/footer.jsp" />
<script type="text/javascript" src="./static/easyui/js/jquery.js"></script>
<script type="text/javascript"
	src="./static/easyui/js/jquery.easyui.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="./static/easyui/css/easyui.css">
<link rel="stylesheet" type="text/css"
	href="./static/easyui/css/demo.css">
<script type="text/javascript">
	function addUser() {
		$('#add-dlg').dialog('open').dialog('center').dialog('setTitle',
				'添加用户信息');
		$('#add_register_tip').html("");
		$('#add-fm').form('clear');
		$("input[name='add_user_status']").get(0).checked = true;
	}
	function confirmAddUser() {
		$.messager.defaults = {
			ok : "确定",
			cancel : "取消"
		};
		if (!/^(\w{5,9})$/.test($("#add_username").val())) {
			$('#add_register_tip').html("昵称由5~9个大小写字母、数字组成");
			return;
		}
		if (!/^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/.test($("#add_email").val())) {
			$('#add_register_tip').html("邮箱地址不合法");
			return;
		}
		if ($('#add_username').val().trim() != ""
				&& $('#add_email').val().trim() != ""
				&& $('#add_password').val().trim() != "") {
			$.ajax({
				type : 'post',
				url : './group.json',
				data : {
					status : $('input[name="add_user_status"]:checked').val(),
					username : $("#add_username").val().trim(),
					email : $('#add_email').val().trim(),
					password : $('#add_password').val().trim(),
				},
				success : function(obj) {
					if (obj.status) {
						$('#add-dlg').dialog('close'); // close the dialog
						$('#dg').datagrid('reload'); // reload the user data
					} else {
						$.messager.alert('添加用户信息', '添加用户信息失败！');
					}
				},
				error : function() {

				}
			});
		}
	}
	function editUser() {
		$('#edit_group_tip').html("");
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			$('#edit-dlg').dialog('open').dialog('center').dialog('setTitle',
					'编辑用户信息');
			$('#edit_username').val(row.username);
			$('#edit_telephone').val(row.telephone);
		} else {
			$.messager.defaults = {
				ok : "确定",
				cancel : "取消"
			};
			$.messager.alert('编辑用户信息', '未选定用户信息！');
		}
	}
	function confirmEditUser() {
		$.messager.defaults = {
			ok : "确定",
			cancel : "取消"
		};
		if (!/^(\w{5,9})$/.test($('#edit_username').val())) {
			$('#edit_group_tip').html("昵称由5~9个大小写字母、数字组成");
			return;
		}
		if ($('#edit_telephone').val().trim() != ""
				&& !/^(\d{11})$/.test($('#edit_telephone').val())) {
			$('#edit_group_tip').html("手机号不合法");
			return;
		}
		var row = $('#dg').datagrid('getSelected');
		$.ajax({
			type : 'put',
			url : './group.json',
			contentType : 'application/json; charset=UTF-8',
			data : JSON.stringify({
				userId : row.userId,
				username : $('#edit_username').val().trim(),
				status : row.status,
				email : row.email,
				telephone : $('#edit_telephone').val().trim(),
			}),
			success : function(obj) {
				if (obj.status) {
					$('#edit-dlg').dialog('close'); // close the dialog
					$('#dg').datagrid('reload'); // reload the user data
				} else {
					$.messager.alert('编辑用户信息', '编辑用户信息失败！');
				}

			},
			error : function() {

			}
		});
	}

	function removeUser() {
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			$.messager.defaults = {
				ok : "确定",
				cancel : "取消"
			};
			$.messager
					.confirm(
							'删除用户信息',
							'确定删除编号为' + row.userId + '的用户？',
							function(r) {
								if (r) {
									$.ajax({
												type : 'delete',
												url : './group.json',
												contentType : 'application/json; charset=UTF-8',
												data : JSON.stringify({
													userId : row.userId,
													username : row.username,
													status : row.status,
													email : row.email,
													telephone : row.telephone,
												}),
												success : function(obj) {
													if (obj.status) {
														$('#dg').datagrid(
																'reload'); // reload the user data
													} else {
														$.messager.alert(
																'删除用户信息',
																'删除用户信息失败！');
													}
												},
												error : function() {

												}
											});
								}
							});
		} else {
			$.messager.defaults = {
				ok : "确定",
				cancel : "取消"
			};
			$.messager.alert('删除用户信息', '未选定用户信息！');
		}
	}
</script>
<style type="text/css">
#edit-fm, #add-fm {
	margin: 0;
	padding: 10px 30px;
}

.fitem {
	margin-bottom: 5px;
}

.fitem label {
	display: inline-block;
	width: 80px;
}

.fitem input {
	width: 160px;
}

.fitem span {
	color: #FF0000;
	width: 160px;
}
</style>