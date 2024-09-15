# Relatório Final: Projeto de Gerenciamento de Projetos

## Introdução

Este relatório apresenta o desenvolvimento de um sistema de gerenciamento de projetos, realizado para o bloco "Engenharia de Softwares Escaláveis". 
O projeto teve como objetivo desenvolver habilidades práticas em arquitetura de software, com foco na criação de soluções baseadas em microsserviços 
utilizando o Framework Spring.

## Visão Geral do Projeto

O sistema de gerenciamento de projetos foi desenvolvido para permitir a criação, atribuição e acompanhamento de projetos e tarefas. 
Iniciado como um monólito simples, o sistema evoluiu para uma arquitetura de microsserviços, incorporando princípios de design como 
responsabilidade única e baixo acoplamento, dentro do contexto de domain-driven design (DDD).

### Tecnologias Principais

- Backend: Spring Boot, Spring Data JPA, Spring Cloud
- Frontend: React com Next.js
- Banco de Dados: MySQL
- Mensageria: RabbitMQ
- Containerização: Docker e Kubernetes
- Monitoramento: Spring Boot Actuator, Zipkin

## Fases de Desenvolvimento

### 1. Monólito Inicial com Spring Boot

Nesta fase, desenvolvemos uma aplicação Spring Boot seguindo um design em camadas:

- **Controle**: Implementação de APIs REST utilizando Spring MVC.
- **Serviço**: Lógica de negócios e operações.
- **Repositório**: Acesso a dados utilizando Spring Data JPA.

Princípios SOLID e padrões de design foram aplicados para garantir um código limpo e manutenível. 
A modelagem inicial do domínio foi realizada considerando conceitos de DDD.

### 2. Desenvolvimento da Camada de Persistência

A camada de persistência foi expandida utilizando anotações JPA e repositórios Spring Data:

- Modelagem de dados considerando requisitos de consulta e isolamento de domínio.
- Utilização de anotações JPA para mapeamento objeto-relacional.
- Criação de repositórios Spring Data para otimizar o acesso a dados.
- Implementação de funcionalidades para registro e consulta de histórico de mudanças.

### 3. Introdução de Microsserviços

Um novo microsserviço (NoteService) foi incorporado à aplicação existente:

- Implementação de endpoints REST para o novo serviço.
- Utilização de Spring Cloud para comunicação entre serviços.
- Expansão da cobertura de testes para incluir o novo microsserviço.
- Atualização da interface do usuário para interagir com o novo serviço.

### 4. Refatoração para Arquitetura Orientada a Eventos

O sistema foi refatorado para uma arquitetura orientada a eventos:

- Implementação de padrões de arquitetura orientada a eventos usando RabbitMQ.
- Utilização de abstrações do Spring Boot para simplificar a integração.
- Documentação da nova arquitetura, incluindo diagramas de componentes e sequência.

### 5. Preparação para Operação em Produção

Preparação do sistema para operação em ambiente de produção:

- Containerização dos microsserviços utilizando Docker.
- Orquestração de containers com Kubernetes.
- Configuração de ferramentas de monitoramento e rastreamento (Spring Boot Actuator, Zipkin).
- Desenvolvimento e aplicação de testes.
- Implementação de CI/CD com GitHub Actions.

## Detalhamento das Entregas
1. [[Primeira Entrega](https://github.com/augustaklug/projectManager/blob/5edba5f65d9f7243b448e74079a4b3efc49cb949/docs/entrega/primeiraEntrega.md)]
2. [[Segunda Entrega](https://github.com/augustaklug/projectManager)]
3. [[Terceira Entrega](https://github.com/augustaklug/projectManager)]
4. [[Quarta Entrega](https://github.com/augustaklug/projectManager)]
5. [[Quinta Entrega](https://github.com/augustaklug/projectManager)]

## Arquitetura Final

A arquitetura final do sistema consiste em:

1. **Serviço Principal**: Gerencia projetos, tarefas e usuários.
2. **NoteService**: Microsserviço dedicado ao gerenciamento de notas.
3. **EurekaServer**: Servidor de descoberta de serviços.
4. **RabbitMQ**: Message broker para comunicação assíncrona entre serviços.
5. **MySQL**: Banco de dados para persistência.
6. **Frontend React/Next.js**: Interface do usuário.

## Conclusão e Próximos Passos

O projeto alcançou seu objetivo principal de criar um sistema de gerenciamento de projetos escalável baseado em microsserviços. As principais funcionalidades foram implementadas tanto no backend quanto no frontend, proporcionando uma base sólida para futuras expansões.

Próximos passos incluem:
1. Implementação das funcionalidades pendentes na interface do usuário.
2. Melhoria na cobertura de testes, especialmente testes de integração e end-to-end.
3. Refinamento do sistema de monitoramento e alerta para operação em produção.
4. Implementação de funcionalidades avançadas como análise de dados e integrações com ferramentas externas.

O código-fonte completo do projeto está disponível no repositório Git: [[augustaklug/projectManager](https://github.com/augustaklug/projectManager)]
