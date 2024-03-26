use projeto_barbearia;
create table categoria (
	id bigint not null auto_increment,
	nome varchar(80) not null,

	primary key (id)
) engine=InnoDB default charset=utf8;

create table cliente (
	id bigint not null auto_increment,
	nome varchar(60) not null,
    celular varchar(11) not null,
    data_nascimento TIMESTAMP,
    ultima_visita TIMESTAMP,
    pontos bigint,
    previsao_retorno TIMESTAMP,
    observacao varchar(255),
    endereco_bairro varchar(60),
    endereco_cep varchar(9),
    endereco_complemento varchar(60),
    endereco_logradouro varchar(100),
    endereco_numero varchar(10),


	primary key (id)
) engine=InnoDB default charset=utf8;

create table comissao (
	id bigint not null auto_increment,
	profissional_id bigint not null,
	produto_id bigint not null,
	porcentagem_comissao decimal not null,


	primary key (id)
) engine=InnoDB default charset=utf8;

create table item_pedido (
	id bigint not null auto_increment,
	preco_unitario decimal not null,
    preco_total decimal not null,
    quantidade bigint not null,
    observacao varchar(255),
    pedido_id bigint not null,
    produto_id bigint not null,

	primary key (id)
) engine=InnoDB default charset=utf8;

create table pedido (
	id bigint not null auto_increment,
	horario TIMESTAMP,
    status_pagamento varchar(10) not null,
    forma_pagamento varchar(10) not null,
    status_pedido varchar(10) not null,
    cliente_id bigint not null,
    profissional_id bigint not null,



	primary key (id)
) engine=InnoDB default charset=utf8;

create table produto (
	id bigint not null auto_increment,
	nome varchar(50) not null,
    preco decimal not null,
    ativo boolean,
    tem_estoque boolean,
    estoque bigint,
    vendido_por_ponto boolean,
    peso_pontuacao_cliente decimal,
    peso_pontuacao_profissional decimal,
    preco_em_pontos decimal,
    comissao_base decimal,
    categoria_id bigint not null,


	primary key (id)
) engine=InnoDB default charset=utf8;


create table profissional (
	id bigint not null auto_increment,
	nome varchar(60) not null,
    nome_exibicao varchar(15) not null,
    celular varchar(11) not null,
    cpf varchar(11),
    data_nascimento TIMESTAMP not null,
    salario_fixo decimal,
    dia_pagamento bigint,
    ativo boolean,
    endereco_bairro varchar(60),
    endereco_cep varchar(9),
    endereco_complemento varchar(60),
    endereco_logradouro varchar(100),
    endereco_numero varchar(10),


	primary key (id)
) engine=InnoDB default charset=utf8;


alter table produto add constraint fk_produto_categoria
foreign key (categoria_id) references categoria (id);

alter table item_pedido add constraint fk_item_pedido_pedido
foreign key (pedido_id) references pedido (id);

alter table item_pedido add constraint fk_item_pedido_produto
foreign key (produto_id) references produto (id);

alter table pedido add constraint fk_pedido_cliente
foreign key (cliente_id) references cliente (id);

alter table pedido add constraint fk_pedido_profissional
foreign key (profissional_id) references profissional (id);

alter table comissao add constraint comissao_produto
FOREIGN KEY (produto_id)  REFERENCES produto(id);

alter table comissao add constraint comissao_profissional
FOREIGN KEY (profissional_id)  REFERENCES profissional(id);

