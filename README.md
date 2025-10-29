# RBAC & Plan Management API

## Project Overview
This API provides **role-based access control (RBAC)** and **plan-based limits** for managing **projects, users, and tasks**.  
All endpoints are RESTful, and JWT authentication is required.

- Supports **roles**: `A / B / C / D`
- Supports **plans**: `BASIC / PRO`
- All requests are validated for **role permissions** and **plan limits**
- Admin (`A`) can adjust roles and permissions for `B/C/D`
- Provides stable and secure API with performance considerations



## Technology Stack
- **Java 17**, Spring Boot 3
- **MySQL** (user/project/task)
- Lombok, JPA/Hibernate
- Postman (API testing & environment management)



## API Specification

### User API
Supports creating users, retrieving user information, and modifying roles/plans.
### Projects API
Supports creating projects, retrieving project details, managing members, and tracking progress.
### Tasks API
Supports creating tasks, updating task status, and assigning task owners.



## Common Logic
- All API responses follow a **standard response format**
- **Error handling** unified across controllers
- Role & plan validation applied globally
- JWT authentication applied globally




## Postman Collection

- Postman Collection: `/postman/ProjectCRUD.postman_collection.json`
- Postman Environment: `/postman/ProjectCRUD.postman_environment.json`

### Environment Upload
1. Open Postman → **Environments** → **Import**
2. Upload `ProjectCRUD.postman_environment.json`
3. Select environment in top-right dropdown
4. Environment variables include:
   - `role` = `A/B/C/D`
   - `plan` = `BASIC/PRO`

### API Testing with Postman
1. Open collection `ProjectCRUD`
2. Select an API request
3. Adjust environment variables as needed (`role`, `plan`)
4. Click **Send** to test
5. Verify response and RBAC / plan-based restrictions



## Installation & Running
1. Clone repository
```bash
git clone <REPO_URL>
cd [project]
```

2. Install dependencies
```bash
./mvnw clean install
```

3.Run application
```bash
./mvnw spring-boot:run
```

4. Base URL: http://localhost:8080
5. Access Swagger UI: http://localhost:8080/swagger-ui.html

