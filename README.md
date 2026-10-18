# Sistema de Arquivos

Sistema em Java (console e Swing) que cadastra arquivos, mantém a lista
ordenada por nome e formato e guarda os dados em um banco MySQL.

## Funcionalidades
- Inserir arquivos em ordem, sem duplicar (nome + formato)
- Remover, buscar e exibir por nome/formato
- Exibir todos os arquivos de um formato
- Tamanhos guardados em bytes e mostrados em B/KB/MB/GB
- Importar arquivos reais do computador (nome, formato e tamanho) pelo seletor de arquivos

## Tecnologias
Java, NetBeans (Ant), Swing, MySQL 8, JDBC (Connector/J)

## Como rodar
1. Instale o JDK e o MySQL
2. Crie o banco e a tabela rodando o `schema.sql` no MySQL Workbench
3. Adicione o Connector/J (.jar) em Libraries
4. Coloque sua senha em `SENHA` no `RepositorioMySQL`
5. Rode a classe `Principal`

## Estrutura
- `Arquivo`: nome, formato e tamanho
- `SistemaArquivo`: lista ordenada e regras
- `RepositorioMySQL`: acesso ao banco
- `Principal` e `InterfaceArquivos`: menu e tela

## Decisões e aprendizados
- Usei ArrayList no lugar de lista encadeada própria
- A chave do arquivo é nome + formato
- Projeto baseado em um exercício da faculdade, adaptado por mim

## Próximos passos
- Relatório por formato com SQL (`GROUP BY`)
- Guardar também o caminho do arquivo no banco


