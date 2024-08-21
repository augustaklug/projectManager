# Camada de Persistência do Project Manager

## Design da Camada de Persistência

A camada de persistência do Project Manager foi projetada seguindo os princípios de Domain-Driven Design (DDD) e utilizando as melhores práticas do Spring Data JPA. Esta camada é responsável pelo armazenamento, recuperação e manipulação de dados no banco de dados.

### Componentes Principais

1. **Entidades**: Representam os objetos de domínio e são mapeadas diretamente para tabelas no banco de dados.
2. **Repositórios**: Interfaces que estendem JpaRepository, fornecendo métodos CRUD e consultas personalizadas.
3. **DTOs (Data Transfer Objects)**: Objetos utilizados para transferir dados entre a camada de serviço e a camada de apresentação.
4. **Serviços**: Contêm a lógica de negócios e interagem com os repositórios para realizar operações de dados.

### Características Principais

- **Soft Delete**: Implementado para permitir a "exclusão lógica" de registros.
- **Versionamento de Entidades**: Utilizado para controle de concorrência otimista.
- **Histórico de Mudanças**: Rastreamento de alterações em entidades principais.
- **Mapeamento Objeto-Relacional**: Realizado através de anotações JPA.

## Modelos de Dados

### Entidades Principais

1. **User**
   - Atributos: id, username, email, password, role, isDeleted, version
   - Relacionamentos: 
     - OneToMany com Task (tarefas atribuídas)
     - ManyToMany com Project (projetos dos quais é membro)

2. **Project**
   - Atributos: id, name, startDate, endDate, isDeleted, version
   - Relacionamentos:
     - ManyToMany com User (membros da equipe)
     - OneToMany com Task (tarefas do projeto)

3. **Task**
   - Atributos: id, name, description, status, deadline, isDeleted, version
   - Relacionamentos:
     - ManyToOne com Project
     - ManyToOne com User (atribuído a)

### Entidades de Histórico

1. **UserHistory**
2. **ProjectHistory**
3. **TaskHistory**

Cada entidade de histórico contém: id, entidadeRelacionada, fieldName, oldValue, newValue, changeDate, changedBy

## Exemplos de Uso dos Repositórios

### UserRepository

```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndIsDeletedFalse(Long id);
    User findByUsernameAndIsDeletedFalse(String username);
    List<User> findByIsDeletedFalse();
    boolean existsByUsernameAndIsDeletedFalse(String username);
}

// Exemplo de uso no serviço
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findUserById(Long id) {
        return userRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new CustomException("Usuário não encontrado"));
    }

    public List<User> getAllActiveUsers() {
        return userRepository.findByIsDeletedFalse();
    }
}
```

### ProjectRepository

```java
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByIdAndIsDeletedFalse(Long id);
    List<Project> findByIsDeletedFalse();
    boolean existsByNameAndIsDeletedFalse(String name);
}

// Exemplo de uso no serviço
@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public Project createProject(ProjectDTO projectDTO) {
        if (projectRepository.existsByNameAndIsDeletedFalse(projectDTO.getName())) {
            throw new CustomException("Projeto com este nome já existe");
        }
        Project project = new Project();
        // ... configurar projeto
        return projectRepository.save(project);
    }
}
```

### TaskRepository

```java
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByIdAndIsDeletedFalse(Long id);
    List<Task> findByProjectIdAndIsDeletedFalse(Long projectId);
    List<Task> findByAssignedToIdAndIsDeletedFalse(Long userId);
}

// Exemplo de uso no serviço
@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getTasksForProject(Long projectId) {
        return taskRepository.findByProjectIdAndIsDeletedFalse(projectId);
    }

    public List<Task> getTasksAssignedToUser(Long userId) {
        return taskRepository.findByAssignedToIdAndIsDeletedFalse(userId);
    }
}
```

## Considerações de Performance

1. **Índices**: Criados em colunas frequentemente usadas em consultas, como `isDeleted`, `username`, e chaves estrangeiras.
2. **Lazy Loading**: Utilizado em relacionamentos @OneToMany e @ManyToMany para evitar carregamento desnecessário de dados.

## Transações

Métodos que realizam múltiplas operações de banco de dados são anotados com `@Transactional` para garantir a integridade dos dados.
