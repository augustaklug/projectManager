# Documentação do Front-end do Project Manager

## Visão Geral

O front-end do Project Manager é desenvolvido utilizando React com Next.js, proporcionando uma experiência de usuário moderna e responsiva. O projeto utiliza TypeScript para tipagem estática, melhorando a manutenibilidade e reduzindo erros.

## Estrutura do Projeto

O projeto segue a estrutura padrão de um aplicativo Next.js:

```
src/
|-- app/
|   |-- dashboard/
|   |-- login/
|   |-- profile/
|   |-- projects/
|   |-- tasks/
|   |-- layout.tsx
|   |-- page.tsx
|-- components/
|   |-- auth/
|   |-- dashboard/
|   |-- layout/
|   |-- projects/
|   |-- profile/
|   |-- ui/
|-- hooks/
|-- lib/
|-- services/
|-- styles/
```

- `app/`: Contém as páginas da aplicação, seguindo o sistema de roteamento baseado em arquivos do Next.js.
- `components/`: Componentes reutilizáveis da aplicação.
- `hooks/`: Hooks personalizados para lógica compartilhada.
- `lib/`: Utilitários e configurações.
- `services/`: Serviços para interação com a API.
- `styles/`: Estilos globais e configurações do Tailwind CSS.

## Componentes Principais

### Layout

- `AuthLayout.tsx`: Layout para páginas autenticadas, incluindo a barra de navegação.
- `Navbar.tsx`: Barra de navegação principal com links para diferentes seções da aplicação.

### Autenticação

- `LoginForm.tsx`: Formulário de login com opção para alternar para o formulário de registro.
- `SignUpForm.tsx`: Formulário de registro de novos usuários.

### Dashboard

- `Dashboard.tsx`: Página principal do dashboard, exibindo uma visão geral dos projetos e tarefas do usuário.

### Projetos

- `ProjectList.tsx`: Lista de projetos do usuário.
- `ProjectDetails.tsx`: Detalhes de um projeto específico, incluindo tarefas associadas.
- `CreateProject.tsx`: Formulário para criação de novos projetos.

### Tarefas

- `CreateTaskForm.tsx`: Formulário para criação de novas tarefas dentro de um projeto.

### Perfil

- `Profile.tsx`: Exibição das informações do perfil do usuário.

## Gerenciamento de Estado

O gerenciamento de estado é realizado principalmente através do hook `useAuth` e dos serviços de API. O `useAuth` hook gerencia o estado de autenticação do usuário, enquanto os serviços (`authService`, `projectService`, `taskService`, `userService`) são responsáveis por interagir com o back-end e atualizar o estado local conforme necessário.

## Roteamento

O roteamento é gerenciado pelo sistema de roteamento baseado em arquivos do Next.js. As páginas principais incluem:

- `/`: Página inicial (redirecionamento baseado na autenticação)
- `/login`: Página de login/registro
- `/dashboard`: Dashboard do usuário
- `/projects`: Lista de projetos
- `/projects/[id]`: Detalhes de um projeto específico
- `/projects/create`: Criação de novo projeto
- `/tasks`: Lista de tarefas
- `/profile`: Perfil do usuário

## Integração com o Back-end

A integração com o back-end é realizada através do Axios, configurado no arquivo `api.ts`. Os serviços (`authService`, `projectService`, `taskService`, `userService`) encapsulam as chamadas à API, proporcionando uma interface limpa para os componentes.

## Estilização

O projeto utiliza Tailwind CSS para estilização, com componentes personalizados definidos na pasta `components/ui/`. A estilização global e temas são configurados no arquivo `globals.css`.

## Autenticação e Segurança

A autenticação é implementada usando JWT (JSON Web Tokens). O `authService` gerencia o armazenamento e recuperação do token, enquanto o `api.ts` configura o Axios para incluir o token em todas as requisições autenticadas.