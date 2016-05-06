<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.include file="../include/head.jsp" />
<div class="container theme-showcase" role="main">
	<div class="row">
		<div class="page-header"></div>

		<div class="col-md-8">
			<div class="panel-heading">
				<a href="javascript:void(0)" class="btn btn-success"
					onclick="addUser()">添加</a> <a href="javascript:void(0)"
					class="btn btn-danger" onclick="removeUser()">删除</a> <a
					href="javascript:void(0)" class="btn btn-primary"
					onclick="editUser()">编辑</a>
			</div>
			<div class="panel panel-info">
				<div class="panel-body">
					<table id="tablewrap" class="table table-striped"></table>
				</div>
			</div>
		</div>
	</div>

	<div class="hide">
		<div id="addwrap">
			<form id="addform" role="form">
				<div class="form-group">
					<span><input type="radio" name="add_user_status"
						value="student" checked="checked" /> 学生</span>
					<c:if test="${sessionScope.sentk != null}">
						<span><input type="radio" name="add_user_status"
							value="teacher" /> 教师</span>
					</c:if>
				</div>
				<div class="form-group">
					<label for="username">昵称</label> <input type="username"
						class="form-control" id="add_username">
				</div>
				<div class="form-group">
					<label for="email">邮箱</label> <input type="email"
						class="form-control" id="add_email">
				</div>
				<div class="form-group">
					<label for="password">密码</label> <input type="password"
						class="form-control" id="add_password">
				</div>
			</form>
		</div>
	</div>
	<div class="hide">
		<div id="editwrap">
			<form id="editform" role="form">
				<div class="form-group">
					<label for="username">昵称</label> <input type="username"
						class="form-control" id="edit_username">
				</div>
				<div class="form-group">
					<label for="telephone">手机号</label> <input type="telephone"
						class="form-control" id="edit_telephone">
				</div>
			</form>
		</div>
	</div>
</div>
<jsp:directive.include file="../include/footer.jsp" />
<script src="/jeadmin/static/bootstrap/jquery.bootstrap.min.js"></script>
<script type="text/javascript">
	$(function() {
		$.messager.model = {
			ok : {
				text : "确定",
				classed : "btn-success",
			},
			cancel : {
				text : "取消",
				classed : "btn-default",
			}
		};
		loadUsers();
	});
	function loadUsers() {
		$.ajax({
			type : 'get',
			url : './group.json',
			success : function(obj) {
				$('#tablewrap').datagrid({
					columns : [ [ {
						title : "身份",
						formatter : function(value, row) {
							if (row.status == 'student') {
								return '学生';
							} else {
								return '教师';
							}
						}
					}, {
						title : "编号",
						field : "userId"
					}, {
						title : "昵称",
						field : "username"
					}, {
						title : "邮箱地址",
						field : "email"
					}, {
						title : "手机号",
						field : "telephone"
					} ] ],
					singleSelect : true
				}).datagrid("loadData", {
					rows : obj.rows
				});

			},
			error : function() {

			}
		});
	}
	function addUser() {
		$("#addwrap").dialog({
			title : "添加用户信息",
			dialogClass : "modal-sm",
			buttons : [ {
				text : "确定",
				classed : "btn-success",
				click : confirmAddUser
			}, {
				text : "取消",
				classed : "btn-default",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		});
		$('#add_username').val("");
		$('#add_email').val("");
		$('#add_password').val("");
	}
	function confirmAddUser() {
		if (!/^(\w{5,9})$/.test($("#add_username").val())) {
			$.messager.alert("昵称由5~9个大小写字母、数字组成");
			return;
		}
		if (!/^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/.test($("#add_email").val())) {
			$.messager.alert("邮箱地址不合法");
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
						$("#addwrap").dialog("close");
						loadUsers();
					} else {
						$.messager.alert('添加用户信息', '添加用户信息失败！');
					}
				},
				error : function() {

				}
			});
		}
	}
	function removeUser() {
		var row = $('#tablewrap').datagrid("getSelections");
		if (row.length != 0) {
			$.messager.confirm('删除用户信息', '确定删除编号为' + row[0].userId + '的用户？',
					function() {
						$.ajax({
							type : 'delete',
							url : './group.json',
							contentType : 'application/json; charset=UTF-8',
							data : JSON.stringify({
								userId : row[0].userId,
								username : row[0].username,
								status : row[0].status,
								email : row[0].email,
								telephone : row[0].telephone,
							}),
							success : function(obj) {
								if (obj.status) {
									loadUsers();
								} else {
									$.messager.alert('删除用户信息', '删除用户信息失败！');
								}
							},
							error : function() {

							}
						});
					});
		} else {
			$.messager.popup('未选定用户信息！');
		}
	}
	function editUser() {
		var row = $('#tablewrap').datagrid("getSelections");
		if (row.length != 0) {
			$("#editwrap").dialog({
				title : "编辑用户信息",
				dialogClass : "modal-sm",
				buttons : [ {
					text : "确定",
					classed : "btn-success",
					click : confirmEditUser
				}, {
					text : "取消",
					classed : "btn-default",
					click : function() {
						$(this).dialog("close");
					}
				} ]
			});
			$('#edit_username').val(row[0].username);
			$('#edit_telephone').val(row[0].telephone);
		} else {
			$.messager.popup('未选定用户信息！');
		}
	}
	function confirmEditUser() {
		if (!/^(\w{5,9})$/.test($('#edit_username').val())) {
			$.messager.alert("昵称由5~9个大小写字母、数字组成");
			return;
		}
		if ($('#edit_telephone').val().trim() != ""
				&& !/^(\d{11})$/.test($('#edit_telephone').val())) {
			$.messager.alert("手机号不合法");
			return;
		}
		var row = $('#tablewrap').datagrid("getSelections");
		$.ajax({
			type : 'put',
			url : './group.json',
			contentType : 'application/json; charset=UTF-8',
			data : JSON.stringify({
				userId : row[0].userId,
				username : $('#edit_username').val().trim(),
				status : row[0].status,
				email : row[0].email,
				telephone : $('#edit_telephone').val().trim(),
			}),
			success : function(obj) {
				if (obj.status) {
					$("#editwrap").dialog("close");
					loadUsers();
				} else {
					$.messager.alert('编辑用户信息', '编辑用户信息失败！');
				}

			},
			error : function() {

			}
		});
	}
</script>
