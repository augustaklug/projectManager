@echo off
start cmd /k "kubectl port-forward service/backend 8080:8080"
start cmd /k "kubectl port-forward service/note-service 8081:8081"
start cmd /k "kubectl port-forward service/mysql 3306:3306"
start cmd /k "kubectl port-forward service/rabbitmq 5672:5672"
start cmd /k "kubectl port-forward service/eureka-server 8761:8761"
start cmd /k "kubectl port-forward service/frontend 3000:3000"
start cmd /k "kubectl port-forward service/zipkin 9411:9411"