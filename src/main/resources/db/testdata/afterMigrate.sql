set foreign_key_checks = 0;
SET sql_safe_updates = 0;

delete from categoria;
delete from cliente;
delete from comissao;
delete from produto;
delete from profissional;
delete from usuario;
delete from permissao;
delete from usuario_permissao;
delete from pacote;
delete from pacote_item_ativo;
delete from pacote_item_consumido;
delete from item_pacote;
delete from pacote_pronto;
delete from pacote_pronto_item;
delete from item_pacote;
delete from pacote_item_ativo;
delete from pacote_item_consumido;
delete from pedido_item_pedido;
delete from pacote_item_expirados;


set foreign_key_checks = 1;
SET sql_safe_updates = 1;

alter table categoria auto_increment = 1;
alter table cliente auto_increment = 1;
alter table comissao auto_increment = 1;
alter table item_pedido auto_increment = 1;
alter table pedido auto_increment = 1;
alter table produto auto_increment = 1;
alter table profissional auto_increment = 1;
alter table permissao auto_increment = 1;
alter table pacote auto_increment = 1;
alter table item_pacote auto_increment =1;
alter table pacote_pronto auto_increment=1;
alter table item_pacote auto_increment=1;
alter table usuario auto_increment=1;



insert into categoria (id, nome) values (1, 'cabelo');

insert into categoria (id, nome) values (2, 'barba');

insert into categoria (id, nome) values (3, 'produto');


insert into profissional (id, nome, nome_exibicao, celular, cpf, data_nascimento, salario_fixo, dia_pagamento, ativo,
                          endereco_bairro, endereco_cep, endereco_complemento, endereco_logradouro, endereco_numero,
                          endereco_cidade, endereco_estado)
values (1, 'Jean Carlo', 'jean', '34999708382', '10158594614', utc_timestamp, 0, 5, true, 'Santa Monica', '38405234',
        'ape 202', 'rua Jose Lelis França', '1275', 'udia', 'MG');

insert into profissional (id, nome, nome_exibicao, celular, cpf, data_nascimento, salario_fixo, dia_pagamento, ativo,
                          endereco_bairro, endereco_cep, endereco_complemento, endereco_logradouro, endereco_numero,
                          endereco_cidade, endereco_estado)
values (2, 'Mc Victor', 'Mono', '34924242424', '10158594614', utc_timestamp, 0, 5, true, 'morumbi', '38407381',
        'casa', 'rua grupiara', '313', 'udia', 'MG');


insert into cliente (id, nome, celular, data_nascimento, ultima_visita, pontos, previsao_retorno, observacao, dias_retorno,
                     ultimo_profissional_id, ativo, endereco_bairro, endereco_cep, endereco_complemento, endereco_logradouro,
                     endereco_numero, endereco_cidade, endereco_estado)
values (1, 'jean', '34999708382', utc_timestamp, utc_timestamp, 100.00, timestamp('2024-04-12 14:30:45.12'), 'sem obs' ,
        30, 1, true, 'morumbi', '38407381', 'casa', 'rua grupiara', '313', 'udia', 'MG');

insert into cliente (id, nome, celular, data_nascimento, ultima_visita, pontos, previsao_retorno, observacao, dias_retorno,
                     ultimo_profissional_id, ativo)
values (2, 'Kirk', '34999708385', utc_timestamp, utc_timestamp, 50.00, timestamp('2024-04-19 14:30:45.12'), 'sem obs', 30, 1, true);

insert into cliente (id, nome, celular, data_nascimento, ultima_visita, pontos, previsao_retorno, observacao, dias_retorno,
                     ultimo_profissional_id, ativo)
values (3, 'Carol', '34999708385', utc_timestamp, utc_timestamp, 50.00, timestamp('2024-04-17 14:30:45.12'), 'sem obs', 30, 2, true);

insert into cliente (id, nome, celular, data_nascimento, ultima_visita, pontos, previsao_retorno, observacao, dias_retorno,
                     ultimo_profissional_id, ativo) values (4, 'Carolina', '34999708385', utc_timestamp, utc_timestamp, 50.00, timestamp('2024-04-17 14:30:45.12'), 'sem obs', 30, 2, true);
insert into cliente (id, nome, celular, data_nascimento, ultima_visita, pontos, previsao_retorno, observacao, dias_retorno, ultimo_profissional_id, ativo) values (5, 'Calorina', '34999708385', utc_timestamp, utc_timestamp, 50.00, timestamp('2024-04-17 14:30:45.12'), 'sem obs', 30, 2, true);


insert into produto (id, nome, preco, ativo, tem_estoque, estoque, vendido_por_ponto, peso_pontuacao_cliente,
                     peso_pontuacao_profissional, preco_em_pontos, comissao_base, categoria_id)
values (1, 'corte', 45.00, true, false, 0, false, 1, 1, 0, 50.00, 1);


insert into produto (id, nome, preco, ativo, tem_estoque, estoque, vendido_por_ponto, peso_pontuacao_cliente,
                     peso_pontuacao_profissional, preco_em_pontos, comissao_base, categoria_id)
values (2, 'barba', 45.00, true, false, 0, false, 1, 1, 0, 50.00, 1);

insert into produto (id, nome, preco, ativo, tem_estoque, estoque, vendido_por_ponto, peso_pontuacao_cliente,
                     peso_pontuacao_profissional, preco_em_pontos, comissao_base, categoria_id)
values (3, 'Pomada Baboom', 90.00, true, false, 0, false, 1, 10, 0, 10.00, 3);


insert into comissao (id, profissional_id, produto_id, porcentagem_comissao) values (1, 1, 1, 50.00);

insert into comissao (id, profissional_id, produto_id, porcentagem_comissao) values (2, 1, 2, 50.00);

insert into comissao (id, profissional_id, produto_id, porcentagem_comissao) values (3, 2, 1, 50.00);

insert into comissao (id, profissional_id, produto_id, porcentagem_comissao) values (4, 2, 2, 50.00);


insert into usuario(id, email, senha, cliente_id, profissional_id, permissao, nome) values (1, "jean.m.pires@gmail.com",
                    "$2a$12$yXbDuQ0PtltBXPiP8wnhrOYfY647RoyNOOcD70kBnSzwgESEISFE.", null, 1, "GERENTE", "Jean");


insert into permissao(id, nome, descricao) values(1, "Gerente", "Acesso total");

insert into permissao(id, nome, descricao) values(2, "Recepcao", "Acesso ilimitado a agenda e comissoes e insercoes ao caixa");

insert into permissao(id, nome, descricao) values(3, "Profissional", "Acesso ilimitado a agenda e comissoes");

insert into permissao(id, nome, descricao) values(4, "Cliente", "Acesso limitado a agenda");


insert into usuario_permissao(usuario_id, permissao_id) values (1, 1);

insert into usuario_permissao(usuario_id, permissao_id) values (1, 2);

insert into usuario_permissao(usuario_id, permissao_id) values (1, 3);

insert into usuario_permissao(usuario_id, permissao_id) values (1, 4);


insert into pacote(id, nome, descricao, cliente_id, data_compra, validade, data_vencimento) values (1, '4 barbas', null, 1, timestamp('2024-04-19 14:30:45.12'), 31, timestamp('2024-05-19 23:59:59.00'));

insert into pacote(id, nome, descricao, cliente_id, data_compra, validade, data_vencimento) values (2, '3 cortes', null, 2, timestamp('2024-04-19 14:30:45.12'), 31, timestamp('2024-05-19 23:59:59.00'));

