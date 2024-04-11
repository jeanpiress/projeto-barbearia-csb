-- Adiciona uma nova coluna na tabela cliente
ALTER TABLE cliente
    ADD COLUMN ultimo_profissional_id BIGINT;

-- Adiciona a restrição de chave estrangeira
ALTER TABLE cliente
    ADD CONSTRAINT fk_ultimo_profissional
        FOREIGN KEY (ultimo_profissional_id)
            REFERENCES profissional (id);