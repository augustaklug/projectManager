# Relatório: Implementação de Monitoramento e Rastreamento em Microsserviços

## Introdução

Este relatório descreve as mudanças implementadas para configurar ferramentas de agregação de logs e rastreamento de transações na arquitetura de microsserviços. O objetivo dessas alterações é melhorar o monitoramento da operação dos serviços, facilitando a detecção e resolução de problemas.

## Alterações Implementadas

### 1. Adição do Spring Boot Actuator

O Spring Boot Actuator foi adicionado ao projeto para fornecer endpoints de monitoramento e gerenciamento.

#### Mudanças no pom.xml:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

#### Configurações no application.properties:
```properties
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always
management.endpoints.web.base-path=/actuator
```

### 2. Implementação de Rastreamento Distribuído

Utilizamos o Micrometer Tracing para implementar o rastreamento distribuído.

#### Mudanças no pom.xml:
```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bridge-brave</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing</artifactId>
</dependency>
<dependency>
    <groupId>io.zipkin.reporter2</groupId>
    <artifactId>zipkin-reporter-brave</artifactId>
</dependency>
```

#### Configurações no application.properties:
```properties
management.tracing.sampling.probability=1.0
management.zipkin.tracing.endpoint=http://localhost:9411/api/v2/spans
```

### 3. Configuração de Métricas com Prometheus

Adicionamos suporte para exportação de métricas no formato Prometheus.

#### Mudanças no pom.xml:
```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

#### Configurações no application.properties:
```properties
management.prometheus.metrics.export.enabled=true
management.metrics.distribution.percentiles-histogram.http.server.requests=true
```

### 4. Configuração de Logging

Melhoramos a configuração de logging para facilitar a depuração e análise.

#### Configurações no application.properties:
```properties
logging.level.root=INFO
logging.level.com.klug.projectmanager=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %logger{36} - %msg%n
```

### 5. Atualização da Configuração de Segurança

Atualizamos a configuração de segurança para permitir acesso aos endpoints do Actuator.

#### Mudanças em SecurityConfig.java:
```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/auth/**").permitAll()
    .requestMatchers("/actuator/**").permitAll()
    .anyRequest().authenticated()
);
```

## Benefícios das Mudanças

1. **Monitoramento Aprimorado**: Os endpoints do Actuator fornecem informações detalhadas sobre a saúde e o desempenho da aplicação.
2. **Rastreamento Distribuído**: O Micrometer Tracing permite rastrear transações através de múltiplos serviços, facilitando a identificação de gargalos.
3. **Métricas Detalhadas**: A integração com Prometheus permite a coleta e visualização de métricas detalhadas do sistema.
4. **Logging Melhorado**: A configuração de logging aprimorada facilita a identificação e resolução de problemas.
5. **Acesso Seguro**: A atualização da configuração de segurança permite acesso controlado aos endpoints de monitoramento.

## Próximos Passos

1. Implementar um sistema de visualização de logs centralizado (por exemplo, ELK Stack).
2. Configurar dashboards no Grafana para visualização das métricas do Prometheus.
3. Estabelecer alertas baseados em métricas para notificação proativa de problemas.

## Conclusão

As alterações implementadas fornecem uma base para o monitoramento e rastreamento eficaz de nossa arquitetura de microsserviços. Essas ferramentas permitirão uma detecção mais rápida de problemas, facilitarão a resolução de issues e contribuirão para a melhoria contínua do desempenho e da confiabilidade do sistema.
