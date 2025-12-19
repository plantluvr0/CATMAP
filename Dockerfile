FROM node:20-slim
LABEL authors="ethan"

WORKDIR /app/

COPY package*.json ./

RUN npm install

COPY ./src/server.js ./

ENV PORT=8080

EXPOSE 8080

CMD [ "npm", "run", "devstart" ]