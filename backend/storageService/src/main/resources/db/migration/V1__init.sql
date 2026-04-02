use `team-work-storage-s`;

CREATE TABLE IF NOT EXISTS `file_nodes`
(
    `id`                binary(16)   not null,
    `version`           bigint       null,
    `name`              varchar(50)  null,
    `parent_id`         binary(16)   null,
    `storage_key`       varchar(36)  null,
    `materialized_path` varchar(255) null,
    `size`              bigint       null     default 0,
    `sub_tree_size`     bigint       null     default 0,
    `node_type`         varchar(32)  not null,
    `deleted`           boolean      not null default 0,
    `user_storage_id`   binary(16)   null,
    `created_by`        varchar(20) not null,
    `updated_by`        varchar(20) null,
    `created_at`        timestamp   not null default current_timestamp,
    `updated_at`        timestamp   not null default current_timestamp on update current_timestamp,
    constraint `pk_file_nodes` primary key (`id`)
);

CREATE TABLE IF NOT EXISTS `user_storage`
(
    `resource_id`  binary(16) not null,
    `version`      bigint     not null,
    `user_id`      binary(16) not null,
    `used_bytes`   bigint     not null default 0,
    `quota_bytes`  bigint     not null default 50000000000,
    `root_node_id` binary(16) null,
    `created_by`        varchar(20) not null,
    `updated_by`        varchar(20) null,
    `created_at`        timestamp   not null default current_timestamp,
    `updated_at`        timestamp   not null default current_timestamp on update current_timestamp,
    constraint `pk_users_storage` primary key (`resource_id`)
);

alter table file_nodes
    add constraint `fk_file_nodes_storage`
        foreign key (`user_storage_id`)
            references `user_storage` (`resource_id`);

create index `idx_file_nodes_storage` on `file_nodes` (`user_storage_id`);


alter table user_storage
    add constraint `uq_user_storage_root_id` unique (`root_node_id`);

alter table user_storage
    add constraint `uq_user_storage_user_id` unique (`user_id`);

alter table user_storage
    add constraint `fk_user_storage_root_node_id` foreign key (`root_node_id`) references `file_nodes` (`id`);

create unique index `idx_user_storage_user_id` on `user_storage` (`user_id`);
