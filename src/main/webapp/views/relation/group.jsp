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
					<label style="width: 25%;"><input type="radio"
						name="add_user_level" value="1" checked="checked" /> 学生</label>
					<c:if test="${sessionScope.sentk != null}">
						<label style="width: 25%;"><input type="radio"
							name="add_user_level" value="2" /> 教师</label>
					</c:if>
				</div>
				<div class="form-group">
					<label for="username">邮箱</label> <input type="username"
						class="form-control" id="add_username">
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
					<label for="nickname">昵称</label> <input type="nickname"
						class="form-control" id="edit_nickname">
				</div>
				<!--  
				<div class="form-group">
					<label for="telephone">手机号</label> <input type="telephone"
						class="form-control" id="edit_telephone">
				</div>
				-->
				<div class="form-group">
					<label style="width: 25%;"><input type="radio"
						name="edit_user_status" value="-1" /> 禁用</label><label
						style="width: 30%;"><input type="radio"
						name="edit_user_status" value="0" /> 待验证</label><label
						style="width: 25%;"><input type="radio"
						name="edit_user_status" value="1" /> 正常</label>
				</div>
			</form>
		</div>
	</div>
</div>
<jsp:directive.include file="../include/footer.jsp" />
<script src="http://7u2pdv.com1.z0.glb.clouddn.com/jquery.bootstrap.min.js"></script>
<script type="text/javascript">
$(function() {
	$.messager.model = {
		ok: {
			text: "确定",
			classed: "btn-success",
		},
		cancel: {
			text: "取消",
			classed: "btn-default",
		}
	};
	loadUsers();
});
function loadUsers() {
	$.ajax({
		type: 'get',
		url: './group.json',
		success: function(obj) {
			$('#tablewrap').datagrid({
				columns: [[{
					title: "身份",
					formatter: function(value, row) {
						if (row.level == '1') {
							return '学生';
						} else {
							return '教师';
						}
					}
				},
				{
					title: "昵称",
					field: "nickname"
				},
				{
					title: "邮箱地址",
					field: "username"
				},
				{
					title: "手机号",
					field: "telephone"
				},
				{
					title: "状态",
					formatter: function(value, row) {
						if (row.status == '-1') {
							return '禁用';
						} else if (row.status == 0) {
							return '待验证';
						} else {
							return '正常';
						}
					}
				}]],
				singleSelect: true
			}).datagrid("loadData", {
				rows: obj.rows
			});

		},
		error: function() {

		}
	});
}
function addUser() {
	$("#addwrap").dialog({
		title: "添加用户信息",
		dialogClass: "modal-sm",
		buttons: [{
			text: "确定",
			classed: "btn-success",
			click: confirmAddUser
		},
		{
			text: "取消",
			classed: "btn-default",
			click: function() {
				$(this).dialog("close");
			}
		}]
	});
	$('#add_username').val("");
	$('#add_password').val("");
}
function confirmAddUser() {
	if (!/^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/.test($("#add_username").val())) {
		$.messager.alert("邮箱地址不合法");
		return;
	}
	if ($('#add_username').val().trim() != "" && $('#add_password').val().trim() != "") {
		$.ajax({
			type: 'post',
			url: './group.json',
			data: {
				level: $('input[name="add_user_level"]:checked').val(),
				username: $("#add_username").val().trim(),
				password: $('#add_password').val().trim(),
			},
			success: function(obj) {
				if (obj.status) {
					$("#addwrap").dialog("close");
					loadUsers();
				} else {
					$.messager.alert('添加用户信息', '添加用户信息失败！');
				}
			},
			error: function() {

			}
		});
	}
}
function removeUser() {
	var row = $('#tablewrap').datagrid("getSelections");
	if (row.length != 0) {
		$.messager.confirm('删除用户信息', '确定删除邮箱地址为' + row[0].username + '的用户？',
		function() {
			$.ajax({
				type: 'delete',
				url: './group.json',
				contentType: 'application/json; charset=UTF-8',
				data: JSON.stringify({
					username: row[0].username,
					level: row[0].level,
					nickname: row[0].nickname,
					telephone: row[0].telephone,
				}),
				success: function(obj) {
					if (obj.status) {
						loadUsers();
					} else {
						$.messager.alert('删除用户信息', '删除用户信息失败！');
					}
				},
				error: function() {

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
			title: "编辑用户信息",
			dialogClass: "modal-sm",
			buttons: [{
				text: "确定",
				classed: "btn-success",
				click: confirmEditUser
			},
			{
				text: "取消",
				classed: "btn-default",
				click: function() {
					$(this).dialog("close");
				}
			}]
		});
		$('#edit_nickname').val(row[0].nickname);
		$('input[name=edit_user_status]').get(row[0].status + 1).checked = true;
		//$('#edit_telephone').val(row[0].telephone);
	} else {
		$.messager.popup('未选定用户信息！');
	}
}
function confirmEditUser() {
	if ($('#edit_nickname').val().trim() != "" && !/^(\w{5,9})$/.test($('#edit_nickname').val())) {
		$.messager.alert("昵称由5~9个大小写字母、数字组成");
		return;
	}
	//if ($('#edit_telephone').val().trim() != ""
	//		&& !/^(\d{11})$/.test($('#edit_telephone').val())) {
	//	$.messager.alert("手机号不合法");
	//	return;
	//}
	var row = $('#tablewrap').datagrid("getSelections");
	$.ajax({
		type: 'put',
		url: './group.json',
		contentType: 'application/json; charset=UTF-8',
		data: JSON.stringify({
			nickname: $('#edit_nickname').val().trim(),
			status: $('input[name="edit_user_status"]:checked').val(),
			level: row[0].level,
			username: row[0].username,
			telephone: row[0].telephone
			//$('#edit_telephone').val().trim(),
		}),
		success: function(obj) {
			if (obj.status) {
				$("#editwrap").dialog("close");
				loadUsers();
			} else {
				$.messager.alert('编辑用户信息', '编辑用户信息失败！');
			}

		},
		error: function() {

		}
	});
}
</script>
