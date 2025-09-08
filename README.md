# VitalCare - Manual de Proyecto

VitalCare es una plataforma de gestión médica diseñada para administrar usuarios (pacientes, doctores y personal administrativo), programación de citas médicas, notificaciones y manejo de ubicaciones médicas. Este documento sirve como guía de comprensión del proyecto, su estructura y funcionalidades.

---

## 1. Visión General del Proyecto

- **Backend**: Java Spring Boot, expone una API RESTful.  
- **Frontend**: React / Next.js, consume la API del backend y ofrece la interfaz de usuario.  
- **Base de datos**: PostgreSQL, almacena toda la información de usuarios, citas, notificaciones, ciudades y sitios médicos.  

El proyecto se encuentra desplegado en Render, incluyendo backend, frontend y base de datos, lo que permite que los servicios estén accesibles de forma remota.

---

## 2. Estructura del Proyecto

La organización general del código es la siguiente:


- **Controllers**: gestionan las solicitudes HTTP y llaman a los servicios correspondientes.  
- **Services**: contienen la lógica de negocio del sistema.  
- **Repositories**: realizan la interacción con la base de datos mediante JPA.  
- **Model**: define las entidades y sus relaciones.  
- **DTO**: objetos de transferencia de datos entre backend y frontend.  
- **Mapper**: convierte entre entidades y DTOs.  
- **Utils**: utilidades como JWT, generación y validación de contraseñas.  

---

## 3. Configuración y Variables de Entorno

El sistema requiere ciertas configuraciones para operar correctamente:

- **JWT**: claves y tiempos de expiración para tokens de acceso y refresh.  
- **Database**: URL, usuario y contraseña de PostgreSQL.  
- **Email**: servidor SMTP para envío de notificaciones por correo.  

Estas configuraciones se gestionan mediante variables de entorno, lo que permite mantener seguras las credenciales y parámetros sensibles.

---

## 4. Funcionalidades Principales

### Autenticación y Usuarios

- Registro de usuarios por rol (`PATIENT`, `DOCTOR`, `STAFF`).  
- Login con generación de JWT (access y refresh tokens).  
- Renovación de tokens y recuperación de usuario actual mediante JWT.  
- Cambio de contraseña con validación de fortaleza (mínimo 8 caracteres, mayúscula, minúscula y número).  
- Gestión de usuarios: creación, actualización, eliminación y consulta de usuarios.

### Perfiles

- **Pacientes**: información personal, teléfono, dirección, ciudad, fecha de nacimiento, tipo de sangre.  
- **Doctores**: especialidad, número de licencia y apellidos.  
- **Staff**: departamento y cargo dentro de la institución.

### Citas Médicas

- Creación de citas asignando paciente, doctor y fecha.  
- Reprogramación y cancelación de citas.  
- Confirmación de asistencia (cambia el estado a COMPLETED).  
- Búsqueda de citas por paciente o doctor.  
- Citas vinculadas opcionalmente a un sitio médico específico.  

### Notificaciones

- Envío de notificaciones por correo electrónico.  
- Tipos de notificación configurables (actualmente EMAIL).  
- Eventos que disparan notificaciones: creación de cita, reprogramación y cancelación.  
- Las notificaciones se almacenan en la base de datos y pueden ser consultadas por usuario.

---

## 5. Seguridad

- **JWT**: tokens para autenticación de endpoints.  
- **Roles**: control de acceso según `PATIENT`, `DOCTOR` o `STAFF`.  
- **Contraseñas**: almacenadas cifradas con BCrypt.  
- **Política de seguridad**: las contraseñas deben cumplir criterios de fortaleza.  
- **Protección de endpoints**: cada recurso se valida contra el rol del usuario para permitir o denegar el acceso.

---

## 6. Base de Datos

- Motor: PostgreSQL.  
- Tablas principales: `users`, `roles`, `patients`, `doctors`, `staff`, `appointments`, `notifications`, `cities`, `sites`.  
- Relaciones clave:
  - 1:N entre `User` y `PatientProfile/DoctorProfile/StaffProfile`.
  - N:1 entre `Appointment` y `PatientProfile/DoctorProfile`.
  - 1:N entre `Notification` y `User` (destinatario).  

- La base de datos contiene todos los registros de usuarios, citas y notificaciones, permitiendo un historial completo y trazable de la actividad del sistema.

---

## 7. API / Endpoints

El backend expone endpoints REST para el manejo del sistema. Algunos ejemplos:

- **Autenticación**
  - POST `/auth/register`: registro de usuario.
  - POST `/auth/login`: login y obtención de JWT.
  - POST `/auth/refresh`: renovar access token.
  - GET `/auth/me`: información del usuario autenticado.

- **Usuarios y Perfiles**
  - GET `/users`: obtener todos los usuarios.
  - GET `/patients`, `/doctors`, `/staff`: listar perfiles.
  - PUT `/users/{id}`: actualizar usuario.
  - DELETE `/users/{id}`: eliminar usuario.

- **Citas**
  - POST `/appointments`: crear cita.
  - PUT `/appointments/{id}`: reprogramar cita.
  - DELETE `/appointments/{id}`: cancelar cita.
  - GET `/appointments/patient/{id}`: citas por paciente.
  - GET `/appointments/doctor/{id}`: citas por doctor.

- **Ciudades**
  - GET `/cities`: listar todas las ciudades.

- **Notificaciones**
  - GET `/notifications/user/{id}`: listar notificaciones de un usuario.

---

## 8. Frontend

- Construido con React y Next.js.  
- Consume los endpoints REST del backend para mostrar datos dinámicos.  
- Maneja autenticación JWT, guardando los tokens en localStorage para llamadas subsecuentes.  
- Interfaz adaptada a roles, mostrando solo la información relevante según usuario.

---

## 9. Despliegue

- Backend, frontend y base de datos están desplegados en Render.  
- Cada servicio tiene su URL pública y se comunica mediante HTTPS.  
- Las variables de entorno se configuran desde el panel de Render para mantener la seguridad y consistencia de las credenciales.  
- El backend y frontend se pueden escalar según demanda.

---

## 10. Notas Generales

- El sistema está preparado para extenderse con nuevos tipos de notificaciones (SMS, Push).  
- La arquitectura separa claramente capas de control, negocio y datos, facilitando mantenibilidad.  
- Se recomienda mantener sincronizadas las versiones de frontend y backend para evitar incompatibilidades de DTOs.  
- La base de datos puede migrarse a Supabase para mejorar tiempos de respuesta y optimizar consultas.

---

