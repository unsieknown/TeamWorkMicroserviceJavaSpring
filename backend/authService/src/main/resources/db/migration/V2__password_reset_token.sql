use `team-work-auth-s`;

create table if not exists `password_reset_tokens`
(
    `id`          bigint     not null auto_increment,
    `used`        boolean    not null default 0,
    `token`       binary(16) not null,
    `expiry_date` timestamp  not null,
    `user_id`     binary(16) not null,
    constraint `pk_prt_id` primary key (`id`),
    constraint `uq_token` unique (`token`)
);