create database sistema_arquivos;
use sistema_arquivos;
create table arquivo(
nome varchar(255) not null,
formato varchar(50) not null,
tamanho bigint not null,
primary key(nome,formato))engine InnoDB;

select * from arquivo;