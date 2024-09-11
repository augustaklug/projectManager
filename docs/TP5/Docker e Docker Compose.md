# Relatório: Containerização do Projeto com Docker e Docker Compose

## Introdução

Este relatório detalha o processo de containerização do projeto utilizando Docker e Docker Compose. A containerização oferece diversos benefícios, incluindo consistência entre ambientes de desenvolvimento e produção, facilidade de implantação e escalabilidade.

## Estrutura do Projeto

O projeto consiste em vários componentes:

1. Backend (Java Spring Boot)
2. Frontend (Next.js)
3. Microsserviço de Notas (Java Spring Boot)
4. Servidor Eureka (para descoberta de serviços)
5. Banco de dados MySQL
6. RabbitMQ (para mensageria)
7. Zipkin (para rastreamento distribuído)

## Containerização com Docker

### Dockerfiles

Cada componente principal do projeto possui seu próprio Dockerfile:

1. **Backend** (arquivo: `backend/Dockerfile`):
   ```dockerfile
   FROM maven:3.9.6-eclipse-temurin-21 AS build
   WORKDIR /app
   COPY backend/pom.xml .
   COPY backend/src ./src
   RUN mvn clean package -DskipTests

   FROM eclipse-temurin:21-jre-jammy
   WORKDIR /app
   COPY --from=build /app/target/*.jar app.jar
   EXPOSE 8080
   ENTRYPOINT ["java", "-jar", "app.jar"]
   ```

2. **Frontend** (arquivo: `frontend/Dockerfile`):
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

3. **Note Service** (arquivo: `note-service/Dockerfile`):
   ```dockerfile
   FROM maven:3.9.6-eclipse-temurin-21 AS build
   WORKDIR /app
   COPY note-service/pom.xml .
   COPY note-service/src ./src
   RUN mvn clean package -DskipTests

   FROM eclipse-temurin:21-jre-jammy
   WORKDIR /app
   COPY --from=build /app/target/*.jar app.jar
   EXPOSE 8081
   ENTRYPOINT ["java", "-jar", "app.jar"]
   ```

4. **Eureka Server** (arquivo: `eureka-server/Dockerfile`):
   ```dockerfile
   FROM maven:3.9.6-eclipse-temurin-21 AS build
   WORKDIR /app
   COPY eureka-server/pom.xml .
   COPY eureka-server/src ./src
   RUN mvn clean package -DskipTests

   FROM eclipse-temurin:21-jre-jammy
   WORKDIR /app
   COPY --from=build /app/target/*.jar app.jar
   EXPOSE 8761
   ENTRYPOINT ["java", "-jar", "app.jar"]
   ```

### Docker Compose

O arquivo `docker-compose.yml` orquestra todos os serviços do projeto:

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    ports:
      - "3306:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=root
      - MYSQL_DATABASE=db_projectmanager
    volumes:
      - mysql_data:/var/lib/mysql

  rabbitmq:
    image: rabbitmq:3.13-management
    ports:
      - "5672:5672"
      - "15672:15672"

  zipkin:
    image: openzipkin/zipkin
    ports:
      - "9411:9411"

  eureka-server:
    image: augustaklug/eureka-server:latest
    ports:
      - "8761:8761"
    depends_on:
      - mysql
      - rabbitmq
      - zipkin

  backend:
    image: augustaklug/backend:latest
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/db_projectmanager?createDatabaseIfNotExist=true
      - SPRING_RABBITMQ_HOST=rabbitmq
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/
      - JWT_SECRET=2e6a98abb5b23339ad14601d32e6a98abb5b23339ad14601d32e6a98abb5b23339ad14601d32e6a98abb5b23339ad14601d3
      - JWT_EXPIRY=3600000
      - MANAGEMENT_ZIPKIN_TRACING_ENDPOINT=http://zipkin:9411/api/v2/spans
    depends_on:
      - eureka-server
      - mysql
      - rabbitmq
      - zipkin

  note-service:
    image: augustaklug/note-service:latest
    ports:
      - "8081:8081"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/db_noteservice?createDatabaseIfNotExist=true
      - SPRING_RABBITMQ_HOST=rabbitmq
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka/
      - MANAGEMENT_ZIPKIN_TRACING_ENDPOINT=http://zipkin:9411/api/v2/spans
    depends_on:
      - eureka-server
      - mysql
      - rabbitmq
      - zipkin

  frontend:
    image: augustaklug/frontend:latest
    ports:
      - "3000:3000"
    environment:
      - NEXT_PUBLIC_API_URL=http://146.235.58.207:8080/api
    depends_on:
      - backend
      - note-service

volumes:
  mysql_data:
```

## Vantagens da Abordagem

1. **Consistência de Ambiente**: Garante que todos os desenvolvedores trabalhem com o mesmo ambiente, reduzindo problemas de "funciona na minha máquina".

2. **Isolamento**: Cada serviço opera em seu próprio container, minimizando conflitos entre dependências e versões.

3. **Escalabilidade**: Facilita a escalabilidade horizontal dos serviços, permitindo adicionar ou remover instâncias conforme necessário.

4. **Facilidade de Implantação**: Simplifica o processo de implantação em diferentes ambientes, desde desenvolvimento até produção.

5. **Gerenciamento de Dependências**: O Docker Compose gerencia eficientemente as dependências entre os serviços, garantindo que sejam iniciados na ordem correta.

6. **Portabilidade**: Os containers podem ser executados em qualquer ambiente que suporte Docker, facilitando a migração entre diferentes infraestruturas.

7. **Versionamento de Infraestrutura**: Permite versionar não apenas o código, mas também a configuração da infraestrutura, facilitando o rastreamento de mudanças.

## Conclusão

A containerização do projeto utilizando Docker e Docker Compose proporciona um ambiente de desenvolvimento e implantação consistente, flexível e escalável. Esta abordagem simplifica significativamente o gerenciamento de dependências e a implantação do sistema, permitindo que a equipe de desenvolvimento se concentre mais na lógica de negócios e menos em questões de infraestrutura.

A estrutura atual do projeto, com serviços bem definidos e isolados, também facilita futuras expansões e integrações, tornando o sistema mais resiliente e adaptável às mudanças nos requisitos do negócio.
