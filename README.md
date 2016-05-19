<del>工程使用的数据库为PostgreSQL 9.3 MySQL，在[me.voler.admin.util.DataBaseUtil](https://github.com/jemoii/je-admin/blob/master/src/main/java/me/voler/admin/util/DataBaseUtil.java)中修改数据库的配置参数，</del>login_info_v2表中的user_status属性标识用户的身份，

- 学生：student、
- 教师：teacher、
- 管理员：admin。

login_info_v2表中的auth属性标识用户注册使用的邮箱是否已验证，

- 已验证：true、
- 未验证：false。

这里使用表mail_auth保存验证信息，也可以使用Redis保存验证信息。

- 在主界面点击注册按钮，填写注册信息，完成注册后进入验证页面`/unauthenticated`，
点击`现在验证`按钮会向注册邮箱发送验证邮件，访问验证邮件正文的链接验证注册使用的邮箱；点击`稍后验证`按钮直接进入登录界面，发送邮件基于commons-email，<del>在[me.voler.admin.util.MailUtil](https://github.com/jemoii/je-admin/blob/master/src/main/java/me/voler/admin/util/MailUtil.java)中修改邮件的配置参数</del>；
- 进入登录界面后，填写登录信息，登录成功后进入个人信息界面`/space`，点击`忘记密码`链接进入重置密码界面`/reset`；
- 在个人信息界面点击`编辑`按钮，可以编辑个人信息，这里可编辑的信息仅限昵称、手机号；以`教师`或`管理员`身份登录时，在个人信息界面可以看到信息列表链接，点击后进入信息列表界面`/group`，教师的信息列表界面可以看到注册的学生列表，管理员的信息列表界面可以看到注册的学生、教师列表，使用列表上方的按钮可以添加、删除、编辑用户信息；以`学生`身份登录时，直接访问信息列表界面会重定向至404界面；
- 登录成功后点击右上角的下拉栏可以看到`注销`按钮，点击退出登录。

```
- me.voler.admin.filter，权限管理、过滤器
- me.voler.admin.relation，个人信息、信息列表
- me.voler.admin.usercenter，注册、验证、登录、注销、重置密码
- me.voler.admin.util，数据库操作、发送邮件、接口包装
```

========================================

使用[Bootstrap jQuery Plugin](http://bootstrap.ourjs.com/)修改了信息列表界面`/group`的样式。

========================================

工程部署到[百度开放云BAE基础版](http://duapp.voler.me/jeadmin/)，做了一些适应性修改。

========================================

将数据库、邮件的配置参数独立到`.properties`，由[me.voler.admin.util.DeployUtil及子类](https://github.com/jemoii/je-admin/blob/master/src/main/java/me/voler/admin/util/DeployUtil.java)根据本地（线上）运行环境加载配置参数。

========================================

支持移动端扫码登录，因为目前没有移动端App，实现上即将“输入密码”这一步放到移动端来做。