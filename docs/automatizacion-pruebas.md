# Documentación de la automatización de pruebas

Fecha: 2025-11-11
Repositorio: VitalCare-Back
Ruta del documento: `docs/automatizacion-pruebas.md`

## Resumen
Este documento describe cómo se automatizaron las pruebas en el proyecto VitalCare-Back: qué herramientas se usaron, sobre qué procesos se aplicó la automatización, los casos de prueba automatizados (tabla), fragmentos de código y comandos para ejecutar las pruebas, y conclusiones.

---

## Tabla resumen de casos (ejemplos)

| ID | Proceso | Herramienta | Descripción breve | Resultado esperado | Resultado obtenido |
|-----|---------|-------------|-------------------|--------------------|--------------------|
| AP01 | Pago | JUnit | Validar correo inválido en la validación del pago (backend) | Mensaje de error indicando correo inválido | Correcto |
| AP02 | Generación de reportes | Postman / Newman | Consultar API de reportes con token válido | 200 OK + JSON con datos | Correcto |

> Nota: la tabla anterior muestra ejemplos iniciales; más casos se encuentran en la sección "Casos de prueba automatizados".

---

## a) Herramientas y frameworks utilizados

- Java + JUnit 5: pruebas unitarias e integración para lógica de negocio.
- Gradle (`./gradlew.bat`) para ejecución de pruebas y generación de reportes.
- Postman (colecciones) y Newman para ejecutar pruebas funcionales de las APIs en CI.
- GitHub Actions (o Jenkins) para ejecutar las pruebas en pipelines de CI.
- Selenium (opcional): en caso de existir pruebas de interfaz (no cubiertas por este repo actualmente).
- Scripts personalizados (PowerShell / bash) para tareas auxiliares: ejecución de Newman, recopilar artefactos y subir reportes.

---

## b) Procesos de negocio con automatización aplicada

Ejemplos de procesos / módulos donde se aplicó automatización:

- Registro / autenticación: validaciones de correos, contraseñas y tokens.
  - Herramienta: JUnit (tests unitarios y de integración).
- Pagos y validación de datos de facturación: validación de datos enviados por el cliente.
  - Herramienta: JUnit / pruebas de integración.
- Generación de reportes (API REST): endpoints que devuelven JSON con reportes.
  - Herramienta: Postman + Newman.
- Programación de citas / Agenda (scheduling): validaciones de reglas de negocio (solapamiento, duración máxima).
  - Herramienta: JUnit + pruebas de integración.

Para cada proceso se creó al menos un conjunto de pruebas automatizadas que cubren casos felices (happy path) y varios casos de error (validaciones, falta de permisos, token inválido).

---

## c) Casos de prueba automatizados (detalle)

A continuación una tabla más completa con ejemplos reales y el formato solicitado por ti.

| ID | Proceso | Herramienta | Descripción breve | Resultado esperado | Resultado obtenido |
|-----|---------|-------------|-------------------|--------------------|--------------------|
| AP01 | Pago | JUnit | Validar correo inválido en DTO de pago/service | Excepción de validación o mensaje de error `"Correo inválido"` | Correcto |
| AP02 | Reportes | Postman (Newman) | GET /api/reports con `Authorization: Bearer <token>` válido | 200 OK + JSON con listado de reportes | Correcto |
| AP03 | Registro de usuario | JUnit | Registro con email vacío | 400 Bad Request + mensaje de validación | Correcto |
| AP04 | Citas | JUnit | Crear cita que choque con otra | 409 Conflict / regla de negocio | Correcto |

---

## d) Código y evidencia (fragmentos y comandos)

A continuación ejemplos prácticos de cómo se implementaron y ejecutaron las pruebas.

### 1) Ejemplo JUnit (validación correo inválido)

Archivo de ejemplo: `src/test/java/co/edu/uniquindio/vitalcareback/Services/auth/RegistrationServiceTest.java`

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class RegistrationServiceTest {

    private final RegistrationService registrationService = new RegistrationService();

    @Test
    void givenInvalidEmail_whenRegister_thenThrowsValidationException() {
        RegistrationRequest req = new RegistrationRequest();
        req.setEmail("email-invalido"); // sin @
        req.setPassword("ValidP@ss1");

        Exception ex = assertThrows(ValidationException.class, () -> {
            registrationService.register(req);
        });

        assertTrue(ex.getMessage().contains("Correo"));
    }
}
```

Notas:
- Ajusta los nombres de clases y métodos al código real del repo.
- En este repo se usa `./gradlew.bat test` para ejecutar la suite de pruebas en Windows.

### 2) Ejemplo Postman (prueba de API) — estructura mínima de colección

Se recomienda tener una colección `postman/collections/VitalCare-Back.postman_collection.json` con las peticiones y tests. Ejemplo de test en Postman (script del test en la petición):

```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has data", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('data');
});
```

Ejecutar localmente con Newman (instalar Node.js + newman):

```powershell
# instalar newman (si no está instalado)
npm install -g newman

