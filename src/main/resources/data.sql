# 初始化用户信息
INSERT INTO prodigal_user (id, name, username, password)
VALUES (1, '管理员', 'admin', '123165465');

# 初始化权限名称
INSERT INTO prodigal_role (id, name)
VALUES (1, 'ADMIN_SYSTEM');
INSERT INTO prodigal_role (id, name)
VALUES (2, 'ADMIN_HOME');

# 分配权限
INSERT INTO prodigal_user_role (prodigal_user_id, roles_id)
VALUES (1, 1);
INSERT INTO prodigal_user_role (prodigal_user_id, roles_id)
VALUES (1, 2);