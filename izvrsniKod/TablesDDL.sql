create table privileges
(
    privilege_id bigint not null,
    name         varchar(255),
    constraint privileges_pkey
        primary key (privilege_id),
    constraint ukm2tnonbcaquofx1ccy060ejyc
        unique (name)
);

create table refresh_token
(
    id           bigserial not null,
    created_date timestamp,
    token        varchar(255),
    constraint refresh_token_pkey
        primary key (id)
);

create table roles
(
    role_id bigint not null,
    name    varchar(255),
    constraint roles_pkey
        primary key (role_id),
    constraint ukofx66keruapi6vyqpv6f2or37
        unique (name)
);

create table users
(
    user_id             bigserial not null,
    cartographer_status integer,
    created_time        timestamp,
    current_location    bytea,
    elo_score           integer,
    email               varchar(255),
    enabled             boolean   not null,
    forced_timeout_end  timestamp,
    iban                varchar(255),
    id_card_photourl    varchar(255),
    losses              integer,
    online              boolean,
    password            varchar(255),
    photourl            varchar(255),
    username            varchar(255),
    wins                integer,
    role_id             bigint,
    constraint users_pkey
        primary key (user_id),
    constraint uk3r2pikgsxo64e847ik4b0gba1
        unique (username),
    constraint ukogjplnrmkbx3xm9jsjhis0y8
        unique (email),
    constraint fkn5fmjfij13xp7oxbqt7rwd4pa
        foreign key (role_id) references roles
);

create table fights
(
    fight_id       bigint not null,
    start_time     timestamp,
    winner_user_id bigint,
    constraint fights_pkey
        primary key (fight_id),
    constraint fk6df3rb1t54403mltmlyx0dxst
        foreign key (winner_user_id) references users
);

create table location_cards
(
    lcd_id              bigint  not null,
    accepted            boolean not null,
    created_at          timestamp,
    description         varchar(4000),
    difficulty          integer,
    enabled_date        timestamp,
    location            bytea,
    name                varchar(255),
    check_needed        boolean,
    photourl            varchar(255),
    population          integer,
    uncommonness        integer,
    accepted_by_user_id bigint,
    created_by_user_id  bigint,
    constraint location_cards_pkey
        primary key (lcd_id),
    constraint fkid99wm8cbjeee01a1b3jiwx4d
        foreign key (accepted_by_user_id) references users,
    constraint fkmi8m8rj2s6isa3mgt09teq2u
        foreign key (created_by_user_id) references users
);

create table roles_privileges
(
    roles_role_id           bigint not null,
    privileges_privilege_id bigint not null,
    constraint fknurvyd6i5hwqcwmmrisbaet60
        foreign key (privileges_privilege_id) references privileges,
    constraint fkk4xiqx2xk6kbimu02075ipqcm
        foreign key (roles_role_id) references roles
);

create table tokens
(
    id           bigserial not null,
    expiry_date  timestamp,
    token        varchar(255),
    user_user_id bigint,
    constraint tokens_pkey
        primary key (id),
    constraint fk9s245iaesqcmb4pxnlcme705a
        foreign key (user_user_id) references users
);

create table user_card
(
    lcd_id              bigint not null,
    user_id             bigint not null,
    cooldown_end        timestamp,
    cooldown_multiplier double precision,
    constraint user_card_pkey
        primary key (lcd_id, user_id),
    constraint fk3y4dg51e7chw2i2ptt1e888p3
        foreign key (lcd_id) references location_cards,
    constraint fkmeq57mkd0b73pmcyt2ru193f8
        foreign key (user_id) references users
);

create table user_card_fight
(
    fight_id bigint not null,
    lcd_id   bigint not null,
    user_id  bigint not null,
    constraint user_card_fight_pkey
        primary key (fight_id, lcd_id, user_id),
    constraint fk66fvhid7maw68t9vfchjobg1p
        foreign key (fight_id) references fights,
    constraint fkdueu11s4fxwfvm8pbya0kdrfs
        foreign key (lcd_id) references location_cards,
    constraint fkd2q378y09w577j4wbb5wmusv6
        foreign key (user_id) references users
);


