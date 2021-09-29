drop table if exists prodigal_user;
create table prodigal_user
(
    id           bigint auto_increment primary key,
    name         varchar(32) comment '用户名称',
    username     varchar(255) not null comment '登录名',
    password     varchar(255) not null comment '用户密码',
    created_time timestamp default current_timestamp,
    updated_time timestamp default current_timestamp,
    extend       json
);
CREATE INDEX idx_prodigal_user_tenant_id_user_id_title ON prodigal_user (name, username);
create index prodigal_user_extend_index on prodigal_user (extend);
alter table prodigal_user
    comment '用户表';

drop table if exists prodigal_role;
create table prodigal_role
(
    id           bigint auto_increment primary key,
    name         varchar(32) comment '权限名',
    created_time timestamp default current_timestamp,
    updated_time timestamp default current_timestamp,
    extend       json
);
CREATE INDEX idx_prodigal_role_tenant_id_user_id_title ON prodigal_role (name);
create index prodigal_role_extend_index on prodigal_role (extend);
alter table prodigal_role
    comment '用户权限表';

drop table if exists prodigal_user_role;
create table prodigal_user_role
(
    user_id      bigint not null comment '用户 ',
    role_id      bigint not null comment '权限id',
    created_time timestamp default current_timestamp,
    updated_time timestamp default current_timestamp,
    extend       json
);
create index prodigal_user_role_extend_index on prodigal_role (extend);
alter table prodigal_user_role
    comment '用户权限赋值表';

