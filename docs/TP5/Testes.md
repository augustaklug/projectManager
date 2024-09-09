# Relatório sobre os Testes Atualizados

Este relatório apresenta uma visão geral dos testes criados e atualizados para o sistema de gerenciamento de projetos.

## 1. Testes Unitários

### 1.1. AuthServiceTest

O AuthServiceTest inclui dois casos de teste:

a) `testRegisterUser`: Verifica se um novo usuário é registrado corretamente no sistema.

b) `testRegisterUserWithExistingUsername`: Assegura que uma exceção é lançada ao tentar registrar um usuário com um nome de usuário já existente.

Estes testes garantem o funcionamento adequado do processo de registro de usuários e a validação de nomes de usuário únicos.

### 1.2. ProjectServiceTest

O ProjectServiceTest foi expandido e agora inclui os seguintes casos de teste:

a) `createProject_ShouldCreateProjectAndHistory`: Verifica a criação de um novo projeto e seu histórico.

b) `updateProject_ShouldUpdateProjectAndCreateHistory`: Testa a atualização de um projeto existente e a criação do histórico correspondente.

c) `updateProject_ProjectNotFound_ShouldThrowException`: Assegura que uma exceção é lançada ao tentar atualizar um projeto inexistente.

d) `getProjectHistory_ShouldReturnHistoryList`: Verifica a recuperação do histórico de um projeto.

e) `getProjectsByUser_ShouldReturnUserProjects`: Testa a recuperação de projetos associados a um usuário específico.

f) `deleteProject_ShouldSoftDeleteProject`: Verifica a exclusão lógica (soft delete) de um projeto.

Estes testes cobrem as principais operações CRUD do serviço de projetos, incluindo o tratamento de erros e a funcionalidade de histórico.

### 1.3. TaskServiceTest

O TaskServiceTest foi atualizado com os seguintes casos de teste:

a) `createTask_ShouldCreateTaskAndHistory`: Verifica a criação de uma nova tarefa e seu histórico.

b) `updateTask_ShouldUpdateTaskAndCreateHistory`: Testa a atualização de uma tarefa existente, incluindo a atribuição a um usuário.

c) `updateTask_TaskNotFound_ShouldThrowException`: Assegura que uma exceção é lançada ao tentar atualizar uma tarefa inexistente.

d) `getTaskHistory_ShouldReturnHistoryList`: Verifica a recuperação do histórico de uma tarefa.

e) `getTasksByUser_ShouldReturnUserTasks`: Testa a recuperação de tarefas associadas a um usuário específico.

f) `deleteTask_ShouldSoftDeleteTask`: Verifica a exclusão lógica (soft delete) de uma tarefa.

Estes testes abrangem as principais operações do serviço de tarefas, incluindo o tratamento de erros e a funcionalidade de histórico.

### 1.4. UserServiceTest

O UserServiceTest foi expandido e agora inclui os seguintes casos de teste:

a) `createUser_ShouldCreateUserAndHistory`: Verifica a criação de um novo usuário e seu histórico.

b) `updateUser_ShouldUpdateUserAndCreateHistory`: Testa a atualização de um usuário existente e a criação do histórico correspondente.

c) `updateUser_UserNotFound_ShouldThrowException`: Assegura que uma exceção é lançada ao tentar atualizar um usuário inexistente.

d) `getUserHistory_ShouldReturnHistoryList`: Verifica a recuperação do histórico de um usuário.

e) `deleteUserById_ShouldSoftDeleteUser`: Testa a exclusão lógica (soft delete) de um usuário.

f) `getUserByUsername_ShouldReturnUser`: Verifica a recuperação de um usuário pelo nome de usuário.

g) `getUserByUsername_UserNotFound_ShouldThrowException`: Assegura que uma exceção é lançada ao tentar recuperar um usuário inexistente pelo nome de usuário.

Estes testes cobrem as principais operações CRUD do serviço de usuários, incluindo o tratamento de erros e a funcionalidade de histórico.

## 2. Testes de Integração

Além dos testes unitários, foram implementados testes de integração para verificar o funcionamento correto dos controllers e suas interações com outras camadas do sistema. Estes testes simulam requisições HTTP e verificam as respostas, garantindo que a API funcione conforme esperado.

### 1. AuthControllerIntegrationTest

Este teste verifica o fluxo completo de autenticação e registro de usuários:

a) `testRegisterAndAuthenticateUser`: 
   - Registra um novo usuário com um nome de usuário único.
   - Autentica o usuário recém-criado.
   - Verifica se um token JWT é retornado após a autenticação bem-sucedida.

Este teste garante que o processo de registro e autenticação está funcionando corretamente em um ambiente integrado.

### 2. TaskControllerIntegrationTest

Este teste cobre as operações CRUD para tarefas e a atribuição de tarefas a usuários:

a) `testCreateUpdateDeleteAndRetrieveTask`:
   - Cria uma nova tarefa.
   - Atualiza a tarefa criada.
   - Recupera a tarefa atualizada.
   - Deleta a tarefa.
   - Verifica se a tarefa foi realmente deletada.

b) `testAssignTaskToUser`:
   - Cria uma nova tarefa.
   - Atribui a tarefa a um usuário específico.
   - Verifica se a atribuição foi feita corretamente.

Estes testes asseguram que todas as operações principais relacionadas às tarefas estão funcionando corretamente, incluindo a interação com usuários.

### 3. UserControllerIntegrationTest

Este teste verifica as operações CRUD para usuários:

a) `testCreateUpdateDeleteAndRetrieveUser`:
   - Cria um novo usuário com um nome de usuário único.
   - Atualiza as informações do usuário.
   - Recupera o usuário atualizado.
   - Deleta o usuário.
   - Verifica se o usuário foi realmente deletado.

Este teste garante que todas as operações principais relacionadas aos usuários estão funcionando corretamente no contexto da API.

## Conclusão

A adição destes testes de integração complementa significativamente a suite de testes do sistema. Enquanto os testes unitários focam no comportamento isolado de componentes individuais, os testes de integração verificam se esses componentes trabalham corretamente juntos no contexto da API REST.

Principais benefícios dos testes de integração adicionados:

1. Validação do fluxo completo de autenticação e autorização.
2. Verificação das operações CRUD através da API para tarefas e usuários.
3. Teste da integração entre diferentes camadas do sistema (controllers, services, repositories).
4. Simulação de cenários reais de uso da API.

A combinação de testes unitários e de integração proporciona uma cobertura abrangente, aumentando a confiabilidade do sistema e facilitando a detecção precoce de problemas potenciais. Esta abordagem de testes robusta suporta o desenvolvimento contínuo e a manutenção do sistema de gerenciamento de projetos, assegurando que novas funcionalidades ou mudanças não quebrem os fluxos existentes.
