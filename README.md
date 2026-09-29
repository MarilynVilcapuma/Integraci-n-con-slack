# S08 | AP3 | RETO 3 — Gestión de Productos

Proyecto Spring Boot de gestión de productos, persistido en PostgreSQL (Neon), desarrollado
para demostrar pruebas unitarias, pruebas parametrizadas (JUnit 5 + Mockito), cobertura de
código con JaCoCo, calidad de código y automatización con Jenkins.

## Funcionalidades implementadas

`ProductoService` (interfaz) / `ProductoServiceImpl` (implementación):

- `crearProducto(codigo, nombre, precio, stock)` — valida y registra un producto a través de `ProductoRepository`.
- `validarNombre(nombre)` — el nombre no puede ser nulo ni vacío.
- `validarPrecio(precio)` — el precio debe ser mayor que cero.
- `validarStock(stock)` — el stock no puede ser negativo.
- `calcularPrecioFinal(precio, descuentoPorcentaje)` — aplica un descuento (0–100 %).
- `tieneStockDisponible(stock)` — indica si hay unidades disponibles (stock > 0).

`ProductoRepository` (Spring Data JPA, interfaz) es la dependencia de persistencia real
sobre PostgreSQL. En las pruebas unitarias de la capa de servicio se sustituye por un
**Mock de Mockito** (`@Mock` + `@InjectMocks`) para aislar `ProductoServiceImpl` de la base
de datos real, siguiendo el patrón enseñado en la sesión (PedidoService / ProductoService
del material del curso).

## Estructura del proyecto

```
S08_AP3_RETO3/
├── pom.xml
├── Jenkinsfile
├── src/main/java/com/reto3/productos/
│   ├── GestionProductosApplication.java   # Clase principal Spring Boot
│   ├── model/
│   │   └── Producto.java                  # Entidad JPA
│   ├── repository/
│   │   └── ProductoRepository.java        # Spring Data JpaRepository (mockeado en pruebas)
│   ├── service/
│   │   ├── ProductoService.java           # Interfaz de negocio
│   │   └── impl/
│   │       └── ProductoServiceImpl.java   # Lógica de negocio
│   ├── rest/
│   │   └── ProductoController.java        # API REST (POST /api/productos)
│   └── exception/
│       └── ProductoInvalidoException.java
├── src/main/resources/
│   └── application.yml                    # Configuración + datasource PostgreSQL (Neon)
├── src/test/java/com/reto3/productos/
│   ├── model/ProductoTest.java                          # 5 pruebas de la entidad
│   ├── service/impl/ProductoServiceImplTest.java        # 13 pruebas unitarias (Mockito)
│   ├── service/impl/ProductoServiceParametrizedTest.java # 4 pruebas parametrizadas (19 casos)
│   └── rest/ProductoControllerTest.java                 # 2 pruebas de la capa REST (WebMvcTest)
└── docs/                                   # Documento y evidencias del reto
```

## Base de datos

El `datasource` apunta a una instancia PostgreSQL administrada por **Neon**. Las credenciales
ya **no** están en `application.yml`: se leen desde variables de entorno para poder subir el
repositorio sin exponer secretos. `spring.jpa.hibernate.ddl-auto: update` crea/actualiza
automáticamente la tabla `productos` al iniciar la aplicación.

Variables de entorno requeridas para levantar la aplicación:

| Variable      | Descripción                                                                 |
|---------------|------------------------------------------------------------------------------|
| `DB_URL`      | URL JDBC de la base, ej. `jdbc:postgresql://<host>/neondb?sslmode=require&channel_binding=require` |
| `DB_USERNAME` | Usuario de la base de datos                                                  |
| `DB_PASSWORD` | Contraseña de la base de datos                                               |

Si alguna falta, la aplicación no arranca (fail-fast) en lugar de arrancar mal configurada.

## Cómo ejecutar

Requiere JDK 17+ y Maven.

```bash
mvn clean verify
```

Este comando compila el proyecto, ejecuta las 39 pruebas (unitarias + parametrizadas + REST),
genera el reporte de JaCoCo en `target/site/jacoco/index.html` y verifica que la cobertura
de líneas sea de al menos el 80 % (regla configurada en `pom.xml`). **Ninguna prueba se
conecta a la base de datos real** (las dependencias se mockean), tal como recomienda el
material del curso.

Para levantar la aplicación (sí requiere conexión a la base de datos real), primero define las
variables de entorno y luego ejecuta `mvn spring-boot:run`:

```bash
# Linux/macOS/Git Bash
export DB_URL="jdbc:postgresql://<host>/neondb?sslmode=require&channel_binding=require"
export DB_USERNAME="neondb_owner"
export DB_PASSWORD="<tu-password>"
mvn spring-boot:run
```

```powershell
# PowerShell
$env:DB_URL="jdbc:postgresql://<host>/neondb?sslmode=require&channel_binding=require"
$env:DB_USERNAME="neondb_owner"
$env:DB_PASSWORD="<tu-password>"
mvn spring-boot:run
```

```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"codigo":"P001","nombre":"Teclado mecanico","precio":150.0,"stock":20}'
```

## Pruebas

- **Pruebas unitarias** (`ProductoServiceImplTest`, `ProductoTest`): 18 pruebas que cubren
  casos exitosos, inválidos (nombre vacío, precio ≤ 0, stock negativo) y límite (stock = 0,
  descuento en los extremos 0 y 100).
- **Pruebas parametrizadas** (`ProductoServiceParametrizedTest`): 4 métodos
  `@ParameterizedTest` con `@CsvSource` (19 combinaciones en total) para `validarPrecio`,
  `validarStock`, `tieneStockDisponible` y `calcularPrecioFinal`, evitando duplicar el
  método de prueba por cada escenario.
- **Pruebas de la capa REST** (`ProductoControllerTest`): usan `@WebMvcTest` + `@MockBean`
  para probar `ProductoController` sin levantar la base de datos real.
- Uso de Mockito: `@Mock` sobre `ProductoRepository`, `@InjectMocks` sobre
  `ProductoServiceImpl`, `when(...).thenReturn(...)` para programar respuestas y
  `verify(...)` para comprobar interacciones (incluye un caso `verify(..., never())`
  cuando la validación falla y no debe llamarse al repositorio).

## Cobertura (JaCoCo)

Meta exigida: 80 % de líneas. Resultado obtenido: **100 % de líneas** y **~96 % de ramas**
sobre `model`, `service.impl`, `rest` y `exception`. Solo se excluye del cálculo
`GestionProductosApplication` (clase de arranque de Spring Boot, sin lógica propia).

## Automatización con Jenkins

El `Jenkinsfile` (pipeline declarativo) define las etapas:

1. **Checkout** — obtiene el proyecto del repositorio.
2. **Compilar** — `mvn clean compile`.
3. **Pruebas unitarias y parametrizadas** — `mvn test`, publica resultados con `junit`.
4. **Reporte de cobertura (JaCoCo)** — `mvn jacoco:report`, publica el reporte con el
   plugin de JaCoCo para Jenkins.
5. **Verificar meta de cobertura** — `mvn jacoco:check` (falla el pipeline si no se
   alcanza el 80 %).

Requiere un Jenkins con los plugins **Maven Integration**, **JUnit** y **JaCoCo**, y las
herramientas `Maven3` / `JDK17` configuradas en *Global Tool Configuration* con esos
nombres (o ajustar los nombres en el bloque `tools {}` del `Jenkinsfile`). El pipeline
ejecuta `mvn test`/`jacoco:check`, que no requieren la base de datos real.
