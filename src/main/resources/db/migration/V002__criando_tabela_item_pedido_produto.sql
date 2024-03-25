CREATE TABLE item_pedido_produto (
    item_pedido_id BIGINT,
    produto_id BIGINT,
    PRIMARY KEY (item_pedido_id, produto_id),
    FOREIGN KEY (item_pedido_id) REFERENCES item_pedido(id),
    FOREIGN KEY (produto_id) REFERENCES produto(id));