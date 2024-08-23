# Project Manager

## Visão Geral

O Project Manager é uma aplicação web desenvolvida como um projeto para a Graduação em Engenharia da Computação, no bloco de Engenharia de Softwares Escaláveis. 
O objetivo do projeto é facilitar a gestão de projetos, tarefas e equipes, oferecendo uma interface intuitiva e funcionalidades voltadas para a organização e monitoramento de atividades.

## Funcionalidades

- **Gestão de Projetos**: Criação, atualização e acompanhamento do progresso de diversos projetos.
- **Gestão de Tarefas**: Atribuição de tarefas aos membros da equipe, definição de prazos e monitoramento da conclusão.
- **Colaboração em Equipe**: Gerenciamento de papéis e permissões dos membros da equipe.
- **Autenticação**: Login seguro e controle de acesso.
- **Design Responsivo**: Acessível tanto em dispositivos desktop quanto móveis.

## Stack Tecnológico

- **Frontend**: Desenvolvido em React com TypeScript, utilizando componentes modernos de UI.
- **Backend**: Implementado em Spring Boot, conectado a um banco de dados MySQL.
- **Banco de Dados**: MySQL para armazenamento persistente de dados.
- **Controle de Versão**: Git

## Instalação

### Pré-requisitos

- Java JDK 21 ou superior
- Maven
- Node.js (v14 ou superior) e npm ou yarn
- MySQL

### Configuração do Backend

1. Navegue até o diretório do backend:
    ```bash
    cd backend
    ```

2. Configure o banco de dados MySQL:
    - Crie um banco de dados no MySQL.
    - Atualize o arquivo `application.properties` com as credenciais do banco de dados:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/seu_banco_de_dados
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    ```

3. Compile e execute o backend:
    ```bash
    mvn spring-boot:run
    ```

### Configuração do Frontend

1. Navegue até o diretório do frontend:
    ```bash
    cd frontend/project-manager-frontend
    ```

2. Instale as dependências:
    ```bash
    npm install
    ```

3. Inicie o servidor frontend:
    ```bash
    npm start
    ```

### Executando a Aplicação

Após configurar o backend e o frontend, abra o navegador e acesse `http://localhost:3000` para utilizar a aplicação.


