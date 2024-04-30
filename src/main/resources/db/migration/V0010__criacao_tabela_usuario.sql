create table usuario (
	id bigint not null auto_increment,
	email varchar(80) not null,
    senha varchar(80) not null,
    cliente_id bigint,
    profissional_id bigint,


	primary key (id)
) engine=InnoDB default charset=utf8;


alter table usuario add constraint fk_usuario_cliente
foreign key (cliente_id) references cliente (id);

alter table usuario add constraint fk_usuario_profissional
foreign key (profissional_id) references profissional (id);

