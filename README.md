# Project Manager - Gerenciador de Projetos

## Visão Geral

O Gerenciador de Projetos é um sistema de gerenciamento de projetos desenvolvido com Spring Boot para o backend e React/Next.js para o frontend. Ele fornece uma plataforma para criar e gerenciar projetos, tarefas e colaboração em equipe, utilizando uma arquitetura orientada a eventos para melhor escalabilidade e resiliência.

Desenvolvido como projeto para o bloco "Engenharia de Softwares Escaláveis" da graduação em Engenharia da Computação - Instituto Infnet.

## Funcionalidades

- Autenticação e autorização de usuários
- Criação e gerenciamento de projetos
- Atribuição e acompanhamento de tarefas
- Gerenciamento de membros da equipe
- Funcionalidade de chat em tempo real
- Criação e gerenciamento assíncrono de notas para projetos e tarefas
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
- Spring Cloud para microsserviços
- RabbitMQ para mensageria
- Spring AMQP para integração com RabbitMQ

### Frontend
- React 18
- Next.js 14
- TypeScript
- Tailwind CSS
- Componentes UI Shadcn

## Arquitetura de Microsserviços e Eventos

O projeto agora utiliza uma arquitetura de microsserviços orientada a eventos:

- **Serviço Principal**: Gerencia projetos, tarefas e usuários
- **NoteService**: Microsserviço dedicado ao gerenciamento de notas
- **EurekaServer**: Servidor de descoberta de serviços
- **RabbitMQ**: Message broker para comunicação assíncrona entre serviços

## Instalação

### Pré-requisitos

- Java JDK 21 ou superior
- Maven
- Node.js (v14 ou superior) e npm ou yarn
- MySQL
- RabbitMQ

### Configuração do RabbitMQ

1. Instale e inicie o RabbitMQ em sua máquina local ou use um serviço em nuvem.
2. Certifique-se de que o RabbitMQ está rodando na porta padrão (5672).

### Configuração do Backend

1. Navegue até o diretório do backend:
    ```bash
    cd backend
    ```

2. Configure o banco de dados MySQL e o RabbitMQ:
    - Atualize o arquivo `application.properties` com as credenciais do banco de dados e do RabbitMQ:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/seu_banco_de_dados
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    
    spring.rabbitmq.host=localhost
    spring.rabbitmq.port=5672
    spring.rabbitmq.username=guest
    spring.rabbitmq.password=guest
    ```

3. Execute o EurekaServer:
    ```bash
    cd ../eureka-server
    mvn spring-boot:run
    ```

4. Execute o NoteService:
    ```bash
    cd ../note-service
    mvn spring-boot:run
    ```

5. Compile e execute o backend:
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
    npm run dev
    ```

### Executando a Aplicação

Após configurar o backend, os microsserviços e o frontend, abra o navegador e acesse `http://localhost:3000` para utilizar a aplicação.

## Estrutura do Projeto

### Backend
- `src/main/java/com/klug/projectmanager/`
  - `config/`: Classes de configuração, incluindo RabbitMQConfig
  - `controller/`: Controladores da API REST
  - `dto/`: Objetos de Transferência de Dados
  - `entity/`: Entidades JPA
  - `exception/`: Exceções personalizadas e manipuladores
  - `repository/`: Repositórios JPA
  - `security/`: Configurações de segurança e utilitários JWT
  - `service/`: Serviços de lógica de negócios
  - `messaging/`: Classes relacionadas à comunicação via RabbitMQ

### NoteService
- `src/main/java/com/klug/noteservice/`
  - `config/`: Configurações, incluindo RabbitMQConfig
  - `dto/`: DTOs para notas
  - `entity/`: Entidade Note
  - `repository/`: Repositório para notas
  - `service/`: Serviço de lógica de negócios para notas
  - `messaging/`: Handlers para processamento de mensagens RabbitMQ

### Frontend
- `src/`
  - `app/`: Páginas e roteamento do Next.js
  - `components/`: Componentes React
  - `hooks/`: Hooks React personalizados
  - `lib/`: Funções utilitárias
  - `services/`: Funções de serviço da API

## Arquitetura Orientada a Eventos

O sistema agora utiliza uma arquitetura orientada a eventos para comunicação entre o serviço principal e o microsserviço de notas:

- Eventos são publicados no RabbitMQ quando ocorrem ações relacionadas a notas (criação, atualização, exclusão).
- O microsserviço de notas consome esses eventos e realiza as operações correspondentes.
- Respostas são enviadas de volta ao serviço principal através do RabbitMQ.

Esta arquitetura proporciona melhor escalabilidade, desacoplamento entre serviços e processamento assíncrono de operações.
