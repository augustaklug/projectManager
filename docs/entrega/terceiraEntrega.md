# Terceira Entrega: Criação de um Microsserviço

## Objetivo da Etapa

O objetivo desta terceira etapa foi desenvolver e integrar um microsserviço ao sistema de gerenciamento de projetos, demonstrando competência em separação de responsabilidades, comunicação entre serviços e modularização da arquitetura. Esta implementação visa melhorar a escalabilidade, manutenibilidade e flexibilidade do sistema.

## Implementação e Subcompetências Desenvolvidas

### 1. Atualização do Modelo de Domínio

- **Incorporação do NoteService**: Um novo serviço foi adicionado ao modelo de domínio para gerenciar notas associadas a projetos e tarefas.
- **Mudanças na Arquitetura**: A arquitetura foi atualizada para incluir o NoteService como um microsserviço independente.
- **Novas Dependências**: Foram estabelecidas relações entre o NoteService e os serviços existentes de projetos e tarefas.

### 2. Criação de Endpoints da API REST

- Implementação de novos endpoints no NoteService:
  - `POST /api/notes`: Criação de novas notas
  - `GET /api/notes/{id}`: Recuperação de notas específicas
  - `PUT /api/notes/{id}`: Atualização de notas existentes
  - `DELETE /api/notes/{id}`: Exclusão de notas
  - `GET /api/notes/task/{taskId}`: Recuperação de notas por tarefa
  - `GET /api/notes/project/{projectId}`: Recuperação de notas por projeto

### 3. Implementação de Microsserviço

- **Uso do Spring Boot**: O NoteService foi desenvolvido como uma aplicação Spring Boot independente.
- **Aplicação do Spring Cloud**: Implementação do EurekaServer para facilitar o registro e descoberta de serviços.
- **Configuração do EurekaServer**:
  - Porta padrão: 8761
  - Configurado para não se registrar como um cliente

### 4. Desenvolvimento de Repositórios

- Criação do `NoteRepository`: Interface para interação com o banco de dados específico do NoteService.
- Implementação de métodos personalizados para consultas específicas de notas.

### 5. Desenvolvimento de Componentes Front-End

- Criação do componente `NoteList`: Exibe as notas associadas a um projeto ou tarefa.
- Implementação do `NoteForm`: Permite a criação e edição de notas.
- Integração desses componentes nas páginas de detalhes de projetos e tarefas.

### 6. Atualização e Criação de Testes

- **Testes Unitários**: Implementação de testes para `NoteService`.
- **Testes de Integração**: Desenvolvimento de testes para verificar a comunicação entre o serviço principal e o NoteService.
