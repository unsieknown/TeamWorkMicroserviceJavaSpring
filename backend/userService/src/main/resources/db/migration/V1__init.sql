use `team-work-user-s`;

CREATE TABLE IF NOT EXISTS `addresses`
(
    `id`       bigint auto_increment not null,
    `street`   varchar(150)          not null,
    `city`     varchar(100)          not null,
    `country`  varchar(100)          not null,
    `zip_code` varchar(20)           not null,
    `district` varchar(100)          not null,
    `user_id`  binary(16)            null,
    constraint `pk_addresses` primary key (`id`)
);

CREATE TABLE IF NOT EXISTS `contacts`
(
    `id`                   bigint auto_increment not null,
    `country_calling_code` varchar(3)            not null,
    `phone_number`         varchar(14)           not null,
    `email`                varchar(50)           not null,
    `user_id`              binary(16)            not null,
    constraint `pk_contacts` primary key (`id`)
);

CREATE TABLE IF NOT EXISTS `roles`
(
    `role_id`  int auto_increment not null,
    `app_role` varchar(20)        not null,
    constraint `pk_roles` primary key (`role_id`)
);

CREATE TABLE IF NOT EXISTS `users`
(
    `user_id`       binary(16)   not null,
    `first_name`    varchar(20)  not null,
    `last_name`     varchar(20)  not null,
    `username`      varchar(20)  not null,
    `password`      varchar(255) null,
    `image_key`     varchar(50)  not null default 'defaultProfileImage',
    `deleted`       boolean      not null default 0,
    `token_version` int          not null default 0,
    `role_id`       int          not null,
    `created_by`    varchar(20)  not null,
    `updated_by`    varchar(20)  null,
    `created_at`    timestamp    not null default current_timestamp,
    `updated_at`    timestamp    not null default current_timestamp on update current_timestamp,
    constraint `pk_users` primary key (`user_id`)
);

alter table addresses
    add constraint `fk_addresses_user`
        foreign key (`user_id`)
            references `users` (`user_id`)
            on delete cascade;

alter table contacts
    add constraint `fk_contacts_user`
        foreign key (`user_id`)
            references `users` (`user_id`)
            on delete cascade;

alter table contacts
    add constraint `uq_contacts_user` unique (`user_id`);

alter table roles
    add constraint `uq_role` unique (`app_role`);

alter table users
    add constraint `fk_role` foreign key (`role_id`) references `roles` (`role_id`);

alter table users
    add constraint `uq_user_username` unique (`username`);

create unique index `idx_user_username` on `users` (`username`);