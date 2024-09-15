# Segunda Entrega: Desenvolver uma Camada de Persistência Real

## Objetivo da Etapa

O objetivo desta segunda etapa foi implementar uma camada de persistência avançada que não apenas suporta operações básicas de CRUD, mas também introduz funcionalidades avançadas como histórico de dados, utilizando as capacidades do JPA e dos Repositórios Spring Data. Esta implementação visa melhorar a rastreabilidade, auditoria e integridade dos dados no sistema de gerenciamento de projetos.

## Implementação e Subcompetências Desenvolvidas

### 1. Modelagem de Dados

A modelagem de dados foi realizada considerando os requisitos de consulta e isolamento de domínio adequado. As principais entidades implementadas são:

- **User**: Representa os usuários do sistema.
- **Project**: Representa os projetos gerenciados.
- **Task**: Representa as tarefas associadas aos projetos.

Além disso, foram criadas entidades de histórico para cada uma das entidades principais:

- **UserHistory**
- **ProjectHistory**
- **TaskHistory**

Estas entidades de histórico permitem o rastreamento detalhado de todas as alterações feitas nas entidades principais, incluindo o campo alterado, valor antigo, novo valor, data e hora da alteração, e o usuário que realizou a alteração.

### 2. Integração de JPA com Spring Data

#### Utilização de anotações JPA

As entidades foram mapeadas para tabelas do banco de dados utilizando anotações JPA, facilitando a interação entre objetos Java e a base de dados. Exemplo:

```java
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Version
    private Long version;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "project_team_members",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> teamMembers;

    // ... outros campos e métodos
}
```

#### Criação e utilização de Repositórios Spring Data

Foram criados repositórios Spring Data para cada entidade, abstraindo e otimizando o acesso a dados. Exemplo:

```java
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByIdAndIsDeletedFalse(Long id);
    List<Project> findByIsDeletedFalse();
    boolean existsByNameAndIsDeletedFalse(String name);
}
```

### 3. Gerenciamento de Dados

O gerenciamento de acesso e manipulação de dados foi implementado nos serviços, assegurando a integridade e a performance da aplicação. Exemplo:

```java
@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectHistoryRepository projectHistoryRepository;

    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        if (projectRepository.existsByNameAndIsDeletedFalse(projectDTO.getName())) {
            throw new CustomException("O nome do projeto já está em uso.", HttpStatus.BAD_REQUEST);
        }

        Project project = mapToEntity(projectDTO);
        project.setDeleted(false);
        Project savedProject = projectRepository.save(project);

        addToHistory(savedProject, "Criação", null, "Projeto criado");

        return mapToDTO(savedProject);
    }

    // ... outros métodos
}
```

### 4. Integração de Funcionalidades de Histórico de Dados

Foi implementada uma funcionalidade para registrar e consultar o histórico de mudanças dos dados. Exemplo:

```java
private void addToHistory(Project project, String fieldName, String oldValue, String newValue) {
    ProjectHistory history = new ProjectHistory();
    history.setProject(project);
    history.setFieldName(fieldName);
    history.setOldValue(oldValue != null ? oldValue : "null");
    history.setNewValue(newValue != null ? newValue : "null");
    history.setChangeDate(LocalDateTime.now());
    history.setChangedBy(getCurrentUsername());
    projectHistoryRepository.save(history);
}
```

### 5. Implementação de Testes

Foram desenvolvidos testes automatizados para a camada de persistência, garantindo a robustez e a confiabilidade do software. Exemplo:

```java
@SpringBootTest
class ProjectServiceTest {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectHistoryRepository projectHistoryRepository;

    @Test
    void createProject_ShouldCreateProjectAndHistory() {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("Test Project");
        projectDTO.setStartDate(LocalDate.now());
        projectDTO.setEndDate(LocalDate.now().plusDays(30));

        ProjectDTO createdProject = projectService.createProject(projectDTO);

        assertNotNull(createdProject.getId());
        assertEquals(projectDTO.getName(), createdProject.getName());

        List<ProjectHistory> history = projectHistoryRepository.findByProjectIdOrderByChangeDateDesc(createdProject.getId());
        assertFalse(history.isEmpty());
        assertEquals("Criação", history.get(0).getFieldName());
    }

    // ... outros testes
}
```

Entendido. Vou adicionar uma seção sobre o front-end ao relatório, baseando-me nas informações fornecidas e atualizando conforme necessário para refletir o estado atual do código. Aqui está a adição ao relatório:

### 6. Desenvolvimento do Front-end

Além da implementação da camada de persistência no back-end, foi desenvolvido um front-end moderno e responsivo utilizando React com Next.js e TypeScript. 
Esta implementação visa proporcionar uma experiência de usuário fluida e eficiente.

#### Tecnologias Utilizadas
- React: Para construção de interfaces de usuário interativas
- Next.js: Framework React para renderização do lado do servidor e geração de sites estáticos
- TypeScript: Para adicionar tipagem estática, melhorando a manutenibilidade e reduzindo erros
- Tailwind CSS: Para estilização rápida e consistente
- Axios: Para realizar requisições HTTP ao back-end

#### Estrutura do Projeto
O projeto segue a estrutura padrão de um aplicativo Next.js, organizado em diretórios como `app/`, `components/`, `hooks/`, `lib/`, `services/`, e `styles/`.

#### Componentes Principais
- **Layout**: `AuthLayout.tsx` e `Navbar.tsx` para estruturar as páginas autenticadas
- **Autenticação**: `LoginForm.tsx` e `SignUpForm.tsx` para gerenciar o acesso dos usuários
- **Dashboard**: `Dashboard.tsx` para exibir uma visão geral dos projetos e tarefas
- **Projetos**: `ProjectList.tsx`, `ProjectDetails.tsx`, e `CreateProject.tsx` para gerenciamento de projetos
- **Tarefas**: `CreateTaskForm.tsx` para criação de novas tarefas
- **Perfil**: `Profile.tsx` para exibição de informações do usuário

#### Gerenciamento de Estado e Integração com Back-end
- Utilização do hook `useAuth` para gerenciar o estado de autenticação
- Serviços (`authService`, `projectService`, `taskService`, `userService`) para encapsular chamadas à API
- Configuração do Axios para incluir tokens JWT em requisições autenticadas

#### Roteamento
Implementado utilizando o sistema de roteamento baseado em arquivos do Next.js, proporcionando uma navegação intuitiva e eficiente entre 
as diferentes seções da aplicação.

#### Estilização
Adoção do Tailwind CSS para uma estilização rápida e consistente, com temas e estilos globais configurados no arquivo `globals.css`.
