# Quinta Entrega: Implantação e Manutenção em Produção

## Objetivo da Etapa

O objetivo desta etapa final foi preparar o sistema desenvolvido para operação em um ambiente de produção, focando na conteinerização, orquestração, monitoramento, automação de processos de integração e entrega contínua, e implementação de testes abrangentes. Esta fase visa garantir a robustez, escalabilidade e manutenibilidade do sistema em um cenário de produção real.

## Implementação e Subcompetências Desenvolvidas

### 1. Implantação com Docker e Kubernetes

- **[[Conteinerização com Docker](https://github.com/augustaklug/projectManager/blob/8cca1921eef9d1b15856224f358f63b9f035f8ea/docs/TP5/Docker%20e%20Docker%20Compose.md)]**: Cada componente do sistema (backend, frontend, note-service, Eureka Server) foi containerizado utilizando Dockerfiles específicos. Isso garante consistência entre ambientes de desenvolvimento e produção, além de facilitar a implantação e escalabilidade.

- **[[Orquestração com Kubernetes](https://github.com/augustaklug/projectManager/blob/8cca1921eef9d1b15856224f358f63b9f035f8ea/docs/TP5/Kubernetes.md)]**: Implementamos manifestos Kubernetes para cada serviço, permitindo a orquestração eficiente dos containers. Isso inclui configurações para deployments, services, configmaps e persistent volume claims, proporcionando uma infraestrutura altamente escalável e resiliente.

### 2. [[Monitoramento de Microsserviços](https://github.com/augustaklug/projectManager/blob/8cca1921eef9d1b15856224f358f63b9f035f8ea/docs/TP5/Monitoramento%20e%20Rastreamento.md)]

- **Spring Boot Actuator**: Implementado para fornecer endpoints de monitoramento e gerenciamento, facilitando a integração com sistemas de monitoramento externos.

- **Rastreamento Distribuído**: Utilizamos Micrometer Tracing e Zipkin para implementar o rastreamento distribuído, permitindo a visualização do fluxo de requisições através dos múltiplos serviços e a análise de latência.

- **Métricas com Prometheus**: Configuramos a exportação de métricas no formato Prometheus, possibilitando uma coleta detalhada de dados de performance do sistema.

- **Logging Aprimorado**: Melhoramos a configuração de logging para facilitar a depuração e análise, com níveis de log adequados e formatação consistente.

### 3. Gestão de Configuração e Versionamento

- **Uso do Git e GitHub**: Todo o código fonte e configurações do projeto foram versionados utilizando Git e hospedados no GitHub, permitindo um controle eficaz de versões e colaboração entre a equipe de desenvolvimento.

### 4. [[Automação com GitHub Actions](https://github.com/augustaklug/projectManager/blob/8cca1921eef9d1b15856224f358f63b9f035f8ea/docs/TP5/CI-CD%20com%20GitHub%20Actions%20e%20Docker.md)]

- **CI/CD Pipeline**: Implementamos dois workflows principais no GitHub Actions:
  1. "Push to Docker Hub": Automatiza a construção e publicação de imagens Docker para todos os serviços do projeto.
  2. "Deploy to Oracle Cloud": Gerencia a implantação automática das imagens atualizadas na infraestrutura da Oracle Cloud.

- Estes workflows garantem que cada push para a branch master resulte em uma série de testes, builds e, se bem-sucedidos, na implantação automática em produção.

### 5. [[Testes Abrangentes](https://github.com/augustaklug/projectManager/blob/8cca1921eef9d1b15856224f358f63b9f035f8ea/docs/TP5/Testes.md)]

- **Testes Unitários**: Expandimos os testes unitários para cobrir serviços críticos como AuthService, ProjectService, TaskService e UserService, garantindo o funcionamento correto de operações CRUD, validações e lógica de negócios.

- **Testes de Integração**: Implementamos testes de integração para verificar o funcionamento correto dos controllers e suas interações com outras camadas do sistema, simulando requisições HTTP e verificando as respostas da API.
