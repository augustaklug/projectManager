# Diagramas do projeto
## Diagrama de classes
<img src="/docs/TP1-Class Diagram.png" alt="Diagrama de Classes">

## Diagrama de componentes
<img src="/docs/TP1-Component Diagram.png" alt="Diagrama de Componentes">


* ● Presentation Layer: Contém os controladores que lidam com as requisições HTTP.
  * ```AuthController, ProjectController, TaskController, UserController, ChatController, NoteController```
* ● Business Layer: Contém os serviços que implementam a lógica de negócios.
  * ```AuthService, ProjectService, TaskService, UserService, ChatService, NoteService  ```
* ● Persistence Layer: Contém os repositórios que interagem com o banco de dados.
  * ```UserRepository, ProjectRepository, TaskRepository, ChatMessageRepository, NoteRepository```
* ● Security Layer: Contém os componentes de segurança.
  * ```JwtTokenProvider, JwtAuthenticationFilter, JwtAuthenticationEntryPoint, UserDetailsServiceImpl```

## Diagramas de sequência
### Autenticação de usuário
<img src="/docs/TP1-Sequence Auth.png" alt="Autenticação de usuário">

* ● Ator: Usuário
* ● Objetivo: Autenticar o usuário e gerar um token JWT
* ● Fluxo:
  * ● O usuário envia uma requisição de autenticação (login) para o controlador de autenticação.
  * ● O controlador de autenticação (```AuthController```) recebe a requisição e a encaminha para o serviço de autenticação (```AuthService```).
  * ● O serviço de autenticação (```AuthService```) usa o gerenciador de autenticação (```AuthenticationManager```) para autenticar o usuário.
  * ● O gerenciador de autenticação autentica o usuário e retorna um objeto de autenticação.
  * ● O serviço de autenticação (```AuthService```) usa o provedor de tokens JWT (```JwtTokenProvider```) para gerar um token JWT.
  * ● O provedor de tokens JWT gera o token e o retorna para o serviço de autenticação.
  * ● O serviço de autenticação retorna o token JWT para o controlador de autenticação.
  * ● O controlador de autenticação retorna o token JWT para o usuário.

### Criar nota em tarefa
<img src="/docs/TP1-Sequence Note for Task.png" alt="Criar nota em tarefa">

* ● Ator: Usuário (Gerente de Projeto ou Membro da Equipe)
* ● Objetivo: Adicionar uma nova nota a uma tarefa existente
* ● Fluxo:
  * ● O usuário envia uma requisição para adicionar uma nova nota a uma tarefa.
  * ● O controlador (```NoteController```) recebe a requisição e a encaminha para o serviço (```NoteService```).
  * ● O serviço (```NoteService```) processa a requisição e interage com o repositório (```NoteRepository```) para salvar a nova nota.
  * ● O repositório (```NoteRepository```) salva a nota no banco de dados.
  * ● O serviço retorna a nota salva para o controlador.
  * ● O controlador retorna a resposta para o usuário.
