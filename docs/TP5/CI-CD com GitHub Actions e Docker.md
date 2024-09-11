# Relatório Atualizado: Configuração de CI/CD com GitHub Actions, Docker e Implantação na Oracle Cloud

Este documento descreve o processo de configuração de Integração Contínua (CI) e Entrega Contínua (CD) utilizando GitHub Actions para automatizar o ciclo de desenvolvimento e implantação do projeto. A configuração inclui a construção de imagens Docker, publicação no Docker Hub e implantação na Oracle Cloud.

## Visão Geral

O pipeline de CI/CD consiste em dois workflows principais:

1. **Push to Docker Hub**: Constrói e publica imagens Docker para todos os serviços do projeto.
2. **Deploy to Oracle Cloud**: Implanta as imagens atualizadas na infraestrutura da Oracle Cloud.

## Estrutura do Workflow "Push to Docker Hub"

Este workflow é acionado nas seguintes situações:
- Push para a branch `master` (excluindo alterações em arquivos .md e .gitignore)
- Manualmente através da interface do GitHub Actions (workflow_dispatch)

Está definido no arquivo `.github/workflows/pushToDockerHub.yml`, com as seguintes etapas principais:

### 1. Configuração do Ambiente
- Checkout do código
- Configuração do Docker Buildx
- Login no Docker Hub
- Configuração do JDK 21
- Configuração do ambiente Node.js (para o frontend)

### 2. Build e Push das Imagens Docker
Para cada serviço (Eureka Server, Backend, Note Service, Frontend):
- Construção da imagem Docker
- Push da imagem para o Docker Hub
- Utilização de cache do GitHub Actions para otimizar o processo de build

## Estrutura do Workflow "Deploy to Oracle Cloud"

Este workflow é acionado:
- Após a conclusão bem-sucedida do workflow "Push to Docker Hub"
- Manualmente através da interface do GitHub Actions (workflow_dispatch)

Está definido no arquivo `.github/workflows/deployToOracle.yml`, com as seguintes etapas principais:

### 1. Configuração do Ambiente
- Checkout do código
- Instalação e configuração do Oracle Cloud CLI

### 2. Implantação na Oracle Cloud
- Obtenção do IP público da instância Oracle Cloud
- Configuração da chave SSH
- Conexão SSH à instância e execução de comandos de implantação:
  - Clonagem ou atualização do repositório
  - Pull das imagens Docker atualizadas
  - Execução do Docker Compose para iniciar os serviços

## Código YAML dos Workflows

### Push to Docker Hub (pushToDockerHub.yml)

```yaml
name: Push to Docker Hub

on:
  push:
    branches: [ "master" ]
    paths-ignore:
      - '**/*.md'
      - '.gitignore'
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

      # Steps for building and pushing Docker images for each service...

      - name: Setup Node.js environment
        uses: actions/setup-node@v4.0.3
        with:
          node-version: '18'
          cache: 'npm'
          cache-dependency-path: './frontend/package-lock.json'

      # Step for building and pushing Frontend Docker image...
```

### Deploy to Oracle Cloud (deployToOracle.yml)

```yaml
name: Deploy to Oracle Cloud

on:
  workflow_run:
    workflows: ["Push to Docker Hub"]
    types:
      - completed
  workflow_dispatch:

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v4.1.7

      # Steps for installing and configuring Oracle Cloud CLI...

      - name: Deploy to Oracle Cloud
        env:
          INSTANCE_OCID: ${{ secrets.INSTANCE_OCID }}
          ORACLE_SSH_PRIVATE_KEY: ${{ secrets.ORACLE_SSH_PRIVATE_KEY }}
          REPO_URL: https://github.com/augustaklug/projectManager.git
        run: |
          # Script for deploying to Oracle Cloud instance...
```

## Configuração Necessária

Para utilizar estes workflows, é necessário configurar os seguintes secrets no repositório GitHub:

1. `DOCKER_USERNAME`: Seu nome de usuário do Docker Hub
2. `DOCKER_PASSWORD`: Sua senha do Docker Hub
3. `INSTANCE_OCID`: OCID da instância Oracle Cloud
4. `ORACLE_SSH_PRIVATE_KEY`: Chave SSH privada para acesso à instância Oracle Cloud
5. `OCI_CLI_USER`: ID do usuário Oracle Cloud
6. `OCI_CLI_TENANCY`: ID do tenant Oracle Cloud
7. `OCI_CLI_FINGERPRINT`: Fingerprint da chave API Oracle Cloud
8. `OCI_CLI_KEY_CONTENT`: Conteúdo da chave API Oracle Cloud
9. `OCI_CLI_REGION`: Região Oracle Cloud

## Docker Compose

O arquivo `docker-compose.yml` define a configuração dos serviços do projeto:

- MySQL
- RabbitMQ
- Zipkin
- Eureka Server
- Backend
- Note Service
- Frontend

Cada serviço é configurado com as devidas dependências, portas expostas e variáveis de ambiente necessárias.

## Execução do Pipeline

1. Ao fazer push para a branch `master`, o workflow "Push to Docker Hub" é acionado automaticamente.
2. Após a conclusão bem-sucedida do "Push to Docker Hub", o workflow "Deploy to Oracle Cloud" é acionado automaticamente.
3. Ambos os workflows podem ser executados manualmente através da interface do GitHub Actions.

Este pipeline CI/CD automatizado garante que as alterações no código sejam rapidamente integradas e implantadas na infraestrutura da Oracle Cloud, mantendo o ambiente sempre atualizado com as últimas mudanças aprovadas.
