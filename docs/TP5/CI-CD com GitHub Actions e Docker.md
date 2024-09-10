# Relatório: Configuração de CI com GitHub Actions e Docker

Este documento descreve o processo de configuração de Integração Contínua (CI) utilizando GitHub Actions para automatizar o ciclo de desenvolvimento do projeto. A configuração inclui testes automatizados, construção de imagens Docker e publicação no Docker Hub.

## Visão Geral

O fluxo de trabalho (workflow) criado no GitHub Actions realiza as seguintes etapas principais:
1. **Executar testes**: Testes automatizados no backend, no serviço de notas, no servidor Eureka e no frontend.
2. **Construir imagens Docker**: As imagens Docker para todos os serviços são construídas após a execução bem-sucedida dos testes.
3. **Publicar imagens no Docker Hub**: As imagens Docker são publicadas no Docker Hub após serem construídas com sucesso.

## Estrutura do Workflow CI

O workflow é acionado sempre que há um push ou pull request para a branch `main`. Ele está definido no arquivo `.github/workflows/cicd.yml`, com as seguintes etapas:

### 1. **Testes e Build**
O job `build-and-push` executa testes, constrói as aplicações e as imagens Docker, e as publica no Docker Hub. A configuração inclui:

- **Eureka Server**: Usa o Maven para rodar os testes e construir o projeto Spring Boot.
- **Backend**: Usa o Maven para rodar os testes e construir o projeto Spring Boot.
- **Note Service**: Também utiliza Maven para rodar testes e construir o projeto.
- **Frontend**: Usa Node.js para rodar testes e construir a aplicação.

### 2. **Construção e Push de Imagens Docker**
Após a execução dos testes e build, o mesmo job cuida da construção e publicação das imagens Docker. As imagens são criadas a partir dos arquivos Dockerfiles contidos nos diretórios `eureka-server`, `backend`, `note-service` e `frontend`. O processo inclui:

- **Login no Docker Hub**: A autenticação no Docker Hub é feita usando secrets armazenados no repositório do GitHub.
- **Construção de Imagens Docker**: O job utiliza o Docker para construir as imagens de cada um dos serviços.
- **Publicação das Imagens**: As imagens são publicadas com a tag `latest` no Docker Hub.

## Código YAML do Workflow

```yaml
name: Project Manager CI/CD

on:
  push:
    branches:
      - main
  pull_request:
    branches:
      - main

jobs:
  build-and-push:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v3

      - name: Login to Docker Hub
        uses: docker/login-action@v2
        with:
          username: ${{ secrets.DOCKER_USERNAME }}
          password: ${{ secrets.DOCKER_PASSWORD }}

      - name: Set up JDK 21
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'

      - name: Build and Test Eureka Server
        run: |
          cd ./eureka-server
          chmod +x ./mvnw
          ./mvnw clean test
          ./mvnw package -DskipTests
          docker build -t ${{ secrets.DOCKER_USERNAME }}/eureka-server:latest .
          docker push ${{ secrets.DOCKER_USERNAME }}/eureka-server:latest

      - name: Build and Test Backend
        run: |
          cd ./backend
          chmod +x ./mvnw
          ./mvnw clean test
          ./mvnw package -DskipTests
          docker build -t ${{ secrets.DOCKER_USERNAME }}/backend:latest .
          docker push ${{ secrets.DOCKER_USERNAME }}/backend:latest

      - name: Build and Test Note Service
        run: |
          cd ./note-service
          chmod +x ./mvnw
          ./mvnw clean test
          ./mvnw package -DskipTests
          docker build -t ${{ secrets.DOCKER_USERNAME }}/note-service:latest .
          docker push ${{ secrets.DOCKER_USERNAME }}/note-service:latest

      - name: Set up Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '18'

      - name: Build and Test Frontend
        run: |
          cd ./frontend
          npm ci
          npm test
          npm run build
          docker build -t ${{ secrets.DOCKER_USERNAME }}/frontend:latest .
          docker push ${{ secrets.DOCKER_USERNAME }}/frontend:latest
```

## Configuração Necessária

Para utilizar este workflow, é necessário configurar os seguintes secrets no repositório GitHub:

1. `DOCKER_USERNAME`: Seu nome de usuário do Docker Hub
2. `DOCKER_PASSWORD`: Sua senha do Docker Hub

## Considerações Finais

Este workflow de CI automatiza o processo de teste, build e publicação de imagens Docker para todos os componentes do projeto. Ele garante que apenas código que passa nos testes seja usado para criar as imagens Docker, e que essas imagens sejam prontamente disponibilizadas no Docker Hub para uso em ambientes de staging ou produção.

A etapa de deploy não foi incluída neste workflow, focando apenas na integração contínua e na preparação das imagens Docker. O deploy para ambientes de staging ou produção pode ser configurado separadamente, permitindo maior flexibilidade e controle sobre o processo de implantação.
