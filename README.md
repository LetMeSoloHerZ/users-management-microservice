# Users Management — Spring Boot, Arquitectura Hexagonal y DDD

Aplicación de gestión de usuarios construida con Java 17 y Spring Boot. La API REST es el punto de entrada activo. El código de la antigua CLI se conserva como adaptador inactivo y no posee un contenedor de dependencias independiente.

Spring es el único *composition root*: `Main` inicia el contexto y las dependencias se resuelven mediante configuración y component scanning de Spring.

## Verificación

`GET /health` es publico y responde HTTP 200 con los metadatos generados desde
`pom.xml` durante el build:

```json
{"status":"UP","name":"users-management","version":"2.2.0"}
```

Indica que la API responde; no comprueba MySQL ni el proveedor de correo.

```bash
./mvnw clean test
./mvnw clean package
```

En Windows se puede utilizar `mvnw.cmd`.

## Despliegue en Render

El archivo `render.yaml` define el servicio web, el build con Docker y el despliegue
automático de cada commit que llegue a la rama `main`.

1. Crear un Blueprint en Render y seleccionar este repositorio.
2. Completar en el panel los secretos marcados como requeridos: `DB_HOST`,
   `DB_USERNAME`, `DB_PASSWORD`, `SMTP_USERNAME`, `SMTP_PASSWORD` y
   `SMTP_FROM_ADDRESS`.
3. Usar una instancia MySQL accesible desde Internet o desde la red privada de
   Render y ejecutar `src/main/resources/schema.sql` una vez para crear el esquema.
4. Desplegar el Blueprint. La API quedará disponible en el subdominio
   `onrender.com` asignado por Render y Swagger UI en `/swagger-ui.html`.

Las credenciales nunca deben guardarse en `application.properties` ni en
`render.yaml`. Para desarrollo local, deben proporcionarse como variables de entorno.

## Ejercicio 21 — Entidad Aplicacion

Se agrego el corte vertical completo de **Aplicacion** (dominio, aplicacion, persistencia JDBC y API REST)
siguiendo la misma arquitectura hexagonal que `User`.

| Metodo | Ruta | Rol |
|---|---|---|
| POST | `/api/aplicaciones` | ADMIN |
| GET | `/api/aplicaciones` y `/api/aplicaciones/{id}` | ADMIN, REVIEWER |
| PUT | `/api/aplicaciones/{id}` | ADMIN |
| DELETE | `/api/aplicaciones/{id}` | ADMIN |

Reglas de negocio: bits 32 o 64; el lenguaje secundario debe diferir del principal; licencias
`GRATUITA`/`OPEN_SOURCE` exigen precio 0 y `COMERCIAL` exige precio mayor que 0; el nombre es unico.

### Ejecutar en local

```bash
docker compose up -d mysql          # crea la BD con src/main/resources/schema.sql
export DB_USERNAME=users_app DB_PASSWORD=local_users_password
./mvnw spring-boot:run              # Swagger en http://localhost:8080/swagger-ui.html
```

Login: `POST /api/auth/login` con el administrador inicial y pulsar **Authorize** en Swagger con el token.

### Pruebas de API

La coleccion `postman/Ejercicio21-Aplicaciones-Spring.postman_collection.json` se importa en Postman
(variable `baseUrl`) y tambien se ejecuta con Newman dentro del workflow de CI.

### CI/CD

- `.github/workflows/ci.yml`: compila y prueba con Maven, construye la imagen Docker y ejecuta la coleccion de Postman contra la API con MySQL.
- `.github/workflows/deploy.yml`: tras un CI exitoso en `main` dispara el Deploy Hook de Render (secreto `RENDER_DEPLOY_HOOK_URL`).
- Base de datos en la nube: `database/database_cloud.sql` (TiDB Cloud). Las credenciales se configuran como variables de entorno en Render.
