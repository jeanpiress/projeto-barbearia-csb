alter table pedido add column pontuacao_cliente_gerada decimal(10, 2);
alter table pedido rename column pontuacao_gerada to pontuacao_profissional_gerada;



