alter table produto add column pacote_pronto_id bigint;
alter table produto add constraint fk_produto_pacote_pronto
 foreign key (pacote_pronto_id) references pacote_pronto(id);

alter table pacote_pronto add column comissao_base decimal(10, 2) not null;
alter table pacote_pronto add column peso_pontuacao_cliente decimal(10, 2);
alter table pacote_pronto add column peso_pontuacao_profissional decimal(10, 2);
alter table pacote_pronto add column categoria_id bigint not null;

alter table pacote_pronto add constraint fk_pacote_pronto_categoria
 foreign key (categoria_id) references categoria(id);

alter table cliente modify column pontos decimal(10, 2);

