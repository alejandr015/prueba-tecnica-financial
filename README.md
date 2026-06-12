# Financial App
Aplicación de administración financiera para gestión de clientes, productos y transacciones

## Tecnologías
**Backend:** Java 21, Spring Boot 3.3, Spring Data JPA, MySQL 8, Maven  
**Frontend:** React, Axios  
**DevOps:** Docker, Docker Compose, Git Flow

## Arquitectura
MVC por capas: `entity` → `repository` → `service` → `controller`

## Requisitos
- Java 21
- Docker Desktop
- Node.js

## Cómo correr el proyecto
### 1. Clonar el repositorio
```bash
git clone https://github.com/alejandr015/prueba-tecnica-financial.git
cd prueba-tecnica-financial
```
### 2. Levantar base de datos con Docker
```bash
docker-compose up -d
```
### 3. Correr el backend
```bash
./mvnw spring-boot:run
```

### 4. Correr el frontend
```bash
cd frontend
npm install
npm start
```
### 5. Acceder a la aplicación
- **Frontend:** http://localhost:3000
- **API:** http://localhost:8080/api


## Patrones de diseño
- Repository Pattern
- DTO Pattern
- Service Layer Pattern
- Dependency Injection

## Principios SOLID aplicados
- **S** — cada clase tiene una única responsabilidad
- **O** — abierto para extensión, cerrado para modificación
- **L** — implementaciones intercambiables por sus interfaces
- **I** — interfaces segregadas por dominio
- **D** — dependencia hacia abstracciones, no implementaciones

## Principio ACID
Implementado con `@Transactional` en transferencias entre cuentas
## 🌐 Demo en producción

**API desplegada en Railway:**  
https://prueba-tecnica-financial-production.up.railway.app/api/clientes