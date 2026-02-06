# Trabalho final da disciplina de Sistemas Distribuidos

Sistema de login e cadastro com Java RMI

Criar um serviço distribuído de autenticação de usuários utilizando Java RMI (Remote Method Invocation).

• Objetivo: aplicar conceitos de comunicação distribuída e serviços de nomes.

• Atividades: implementar servidor RMI para cadastro e login, clientes que acessam remotamente os métodos, e persistência simples em banco de dados ou arquivos.

• Ferramentas: Java RMI, JDBC para persistência.

## Recursos utilizados
• Java RMI

• Maven Standard Directory Layout

• Banco de dados PostgreSQL.

## Como Executar
Siga exatamente estes passos no terminal (na pasta do projeto):

Gerar o JAR:

Bash
mvn clean package
(Verifique se apareceu a pasta target com o arquivo .jar dentro).

Subir o Docker:

Bash
docker-compose up --build
O que você verá no console:
O Postgres inicializando e criando a tabela.

O Servidor RMI iniciando e mostrando >>> Servidor RMI pronto....

O Cliente aguardando 5 segundos (sleep 5 no compose para dar tempo ao servidor iniciar) e depois conectando, cadastrando e logando com sucesso.


## Para limpar o banco antigo, em caso de alterações
Derruba tudo e apaga os volumes (apaga o banco velho com a tabela errada)
docker-compose down -v

Recompila o Java
mvn clean package

Reconstrói as imagens e sobe
docker-compose up --build