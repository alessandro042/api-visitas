# api-visitas

API REST para el registro y control de visitantes en las Oficinas Centrales del CECyTE. Desarrollada con Spring Boot, incluye autenticación con JWT, roles de usuario, bitácora de auditoría, panel de administración, validación de datos, base de datos H2 y frontend estático integrado.

## Tecnologías

- Java 17 / Spring Boot 3
- Spring Security + JWT (JJWT) con roles por claim
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
    │   │   ├── AuditController.java      ← nuevo
    │   │   ├── UsuarioController.java    ← nuevo
    │   │   └── VisitanteController.java
    │   ├── dto/
    │   │   ├── LoginRequest.java
    │   │   ├── UsuarioRequest.java       ← nuevo
    │   │   └── UsuarioResponse.java      ← nuevo
    │   ├── entity/
    │   │   ├── AuditLog.java             ← nuevo
    │   │   ├── Usuario.java
    │   │   └── Visitante.java
    │   ├── exception/
    │   │   └── GlobalExceptionHandler.java
    │   ├── repository/
    │   │   ├── AuditLogRepository.java   ← nuevo
    │   │   ├── UsuarioRepository.java
    │   │   └── VisitanteRepository.java
    │   ├── security/
    │   │   ├── JwtRequestFilter.java
    │   │   ├── JwtUtil.java
    │   │   ├── SecurityConfig.java
    │   │   └── UserDetailsServiceImpl.java
    │   └── service/
    │       ├── AuditService.java         ← nuevo
    │       ├── UsuarioService.java       ← nuevo
    │       └── VisitanteService.java
    └── resources/
        ├── application.properties
        └── static/
            ├── index.html
            ├── recepcion.html
            ├── admin.html                ← nuevo
            ├── app.js
            └── style.css
```

## Roles y permisos

El sistema define tres roles de usuario. El rol se almacena en la entidad `Usuario` y se incluye como claim en el JWT.

| Recurso | ADMIN | RECEPCIONISTA | VIGILANTE |
|---------|:-----:|:-------------:|:---------:|
| CRUD Visitantes (registro, edición, salida, eliminación) | ✅ | ✅ | ✅ |
| Registrar salida de visitante | ✅ | ✅ | ✅ |
| CRUD Usuarios del sistema | ✅ | ❌ | ❌ |
| Ver bitácora de auditoría | ✅ | ❌ | ❌ |
| Acceso a Panel de Administración | ✅ | ❌ | ❌ |

### Usuarios iniciales

`DataLoader` crea los siguientes usuarios al iniciar si la base de datos está vacía:

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| `admin` | `Admin2026*` | ADMIN |
| `recepcion_admin` | `Cecyte2026*` | RECEPCIONISTA |
| `vigilante01` | `Cecyte2026*` | VIGILANTE |

## Endpoints

### Autenticación

| Método | Ruta | Auth | Descripción |
|--------|------|:----:|-------------|
| `POST` | `/api/auth/login` | — | Retorna JWT, username y rol |

### Visitantes

| Método | Ruta | Auth | Descripción |
|--------|------|:----:|-------------|
| `GET` | `/api/visitantes` | JWT | Lista todos los visitantes |
| `GET` | `/api/visitantes/{id}` | JWT | Busca visitante por ID |
| `POST` | `/api/visitantes` | JWT | Registra un nuevo visitante |
| `PUT` | `/api/visitantes/{id}` | JWT | Actualiza nombre, motivo y MAC (sin modificar fechas) |
| `PATCH` | `/api/visitantes/{id}/salida` | JWT | Registra la hora de salida del visitante |
| `DELETE` | `/api/visitantes/{id}` | JWT | Elimina un visitante |

### Usuarios *(solo ADMIN)*

| Método | Ruta | Auth | Descripción |
|--------|------|:----:|-------------|
| `GET` | `/api/usuarios` | JWT + ADMIN | Lista todos los usuarios |
| `GET` | `/api/usuarios/{id}` | JWT + ADMIN | Busca usuario por ID |
| `POST` | `/api/usuarios` | JWT + ADMIN | Crea un nuevo usuario |
| `PUT` | `/api/usuarios/{id}` | JWT + ADMIN | Actualiza datos de usuario |
| `DELETE` | `/api/usuarios/{id}` | JWT + ADMIN | Elimina un usuario |

### Auditoría *(solo ADMIN)*

| Método | Ruta | Auth | Descripción |
|--------|------|:----:|-------------|
| `GET` | `/api/audit` | JWT + ADMIN | Retorna todos los eventos ordenados por fecha desc. |

---

### Ejemplo — Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "Admin2026*"
}
```

**Respuesta:**
```json
{
  "token": "<jwt>",
  "username": "admin",
  "rol": "ADMIN",
  "mensaje": "Autenticación exitosa"
}
```

### Ejemplo — Registrar visitante

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

### Ejemplo — Registrar salida

```http
PATCH /api/visitantes/1/salida
Authorization: Bearer <token>
```

**Respuesta:** objeto `Visitante` con `fechaSalida` actualizada.  
Si ya tenía salida registrada → `409 Conflict`.

### Ejemplo — Crear usuario (ADMIN)

```http
POST /api/usuarios
Authorization: Bearer <token>
Content-Type: application/json

{
  "username": "recepcion02",
  "password": "Segura2026*",
  "nombreCompleto": "María López",
  "rol": "RECEPCIONISTA"
}
```

### Ejemplo — Entrada de auditoría

```json
{
  "id": 1,
  "usuario": "admin",
  "accion": "REGISTRAR_VISITANTE",
  "detalle": "Visitante registrado: Juan Pérez García (ID 1)",
  "timestamp": "2026-03-11T17:45:00"
}
```

