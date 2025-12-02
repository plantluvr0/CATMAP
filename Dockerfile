FROM node:20-slim
LABEL authors="ethan"

WORKDIR /app/

COPY package*.json ./

RUN npm install

COPY . ./

ENV PORT=8080, DB_USER="postgres", DB_PASS="aL?c\$ev?y//,|=7N"

EXPOSE 8080

CMD [ "npm", "run", "devstart" ]