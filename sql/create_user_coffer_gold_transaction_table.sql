CREATE TABLE coffers.user_coffer_gold_transaction(
    id bigserial primary key,
    user_id bigint not null,
    transaction_id bigint not null,

    constraint transaction_user_fk foreign key (transaction_id) references coffers.transaction(id)
);