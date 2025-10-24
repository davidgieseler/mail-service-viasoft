# Viasoft Back-end — Email Service Adapter

-----

## Project Objective

The system simulates sending emails from a single REST endpoint.  
According to the configuration defined in `application.properties`, the received data is **dynamically adapted** to the format required by each integration provider (`AWS` or `OCI`) and **serialized into JSON**, being printed to the server console.

-----

## Technologies and Dependencies

| Category                  | Technologies |
|---------------------------|----------------|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3 |
| **Build Tool** | Maven |
| **Validation** | `spring-boot-starter-validation` |
| **Mapper** | MapStruct |
| **Boilerplate Reduction** | Lombok |
| **Logging** | SLF4J |
| **Testing** | JUnit 5 + Mockito |
| **Swagger** | springdoc-openapi |

-----

## Architecture and Design

The architecture follows **Clean Architecture** and **SOLID** principles, ensuring decoupling between layers, testability, and extensibility.  
The main architectural points are described below.

### 1\. System Layers

| Layer                          | Responsibility |
|--------------------------------|------------------|
| **Controller** | Exposes the REST endpoint (`/api/email/send`). Receives the input DTO and delegates to the service. |
| **Service** | Contains the main business logic and dynamic selection of the adaptation strategy. |
| **Strategy (AdapterStrategy)** | Implements the specific adaptation rules for each provider (AWS / OCI). |
| **Mapper** | Uses MapStruct for performant and type-safe DTO conversion. |
| **Exception Handling** | Centralized via `@ControllerAdvice` to ensure standardized responses. |

-----

## Design Patterns Applied

### Strategy Pattern

Implemented to decouple the provider adaptation logic.

- **Interface:** `AdapterStrategy`
- **Implementations:** `AwsAdapterStrategy`, `OciAdapterStrategy`
- **Context:** `EmailServiceImpl` receives a `Map<IntegrationType, AdapterStrategy>` automatically injected by Spring, selecting the correct implementation at runtime.

-----

## Mapping and DTOs

Mapping is performed with **MapStruct**, avoiding reflection and ensuring high performance.  
The input DTO (`EmailRequestDTO`) is converted into `EmailAwsDTO` or `EmailOciDTO`, according to the active configuration.

**Example DTOs:**

```java
// Input DTO
public class EmailRequestDTO {
    private String recipientEmail;
    private String recipientName;
    private String senderEmail;
    private String subject;
    private String content;
}

// Output DTOs
public class EmailAwsDTO {
    private String recipient;
    private String recipientName;
    private String sender;
    private String subject;
    private String content;
}

public class EmailOciDTO {
    private String recipientEmail;
    private String recipientName;
    private String senderEmail;
    private String subject;
    private String body;
}
```

-----

## Error Handling

The application has a `GlobalExceptionHandler` responsible for catching exceptions and returning standardized responses:

* **400 – Bad Request:** field validation errors.
* **500 – Internal Server Error:** configuration errors or unexpected failures.

Example error response:

```json
{
  "status": 400,
  "message": "Validation error",
  "path": "/api/email/send",
  "fieldErrors": {
    "recipientEmail": "must be a well-formed email address"
  },
  "timestamp": "2025-10-22T19:30:00Z"
}
```

-----

## Configuration

### `application.properties` File

```properties
# Defines which provider will be used for adaptation
mail.integracao=AWS
```

Accepted values: `AWS` or `OCI`.

The configuration is injected as an **Enum (`IntegrationType`)**, ensuring validation at startup (Fail-Fast).

-----

## Running the Project

### Prerequisites

* Java 17+
* Maven 3.8+

### Steps

```bash
# Clone the repository
git clone <repository-url>
cd mail-service-viasoft

# Compile and run
mvn spring-boot:run
```

The application will be available at: `http://localhost:8080`

-----

## API Endpoint

### `POST /api/email/send`

#### Request Body (JSON)

```json
{
  "recipientEmail": "recipient@exemple.com",
  "recipientName": "Recipient Name",
  "senderEmail": "sender@example.com",
  "subject": "Test",
  "content": "content example."
}
```

#### Success Response

**Status:** `204 No Content`
**Console Output:**

AWS

```json
{
  "recipient": "recipient@exemple.com",
  "recipientName": "Recipient Name",
  "sender": "sender@example.com",
  "subject": "Test",
  "content": "content example."
}
```

OCI

```json
{
  "recipientEmail": "recipient@exemple.com",
  "recipientName": "Recipient Name",
  "senderEmail": "sender@example.com",
  "subject": "Test",
  "body": "content example."
}
```

-----

## Unit Tests

The project includes tests covering:

* Mappings (`EmailMapperTest`)
* Strategies (`AwsAdapterStrategyTest`, `OciAdapterStrategyTest`)
* Service (`EmailServiceImplTest`)
* Controller (`EmailControllerTest`)

To run:

```bash
mvn test
```

-----

## Swagger

`localhost:8080/api/swagger-ui.html`

-----

## Author

