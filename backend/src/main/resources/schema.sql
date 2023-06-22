create table if not exists users
(
    id       bigserial primary key,
    name     varchar(255) not null,
    username varchar(255) not null unique,
    password varchar(255) not null,
    role     varchar(255) not null
);

create table if not exists tasks
(
    id              bigserial primary key,
    title           varchar(255),
    description     varchar(255),
    expiration_date timestamp,
    assigned_by_id  bigint not null,
    constraint fk_tasks_users foreign key (assigned_by_id) references users (id) on delete cascade on update no action
);

create table if not exists user_tasks
(
    id          bigserial primary key,
    user_id     bigint       not null,
    task_id     bigint       not null,
    task_status varchar(255) not null,
    constraint fk_user_tasks_users foreign key (user_id) references users (id) on delete cascade on update no action,
    constraint fk_user_tasks_tasks foreign key (task_id) references tasks (id) on delete cascade on update no action
);

create table if not exists comments
(
    id           bigserial primary key,
    posting_date timestamp not null,
    text         text,
    user_task_id bigint    not null,
    constraint fk_comments_user_tasks foreign key (user_task_id) references user_tasks (id) on delete cascade on update no action
);

create table if not exists user_solutions
(
    id           bigserial primary key,
    posting_date timestamp not null,
    text         text,
    user_task_id bigint    not null,
    constraint fk_user_solutions_user_tasks foreign key (user_task_id) references user_tasks (id) on delete cascade on update no action
);

create table if not exists tests
(
    id      bigserial primary key,
    input   varchar(255) not null,
    output  varchar(255) not null,
    task_id bigint       not null,
    constraint fk_test_cases_tasks foreign key (task_id) references tasks (id) on delete cascade on update no action
);

create table if not exists task_properties
(
    id            bigserial primary key,
    code_template text   not null,
    task_id       bigint not null,
    constraint fk_task_props_tasks foreign key (task_id) references tasks (id) on delete cascade on update no action
);

create table if not exists verification_results
(
    id bigserial primary key,
    received varchar(255),
    expected varchar(255),
    result boolean,
    test_id bigint,
    user_id bigint,
    task_id bigint,
    constraint fk_verif_results_tests foreign key (test_id) references tests (id) on delete cascade on update no action,
    constraint fk_verif_results_users foreign key (user_id) references users (id) on delete cascade on update no action,
    constraint fk_verif_results_tasks foreign key (task_id) references tasks (id) on delete cascade on update no action
);