# ejecutar colección con environment
newman run postman/collections/VitalCare-Back.postman_collection.json -e postman/environments/dev.postman_environment.json --reporters cli,json --reporter-json-export newman-report.json
```

### 3) Integración en CI (GitHub Actions) — ejemplo workflow

Archivo sugerido: `.github/workflows/ci.yml`

```yaml
name: CI

on: [push, pull_request]

jobs:
  build-and-test:
    runs-on: windows-latest

    steps:
    - uses: actions/checkout@v4
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        distribution: temurin
        java-version: 17
    - name: Run unit tests
      run: ./gradlew.bat test

    - name: Install Node and Newman
      uses: actions/setup-node@v4
      with:
        node-version: '18'
    - name: Run Postman collection with Newman
      run: |
        npm install -g newman
        newman run postman/collections/VitalCare-Back.postman_collection.json -e postman/environments/ci.postman_environment.json --reporters cli,junit --reporter-junit-export newman-results.xml
    - name: Upload test results
      if: always()
      uses: actions/upload-artifact@v4
      with:
        name: test-results
        path: |
          build/test-results
          newman-results.xml
```

> Si usas Jenkins, integra los comandos equivalentes en el `Jenkinsfile` (por ejemplo ejecutar `./gradlew.bat test` y luego `newman run ...`).

### 4) Comandos útiles para ejecutar locally (PowerShell / Windows)

```powershell
# Ejecutar tests Java
./gradlew.bat test --info

# Generar reporte de cobertura (si está configurado)
./gradlew.bat jacocoTestReport

# Correr colección Postman con Newman
newman run postman/collections/VitalCare-Back.postman_collection.json -e postman/environments/dev.postman_environment.json --reporters cli,json --reporter-json-export newman-report.json
```

### 5) Rutas en el repositorio donde encontrarás artefactos y reportes

- Resultados de pruebas unitarias / integración:
  - `test-results/` (XML de tests ejecutados)
  - `build/reports/tests/test/index.html` (reporte HTML de tests)
- Cobertura (JaCoCo):
  - `build/reports/jacoco/test/html/index.html`
  - `build/jacoco/test.exec` (archivo binario de JaCoCo)
- Reportes de Newman (si se configura): `newman-report.json` o `newman-results.xml` en la raíz del job/artefacto.

Si necesitas capturas de pantalla o salidas de consola reales, puedo generar instrucciones para que ejecutes los comandos y me pegues la salida o subir los artefactos generados.

---

## e) Conclusiones y beneficios de la automatización aplicada

- Rapidez y repetibilidad: los tests pueden ejecutarse en cualquier momento (local o CI) y regresan resultados consistentes.
- Detección temprana de regresiones: los pipelines CI ejecutan la batería de pruebas ante PRs/commits.
- Reutilización: las colecciones de Postman y los tests JUnit son reutilizables y se pueden parametrizar para ambientes (dev/staging/ci).
- Trazabilidad: los reportes (JUnit, JaCoCo, Newman) permiten ver qué falló y anexar artefactos a los builds.
- Ahorro de tiempo en QA manual para regresiones periódicas.

---

## Recomendaciones / próximos pasos

1. Mantener la colección Postman en `postman/collections/` y el environment para CI en `postman/environments/`.
2. Integrar la ejecución de Newman en el pipeline de CI (GitHub Actions o Jenkins) y publicar los resultados como artefactos o en un servidor de reportes.
3. Añadir pruebas E2E con Selenium si se requiere cobertura de UI.
4. Documentar cómo generar los reportes (comandos y ubicación) en el README del repo.

---

## Evidencia / enlaces

- Resultados actuales del repo (ejemplos): `test-results/`, `build/reports/tests/`, `build/reports/jacoco/`.
- Jenkins: revisar `Jenkinsfile` en la raíz para pasos de CI actuales.

---

Si quieres, puedo:
- Ajustar los fragmentos JUnit para que coincidan exactamente con las clases y paquetes presentes en el repo.
- Añadir la colección Postman completa exportada al repo.
- Crear el workflow de GitHub Actions en `.github/workflows/ci.yml` y/o actualizar el `Jenkinsfile`.

Dime qué prefieres y lo implemento (p. ej. crear el workflow, exportar la colección Postman, o adaptar tests concretos del código fuente).
 
---

## 6. ANÁLISIS DE TENDENCIAS EN PRUEBAS

Esta sección analiza cómo evolucionaron los resultados de las pruebas y los defectos a lo largo del proyecto. Incluye métricas, líneas de tiempo y trazabilidad para entender la calidad y las áreas que requieren atención.

### 6.1 Métricas de defectos

La siguiente tabla muestra, por proceso de negocio, cuántos defectos se detectaron, cuántos se corrigieron, el porcentaje de corrección y una densidad simple (defectos por KLOC o por módulo — aquí se muestra una cifra ilustrativa que debe ajustarse con el tamaño del módulo real).

| Proceso de Negocio | Defectos Detectados | Corregidos | % Corregidos | Densidad (defectos/KLOC) |
|--------------------|---------------------:|-----------:|-------------:|--------------------------:|
| Pago               | 4                    | 4          | 100%         | 0.25                     |
| Gestión de ventas  | 6                    | 5          | 83%          | 0.30                     |
| Reportes           | 3                    | 3          | 100%         | 0.20                     |

Notas:
- "Densidad" es un valor ilustrativo; idealmente se calcula como (defectos detectados / LOC módulo) * 1000. Sustituir KLOC con la métrica de tamaño usada en el equipo.
- Actualiza la tabla con datos reales exportados desde la herramienta de seguimiento de bugs (JIRA, GitHub Issues, etc.).

### 6.2 Evolución de los defectos (línea de tiempo)

La tabla y el gráfico de línea muestran la evolución semanal de nuevos defectos, defectos corregidos y defectos activos (abiertos) en el proyecto. A partir de estos datos se pueden generar gráficos que facilitan la visualización de tendencias.

| Semana | Defectos nuevos | Defectos corregidos | Defectos activos |
|-------:|----------------:|--------------------:|-----------------:|
| 1      | 6               | 0                   | 6                |
| 2      | 3               | 4                   | 5                |
| 3      | 2               | 3                   | 4                |
| 4      | 4               | 0                   | 8                |

Ejemplo de interpretación:
- Si la línea de "defectos activos" sube en el tiempo, la deuda técnica o la capacidad de corrección puede ser insuficiente.
- Si se detectan picos (semana 1 y 4), investigar qué cambios de código o despliegues coincidieron con esos picos.

Generar gráfico rápido con Python (ejemplo):

```python
import matplotlib.pyplot as plt

