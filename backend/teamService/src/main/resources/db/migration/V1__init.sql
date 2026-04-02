use `team-work-team-s`;

CREATE TABLE IF NOT EXISTS `teams`
(
    `team_id`           binary(16)  not null,
    `team_name`         varchar(40) not null,
    `presentation_name` varchar(40) not null,
    `manager_id`        binary(16)  null,
    `active`            boolean     not null default 1,
    `created_by`        varchar(20) not null,
    `updated_by`        varchar(20) null,
    `created_at`        timestamp   not null default current_timestamp,
    `updated_at`        timestamp   not null default current_timestamp on update current_timestamp,
    constraint `pk_teams` primary key (`team_id`)
);

CREATE TABLE IF NOT EXISTS `teams_users`
(
    `team_id` binary(16) not null,
    `user_id` binary(16) not null,
    constraint `pk_team_users` primary key (`team_id`, `user_id`)
);

alter table teams
    add constraint `uq_team_name` unique (`team_name`);

create index `idx_teams_manager` on `teams` (`manager_id`);

alter table teams_users
    add constraint `fk_team_users_team` foreign key (`team_id`) references `teams` (`team_id`);

create index `idx_team_users_user` on `teams_users` (`user_id`);