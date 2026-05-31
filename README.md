# 📑 DocumentFlow — Automated Document Approval Workflow API

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Integration](https://img.shields.io/badge/Spring%20Integration-6.x-blue.svg)](https://spring.io/projects/spring-integration)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**DocumentFlow** is an enterprise-grade backend system designed to automate document submission and approval workflows within an organization. Utilizing **Spring Integration**, the system orchestrates decoupled, asynchronous messaging flows, manages complex state transitions, and automates email-based approval requests while ensuring strict auditability and cryptographic security.

---

## 🎯 Learning Objectives Achieved
* **Mastered Spring Integration Framework:** Designed complex event-driven workflows using integration flows, message channels, splitters, and aggregators.
* **Asynchronous Messaging Pipelines:** Achieved a highly decoupled and scalable architecture using asynchronous message channels and system event routers.
* **Automated Enterprise Mail Systems:** Configured robust outbound SMTP email workflows via Spring Integration Mail adapters.
* **Stateful Workflow Management:** Implemented bulletproof database-backed state machine logic ensuring high traceablity and consistent workflow status transitions.
* **Advanced Error Resiliency:** Designed transient fault-handling networks using exponential backoff retry policies and Dead Letter Queues (DLQ).
* **Modern Security Standards:** Secured endpoints using stateless JWT authentication with fine-grained Spring Security Role-Based Access Control (RBAC).

---

## 🚀 Key Features

### 🔹 Core Capabilities
* **Document Submission Pipeline:** Multi-part document upload endpoint that saves metadata, initiates database persistence, and securely injects the record into the messaging pipeline.
* **Automated Email Notifications:** Instantly sends well-formatted approval request emails to assigned managers when a new document is submitted.
* **RESTful Decision Gateways:** Secure API endpoints enabling approvers to cast `APPROVE` or `REJECT` decisions, instantly advancing the integration flow.
* **Comprehensive Audit Trail:** Captures every event (submission, email sent, approval, rejection, state change) for end-to-end organizational auditability.

### 🌟 Bonus Features Implemented
* **Real-time Push Notifications:** Integrated **WebSockets / Server-Sent Events (SSE)** to push live document status updates directly to users' dashboards.
* **Admin Control Suite:** Robust analytical endpoints for administrators to filter documents by state, manage user roles, and monitor queues.
* **Advanced Data Export:** Built-in reporting system to export historical audit logs and workflow timelines into **CSV** and **JSON** formats.

---

## 🛠️ Tech Stack & Infrastructure

* **Framework:** Spring Boot 3.x (Java 17+)
* **Enterprise Integration:** Spring Integration (Channels, Gateways, Service Activators, Transformers)
* **Security:** Spring Security, Stateless **JWT (JSON Web Tokens)**
* **Persistence:** Spring Data JPA, Hibernate ORM
* **Database:** PostgreSQL (Production) / H2 (Testing)
* **Containerization:** Docker & Docker Compose
* **Documentation:** Swagger UI / OpenAPI 3.0

---

## 📊 Workflow State Machine

[ SUBMITTED ]│▼[ PENDING_APPROVAL ] ──────┐│                  │(Action: APPROVE)  (Action: REJECT)│                  │▼                  ▼[ APPROVED ]       [ REJECTED ]│                  │└─────────┬────────┘▼[ ARCHIVED ]
---

## ⚙️ Setup and Installation

### 1. Clone the repository
```bash
git clone [https://github.com/yourusername/DocumentFlow.git](https://github.com/yourusername/DocumentFlow.git)
cd DocumentFlow
2. Environment ConfigurationCreate an application.yml or fill in your root .env file with your credentials:YAMLspring:
  datasource:
    url: jdbc:postgresql://localhost:5432/documentflow
    username: your_db_user
    password: your_db_password
  mail:
    host: smtp.mailtrap.io
    port: 2525
    username: your_smtp_username
    password: your_smtp_password

jwt:
  secret: your_super_secret_64_character_long_key_here
  expiration: 86400000 # 24 hours
3. Run via Docker ComposeTo easily build and spin up the database, application, and mail services containerized:Bashdocker-compose up -d --build
🔌 API Endpoints SummaryComplete interactive documentation can be viewed via Swagger at http://localhost:8080/swagger-ui.html when the application is running.MethodEndpointAccess ControlDescriptionPOST/api/v1/auth/registerPublicRegister a new system user or approver.POST/api/v1/auth/loginPublicAuthenticate user and receive JWT Token.POST/api/v1/documentsROLE_USERUpload and submit a new document to the workflow.GET/api/v1/documents/{id}/statusROLE_USER, ROLE_ADMINTrack the real-time status of a document.POST/api/v1/workflow/approveROLE_APPROVERApprove a document pending review.POST/api/v1/workflow/rejectROLE_APPROVERReject a document pending review.GET/api/v1/admin/dashboardROLE_ADMINMonitor pipeline metrics and system statuses.GET/api/v1/admin/audit/exportROLE_ADMINExport history logs (Query param: ?format=csv or json).🛡️ Resiliency & Fault ToleranceThe system utilizes an automated Exponential Backoff Retry Strategy within the Spring Integration pipeline to protect against network drops or temporary email server crashes:Java@Bean
public IntegrationFlow emailRequestHandlerFlow() {
    return IntegrationFlow.from(EmailChannels.REQUEST_CHANNEL)
        .handler(Mail.outboundAdapter(mailSender)
            .javaMailProperties(p -> p.put("mail.smtp.auth", "true")),
            c -> c.advice(retryAdvice())) // Automated retry configuration
        .get();
}
If a mail delivery fails permanently after maximum retries, the message drops safely into a Dead Letter Queue (DLQ), triggers a system alert, and preserves the workflow workflow state for administrative review.🧪 Running TestsThe test suite includes complete end-to-end integration flows and unit tests using Mockito.Bash# Run all tests
mvn clean test

# Run workflow pipeline integration tests specifically
mvn test -Dtest=*WorkflowIntegrationTest
