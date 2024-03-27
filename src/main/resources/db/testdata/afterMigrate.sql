set foreign_key_checks = 0;
SET sql_safe_updates = 0;

delete from categoria;
delete from cliente;
delete from comissao;
delete from item_pedido;
delete from pedido;
delete from produto;
delete from profissional;

set foreign_key_checks = 1;
SET sql_safe_updates = 1;

alter table categoria auto_increment = 1;
alter table cliente auto_increment = 1;
alter table comissao auto_increment = 1;
alter table item_pedido auto_increment = 1;
alter table pedido auto_increment = 1;
alter table produto auto_increment = 1;
alter table profissional auto_increment = 1;


insert into categoria (id, nome) values (1, 'cabelo');
insert into categoria (id, nome) values (2, 'barba');
insert into categoria (id, nome) values (3, 'produto');

insert into cliente (id, nome, celular, data_nascimento, ultima_visita, pontos, previsao_retorno, observacao, endereco_bairro, endereco_cep, endereco_complemento, endereco_logradouro, endereco_numero) values (1, 'jean', '34999708382', utc_timestamp, utc_timestamp, 100, utc_timestamp, 'sem obs','morumbi', '38407381', 'casa', 'rua grupiara', '313');
insert into cliente (id, nome, celular, data_nascimento, ultima_visita, pontos, previsao_retorno, observacao) values (2, 'Kirk', '34999708385', utc_timestamp, utc_timestamp, 50, utc_timestamp, 'sem obs');

insert into produto (id, nome, preco, ativo, tem_estoque, estoque, vendido_por_ponto, peso_pontuacao_cliente, peso_pontuacao_profissional, preco_em_pontos, comissao_base, categoria_id) values (1, 'corte', 45.00, true, false, 0, false, 1, 1, 0, 50.00, 1);

insert into produto (id, nome, preco, ativo, tem_estoque, estoque, vendido_por_ponto, peso_pontuacao_cliente, peso_pontuacao_profissional, preco_em_pontos, comissao_base, categoria_id) values (2, 'barba', 45.00, true, false, 0, false, 1, 1, 0, 50.00, 1);

insert into profissional (id, nome, nome_exibicao, celular, cpf, data_nascimento, salario_fixo, dia_pagamento, ativo) values (1, 'Jean Carlo', 'jean', '34999708382', '10158594614', utc_timestamp, 0, 5, true);

insert into comissao (id, profissional_id, produto_id, porcentagem_comissao) values (1, 1, 1, 50.00);

insert into comissao (id, profissional_id, produto_id, porcentagem_comissao) values (2, 1, 2, 50.00);

insert into pedido (id, horario, status_pagamento, forma_pagamento, status_pedido, cliente_id, profissional_id, comissao_gerada) values(1, utc_timestamp, 1, 1, 1, 1, 1, 0);

insert into item_pedido (id, preco_unitario, preco_total, quantidade, observacao, pedido_id, produto_id) values (1, 45.00, 45.00, 1, null, null, 1);

insert into item_pedido (id, preco_unitario, preco_total, quantidade, observacao, pedido_id, produto_id) values (2, 45.00, 45.00, 1, null, null, 2);

