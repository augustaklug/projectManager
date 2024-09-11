# Relatório de Implantação com Orquestração via Kubernetes

## Visão Geral

Este relatório descreve o processo de implantação de um sistema distribuído utilizando **Kubernetes** para orquestração de múltiplos serviços, incluindo backend, note-service, MySQL, RabbitMQ, Zipkin e Eureka Server. A orquestração foi realizada utilizando manifestos Kubernetes para definir e gerenciar os recursos do cluster.

## Estrutura do Sistema

O sistema é composto por vários microsserviços implantados em containers gerenciados por Kubernetes. A comunicação entre esses serviços é fundamental para garantir o funcionamento correto da aplicação.

### Serviços Implantados

1. **Backend**: Serviço principal da aplicação, responsável pelo gerenciamento de dados e comunicação com o banco de dados MySQL e o serviço RabbitMQ.
2. **Note-service**: Microsserviço que lida com notas, também conectado ao MySQL e RabbitMQ.
3. **MySQL**: Banco de dados utilizado pelos serviços backend e note-service.
4. **RabbitMQ**: Message broker responsável pela comunicação assíncrona entre os serviços.
5. **Zipkin**: Sistema de rastreamento distribuído para monitoramento e diagnóstico de microsserviços.
6. **Eureka Server**: Serviço de descoberta para registro e busca dinâmica dos microsserviços.
7. **Frontend**: Interface do usuário da aplicação.


## Configuração do Kubernetes

### Arquivos YAML de Configuração

Cada serviço foi configurado com um arquivo `.yaml` dedicado para definir os recursos do Kubernetes. Abaixo estão os principais aspectos da configuração:

### 1. MySQL (mysql-deployment.yaml)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
      - name: mysql
        image: mysql:8.0
        env:
        - name: MYSQL_ROOT_PASSWORD
          valueFrom:
            secretKeyRef:
              name: mysql-secret
              key: root-password
        - name: MYSQL_DATABASE
          value: db_projectmanager
        volumeMounts:
        - name: mysql-data
          mountPath: /var/lib/mysql
      volumes:
      - name: mysql-data
        persistentVolumeClaim:
          claimName: mysql-pvc
---
apiVersion: v1
kind: Service
metadata:
  name: mysql
spec:
  selector:
    app: mysql
  ports:
  - port: 3306
    targetPort: 3306
---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mysql-pvc
spec:
  accessModes:
  - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
```

### 2. RabbitMQ (rabbitmq-deployment.yaml)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: rabbitmq
spec:
  replicas: 1
  selector:
    matchLabels:
      app: rabbitmq
  template:
    metadata:
      labels:
        app: rabbitmq
    spec:
      containers:
      - name: rabbitmq
        image: rabbitmq:3.13-management
        ports:
        - containerPort: 5672
        - containerPort: 15672
---
apiVersion: v1
kind: Service
metadata:
  name: rabbitmq
spec:
  selector:
    app: rabbitmq
  ports:
  - port: 5672
    targetPort: 5672
  - port: 15672
    targetPort: 15672
```

### 3. Zipkin (zipkin-deployment.yaml)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: zipkin
spec:
  replicas: 1
  selector:
    matchLabels:
      app: zipkin
  template:
    metadata:
      labels:
        app: zipkin
    spec:
      containers:
      - name: zipkin
        image: openzipkin/zipkin
        ports:
        - containerPort: 9411
---
apiVersion: v1
kind: Service
metadata:
  name: zipkin
spec:
  selector:
    app: zipkin
  ports:
  - port: 9411
    targetPort: 9411
```

### 4. Eureka Server (eureka-server-deployment.yaml)

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
---
apiVersion: v1
kind: Service
metadata:
  name: eureka-server
spec:
  selector:
    app: eureka-server
  ports:
  - port: 8761
    targetPort: 8761
```

### 5. Backend (backend-deployment.yaml)

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
        ports:
        - containerPort: 8080
        envFrom:
        - configMapRef:
            name: backend-config
        env:
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: backend-secret
              key: jwt-secret
---
apiVersion: v1
kind: Service
metadata:
  name: backend
spec:
  selector:
    app: backend
  ports:
  - port: 8080
    targetPort: 8080
---
apiVersion: v1
kind: ConfigMap
metadata:
  name: backend-config
data:
  SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/db_projectmanager?createDatabaseIfNotExist=true
  SPRING_RABBITMQ_HOST: rabbitmq
  EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://eureka-server:8761/eureka/
  JWT_EXPIRY: "3600000"
  MANAGEMENT_ZIPKIN_TRACING_ENDPOINT: http://zipkin:9411/api/v2/spans
```

### 6. Note Service (note-service-deployment.yaml)

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
        ports:
        - containerPort: 8081
        envFrom:
        - configMapRef:
            name: note-service-config
---
apiVersion: v1
kind: Service
metadata:
  name: note-service
spec:
  selector:
    app: note-service
  ports:
  - port: 8081
    targetPort: 8081
---
apiVersion: v1
kind: ConfigMap
metadata:
  name: note-service-config
data:
  SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/db_noteservice?createDatabaseIfNotExist=true
  SPRING_RABBITMQ_HOST: rabbitmq
  EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://eureka-server:8761/eureka/
  MANAGEMENT_ZIPKIN_TRACING_ENDPOINT: http://zipkin:9411/api/v2/spans
```

### 7. Frontend (frontend-deployment.yaml)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: frontend
spec:
  replicas: 1
  selector:
    matchLabels:
      app: frontend
  template:
    metadata:
      labels:
        app: frontend
    spec:
      containers:
      - name: frontend
        image: augustaklug/frontend:latest
        ports:
        - containerPort: 3000
        env:
        - name: NEXT_PUBLIC_API_URL
          value: http://backend:8080/api
---
apiVersion: v1
kind: Service
metadata:
  name: frontend
spec:
  selector:
    app: frontend
  ports:
  - port: 3000
    targetPort: 3000
```

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

Aplique os arquivos YAML para implantar os serviços:

```shellscript
kubectl apply -f mysql-deployment.yaml
kubectl apply -f rabbitmq-deployment.yaml
kubectl apply -f zipkin-deployment.yaml
kubectl apply -f eureka-server-deployment.yaml
kubectl apply -f backend-deployment.yaml
kubectl apply -f note-service-deployment.yaml
kubectl apply -f frontend-deployment.yaml
```

### 4. Verificar os Pods e Serviços

Verifique se os pods estão rodando corretamente com:

```shellscript
kubectl get pods
kubectl get services
```

### 5. Expor o Frontend Localmente com Port Forwarding

Para acessar o serviço, é necessário apenas fazer o port-forward do frontend:

```shellscript
kubectl port-forward service/frontend 3000:3000
```

Após executar este comando, você poderá acessar a aplicação em `http://localhost:3000` no seu navegador.

## Resultados e Considerações

- **Orquestração**: A orquestração dos serviços funciona corretamente, utilizando Kubernetes para gerenciar o ciclo de vida dos containers e a descoberta de serviços com o Eureka.
- **Comunicação Interna**: Os serviços se comunicam internamente no cluster Kubernetes, eliminando a necessidade de expor todos os serviços localmente.
- **Acesso à Aplicação**: Apenas o frontend precisa ser exposto via port-forward para acessar a aplicação completa, simplificando o processo de desenvolvimento e teste.

## Próximos Passos

- **Ingress Controller**: Considerar a implementação de um Ingress Controller para gerenciar o roteamento de tráfego externo de forma mais eficiente em ambientes de produção.
- **Escalabilidade**: Explorar opções de escalabilidade horizontal para os serviços conforme a demanda aumenta.
