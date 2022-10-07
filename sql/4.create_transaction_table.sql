CREATE TABLE coffers.transaction (
    id bigserial primary key,
    date timestamp not null ,
    clan_id bigint not null,
    action varchar not null ,
    gold_before integer not null ,
    gold_change integer not null ,
    status varchar not null ,
    error_description varchar,

    constraint clan_fk foreign key (clan_id) references coffers.clan(id)
);