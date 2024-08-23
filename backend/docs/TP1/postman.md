# Executando Requisições aos Endpoints do Projeto Utilizando o Postman

Para testar e interagir com os endpoints do projeto, foi utilizado o Postman.

## 1. Autenticação de Usuário

Antes de realizar outras operações, você precisa autenticar o usuário para obter um token JWT.

- **URL:** `http://localhost:8080/api/auth/signin`
- **Método:** POST

### Corpo da Requisição (JSON):

```json
{
  "username": "user1",
  "password": "password123"
}
```

### Resposta da Requisição (JSON):

A resposta deve conter um token JWT, algo como:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

![image](https://github.com/augustaklug/projectManager/assets/56510700/39c4c5ce-6837-4452-a8d8-6ea1eb004cd1)

**Após isso, para todas as operações, selecionar na aba ```Authorization``` a ```Auth Type``` ```Bearer Token``` e preencher com o token obtido.**

## 2. Criar Projeto
- **URL:** `http://localhost:8080/api/projects`
- **Método:** POST

### Corpo da Requisição (JSON):

```json
{
  "name": "Novo Projeto",
  "startDate": "2024-05-01",
  "endDate": "2025-01-31",
  "teamMemberIds": [1, 2, 3]
}
```

### Resposta da Requisição (JSON):

A resposta deve conter um JSON com os dados do projeto criado, e o status:

```Status: 201 Created```

![image](https://github.com/augustaklug/projectManager/assets/56510700/2af30e63-d21d-4616-b868-fa8b9240a840)

## 3. Obter Todos os Projetos
- **URL:** `http://localhost:8080/api/projects`
- **Método:** GET

### Resposta da Requisição (JSON):

A resposta deve conter um JSON com os dados de todos os projetos, e o status:

```Status: 200 OK```

![image](https://github.com/augustaklug/projectManager/assets/56510700/35f7e69e-aac1-4353-9fba-18344d94159d)

## 4. Obter Projeto por ID
- **URL:** `http://localhost:8080/api/projects/{id}`
- **Método:** GET

### Resposta da Requisição (JSON):

A resposta deve conter um JSON com os dados do projeto selecionado, e o status:

```Status: 200 OK```

![image](https://github.com/augustaklug/projectManager/assets/56510700/54a7132b-e570-44f4-a38f-133f9c3579a8)

## 5. Atualizar Projeto
- **URL:** `http://localhost:8080/api/projects/{id}`
- **Método:** PUT

### Corpo da Requisição (JSON):

```json
{
  "name": "Projeto Atualizado",
  "startDate": "2024-01-01",
  "endDate": "2024-12-31",
  "teamMemberIds": [1, 3]
}
```

### Resposta da Requisição (JSON):

A resposta deve conter um JSON com os dados do projeto atualizado, e o status:

```Status: 200 OK```

![image](https://github.com/augustaklug/projectManager/assets/56510700/05b6b8b9-387f-44a2-8512-96282e27da2f)

## 6. Excluir Projeto
- **URL:** `http://localhost:8080/api/projects/{id}`
- **Método:** DELETE

### Resposta da Requisição (JSON):

A resposta será vazia, e o status:

```Status: 204 No Content```

![image](https://github.com/augustaklug/projectManager/assets/56510700/ebcb49ad-7ab8-4d64-a860-2cfd69d95de7)

