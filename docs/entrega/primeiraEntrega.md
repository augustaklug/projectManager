# Primeira Entrega: Construção de um Monólito Simples com Spring Boot

## Objetivo da Etapa

O objetivo desta primeira etapa foi desenvolver uma aplicação monolítica simples utilizando Spring Boot, estabelecendo a base para 
futuras evoluções em direção a uma arquitetura de microsserviços.

## Implementação e Subcompetências Desenvolvidas

### 1. **Implementação de uma Aplicação com Spring Boot**
   - Estrutura do Projeto:
     ```
     src/
     ├── main/
     │   ├── java/
     │   │   └── com/
     │   │       └── klug/
     │   │           └── projectmanager/
     │   │               ├── controller/
     │   │               ├── service/
     │   │               ├── repository/
     │   │               ├── model/
     │   │               └── ProjectManagerApplication.java
     │   └── resources/
     │       └── application.properties
     └── test/
         └── java/
             └── com/
                 └── klug/
                     └── projectmanager/
     ```
   - Utilizamos a estrutura padrão do Spring Boot, com pacotes separados para controllers, services, repositories e models.
   - A classe `ProjectManagerApplication` foi configurada como ponto de entrada da aplicação, aproveitando a autoconfiguração do Spring Boot.

### 2. **Gerenciamento de Dependências**
   - Utilizamos o Maven para gerenciamento de dependências.
   - Principais dependências incluídas no `pom.xml`:
     - `spring-boot-starter-web`: Para criar APIs REST.
     - `spring-boot-starter-data-jpa`: Para persistência de dados.
     - `mysql-connector-java`: Para conexão com o banco de dados MySQL.
     - `spring-boot-starter-security`: Para implementação de segurança básica.

### 3. **Desenvolvimento de APIs REST**
   - Implementamos controllers RESTful para as entidades principais:
     - `ProjectController`: Gerenciamento de projetos.
     - `TaskController`: Gerenciamento de tarefas.
     - `UserController`: Gerenciamento de usuários.

### 4. **Práticas de Código Limpo e Manutenção**
   - Aplicamos princípios SOLID:
     - Single Responsibility Principle: Cada classe tem uma única responsabilidade.
     - Open/Closed Principle: Utilizamos interfaces para permitir extensões sem modificar o código existente.
     - Liskov Substitution Principle: Garantimos que as subclasses possam ser usadas no lugar das classes base.
     - Interface Segregation Principle: Criamos interfaces específicas para diferentes funcionalidades.
     - Dependency Inversion Principle: Dependemos de abstrações, não de implementações concretas.
   - Utilizamos padrões de design como Repository e Service Layer.

### 5. **Modelagem de Domínios**
   - Aplicamos conceitos de DDD na modelagem das entidades principais:
     - `Project`: Representa um projeto com atributos como nome, data de início, data de término e lista de tarefas.
     - `Task`: Representa uma tarefa dentro de um projeto, com atributos como descrição, status e prazo.
     - `User`: Representa um usuário do sistema, com atributos como nome, email e role.

### 6. **Digramas do Projeto**

