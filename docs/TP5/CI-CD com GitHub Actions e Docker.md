# Relatório: Configuração de CI/CD com GitHub Actions e Docker

Este documento descreve o processo de configuração de Integração Contínua (CI) e Entrega Contínua (CD) utilizando GitHub Actions para automatizar o ciclo de desenvolvimento do projeto. A configuração inclui a construção de imagens Docker e publicação no Docker Hub para todos os serviços do projeto.

## Visão Geral

O fluxo de trabalho (workflow) criado no GitHub Actions realiza as seguintes etapas principais:
1. **Configuração do ambiente**: Prepara o ambiente de execução com as ferramentas necessárias.
2. **Construir imagens Docker**: As imagens Docker para todos os serviços são construídas.
3. **Publicar imagens no Docker Hub**: As imagens Docker são publicadas no Docker Hub após serem construídas com sucesso.

## Estrutura do Workflow CI/CD

O workflow é acionado nas seguintes situações:
- Push para a branch `master`
- Manualmente através da interface do GitHub Actions (workflow_dispatch)

Ele está definido no arquivo `.github/workflows/cicd.yml`, com as seguintes etapas principais:

### 1. **Configuração do Ambiente**
- Checkout do código (actions/checkout@v4.1.7)
- Configuração do Docker Buildx (docker/setup-buildx-action@v3.6.1)
- Login no Docker Hub (docker/login-action@v3.3.0)
- Configuração do JDK 21 (actions/setup-java@v4.3.0)
- Configuração do ambiente Node.js (actions/setup-node@v4.0.3)

### 2. **Build e Push das Imagens Docker**
Para cada serviço (Eureka Server, Backend, Note Service, Frontend):
- Construção da imagem Docker
- Push da imagem para o Docker Hub
- Utilização de cache do GitHub Actions para otimizar o processo de build

## Código YAML do Workflow

```yaml
name: Project Manager CI/CD

on:
  push:
    branches: [ "master" ]
  workflow_dispatch:

jobs:
  build-and-push:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v4.1.7

      - name: Set up Docker Buildx
        uses: docker/setup-buildx-action@v3.6.1

      - name: Login to Docker Hub
        uses: docker/login-action@v3.3.0
        with:
          username: ${{ secrets.DOCKER_USERNAME }}
          password: ${{ secrets.DOCKER_PASSWORD }}

      - name: Set up JDK 21
        uses: actions/setup-java@v4.3.0
        with:
          java-version: '21'
          distribution: 'temurin'

      - name: Build and push Eureka Server Docker image
        uses: docker/build-push-action@v6.7.0
        with:
          context: .
          file: ./eureka-server/Dockerfile
          push: true
          cache-from: type=gha
          tags: ${{ secrets.DOCKER_USERNAME }}/eureka-server:latest

      # Etapas similares para Backend e Note Service...

      - name: Setup Node.js environment
        uses: actions/setup-node@v4.0.3
        with:
          node-version: '18'
          cache: 'npm'
          cache-dependency-path: './frontend/package-lock.json'

      - name: Build and push Frontend Docker image
        uses: docker/build-push-action@v6.7.0
        with:
          context: .
          file: ./frontend/Dockerfile
          push: true
          cache-from: type=gha
          tags: ${{ secrets.DOCKER_USERNAME }}/frontend:latest
```

## Configuração Necessária

Para utilizar este workflow, é necessário configurar os seguintes secrets no repositório GitHub:

1. `DOCKER_USERNAME`: Seu nome de usuário do Docker Hub
2. `DOCKER_PASSWORD`: Sua senha do Docker Hub

## Execução do Workflow

O workflow pode ser executado de duas maneiras:

1. **Automaticamente em push**: Quando um push é feito para a branch `master`.
2. **Manualmente**: Através da interface do GitHub Actions, clicando em "Run workflow".

