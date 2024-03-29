alter table item_pedido drop column observacao;
alter table produto modify comissao_base decimal not null;
alter table profissional modify data_nascimento TIMESTAMP null;