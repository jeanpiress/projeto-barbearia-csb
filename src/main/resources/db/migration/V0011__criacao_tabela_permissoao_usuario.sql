create table permissao(
    id bigint not null auto_increment,
    nome varchar(80) not null,
    descricao varchar(255),
    PRIMARY KEY (id)
)engine=InnoDB default charset=utf8;

CREATE TABLE usuario_permissao (
    usuario_id BIGINT NOT NULL,
    permissao_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, permissao_id)
)engine=InnoDB default charset=utf8;

alter table usuario_permissao add constraint fk_usuario_permissao_usuario
FOREIGN KEY (usuario_id) REFERENCES Usuario(id);

alter table usuario_permissao add constraint fk_usuario_permissao_permissao
FOREIGN KEY (permissao_id) REFERENCES Permissao(id);

