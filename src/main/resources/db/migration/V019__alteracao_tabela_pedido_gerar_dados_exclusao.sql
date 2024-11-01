alter table pedido add column
    excluido_por bigint;

alter table pedido add column
    excluido_as timestamp;

alter table pedido add constraint fk_usuario_excluido_por
    foreign key (excluido_por) references usuario (id);

