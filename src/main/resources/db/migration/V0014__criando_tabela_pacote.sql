CREATE TABLE pacote (
 id BIGINT not null AUTO_INCREMENT,
 nome varchar(80) not null,
 descricao varchar(255),
 cliente_id BIGINT not null,
 data_compra TIMESTAMP not null,
 validade BIGINT not null,
 data_vencimento TIMESTAMP,
 primary key(id)
)engine=InnoDB default charset=utf8;


CREATE TABLE item_pacote (
 id BIGINT not null AUTO_INCREMENT,
 item_pedido_id BIGINT not null,
 profissional_id BIGINT,
 data_consumo TIMESTAMP,

 primary key(id)
)engine=InnoDB default charset=utf8;


CREATE TABLE pacote_item_ativo (
 pacote_id BIGINT,
 item_id BIGINT,

 PRIMARY KEY (pacote_id, item_id)
)engine=InnoDB default charset=utf8;


CREATE TABLE pacote_item_consumido (
 pacote_id BIGINT,
 item_id BIGINT,

 PRIMARY KEY (pacote_id, item_id)
)engine=InnoDB default charset=utf8;

CREATE TABLE pacote_pronto (
 id BIGINT not null AUTO_INCREMENT,
 validade BIGINT not null,
 nome varchar(80) not null,
 descricao varchar(255),
 ativo boolean,

 primary key(id)
)engine=InnoDB default charset=utf8;


CREATE TABLE pacote_pronto_item (
 pacote_pronto_id BIGINT,
 item_id BIGINT,

 PRIMARY KEY (pacote_pronto_id, item_id)
)engine=InnoDB default charset=utf8;

alter table pacote add constraint fk_pacote_cliente
    foreign key (cliente_id) references cliente(id);

alter table pacote_item_ativo add constraint fk_pacote_item_ativo_pacote
    FOREIGN KEY (pacote_id) REFERENCES pacote(id);

alter table pacote_item_ativo add constraint fk_pacote_item_ativo_item
    FOREIGN KEY (item_id) REFERENCES item_pacote(id);

alter table pacote_item_consumido add constraint fk_pacote_item_consumido_pacote
    FOREIGN KEY (pacote_id) REFERENCES pacote(id);

alter table pacote_item_consumido add constraint fk_pacote_item_consumido_item
    FOREIGN KEY (item_id) REFERENCES item_pacote(id);

alter table pacote_pronto_item add constraint fk_pacote_pronto_item_pacote
    FOREIGN KEY (pacote_pronto_id) REFERENCES pacote_pronto(id);

alter table pacote_pronto_item add constraint fk_pacote_pronto_item_item
    FOREIGN KEY (item_id) REFERENCES item_pacote(id);

ALTER TABLE item_pacote ADD CONSTRAINT fk_item_pacote_item_pedido
    FOREIGN KEY (item_pedido_id) REFERENCES item_pedido(id);

ALTER TABLE item_pacote ADD CONSTRAINT fk_item_pacote_profissional
    FOREIGN KEY (profissional_id) REFERENCES profissional(id);