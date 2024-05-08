CREATE TABLE pedido_item_pedido (
 pedido_id BIGINT NOT NULL,
 item_pedido_id BIGINT NOT NULL,
 PRIMARY KEY (pedido_id, item_pedido_id)
);

alter table pedido_item_pedido add constraint fk_pedido_item_pedido_pedido
 FOREIGN KEY (pedido_id) REFERENCES pedido(id);

alter table pedido_item_pedido add constraint fk_pedido_item_pedido_item
 FOREIGN KEY (item_pedido_id) REFERENCES item_pedido(id);

alter table item_pedido drop constraint fk_item_pedido_pedido;

alter table item_pedido drop column pedido_id;