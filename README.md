# Desafio Técnico Itaú - Desenvolvedor Júnior

API REST desenvolvida para o desafio técnico do Itaú, que permite:

* Registrar transações
* Calcular estatísticas das transações recentes
* Limpar todas as transações armazenadas

## 🚀 Tecnologias Utilizadas

* Java 17
* Spring Boot 3
* Lombok
* Spring Web
* Spring Validation
* Jakarta Validation
* Spring Boot Actuator
* JUnit 5
* Mockito
* Docker
* Swagger (OpenAPI)

## 🛠️ Funcionalidades

* **POST /api/transacao** - Registrar nova transação.
* **DELETE /api/transacao** - Limpar todas as transações armazenadas.
* **GET /api/estatistica** - Obter estatísticas das transações (últimos 60 segundos por padrão).

## 🖥️ Pré-requisitos

* JDK 17 ou superior
* Maven 3.x
* Docker (opcional, caso queira rodar em container)

## 🏗️ Como executar o projeto

### Executar localmente

```bash
./mvnw spring-boot:run
```

### Gerar e executar o JAR

```bash
./mvnw clean package
java -jar target/desafio-itau.jar
```

### Docker

Gerar imagem:

```bash
docker build -t desafio-itau .
```

Executar container:

```bash
docker run -p 8080:8080 desafio-itau
```

## 🔍 Documentação da API

Swagger UI disponível em:

```
http://localhost:8080/swagger-ui.html
```

## ✅ Testes automatizados

Executar testes:

```bash
./mvnw test
```

## 📦 Configurações

* Ajuste o tempo de cálculo das estatísticas no arquivo `application.properties`:

```properties
estatistica.tempo.segundos=60
```

## 📄 Logs

Logs são gerados na pasta `logs/` com rotação diária e histórico de até 30 dias.

## 📌 Observabilidade

Healthcheck disponível no endpoint:

```
GET /actuator/health
```

## 🔖 Autor

* Pedro Henrique de Lima