#### **Diagrama de classes**
![pLHDRy8m3BttLrWSaK3Zoc52YCCcJMWIi7je36IaIHMdGsZYly_zeaa5cZXjfzREy_Dpx7X6NZXVP3gXepBWB66If5eGmRkpdvXw-4u0Hc_Mx479ydVbdMB3Y0pharJeXV085LNv5xlHoDOvjrQZCA1eWHezoYlr5pfIrYINDljQqF6tr8qfaLAdSb_6Hvab2gcLgR0btyeBNr2fapyFwj](https://github.com/user-attachments/assets/1ac7daaf-6746-4acc-84fa-225ecf1c77e1)
#### **Diagrama de componentes**

![TLF1Jjj04BtxAxOv6IaY5uu8GPBILWg9YHrLdApnUg1Bujsmiui8LJ-cue24r2DVu1zZDd4LOu4l-sRdxPkPD_8fER3ELwleYz9ovKiKnwEZmUWe7vbgRJHgTn95axEhsIBBircIZsS_f_5152LeoUZO4zg-w2t8rt-jCZtEZ8nse3JQ-DeoH5qouRo1FSPp1Ea6KtnmXxSKBdvmobw3H5](https://github.com/user-attachments/assets/5c366c96-b269-478b-abde-fe9f9289431f)

#### **Diagrama de sequência: Autenticação de usuário**

![LP31JW8n48RlVOh9dlH0k7CmI1QC9aJYebwy3Es04s1AfbtZwnWFFCY-cBFL3V7M_j_yV__ED6NKt1uFBkrPG6CYXpv7XUT4wau6sVC99KErq2YP95I02HRp_x0X_U3k71EibALNERt9rMsRtmloT5ryoyO5pBZ1H53LVwT2wfbp_QCmkh5qcC2zs8t0AmMoH0jqfXjTDaPNZyqQncZvuy](https://github.com/user-attachments/assets/60be97ac-cbd9-40d1-90bd-0735e4679166)

#### **Diagrama de sequência: Criar Nota em Projeto**

![RPA_JiCm4CPtFyKf4mnjTWPKzP_4q90siB5StACoD7OuEnKIucaO-Y1zCSvk2YrYiMn_t-_kqzaZ7v13rsoLVpKMs7bIg8DZUFJ4gXNHQDEY3P3DsTb0Tfq1UfZFBiMAU6SEU-UXP6FZhV5Fzgfazq8w95NJTKBBIhrrr15aAwnh4nRBTBrQB2zB5YvG3oP3KQar1gpH2phjTqcPZfMAUM](https://github.com/user-attachments/assets/64dfca58-2877-488f-9d90-9046b33e8572)

### 7. **Implementação das User Stories previstas**

O front-end implementa completamente ou parcialmente 10 das 24 user stories, focando nas funcionalidades básicas de
gerenciamento de projetos e tarefas. As 14 user stories restantes, que incluem funcionalidades mais avançadas como relatórios detalhados, 
gerenciamento de tempo e integrações, ainda não foram implementadas 

| # | User Story | Status | Evidência/Observações |
|---|------------|--------|------------------------|
| 1 | Criação de Novos Projetos | 🟩 | `CreateProject.tsx` permite criação. |
| 2 | Visualização de Tarefas Atribuídas | 🟩 | `TaskList.tsx` exibe tarefas atribuídas ao usuário. |
| 3 | Atribuição de Tarefas | 🟨 | `CreateTaskForm.tsx` permite atribuição. Notificações não implementadas. |
| 4 | Definição de Prioridades para Tarefas | 🟥 |  |
| 5 | Exportação de Relatórios de Progresso | 🟥 |  |
| 6 | Adição de Notas aos Projetos | 🟩 | `NoteForm.tsx` e `NoteList.tsx` implementam funcionalidade. |
| 7 | Adição de Notas às Tarefas | 🟥 |  |
| 8 | Definição de Dependências entre Tarefas | 🟥 | |
| 9 | Reatribuição de Tarefas | 🟨 | `TaskDetails.tsx` permite atualização de status, mas não reatribuição específica. |
| 10 | Criação de Marcos no Projeto | 🟥 | Sem evidência de implementação. |
| 11 | Visualização de Dashboard do Projeto | 🟩 | `Dashboard.tsx` fornece visão geral de projetos e tarefas. |
| 12 | Arquivamento de Projetos Concluídos | 🟥 | |
| 13 | Reporte de Problemas nas Tarefas | 🟥 |  |
| 14 | Visualização de Histórico de Alterações | 🟥 | |
| 15 | Definição de Permissões de Acesso | 🟨 | Sistema de autenticação implementado, sem controle granular visível. |
| 16 | Configuração de Notificações | 🟥 | |
| 17 | Acompanhamento de Tempo Gasto nas Tarefas | 🟥 |  |
| 18 | Integração com Ferramentas de Terceiros | 🟥 |  |
| 19 | Visualização de Relatórios de Desempenho | 🟨 | `Dashboard.tsx` fornece métricas básicas, sem relatórios detalhados. |
| 20 | Visualização de Marcos na Barra de Progresso | 🟨 | `ProjectDetails.tsx` mostra barra de progresso, sem marcos específicos. |
| 21 | Visualização do Status do Projeto | 🟩 | Status mostrado em `ProjectList.tsx` e `ProjectDetails.tsx`. |
| 22 | Comparação de Pontos vs Tempo Real Gasto | 🟥 |  |
| 23 | Atribuição de Setores a Usuários e Projetos | 🟥 | |
| 24 | Visualização de Relatório Financeiro | 🟥 |  |

Legenda:
- 🟩 Implementado
- 🟨 Parcialmente implementado
- 🟥 Não implementado
