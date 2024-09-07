# Relatório de Entrega Parcial: Criação de Microsserviço

## 1. Introdução

Este relatório documenta a terceira entrega (TP3) do projeto, que consiste na criação e integração de um novo microsserviço, o NoteService, 
e na implementação do EurekaServer para gerenciamento de serviços. Esta etapa demonstra a competência em separação de responsabilidades, 
comunicação entre serviços e modularização da arquitetura.

## 2. Microsserviço Implementado: NoteService

### 2.1 Objetivo
O NoteService foi criado para gerenciar notas associadas a projetos e tarefas, permitindo aos usuários adicionar, 
visualizar, atualizar e excluir notas de forma independente do serviço principal.

### 2.2 Estrutura
- **Pacote:** `com.klug.noteservice`
- **Principais Componentes:**
  - `NoteController`: Gerencia os endpoints da API REST para operações com notas.
  - `NoteService`: Contém a lógica de negócios para manipulação de notas.
  - `NoteRepository`: Interface para interação com o banco de dados.
  - `Note`: Entidade que representa uma nota no sistema.

### 2.3 Endpoints da API REST
- `POST /api/notes`: Cria uma nova nota
- `GET /api/notes/{id}`: Recupera uma nota específica
- `PUT /api/notes/{id}`: Atualiza uma nota existente
- `DELETE /api/notes/{id}`: Exclui uma nota
- `GET /api/notes/task/{taskId}`: Recupera todas as notas associadas a uma tarefa
- `GET /api/notes/project/{projectId}`: Recupera todas as notas associadas a um projeto

## 3. Implementação do EurekaServer

### 3.1 Objetivo
O EurekaServer foi implementado para facilitar o registro e descoberta de serviços, permitindo uma comunicação eficiente entre os microsserviços.

### 3.2 Estrutura
- **Pacote:** `com.klug.eurekaserver`
- **Componente Principal:** `EurekaServerApplication`

### 3.3 Configuração
- Porta padrão: 8761
- Configurado para não se registrar como um cliente

## 4. Integração com o Sistema Principal

### 4.1 Comunicação entre Serviços
- Utilização do Feign Client no serviço principal para comunicação com o NoteService.
- Implementação de `NoteClient` para chamadas de API ao NoteService.

### 4.2 Atualização do Modelo de Domínio
- Adição de relacionamentos entre notas e projetos/tarefas no modelo de domínio.

## 5. Desenvolvimento Front-End

### 5.1 Novos Componentes
- `NoteList`: Exibe as notas associadas a um projeto ou tarefa.
- `NoteForm`: Permite a criação e edição de notas.

### 5.2 Integração na Interface do Usuário
- Adição de seções de notas nas páginas de detalhes de projetos e tarefas.

## 6. Testes

### 6.1 Testes Unitários
- Implementação de testes para `NoteService` e `NoteController`.

### 6.2 Testes de Integração
- Testes para verificar a comunicação entre o serviço principal e o NoteService.

## 7. Conclusão

A implementação do NoteService como um microsserviço independente e a integração do EurekaServer demonstram a capacidade de modularizar 
a aplicação e gerenciar serviços distribuídos. Esta abordagem melhora a escalabilidade e a manutenibilidade do sistema, permitindo o 
desenvolvimento e implantação independentes de diferentes funcionalidades.

---
