drop table if exists prodigal_user;
create table prodigal_user
(
    id           bigint auto_increment primary key,
    name         varchar(32) comment '用户名称',
    username     varchar(255)                            not null comment '登录名',
    phone        varchar(32)   default null comment '登录名',
    avatar       varchar(2048) default null comment '登录名',
    password     varchar(255)                            not null comment '用户密码',
    created_time timestamp     default current_timestamp not null comment '创建时间',
    updated_time timestamp     default current_timestamp not null comment '修改时间'
) comment '用户表';
CREATE INDEX idx_prodigal_user_tenant_id_user_id_title ON prodigal_user (name, username);

drop table if exists prodigal_role;
create table prodigal_role
(
    id           bigint auto_increment primary key,
    name         varchar(32) comment '权限名',
    introduce    varchar(255) comment '描述',
    created_time timestamp default current_timestamp not null comment '创建时间',
    updated_time timestamp default current_timestamp not null comment '修改时间'
) comment '用户角色';
CREATE INDEX idx_prodigal_role_tenant_id_user_id_title ON prodigal_role (name);

drop table if exists prodigal_user_role;
create table prodigal_user_role
(
    user_id bigint not null comment '用户 ',
    role_id bigint not null comment '权限id'
) comment '用户权限赋值表';

drop table if exists prodigal_authority;
create table prodigal_authority
(
    id             bigint auto_increment primary key,
    authority      varchar(255)                        not null comment '角色',
    interface_path varchar(255)                        not null comment '请求路径',
    created_time   timestamp default current_timestamp not null comment '创建时间',
    updated_time   timestamp default current_timestamp not null comment '修改时间',
    unique (authority, interface_path)
) comment '权限与接口对应关系表';

drop table if exists prodigal_resources;
create table prodigal_resources
(
    id           bigint auto_increment primary key,
    path         varchar(255)                        not null comment '资源路径',
    name         varchar(255)                        not null comment '资源描述',
    created_time timestamp default current_timestamp not null comment '创建时间',
    updated_time timestamp default current_timestamp not null comment '修改时间',
    unique (path, name)
) comment '请求资源表';

drop table if exists user_forum_post;
CREATE TABLE user_forum_post
(
    id           bigint auto_increment primary key,
    title        VARCHAR(255) COMMENT '标题',
    content      TEXT COMMENT '内容',
    username     VARCHAR(255) COMMENT '用户账户',
    usernames    VARCHAR(255) COMMENT '用户姓名',
    created_time timestamp default current_timestamp not null comment '创建时间',
    updated_time timestamp default current_timestamp not null comment '修改时间',
    INDEX idx_title (title)
) COMMENT ='帖子';


