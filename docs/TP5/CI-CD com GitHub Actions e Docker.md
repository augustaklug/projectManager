# Relatório: Configuração de CI/CD com GitHub Actions e Docker

Este documento descreve o processo de configuração de Integração Contínua (CI) e Entrega Contínua (CD) utilizando GitHub Actions para automatizar o ciclo de desenvolvimento do projeto. A configuração inclui testes automatizados, construção de imagens Docker, publicação no Docker Hub e deploy em um ambiente de staging.

## Visão Geral

O fluxo de trabalho (workflow) criado no GitHub Actions realiza as seguintes etapas principais:
1. **Executar testes**: Testes automatizados no backend, no serviço de notas e no frontend.
2. **Construir e publicar imagens Docker**: As imagens Docker para todos os serviços são construídas e publicadas no Docker Hub.
3. **Deploy em ambiente de staging**: O projeto é implantado automaticamente em um ambiente de staging após a publicação das imagens.

## Estrutura do Workflow CI/CD

O workflow é acionado sempre que há um push ou pull request para a branch `main`. Ele está definido no arquivo `.github/workflows/ci-cd.yml`, com as seguintes etapas:

### 1. **Testes Automatizados**
O primeiro job executa testes para garantir que o código do backend, note-service e frontend esteja funcionando corretamente. A configuração inclui:

- **Backend**: Usa o Maven para rodar os testes do projeto Spring Boot.
- **Note-Service**: Também utiliza Maven para rodar testes.
- **Frontend**: Usa Node.js para rodar testes com o framework JavaScript configurado.

### 2. **Build e Push de Imagens Docker**
Após a execução dos testes, o próximo job cuida da construção e publicação das imagens Docker. As imagens são criadas a partir dos arquivos Dockerfiles contidos nos diretórios `backend`, `note-service` e `frontend`. O processo inclui:

- **Login no Docker Hub**: A autenticação no Docker Hub é feita usando secrets armazenados no repositório do GitHub.
- **Construção de Imagens Docker**: O job utiliza o Docker para construir as imagens de cada um dos serviços.
- **Publicação das Imagens**: As imagens são publicadas com a tag `latest` no Docker Hub.

### 3. **Deploy para o Ambiente de Staging**
Após a construção e publicação das imagens Docker, o último job simula um deploy para um ambiente de staging. Esta etapa pode ser personalizada para incluir um deploy real, utilizando, por exemplo, SSH para acessar o servidor de staging e atualizar os containers.

## Código YAML do Workflow

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

env:
  DOCKER_USERNAME: ${{ secrets.DOCKER_USERNAME }}
  DOCKER_PASSWORD: ${{ secrets.DOCKER_PASSWORD }}

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2

    - name: Set up JDK 21
      uses: actions/setup-java@v2
      with:
        java-version: '21'
        distribution: 'adopt'

    - name: Test Backend
      run: |
        cd backend
        mvn test

    - name: Test Note Service
      run: |
        cd note-service
        mvn test

    - name: Set up Node.js
      uses: actions/setup-node@v2
      with:
        node-version: '18'

    - name: Test Frontend
      run: |
        cd frontend
        npm ci
        npm test

  build-and-push:
    needs: test
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2

    - name: Login to DockerHub
      uses: docker/login-action@v1
      with:
        username: ${{ env.DOCKER_USERNAME }}
        password: ${{ env.DOCKER_PASSWORD }}

    - name: Build and push Backend
      uses: docker/build-push-action@v2
      with:
        context: ./backend
        push: true
        tags: ${{ env.DOCKER_USERNAME }}/project-manager-backend:latest

    - name: Build and push Note Service
      uses: docker/build-push-action@v2
      with:
        context: ./note-service
        push: true
        tags: ${{ env.DOCKER_USERNAME }}/project-manager-note-service:latest

    - name: Build and push Frontend
      uses: docker/build-push-action@v2
      with:
        context: ./frontend
        push: true
        tags: ${{ env.DOCKER_USERNAME }}/project-manager-frontend:latest

  deploy:
    needs: build-and-push
    runs-on: ubuntu-latest
    steps:
    - name: Deploy to staging
      run: |
        echo "Deploying to staging environment"
        # Aqui você adicionaria os comandos para fazer o deploy
        # Por exemplo, usando ssh para conectar ao servidor de staging e atualizar os containers
        # ssh user@staging-server 'docker-compose pull && docker-compose up -d'