**David Gieseler**
[LinkedIn](https://www.linkedin.com/in/davidmgieseler) | [GitHub](https://github.com/davidgieseler)

-----

-----

# Back-end Viasoft — Adaptador de Serviço de E-mail

---

## Objetivo do Projeto

O sistema simula o envio de e-mails a partir de um endpoint REST único.  
De acordo com a configuração definida em `application.properties`, os dados recebidos são **adaptados dinamicamente** para o formato exigido por cada provedor de integração (`AWS` ou `OCI`) e **serializados em JSON**, sendo impressos no console do servidor.

---

## Tecnologias e Dependências

| Categoria                 | Tecnologias |
|---------------------------|-------------|
| **Linguagem**             | Java 17 |
| **Framework**             | Spring Boot 3 |
| **Build Tool**            | Maven |
| **Validação**             | `spring-boot-starter-validation` |
| **Mapper**                | MapStruct |
| **Boilerplate Reduction** | Lombok |
| **Logging**               | SLF4J |
| **Testes**                | JUnit 5 + Mockito |
| **Swagger**               | springdoc-openapi |


---

## Arquitetura e Design

A arquitetura segue os princípios **Clean Architecture** e **SOLID**, garantindo desacoplamento entre camadas, testabilidade e extensibilidade.  
Os principais pontos arquiteturais são descritos abaixo.

### 1. Camadas do Sistema

| Camada                         | Responsabilidade |
|--------------------------------|------------------|
| **Controller**                 | Exposição do endpoint REST (`/api/email/send`). Recebe o DTO de entrada e delega ao serviço. |
| **Service**                    | Contém a lógica principal de negócio e seleção dinâmica da estratégia de adaptação. |
| **Strategy (AdapterStrategy)** | Implementa as regras específicas de adaptação para cada provedor (AWS / OCI). |
| **Mapper**                     | Utiliza MapStruct para conversão de DTOs de forma performática e type-safe. |
| **Exception Handling**         | Centralizado via `@ControllerAdvice` para garantir respostas padronizadas. |

---

## Padrões de Projeto Aplicados

### Strategy Pattern

Implementado para desacoplar a lógica de adaptação dos provedores.

- **Interface:** `AdapterStrategy`
- **Implementações:** `AwsAdapterStrategy`, `OciAdapterStrategy`
- **Contexto:** `EmailServiceImpl` recebe um `Map<IntegrationType, AdapterStrategy>` injetado automaticamente pelo Spring, selecionando a implementação correta em tempo de execução.

---

## Mapeamento e DTOs

O mapeamento é realizado com **MapStruct**, evitando reflection e garantindo alto desempenho.  
O DTO de entrada (`EmailRequestDTO`) é convertido em `EmailAwsDTO` ou `EmailOciDTO`, conforme a configuração ativa.

**Exemplo de DTOs:**

```java
// DTO de entrada
public class EmailRequestDTO {
    private String recipientEmail;
    private String recipientName;
    private String senderEmail;
    private String subject;
    private String content;
}

// DTOs de saída
public class EmailAwsDTO {
    private String recipient;
    private String recipientName;
    private String sender;
    private String subject;
    private String content;
}

public class EmailOciDTO {
    private String recipientEmail;
    private String recipientName;
    private String senderEmail;
    private String subject;
    private String body;
}
````

---

## Tratamento de Erros

A aplicação possui um `GlobalExceptionHandler` responsável por capturar exceções e retornar respostas padronizadas:

* **400 – Bad Request:** erros de validação de campos.
* **500 – Internal Server Error:** erros de configuração ou falhas inesperadas.

Exemplo de resposta de erro:

```json
{
  "status": 400,
  "message": "Validation error",
  "path": "/api/email/send",
  "fieldErrors": {
    "recipientEmail": "deve ser um endereço de e-mail bem formado"
  },
  "timestamp": "2025-10-22T19:30:00Z"
}
```

---

## Configuração

### Arquivo `application.properties`

```properties
# Define qual provedor será utilizado na adaptação
mail.integracao=AWS
```

Valores aceitos: `AWS` ou `OCI`.

A configuração é injetada como um **Enum (`IntegrationType`)**, garantindo validação em tempo de inicialização (Fail-Fast).

---

## Execução do Projeto

### Pré-requisitos

* Java 17+
* Maven 3.8+

### Passos

```bash
# Clonar o repositório
git clone <url-do-repositorio>
cd mail-service-viasoft

# Compilar e executar
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

---

## Endpoint da API

### `POST /api/email/send`

#### Corpo da Requisição (JSON)

```json
{
  "recipientEmail": "recipient@exemple.com",
  "recipientName": "Recipient Name",
  "senderEmail": "sender@example.com",
  "subject": "Test",
  "content": "content example."
}
```

#### Resposta de Sucesso

**Status:** `204 No Content`
**Saída no console:**

AWS
```json
{
  "recipient": "recipient@exemple.com",
  "recipientName": "Recipient Name",
  "sender": "sender@example.com",
  "subject": "Test",
  "content": "content example."
}
```
OCI
```json
{
"recipientEmail" : "recipient@example.com",
"recipientName" : "recipient",
"senderEmail" : "sender@example.com",
"subject" : "Test",
"body" : "content example."
}
```

---

## Testes Unitários

O projeto inclui testes cobrindo:

* Mapeamentos (`EmailMapperTest`)
* Estratégias (`AwsAdapterStrategyTest`, `OciAdapterStrategyTest`)
* Serviço (`EmailServiceImplTest`)
* Controller (`EmailControllerTest`)

Para executar:

```bash
mvn test
```

---

## Swagger
`localhost:8080/api/swagger-ui.html`

---

## Autor

**David Gieseler**
[LinkedIn](https://www.linkedin.com/in/davidmgieseler) | [GitHub](https://github.com/davidgieseler)

---
