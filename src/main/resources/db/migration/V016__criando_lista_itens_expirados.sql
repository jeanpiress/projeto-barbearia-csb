CREATE TABLE pacote_item_expirados (
 pacote_id BIGINT,
 item_id BIGINT,
 PRIMARY KEY (pacote_id, item_id)
);

alter table pacote_item_expirados add constraint fk_pacote_item_expirados_pacote
 FOREIGN KEY (pacote_id) REFERENCES pacote(id);

alter table pacote_item_expirados add constraint fk_pacote_item_expirados_item
 FOREIGN KEY (item_id) REFERENCES item_pedido(id);