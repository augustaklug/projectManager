# Project Manager

## Descrição

Este é um sistema de gerenciamento de projetos desenvolvido com Spring Boot no back-end e React/Next.js no front-end. O sistema permite a criação e gerenciamento de projetos, tarefas, notas, e comunicação em tempo real entre os membros da equipe.

## Funcionalidades

- Criação e gerenciamento de projetos
- Atribuição de tarefas a membros da equipe
- Definição de prioridades e dependências entre tarefas
- Exportação de relatórios de progresso do projeto
- Adição de notas a projetos e tarefas
- Ferramenta de chat em tempo real
- Definição de horas de disponibilidade dos membros da equipe
- Visualização da carga de trabalho da equipe
- Atribuição de setores aos usuários e projetos
- Visualização de marcos e status do projeto

## Pacotes do Projeto

### Back-end (Spring Boot)

- **Spring Boot Starter Web**: Para criar APIs RESTful.
- **Spring Boot Starter Security**: Para configurar a segurança da aplicação.
- **Spring Boot Starter Data JPA**: Para interagir com o banco de dados usando JPA.
- **MySQL Connector**: Driver JDBC para conectar ao banco de dados MySQL.
- **JWT Library (jjwt)**: Para criar e validar tokens JWT.
- **Lombok**: Para reduzir a verbosidade do código, gerando automaticamente getters, setters, e outros métodos comuns.
- **ModelMapper**: Para mapeamento entre entidades e DTOs.
- **Spring Boot DevTools**: Ferramentas de desenvolvimento para Spring Boot.
- **Spring Boot Starter Test**: Ferramentas para testes unitários e de integração.
- **Spring Security Test**: Ferramentas para testes de segurança.

### Front-end (React/Next.js)

- **React**: Biblioteca para construção de interfaces de usuário.
- **Next.js**: Framework para renderização do lado do servidor e geração de sites estáticos.
- **Axios**: Cliente HTTP para fazer requisições ao back-end.

## Princípios SOLID Empregados

- **Single Responsibility Principle (SRP)**: Cada classe tem uma única responsabilidade. Por exemplo, as classes de serviço (`ProjectService`, `TaskService`, `UserService`, `ChatService`) são responsáveis apenas pela lógica de negócios.
- **Open/Closed Principle (OCP)**: O código é aberto para extensão, mas fechado para modificação. Por exemplo, novos métodos podem ser adicionados aos serviços sem modificar os existentes.
- **Liskov Substitution Principle (LSP)**: As subclasses podem ser substituídas por suas superclasses sem alterar o comportamento do programa. Por exemplo, a classe `UserDetailsServiceImpl` implementa a interface `UserDetailsService`.
- **Interface Segregation Principle (ISP)**: Interfaces específicas são criadas para diferentes funcionalidades. Por exemplo, os repositórios JPA (`ProjectRepository`, `TaskRepository`, `UserRepository`, `ChatMessageRepository`) seguem este princípio.
- **Dependency Inversion Principle (DIP)**: As classes de alto nível não dependem de classes de baixo nível, mas de abstrações. Por exemplo, os serviços dependem de interfaces de repositório.

## Design Patterns Usados

- **Service Layer Pattern**: As classes de serviço (`ProjectService`, `TaskService`, `UserService`, `ChatService`) encapsulam a lógica de negócios e interagem com os repositórios.
- **Repository Pattern**: As interfaces de repositório (`ProjectRepository`, `TaskRepository`, `UserRepository`, `ChatMessageRepository`) fornecem uma abstração para a camada de acesso a dados.
- **DTO (Data Transfer Object) Pattern**: Os DTOs (`ProjectDTO`, `TaskDTO`, `UserDTO`, `ChatMessageDTO`) são usados para transferir dados entre as camadas de apresentação e de serviço.
- **Singleton Pattern**: O `JwtTokenProvider` é um exemplo de singleton, garantindo que apenas uma instância da classe seja criada.
- **Factory Pattern**: Pode ser usado para criar instâncias de entidades ou DTOs, embora não esteja explicitamente implementado neste projeto.
