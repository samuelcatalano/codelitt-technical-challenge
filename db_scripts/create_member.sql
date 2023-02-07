create table member
(
    id                bigserial
        primary key,
    created_at        timestamp(6),
    contract_duration integer,
    country           varchar(255),
    currency          varchar(255),
    first_name        varchar(255),
    last_name         varchar(255),
    role              varchar(255),
    salary            numeric(38, 2),
    type              varchar(255)
);

alter table member
    owner to postgres;