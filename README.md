# api-visitas

API REST para el registro y control de visitantes, desarrollada con Spring Boot. Incluye autenticación con JWT, validación de datos, base de datos H2 y frontend estático integrado.

## Tecnologías

- Java 17 / Spring Boot 3
- Spring Security + JWT (JJWT)
- Spring Data JPA + H2 (in-memory)
- Bean Validation (Jakarta)
- Lombok
- HTML + CSS + JavaScript (frontend estático servido por Spring Boot)

## Estructura del proyecto

```
src/
└── main/
    ├── java/.../api_visitas/
    │   ├── ApiVisitasApplication.java
    │   ├── DataLoader.java
    │   ├── controller/
    │   │   ├── AuthController.java
    │   │   └── VisitanteController.java
    │   ├── dto/
    │   │   └── LoginRequest.java
    │   ├── entity/
    │   │   ├── Usuario.java
    │   │   └── Visitante.java
    │   ├── exception/
    │   │   └── GlobalExceptionHandler.java
    │   ├── repository/
    │   │   ├── UsuarioRepository.java
    │   │   └── VisitanteRepository.java
    │   ├── security/
    │   │   ├── JwtRequestFilter.java
    │   │   ├── JwtUtil.java
    │   │   ├── SecurityConfig.java
    │   │   └── UserDetailsServiceImpl.java
    │   └── service/
    │       └── VisitanteService.java
    └── resources/
        ├── application.properties
        └── static/
            ├── index.html
            ├── recepcion.html
            ├── app.js
            └── style.css
```

## Endpoints

| Método | Ruta | Autenticación | Descripción |
|--------|------|:---:|-------------|
| `POST` | `/api/auth/login` | — | Retorna JWT al autenticarse |
| `GET` | `/api/visitantes` | JWT | Lista todos los visitantes |
| `POST` | `/api/visitantes` | JWT | Registra un nuevo visitante |
| `GET` | `/api/visitantes/{id}` | JWT | Busca visitante por ID |
| `DELETE` | `/api/visitantes/{id}` | JWT | Elimina un visitante |

### Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "recepcion_admin",
  "password": "Cecyte2026*"
}
```

**Respuesta:**
```json
{
  "token": "<jwt>",
  "mensaje": "Autenticación exitosa"
}
```

### Registrar visitante

```http
POST /api/visitantes
Authorization: Bearer <token>
Content-Type: application/json

{
  "nombreCompleto": "Juan Pérez García",
  "motivoVisita": "Reunión con TI",
  "macAddress": "AA:BB:CC:DD:EE:FF"
}
```

## Validaciones

- `nombreCompleto` y `motivoVisita`: campo requerido (`@NotBlank`)
- `macAddress`: formato `XX:XX:XX:XX:XX:XX` o `XX-XX-XX-XX-XX-XX` validado con regex
- Errores de validación devuelven `400` con detalle por campo

## Configuración

`src/main/resources/application.properties`:

```properties
server.port=8080

spring.datasource.url=jdbc:h2:mem:cecytedb
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

jwt.secret=<Base64 de clave ≥ 32 bytes>
jwt.expiration-ms=28800000
```


## Ejecutar localmente

**Requisitos:** Java 17+, Maven 3.8+

```bash
# Desde la raíz del proyecto
./mvnw spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`.

- **Frontend:** `http://localhost:8080/`
- **Consola H2:** `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:cecytedb`)

## Usuario por defecto

Al iniciar la aplicación, `DataLoader` crea automáticamente un usuario si la base de datos está vacía:

| Campo | Valor |
|-------|-------|
| Usuario | `recepcion_admin` |
| Contraseña | `Cecyte2026*` |

## Seguridad

- Contraseñas almacenadas con **BCrypt**
- Tokens JWT firmados con **HS256** (clave mínimo 256 bits)
- API **stateless** — sin sesiones en servidor
- CORS configurado para `localhost` en desarrollo
- `GlobalExceptionHandler` centraliza errores sin exponer stack traces al cliente

## Propuesta VLAN

El archivo [`VLAN_PROPUESTA_TECNICA.md`](./VLAN_PROPUESTA_TECNICA.md) describe la segmentación de red recomendada para aislar el tráfico de visitantes de la red administrativa, incluyendo configuración de VLANs, ACLs e integración de MAC address con el controlador del punto de acceso.
