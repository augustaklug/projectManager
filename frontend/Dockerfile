# Use a imagem Node.js oficial como base
FROM node:18-alpine

# Define o diretório de trabalho no container
WORKDIR /app

# Copia os arquivos de definição de pacotes
COPY frontend/package*.json ./

# Instala as dependências
RUN npm ci

# Copia os arquivos do projeto
COPY frontend .

# Constrói a aplicação
RUN npm run build

# Expõe a porta que a aplicação usará
EXPOSE 3000

# Comando para iniciar a aplicação
CMD ["npm", "start"]