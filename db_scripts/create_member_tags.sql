create table member_tags
(
    member_id bigint not null
        constraint fkkac1mrsxhegfk3jci0arjt7a8
            references member,
    tags      varchar(255)
);

alter table member_tags
    owner to postgres;