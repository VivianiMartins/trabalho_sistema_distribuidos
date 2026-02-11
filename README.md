# Sistema de login e cadastro com Java RMI
**Trabalho final da disciplina de Sistemas Distribuídos**

Este projeto implementa um serviço distribuído de autenticação de usuários utilizando **Java RMI (Remote Method Invocation)**. A arquitetura separa a aplicação em microsserviços containerizados (Cliente Web, Servidor de Negócios e Banco de Dados).

### 🎯 Objetivos
* **Objetivo Geral:** Criar um serviço distribuído de autenticação.
* **Objetivo Específico:** Aplicar conceitos de comunicação distribuída e serviços de nomes.
* **Atividades:** Implementar servidor RMI para cadastro e login, clientes que acessam remotamente os métodos, e persistência em banco de dados.

### 🛠️ Recursos Utilizados
* **Linguagem:** Java 17+ (RMI)
* **Gerenciamento de Build:** Maven (Standard Directory Layout)
* **Banco de Dados:** PostgreSQL 15
* **Interface:** HTML5 + Bootstrap 5 (Responsivo)
* **Containerização:** Docker & Docker Compose

---

## 🚀 Como Executar o Projeto

Siga exatamente estes passos no terminal (na pasta raiz do projeto):

### 1. Gerar o Executável (Build)
Primeiro, compilamos o código Java e geramos o pacote `.jar`.

```bash
mvn clean package -DskipTests
```

> *Verifique se a pasta `target/` foi criada e se contém o arquivo `.jar`.*

### 2. Subir o Ambiente (Docker)
Este comando constrói as imagens e inicia os containers do Banco, Servidor e Cliente de forma orquestrada.

```bash
docker compose up --build
```

**O que você verá no console:**
* 🐘 **Postgres:** Inicializa e cria a tabela `usuarios` (script `init.sql`).
* ⚙️ **Servidor RMI:** Aguarda o banco iniciar e exibe: `>>> Servidor RMI pronto...`.
* 🌐 **Cliente Web:** Aguarda o servidor e exibe: `>>> Servidor Web (Http.cat Edition) rodando...`.

---

## 🌐 Testando como Sistema Distribuído

Para validar que o sistema suporta múltiplos acessos simultâneos e funciona em rede (concorrência), utilize os cenários abaixo:

### Cenário A: Múltiplos Navegadores (Local)
Simule usuários diferentes na mesma máquina:
1.  Abra o Chrome e acesse: `http://localhost:8080`
2.  Abra uma **Janela Anônima** (`Ctrl+Shift+N`) e acesse o mesmo link.
3.  Abra outro navegador (Firefox/Edge) e acesse o mesmo link.

> **Resultado:** Você pode logar com contas diferentes em cada janela sem interferência.

### Cenário B: Acesso via Celular (Wi-Fi) 📱
Este teste demonstra o acesso remoto real através da rede local (simulando a internet).

1.  **Descubra seu IP Local no computador:**
    * **Linux/Mac:** Digite `ip addr` no terminal (procure o IP da interface Wi-Fi, ex: `192.168.0.15`).
    * **Windows:** Digite `ipconfig` no CMD.
2.  **Libere a Porta (Se necessário no Linux):**
    ```bash
    sudo firewall-cmd --add-port=8080/tcp --temporary
    ```
3.  **Acesse pelo Celular:**
    * Conecte o celular no **mesmo Wi-Fi** do computador.
    * Abra o navegador do celular.
    * Digite: `http://SEU_IP_DO_COMPUTADOR:8080` (Ex: `http://192.168.0.15:8080`).

> **Dica:** Tente errar a senha propositalmente para ver as reações dos gatinhos HTTP (401 Unauthorized, 409 Conflict, etc)! 🐱

---

## 🕵️‍♂️ Monitorando o RMI em Tempo Real

Para ver a comunicação distribuída acontecendo (as requisições saindo do cliente e chegando no servidor), você pode acompanhar os logs do container:

1.  Mantenha o sistema rodando.
2.  Abra um **novo terminal**.
3.  Execute o comando abaixo:

```bash
docker compose logs -f server
---

## 🧹 Comandos Úteis (Manutenção)

### Parar o Sistema
Para desligar os containers sem perder os dados do banco:
```bash
docker compose down
```
### Limpar Tudo (Reset Total)
Use este comando se precisar recriar o banco de dados do zero (caso tenha alterado a estrutura da tabela ou init.sql). O flag -v remove os volumes persistentes.

```bash
docker compose down -v
```

### Reiniciar após Alterações no Código
Se você mudou algo no Java (.java) ou no banco, rode a sequência completa para recompilar e reconstruir:

```bash
# 1. Limpa o ambiente antigo
docker compose down -v

# 2. Recompila o código Java
mvn clean package -DskipTests

# 3. Reconstrói e sobe os containers
docker compose up --build
```

---

## 🗄️ Acesso ao Banco de Dados (IDE)
Para visualizar os dados cadastrados via IntelliJ (DataGrip) ou DBeaver:

* Host: localhost

* Port: 5432 (ou 5433 se você alterou no docker-compose)

* User: admin

* Password: senha123

* Database: sistema_auth (Selecione este banco especificamente na sua IDE)

## 📋 Estrutura do Projeto
* ```src/main/java```: Código fonte Java (Cliente, Servidor, Interfaces).

* ```src/scripts/init.sql```: Script SQL executado automaticamente na criação do banco.

* ```Dockerfile```: Configuração da imagem Java para os containers.

* ```docker-compose.yml ```: Orquestração dos 3 serviços (App, RMI, DB).