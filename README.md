
# La Casa de JoJo

## Descripción del proyecto

Aplicación móvil para la gestión digital de comandas y pedidos de menú diario de La Casa de JoJo SpA.

El sistema busca optimizar la administración del menú, la recepción de pedidos y la coordinación entre clientes, administradores y operadores de caja.

## Tecnologías

### Aplicación móvil
- Kotlin
- Jetpack Compose
- Material Design 3
- Arquitectura MVVM
- Retrofit
- Room

### Backend
- Java 17
- Spring Boot
- Spring Cloud Gateway
- Spring Data JPA
- MySQL
- Flyway
- Swagger / OpenAPI
- Maven

## Estructura del proyecto

- android: Aplicación móvil.
- backend: Microservicios del sistema.

## Microservicios planificados

| Microservicio | Responsabilidad | Puerto |
|---|---|---|
| ms-gateway | Enrutamiento de solicitudes | 8080 |
| ms-menu | Gestión del menú y platos | 8081 |
| ms-pedidos | Gestión de clientes y comandas | 8082 |

## Asignatura

Desarrollo de Aplicaciones Móviles - DSY1105

Duoc UC

## Estado del proyecto

En desarrollo.
