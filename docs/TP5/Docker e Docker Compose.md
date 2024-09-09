# Relatório: Conteinerização do Projeto com Docker e Docker Compose

## Introdução

Este relatório detalha o processo de conteinerização do projeto de gerenciamento de projetos utilizando Docker e Docker Compose. A conteinerização oferece diversos benefícios, incluindo consistência entre ambientes de desenvolvimento e produção, facilidade de implantação e escalabilidade.

## Estrutura do Projeto

Nosso projeto consiste em várias partes:

1. Backend (Java Spring Boot)
2. Frontend (Next.js)
3. Microsserviço de Notas (Java Spring Boot)
4. Servidor Eureka (para descoberta de serviços)
5. Banco de dados MySQL
6. RabbitMQ (para mensageria)

## Dockerização dos Componentes

### Backend

O Dockerfile para o backend foi criado para construir e executar nossa aplicação Spring Boot:

```dockerfile
FROM maven:latest AS build
WORKDIR /app
COPY backend/pom.xml .
COPY backend/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-focal
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Este Dockerfile utiliza um build em múltiplos estágios para minimizar o tamanho final da imagem.

### Frontend

Para o frontend, utilizamos o seguinte Dockerfile:

```dockerfile
FROM node:18-alpine
WORKDIR /app
COPY frontend/package*.json ./
RUN npm ci
COPY frontend .
RUN npm run build
EXPOSE 3000
CMD ["npm", "start"]
```

Esta abordagem constrói e serve nossa aplicação Next.js.

### Serviço de Notas e Servidor Eureka

Dockerfiles semelhantes foram criados para o microsserviço de notas e o servidor Eureka, adaptando as especificidades de cada componente.

## Docker Compose

O Docker Compose foi utilizado para orquestrar todos os serviços. Aqui está um resumo do `docker-compose.yml`:

```yaml
version: '3.8'

services:
  backend:
    build:
      context: .
      dockerfile: backend/Dockerfile
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - rabbitmq
      - eureka-server

  frontend:
    build:
      context: .
      dockerfile: frontend/Dockerfile
    ports:
      - "3000:3000"
    depends_on:
      - backend

  note-service:
    build:
      context: .
      dockerfile: note-service/Dockerfile
    ports:
      - "8081:8081"
    depends_on:
      - mysql
      - rabbitmq
      - eureka-server

  eureka-server:
    build:
      context: .
      dockerfile: eureka-server/Dockerfile
    ports:
      - "8761:8761"

  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: db_projectmanager

  rabbitmq:
    image: rabbitmq:3.13-management
    ports:
      - "5672:5672"
      - "15672:15672"
```

Este arquivo Docker Compose define como nossos serviços devem ser executados, suas dependências e configurações de rede.

## Vantagens da Abordagem

1. **Consistência de Ambiente**: Garante que todos os desenvolvedores trabalhem com o mesmo ambiente.
2. **Isolamento**: Cada serviço opera em seu próprio container, minimizando conflitos.
3. **Escalabilidade**: Facilita a escalabilidade horizontal dos serviços.
4. **Facilidade de Implantação**: Simplifica o processo de implantação em diferentes ambientes.
5. **Gerenciamento de Dependências**: O Docker Compose gerencia as dependências entre os serviços.
