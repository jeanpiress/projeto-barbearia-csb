create table foto_usuario (
    usuario_id bigint not null,
    nome_arquivo varchar(150) not null,
    descricao varchar(150),
    content_type varchar(80) not null,
    tamanho int not null,

    primary key (usuario_id),
    constraint fk_foto_usuario_usuario foreign key (usuario_id) references usuario (id)
) engine=InnoDB default charset=utf8;