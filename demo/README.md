# 🔧 AutoTaller API

Sistema backend para la gestión de un taller mecánico. Permite administrar clientes, vehículos, órdenes de servicio y técnicos, con autenticación por roles.

---

## 📋 Descripción

API REST que centraliza las operaciones de un taller automotriz. Tiene dos tipos de usuario:

- **Admin** → gestiona clientes, vehículos, técnicos y órdenes de servicio. Accede a estadísticas globales del taller.
- **Técnico** → visualiza y actualiza el estado de sus propias órdenes asignadas. Accede a su propio dashboard personal.

---

## ⚙️ Lógica de negocio

1. El **admin** registra clientes y sus vehículos.
2. Se crea una **orden de servicio** asociando un vehículo, un cliente y un técnico.
3. Las órdenes pasan por tres estados: `PENDIENTE` → `EN_PROCESO` → `TERMINADO`.
4. El **técnico** solo puede cambiar el estado de sus órdenes asignadas.
5. El **dashboard de admin** muestra estadísticas globales (vehículos, clientes nuevos, órdenes del día, resumen por estado).
6. El **dashboard del técnico** muestra sus propias métricas y sus últimas órdenes.

---

## 🛠️ Stack tecnológico

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.5 |
| Seguridad | Spring Security + JWT |
| Persistencia | Spring Data JPA / Hibernate |
| Base de datos | PostgreSQL (NeonDB en producción) |
| Documentación | Swagger UI (SpringDoc OpenAPI) |
| Deploy | Render |
| Utilidades | Lombok |
| Build | Maven |

---

## 📡 Endpoints principales

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/api/auth/login` | Inicio de sesión, retorna JWT |
| `GET` | `/api/customers` | Listar clientes |
| `GET` | `/api/vehicles` | Listar vehículos |
| `GET` | `/api/technicals` | Listar técnicos |
| `GET` | `/api/service-orders` | Listar todas las órdenes |
| `POST` | `/api/service-orders` | Crear nueva orden |
| `PATCH` | `/api/service-orders/{id}/status` | Actualizar estado de una orden |
| `GET` | `/api/dashboard/admin` | Dashboard del administrador |
| `GET` | `/api/dashboard/tecnico/{id}` | Dashboard del técnico |

> Documentación interactiva disponible en: `/api/docs`

---

## 🚀 Cómo ejecutar localmente

### Requisitos
- Java 21
- Maven
- PostgreSQL

### Variables de entorno

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/taller_db
SPRING_DATASOURCE_USERNAME=tu_usuario
SPRING_DATASOURCE_PASSWORD=tu_password
JWT_SECRET=tu_secreto_base64
JWT_EXPIRATION=10800000
```

### Ejecutar

```bash
./mvnw spring-boot:run
```

La API estará disponible en `http://localhost:8080/api`

---

## 🐳 Docker

```bash
docker build -t autotaller-api .
docker run -p 8080:8080 --env-file .env autotaller-api
```

