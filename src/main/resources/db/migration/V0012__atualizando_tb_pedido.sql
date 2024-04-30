alter table pedido add column
    criado_por bigint;

alter table pedido add column
    alterado_por bigint;

alter table pedido add column
    recebido_por bigint;

alter table pedido add column
    cancelado_por bigint;

alter table pedido add column
    criado_as timestamp;

alter table pedido add column
    modificado_as timestamp;

alter table pedido add column
    cancelado_as timestamp;


alter table pedido add constraint fk_usuario_criado_por
    foreign key (criado_por) references usuario (id);

alter table pedido add constraint fk_usuario_alterado_por
    foreign key (alterado_por) references usuario (id);

alter table pedido add constraint fk_usuario_recebido_por
    foreign key (recebido_por) references usuario (id);

alter table pedido add constraint fk_usuario_cancelado_por
    foreign key (cancelado_por) references usuario (id);