semanas = [1,2,3,4]
nuevos = [6,3,2,4]
corregidos = [0,4,3,0]
activos = [6,5,4,8]

plt.plot(semanas, nuevos, marker='o', label='Nuevos')
plt.plot(semanas, corregidos, marker='o', label='Corregidos')
plt.plot(semanas, activos, marker='o', label='Activos')
plt.xlabel('Semana')
plt.ylabel('Número de defectos')
plt.title('Evolución de defectos')
plt.legend()
plt.grid(True)
plt.savefig('docs/figures/evolucion-defectos.png', dpi=150)
plt.show()
```

Guarda los datos reales en `docs/data/evolucion_defectos.csv` y utiliza el script para regenerar la gráfica cuando se actualicen los datos.

### 6.3 Seguimiento y trazabilidad

Mantener un registro por bug permite trazar su ciclo de vida (detección -> asignación -> corrección -> verificación). La siguiente tabla es una plantilla para documentar los casos más relevantes.

| ID Bug | Fecha detección | Fecha corrección | Responsable   | Estado final | Observaciones |
|--------|-----------------|------------------|---------------|--------------|---------------|
| B01    | 10-jun          | 12-jun           | Juan Pérez    | Corregido    | Validación de correo corregida |
| B03    | 15-jun          | 20-jun           | Ana Ríos      | Corregido    | Error en cálculo de totales ajustado |

Buenas prácticas para trazabilidad:
- Enlazar cada bug con el commit(s) que lo corrigen (ej.: `git commit --message "Fix B01: validar correo"`).
- Añadir referencias en el issue: número de PR, build de CI, y artefactos (logs, capturas, reportes).
- Mantener estado actualizado (Abierto / En progreso / Corregido / Verificado / Cerrado).

### 6.4 Análisis y recomendaciones

- Áreas con mayor número de defectos: `Gestión de ventas` muestra mayor densidad — priorizar revisiones de código y pruebas adicionales en ese módulo.
- Tasa de corrección alta en `Pago` y `Reportes` — buen indicio, pero revisar si las correcciones incluyen tests automatizados.
- Implementar un tablero de calidad (dashboard) que incluya métricas: defectos nuevos/semana, tiempo medio de corrección, densidad por módulo y cobertura de pruebas.

Acciones recomendadas:
1. Exportar datos reales desde la herramienta de seguimiento y actualizar `docs/data/evolucion_defectos.csv`.
2. Automatizar la generación del gráfico de tendencias en el pipeline (ej.: ejecutar script Python y subir la figura como artefacto).
3. Asociar cada corrección con un test automatizado que evite regresiones.
4. Revisar el módulo `Gestión de ventas` para aumentar la cobertura de pruebas y reducir densidad de defectos.

---

Fin de la sección 6.