**Acciones registradas automáticamente:**

| Acción | Disparador |
|--------|------------|
| `REGISTRAR_VISITANTE` | POST /api/visitantes |
| `ACTUALIZAR_VISITANTE` | PUT /api/visitantes/{id} |
| `SALIDA_VISITANTE` | PATCH /api/visitantes/{id}/salida |
| `ELIMINAR_VISITANTE` | DELETE /api/visitantes/{id} |
| `CREAR_USUARIO` | POST /api/usuarios |
| `ACTUALIZAR_USUARIO` | PUT /api/usuarios/{id} |
| `ELIMINAR_USUARIO` | DELETE /api/usuarios/{id} |

## Validaciones

- `nombreCompleto` y `motivoVisita`: campo requerido (`@NotBlank`)
- `macAddress`: formato `XX:XX:XX:XX:XX:XX` o `XX-XX-XX-XX-XX-XX` validado con regex, valor único en la base de datos
- `username`: único por usuario
- Al actualizar visitante, `fechaRegistro` y `fechaSalida` no se modifican
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

| Ruta | Descripción |
|------|-------------|
| `http://localhost:8080/` | Login |
| `http://localhost:8080/recepcion.html` | Módulo de recepción |
| `http://localhost:8080/admin.html` | Panel de administración (solo ADMIN) |
| `http://localhost:8080/h2-console` | Consola H2 (JDBC URL: `jdbc:h2:mem:cecytedb`) |

## Seguridad

- Contraseñas almacenadas con **BCrypt**
- Tokens JWT firmados con **HS256** (clave mínimo 256 bits), incluyen `username` y `rol` como claims
- Rol embebido en el token — la autorización no requiere consulta adicional a la BD por request
- `@EnableMethodSecurity` + `@PreAuthorize("hasRole('ADMIN')")` protegen rutas de admin a nivel de controlador
- API **stateless** — sin sesiones en servidor
- CORS configurado para `localhost` en desarrollo
- `GlobalExceptionHandler` centraliza errores (400, 401, 403, 404, 409, 500) sin exponer stack traces al cliente

## Frontend

### `index.html` — Login
Formulario de acceso. Al autenticarse, guarda `jwt_token`, `user_rol` y `username` en `localStorage` y redirige según el rol.

### `recepcion.html` — Módulo de recepción
Accesible por todos los roles autenticados.

- **4 contadores:** Total registrados · Dentro ahora · Hoy · Con salida
- **Formulario dual:** modo registrar / modo editar (con banner azul y botón Cancelar)
- **Tabla de visitantes:** columna Estado (verde "Dentro" / hora de salida), botón **✓ Salida** (si aún está dentro), botón **Editar**, botón **Eliminar**
- **Confirmaciones** mediante modales propios del sistema (sin `alert()` / `confirm()` del navegador)
- **Guía MAC Address:** panel expandible con instrucciones paso a paso para Windows, Android, iOS y macOS
- **Enlace "Panel Admin"** visible en el header únicamente para usuarios con rol ADMIN

### `admin.html` — Panel de administración
Accesible únicamente por usuarios con rol ADMIN (guard en JS; redirige a recepción si no aplica).

- **Sidebar de navegación** con secciones Gestión y Seguridad, contadores en vivo
- **Sección Usuarios:** formulario crear/editar con modo dual, tabla con roles (color-coded), modales de confirmación
- **Sección Bitácora:** tabla de auditoría con badges de colores por tipo de acción (verde = registro, rojo = eliminación, azul = actualización, amarillo = salida)

### `app.js` — Capa de comunicación con la API
Centraliza todas las llamadas HTTP. Métodos disponibles:

```javascript
Api.login(username, password)
Api.listarVisitantes()
Api.buscarPorId(id)
Api.registrarVisitante(data)
Api.actualizarVisitante(id, data)
Api.registrarSalida(id)
Api.eliminarVisitante(id)
Api.listarUsuarios()
Api.crearUsuario(data)
Api.actualizarUsuario(id, data)
Api.eliminarUsuario(id)
Api.listarAuditLogs()
```

## Propuesta VLAN

Propuesta Técnica: Red para Visitantes
1. Segmentación Lógica (VLANs)
Para separar el tráfico físico en redes virtuales independientes, se proponen dos VLANs principales:
•	VLAN 10 (Administrativa): Red privada para los empleados del CECyTE, computadoras de recepción.
•	VLAN 20 (Visitantes): Red aislada. Aquí se conectarán los celulares o laptops de los invitados a través de una red WiFi específica para ellos (ej. SSID: CECyTE-Invitados).
2. MAC Filtering
Capturar la MAC Address en el sistema es nuestra llave de acceso a la red.
Se propone usar Filtrado MAC (MAC Filtering) en el Access Point (WiFi) o Router de los visitantes. El cual funcionaría como una white list:
1.	El visitante se conecta al WiFi de invitados.
2.	El dispositivo no tiene internet por defecto.
3.	El recepcionista registra la MAC.
4.	El administrador de red permite esa MAC en el router.
5.	Solo las MACs registradas en la base de datos pueden navegar. Dispositivos no registrados son bloqueados automáticamente en la capa de red.
3. Reglas de Aislamiento 
Para asegurar que un visitante con conocimientos técnicos no intente acceder a los servidores de la escuela, se configuran las siguientes reglas básicas en el Firewall o Router principal:
•	PERMITIR: Tráfico desde la VLAN 20 (Visitantes) exclusivamente hacia Internet.
•	DENEGAR: Cualquier intento de conexión desde la VLAN 20 (Visitantes) hacia la VLAN 10 (Administrativa).
•	DENEGAR: Acceso a Internet a cualquier MAC Address de la VLAN 20 que no esté en la base de datos de recepción.


