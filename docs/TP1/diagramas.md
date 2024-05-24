# Diagramas do projeto
## Diagrama de classes
![image](https://github.com/augustaklug/projectManager/assets/56510700/75308d2a-76d1-45ee-b3b8-87dc531c38b5)

## Diagrama de componentes

![image](https://github.com/augustaklug/projectManager/assets/56510700/c38c38d6-6af3-4f06-b6bb-2bd880cc5dd4)


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
![image](https://github.com/augustaklug/projectManager/assets/56510700/c763b5c3-ac53-4d11-a61e-775e6a7623b7)


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

![image](https://github.com/augustaklug/projectManager/assets/56510700/e4f068aa-1f32-468a-a9ce-1cc8b355f6e3)


* ● Ator: Usuário (Gerente de Projeto ou Membro da Equipe)
* ● Objetivo: Adicionar uma nova nota a uma tarefa existente
* ● Fluxo:
  * ● O usuário envia uma requisição para adicionar uma nova nota a uma tarefa.
  * ● O controlador (```NoteController```) recebe a requisição e a encaminha para o serviço (```NoteService```).
  * ● O serviço (```NoteService```) processa a requisição e interage com o repositório (```NoteRepository```) para salvar a nova nota.
  * ● O repositório (```NoteRepository```) salva a nota no banco de dados.
  * ● O serviço retorna a nota salva para o controlador.
  * ● O controlador retorna a resposta para o usuário.
