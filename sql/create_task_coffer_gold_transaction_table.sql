CREATE TABLE coffers.task_coffer_gold_transaction(
     id bigserial primary key,
     task_id bigint not null,
     transaction_id bigint not null,

     constraint transaction_task_fk foreign key (transaction_id) references coffers.transaction(id)
);