# Project Manager - Gerenciador de Projetos

## Visão Geral

O Gerenciador de Projetos é um sistema  de gerenciamento de projetos desenvolvido com Spring Boot para o backend e React/Next.js para o frontend. Ele fornece uma plataforma para criar e gerenciar projetos, tarefas e colaboração em equipe.

Desenvolvido como projeto para o bloco "Engenharia de Softwares Escaláveis" da gradução em Engenharia da Computação - Instituto Infnet.

## Funcionalidades

- Autenticação e autorização de usuários
- Criação e gerenciamento de projetos
- Atribuição e acompanhamento de tarefas
- Gerenciamento de membros da equipe
- Funcionalidade de chat em tempo real
- Criação de notas para projetos e tarefas
- Acompanhamento de progresso e geração de relatórios
- Gerenciamento de perfil de usuário
- Design responsivo para uso em desktop e dispositivos móveis

## Stack Tecnológica

### Backend
- Java 21
- Spring Boot 3.2.6
- Spring Security
- Spring Data JPA
- MySQL
- JWT para autenticação

### Frontend
- React 18
- Next.js 14
- TypeScript
- Tailwind CSS
- Componentes UI Shadcn

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

## Estrutura do Projeto

### Backend
- `src/main/java/com/klug/projectmanager/`
  - `config/`: Classes de configuração
  - `controller/`: Controladores da API REST
  - `dto/`: Objetos de Transferência de Dados
  - `entity/`: Entidades JPA
  - `exception/`: Exceções personalizadas e manipuladores
  - `repository/`: Repositórios JPA
  - `security/`: Configurações de segurança e utilitários JWT
  - `service/`: Serviços de lógica de negócios

### Frontend
- `src/`
  - `app/`: Páginas e roteamento do Next.js
  - `components/`: Componentes React
  - `hooks/`: Hooks React personalizados
  - `lib/`: Funções utilitárias
  - `services/`: Funções de serviço da API

