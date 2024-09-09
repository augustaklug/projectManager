# Relatório de Implantação com Orquestração via Kubernetes

## Visão Geral
Este relatório descreve o processo de implantação de um sistema distribuído utilizando **Kubernetes** para orquestração de múltiplos serviços, incluindo backend, note-service, MySQL, RabbitMQ e Eureka Server. A orquestração foi feita utilizando **Minikube** e **kubectl** em um ambiente de desenvolvimento local. A automação foi complementada por scripts de port forwarding para expor os serviços localmente no ambiente de desenvolvimento.

## Estrutura do Sistema
O sistema é composto por vários microsserviços que foram implantados em containers gerenciados por Kubernetes. A comunicação entre esses serviços é fundamental para garantir o funcionamento correto da aplicação.

### Serviços Desdobrados
1. **Backend**: O serviço principal da aplicação, responsável pelo gerenciamento de dados e comunicação com o banco de dados MySQL e o serviço RabbitMQ.
2. **Note-service**: Microsserviço que lida com notas, também conectado ao MySQL e RabbitMQ.
3. **MySQL**: Banco de dados utilizado por ambos os serviços (backend e note-service).
4. **RabbitMQ**: Message broker responsável pela comunicação assíncrona entre os serviços.
5. **Eureka Server**: Serviço de descoberta para registro e busca dinâmica dos microsserviços.

## Ferramentas Utilizadas

### Minikube
O **Minikube** foi utilizado para criar e gerenciar um cluster Kubernetes local. Ele fornece um ambiente de desenvolvimento local rápido e leve para testes e experimentação com Kubernetes.

### Kubectl
O **kubectl** é a ferramenta de linha de comando usada para interagir com o cluster Kubernetes, permitindo a aplicação de configurações, monitoramento de pods, execução de comandos e muito mais.

## Passos para Execução Local

### 1. Instalar Minikube e Kubectl

Antes de iniciar o processo, certifique-se de que **Minikube** e **kubectl** estão instalados em sua máquina:

- [Minikube Installation Guide](https://minikube.sigs.k8s.io/docs/start/)
- [Kubectl Installation Guide](https://kubernetes.io/docs/tasks/tools/install-kubectl/)

### 2. Iniciar o Cluster Minikube

Com o Minikube instalado, inicie o cluster local:

```bash
minikube start
```

Isso inicializará um cluster Kubernetes local.

### 3. Aplicar Arquivos de Configuração YAML

Após configurar o cluster, aplique os arquivos YAML para implantar os serviços:

```bash
kubectl apply -f backend-deployment.yaml
kubectl apply -f note-service-deployment.yaml
kubectl apply -f eureka-server-deployment.yaml
kubectl apply -f mysql-deployment.yaml
kubectl apply -f rabbitmq-deployment.yaml
```

### 4. Verificar os Pods e Serviços

Você pode verificar se os pods estão rodando corretamente com:

```bash
kubectl get pods
kubectl get services
```

### 5. Expor os Serviços Localmente com Port Forwarding

Para expor os serviços localmente no **localhost**, foi implementado um script de **port-forward** que redireciona as portas de cada serviço para o `localhost`. O script `port-forward-all.bat` foi criado para simplificar esse processo, automatizando a exposição das portas.

#### Exemplo do Script `port-forward-all.bat`

```batch
@echo off
start cmd /k "kubectl port-forward service/backend 8080:8080"
start cmd /k "kubectl port-forward service/note-service 8081:8081"
start cmd /k "kubectl port-forward service/mysql 3306:3306"
start cmd /k "kubectl port-forward service/rabbitmq 5672:5672"
start cmd /k "kubectl port-forward service/eureka-server 8761:8761"
start cmd /k "kubectl port-forward service/frontend 3000:3000"
```

O script permite que o desenvolvedor trabalhe com todos os serviços expostos em localhost para facilitar o desenvolvimento e testes locais.

### 6. Parar o Cluster

Após o desenvolvimento ou testes, você pode parar o cluster Minikube com:

```bash
minikube stop
```

## Configuração do Kubernetes

### Arquivos YAML de Configuração
Cada serviço foi configurado com um arquivo `.yaml` dedicado para definir os recursos do Kubernetes. Abaixo estão os principais aspectos da configuração:

### 1. **backend-deployment.yaml**
Este arquivo define o deployment e service para o microsserviço backend, conectando-o ao banco de dados MySQL e ao RabbitMQ, assim como ao Eureka Server.

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: backend
spec:
  replicas: 1
  selector:
    matchLabels:
      app: backend
  template:
    metadata:
      labels:
        app: backend
    spec:
      containers:
      - name: backend
        image: augustaklug/backend:latest
        env:
        - name: SPRING_DATASOURCE_URL
          value: "jdbc:mysql://host.docker.internal:3306/db_projectmanager?useSSL=false&createDatabaseIfNotExist=true"
        - name: SPRING_RABBITMQ_HOST
          value: "host.docker.internal"
        - name: EUREKA_CLIENT_SERVICEURL_DEFAULTZONE
          value: "http://host.docker.internal:8761/eureka/"
```

### 2. **note-service-deployment.yaml**
Define o serviço note-service, incluindo as variáveis de ambiente necessárias para comunicação com MySQL e RabbitMQ.

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: note-service
spec:
  replicas: 1
  selector:
    matchLabels:
      app: note-service
  template:
    metadata:
      labels:
        app: note-service
    spec:
      containers:
        - name: note-service
          image: augustaklug/note-service:latest
          env:
            - name: SPRING_DATASOURCE_URL
              value: "jdbc:mysql://host.docker.internal:3306/db_noteservice?useSSL=false&createDatabaseIfNotExist=true"
            - name: SPRING_RABBITMQ_HOST
              value: "host.docker.internal"
            - name: EUREKA_CLIENT_SERVICEURL_DEFAULTZONE
              value: "http://host.docker.internal:8761/eureka/"
```

### 3. **eureka-server-deployment.yaml**
Este arquivo configura o Eureka Server, que é fundamental para a descoberta e comunicação entre os serviços.

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: eureka-server
spec:
  replicas: 1
  selector:
    matchLabels:
      app: eureka-server
  template:
    metadata:
      labels:
        app: eureka-server
    spec:
      containers:
      - name: eureka-server
        image: augustaklug/eureka-server:latest
        ports:
          - containerPort: 8761
```

## Resultados e Considerações

- **Orquestração**: A orquestração dos serviços funcionou corretamente, utilizando Kubernetes (via Minikube) para gerenciar o ciclo de vida dos containers e a descoberta de serviços com o Eureka.
- **Port Forwarding**: O script de port-forwarding automatizado facilitou o desenvolvimento local, garantindo acesso aos serviços Kubernetes diretamente do localhost.

## Próximos Passos
- **Explorar Ingress**: No futuro, pode ser interessante explorar o uso de Ingress Controller para gerenciar o roteamento de serviços de forma mais eficiente em ambientes de produção.
