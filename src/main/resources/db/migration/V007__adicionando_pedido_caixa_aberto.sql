-- Adiciona uma nova coluna na tabela cliente
ALTER TABLE pedido
    ADD COLUMN caixa_aberto BOOLEAN,
    ADD COLUMN valor_total DECIMAL(10, 2);

