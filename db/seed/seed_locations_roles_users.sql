-- Idempotent seed for locations (country/department/city/site), roles, and users with role assignments
-- Requires pgcrypto for gen_random_uuid() and crypt() (bcrypt)

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Country: Colombia
INSERT INTO countries (id, name, createdAt)
SELECT gen_random_uuid(), 'Colombia', now()
WHERE NOT EXISTS (SELECT 1 FROM countries WHERE name = 'Colombia');

-- Departments (linked to Colombia)
-- Antioquia
INSERT INTO departments (id, name, country_id, createdAt)
SELECT gen_random_uuid(), 'Antioquia', c.id, now()
FROM countries c
WHERE c.name = 'Colombia'
  AND NOT EXISTS (
    SELECT 1 FROM departments d WHERE d.name = 'Antioquia' AND d.country_id = c.id
  );

-- Valle del Cauca
INSERT INTO departments (id, name, country_id, createdAt)
SELECT gen_random_uuid(), 'Valle del Cauca', c.id, now()
FROM countries c
WHERE c.name = 'Colombia'
  AND NOT EXISTS (
    SELECT 1 FROM departments d WHERE d.name = 'Valle del Cauca' AND d.country_id = c.id
  );

-- Atlántico
INSERT INTO departments (id, name, country_id, createdAt)
SELECT gen_random_uuid(), 'Atlántico', c.id, now()
FROM countries c
WHERE c.name = 'Colombia'
  AND NOT EXISTS (
    SELECT 1 FROM departments d WHERE d.name = 'Atlántico' AND d.country_id = c.id
  );

-- Bolívar
INSERT INTO departments (id, name, country_id, createdAt)
SELECT gen_random_uuid(), 'Bolívar', c.id, now()
FROM countries c
WHERE c.name = 'Colombia'
  AND NOT EXISTS (
    SELECT 1 FROM departments d WHERE d.name = 'Bolívar' AND d.country_id = c.id
  );

-- Santander
INSERT INTO departments (id, name, country_id, createdAt)
SELECT gen_random_uuid(), 'Santander', c.id, now()
FROM countries c
WHERE c.name = 'Colombia'
  AND NOT EXISTS (
    SELECT 1 FROM departments d WHERE d.name = 'Santander' AND d.country_id = c.id
  );

-- Risaralda
INSERT INTO departments (id, name, country_id, createdAt)
SELECT gen_random_uuid(), 'Risaralda', c.id, now()
FROM countries c
WHERE c.name = 'Colombia'
  AND NOT EXISTS (
    SELECT 1 FROM departments d WHERE d.name = 'Risaralda' AND d.country_id = c.id
  );

-- Caldas
INSERT INTO departments (id, name, country_id, createdAt)
SELECT gen_random_uuid(), 'Caldas', c.id, now()
FROM countries c
WHERE c.name = 'Colombia'
  AND NOT EXISTS (
    SELECT 1 FROM departments d WHERE d.name = 'Caldas' AND d.country_id = c.id
  );

-- Magdalena
INSERT INTO departments (id, name, country_id, createdAt)
SELECT gen_random_uuid(), 'Magdalena', c.id, now()
FROM countries c
WHERE c.name = 'Colombia'
  AND NOT EXISTS (
    SELECT 1 FROM departments d WHERE d.name = 'Magdalena' AND d.country_id = c.id
  );

-- Tolima
INSERT INTO departments (id, name, country_id, createdAt)
SELECT gen_random_uuid(), 'Tolima', c.id, now()
FROM countries c
WHERE c.name = 'Colombia'
  AND NOT EXISTS (
    SELECT 1 FROM departments d WHERE d.name = 'Tolima' AND d.country_id = c.id
  );

-- Distrito Capital (Bogotá D.C.)
INSERT INTO departments (id, name, country_id, createdAt)
SELECT gen_random_uuid(), 'Bogotá D.C.', c.id, now()
FROM countries c
WHERE c.name = 'Colombia'
  AND NOT EXISTS (
    SELECT 1 FROM departments d WHERE d.name = 'Bogotá D.C.' AND d.country_id = c.id
  );

-- Cities mapped to their departments
-- Bogotá -> Bogotá D.C.
INSERT INTO cities (id, name, department_id, createdAt)
SELECT gen_random_uuid(), 'Bogotá', d.id, now()
FROM departments d
WHERE d.name = 'Bogotá D.C.'
  AND NOT EXISTS (
    SELECT 1 FROM cities ci WHERE ci.name = 'Bogotá' AND ci.department_id = d.id
  );

-- Medellín -> Antioquia
INSERT INTO cities (id, name, department_id, createdAt)
SELECT gen_random_uuid(), 'Medellín', d.id, now()
FROM departments d
WHERE d.name = 'Antioquia'
  AND NOT EXISTS (
    SELECT 1 FROM cities ci WHERE ci.name = 'Medellín' AND ci.department_id = d.id
  );

-- Cali -> Valle del Cauca
INSERT INTO cities (id, name, department_id, createdAt)
SELECT gen_random_uuid(), 'Cali', d.id, now()
FROM departments d
WHERE d.name = 'Valle del Cauca'
  AND NOT EXISTS (
    SELECT 1 FROM cities ci WHERE ci.name = 'Cali' AND ci.department_id = d.id
  );

-- Barranquilla -> Atlántico
INSERT INTO cities (id, name, department_id, createdAt)
SELECT gen_random_uuid(), 'Barranquilla', d.id, now()
FROM departments d
WHERE d.name = 'Atlántico'
  AND NOT EXISTS (
    SELECT 1 FROM cities ci WHERE ci.name = 'Barranquilla' AND ci.department_id = d.id
  );

-- Cartagena -> Bolívar
INSERT INTO cities (id, name, department_id, createdAt)
SELECT gen_random_uuid(), 'Cartagena', d.id, now()
FROM departments d
WHERE d.name = 'Bolívar'
  AND NOT EXISTS (
    SELECT 1 FROM cities ci WHERE ci.name = 'Cartagena' AND ci.department_id = d.id
  );

-- Bucaramanga -> Santander
INSERT INTO cities (id, name, department_id, createdAt)
SELECT gen_random_uuid(), 'Bucaramanga', d.id, now()
FROM departments d
WHERE d.name = 'Santander'
  AND NOT EXISTS (
    SELECT 1 FROM cities ci WHERE ci.name = 'Bucaramanga' AND ci.department_id = d.id
  );

-- Pereira -> Risaralda
INSERT INTO cities (id, name, department_id, createdAt)
SELECT gen_random_uuid(), 'Pereira', d.id, now()
FROM departments d
WHERE d.name = 'Risaralda'
  AND NOT EXISTS (
    SELECT 1 FROM cities ci WHERE ci.name = 'Pereira' AND ci.department_id = d.id
  );

-- Manizales -> Caldas
INSERT INTO cities (id, name, department_id, createdAt)
SELECT gen_random_uuid(), 'Manizales', d.id, now()
FROM departments d
WHERE d.name = 'Caldas'
  AND NOT EXISTS (
    SELECT 1 FROM cities ci WHERE ci.name = 'Manizales' AND ci.department_id = d.id
  );

-- Santa Marta -> Magdalena
INSERT INTO cities (id, name, department_id, createdAt)
SELECT gen_random_uuid(), 'Santa Marta', d.id, now()
FROM departments d
WHERE d.name = 'Magdalena'
  AND NOT EXISTS (
    SELECT 1 FROM cities ci WHERE ci.name = 'Santa Marta' AND ci.department_id = d.id
  );

-- Ibagué -> Tolima
INSERT INTO cities (id, name, department_id, createdAt)
SELECT gen_random_uuid(), 'Ibagué', d.id, now()
FROM departments d
WHERE d.name = 'Tolima'
  AND NOT EXISTS (
    SELECT 1 FROM cities ci WHERE ci.name = 'Ibagué' AND ci.department_id = d.id
  );

-- One site per city (simple placeholder data)
-- For each known city, create a default site if missing
INSERT INTO sites (id, name, address, city_id, createdAt)
SELECT gen_random_uuid(), 'Central Clinic - Bogotá', 'Main St 123', ci.id, now()
FROM cities ci
JOIN departments d ON d.id = ci.department_id
WHERE ci.name = 'Bogotá' AND d.name = 'Bogotá D.C.'
  AND NOT EXISTS (
    SELECT 1 FROM sites s WHERE s.city_id = ci.id
  );

INSERT INTO sites (id, name, address, city_id, createdAt)
SELECT gen_random_uuid(), 'Central Clinic - Medellín', 'Main St 123', ci.id, now()
FROM cities ci
JOIN departments d ON d.id = ci.department_id
WHERE ci.name = 'Medellín' AND d.name = 'Antioquia'
  AND NOT EXISTS (
    SELECT 1 FROM sites s WHERE s.city_id = ci.id
  );

INSERT INTO sites (id, name, address, city_id, createdAt)
SELECT gen_random_uuid(), 'Central Clinic - Cali', 'Main St 123', ci.id, now()
FROM cities ci
JOIN departments d ON d.id = ci.department_id
WHERE ci.name = 'Cali' AND d.name = 'Valle del Cauca'
  AND NOT EXISTS (
    SELECT 1 FROM sites s WHERE s.city_id = ci.id
  );

INSERT INTO sites (id, name, address, city_id, createdAt)
SELECT gen_random_uuid(), 'Central Clinic - Barranquilla', 'Main St 123', ci.id, now()
FROM cities ci
JOIN departments d ON d.id = ci.department_id
WHERE ci.name = 'Barranquilla' AND d.name = 'Atlántico'
  AND NOT EXISTS (
    SELECT 1 FROM sites s WHERE s.city_id = ci.id
  );

INSERT INTO sites (id, name, address, city_id, createdAt)
SELECT gen_random_uuid(), 'Central Clinic - Cartagena', 'Main St 123', ci.id, now()
FROM cities ci
JOIN departments d ON d.id = ci.department_id
WHERE ci.name = 'Cartagena' AND d.name = 'Bolívar'
  AND NOT EXISTS (
    SELECT 1 FROM sites s WHERE s.city_id = ci.id
  );

INSERT INTO sites (id, name, address, city_id, createdAt)
SELECT gen_random_uuid(), 'Central Clinic - Bucaramanga', 'Main St 123', ci.id, now()
FROM cities ci
JOIN departments d ON d.id = ci.department_id
WHERE ci.name = 'Bucaramanga' AND d.name = 'Santander'
  AND NOT EXISTS (
    SELECT 1 FROM sites s WHERE s.city_id = ci.id
  );

INSERT INTO sites (id, name, address, city_id, createdAt)
SELECT gen_random_uuid(), 'Central Clinic - Pereira', 'Main St 123', ci.id, now()
FROM cities ci
JOIN departments d ON d.id = ci.department_id
WHERE ci.name = 'Pereira' AND d.name = 'Risaralda'
  AND NOT EXISTS (
    SELECT 1 FROM sites s WHERE s.city_id = ci.id
  );

INSERT INTO sites (id, name, address, city_id, createdAt)
SELECT gen_random_uuid(), 'Central Clinic - Manizales', 'Main St 123', ci.id, now()
FROM cities ci
JOIN departments d ON d.id = ci.department_id
WHERE ci.name = 'Manizales' AND d.name = 'Caldas'
  AND NOT EXISTS (
    SELECT 1 FROM sites s WHERE s.city_id = ci.id
  );

INSERT INTO sites (id, name, address, city_id, createdAt)
SELECT gen_random_uuid(), 'Central Clinic - Santa Marta', 'Main St 123', ci.id, now()
FROM cities ci
JOIN departments d ON d.id = ci.department_id
WHERE ci.name = 'Santa Marta' AND d.name = 'Magdalena'
  AND NOT EXISTS (
    SELECT 1 FROM sites s WHERE s.city_id = ci.id
  );

INSERT INTO sites (id, name, address, city_id, createdAt)
SELECT gen_random_uuid(), 'Central Clinic - Ibagué', 'Main St 123', ci.id, now()
FROM cities ci
JOIN departments d ON d.id = ci.department_id
WHERE ci.name = 'Ibagué' AND d.name = 'Tolima'
  AND NOT EXISTS (
    SELECT 1 FROM sites s WHERE s.city_id = ci.id
  );

-- Roles (English)
INSERT INTO roles (id, name, createdAt)
SELECT gen_random_uuid(), 'PATIENT', now()
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'PATIENT');

INSERT INTO roles (id, name, createdAt)
SELECT gen_random_uuid(), 'DOCTOR', now()
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'DOCTOR');

INSERT INTO roles (id, name, createdAt)
SELECT gen_random_uuid(), 'STAFF', now()
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'STAFF');

-- Users (three per role) with bcrypt password using pgcrypto's crypt() and gen_salt('bf')
-- Common password: "Password123!" (change later in production)

-- Patients
INSERT INTO users (id, name, idNumber, email, passwordHash, enabled, state, createdAt)
SELECT gen_random_uuid(), 'Alice Patient', 'P0001', 'alice.patient1@example.com', crypt('Password123!', gen_salt('bf', 10)), true, true, now()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'alice.patient1@example.com');

INSERT INTO users (id, name, idNumber, email, passwordHash, enabled, state, createdAt)
SELECT gen_random_uuid(), 'Bob Patient', 'P0002', 'bob.patient2@example.com', crypt('Password123!', gen_salt('bf', 10)), true, true, now()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'bob.patient2@example.com');

INSERT INTO users (id, name, idNumber, email, passwordHash, enabled, state, createdAt)
SELECT gen_random_uuid(), 'Carol Patient', 'P0003', 'carol.patient3@example.com', crypt('Password123!', gen_salt('bf', 10)), true, true, now()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'carol.patient3@example.com');

-- Doctors
INSERT INTO users (id, name, idNumber, email, passwordHash, enabled, state, createdAt)
SELECT gen_random_uuid(), 'Diego Doctor', 'D0001', 'diego.doctor1@example.com', crypt('Password123!', gen_salt('bf', 10)), true, true, now()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'diego.doctor1@example.com');

INSERT INTO users (id, name, idNumber, email, passwordHash, enabled, state, createdAt)
SELECT gen_random_uuid(), 'Elena Doctor', 'D0002', 'elena.doctor2@example.com', crypt('Password123!', gen_salt('bf', 10)), true, true, now()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'elena.doctor2@example.com');

INSERT INTO users (id, name, idNumber, email, passwordHash, enabled, state, createdAt)
SELECT gen_random_uuid(), 'Fabian Doctor', 'D0003', 'fabian.doctor3@example.com', crypt('Password123!', gen_salt('bf', 10)), true, true, now()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'fabian.doctor3@example.com');

-- Staff
INSERT INTO users (id, name, idNumber, email, passwordHash, enabled, state, createdAt)
SELECT gen_random_uuid(), 'Sara Staff', 'S0001', 'sara.staff1@example.com', crypt('Password123!', gen_salt('bf', 10)), true, true, now()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'sara.staff1@example.com');

INSERT INTO users (id, name, idNumber, email, passwordHash, enabled, state, createdAt)
SELECT gen_random_uuid(), 'Tom Staff', 'S0002', 'tom.staff2@example.com', crypt('Password123!', gen_salt('bf', 10)), true, true, now()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'tom.staff2@example.com');

INSERT INTO users (id, name, idNumber, email, passwordHash, enabled, state, createdAt)
SELECT gen_random_uuid(), 'Uma Staff', 'S0003', 'uma.staff3@example.com', crypt('Password123!', gen_salt('bf', 10)), true, true, now()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'uma.staff3@example.com');

-- User -> Role assignments (idempotent)
-- Helper to avoid duplicates: only insert mapping if not exists
-- Patients
INSERT INTO user_roles (id, user_id, role_id, createdAt)
SELECT gen_random_uuid(), u.id, r.id, now()
FROM users u, roles r
WHERE u.email = 'alice.patient1@example.com' AND r.name = 'PATIENT'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id
  );
INSERT INTO user_roles (id, user_id, role_id, createdAt)
SELECT gen_random_uuid(), u.id, r.id, now()
FROM users u, roles r
WHERE u.email = 'bob.patient2@example.com' AND r.name = 'PATIENT'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id
  );
INSERT INTO user_roles (id, user_id, role_id, createdAt)
SELECT gen_random_uuid(), u.id, r.id, now()
FROM users u, roles r
WHERE u.email = 'carol.patient3@example.com' AND r.name = 'PATIENT'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id
  );

-- Doctors
INSERT INTO user_roles (id, user_id, role_id, createdAt)
SELECT gen_random_uuid(), u.id, r.id, now()
FROM users u, roles r
WHERE u.email = 'diego.doctor1@example.com' AND r.name = 'DOCTOR'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id
  );
INSERT INTO user_roles (id, user_id, role_id, createdAt)
SELECT gen_random_uuid(), u.id, r.id, now()
FROM users u, roles r
WHERE u.email = 'elena.doctor2@example.com' AND r.name = 'DOCTOR'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id
  );
INSERT INTO user_roles (id, user_id, role_id, createdAt)
SELECT gen_random_uuid(), u.id, r.id, now()
FROM users u, roles r
WHERE u.email = 'fabian.doctor3@example.com' AND r.name = 'DOCTOR'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id
  );

-- Staff
INSERT INTO user_roles (id, user_id, role_id, createdAt)
SELECT gen_random_uuid(), u.id, r.id, now()
FROM users u, roles r
WHERE u.email = 'sara.staff1@example.com' AND r.name = 'STAFF'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id
  );
INSERT INTO user_roles (id, user_id, role_id, createdAt)
SELECT gen_random_uuid(), u.id, r.id, now()
FROM users u, roles r
WHERE u.email = 'tom.staff2@example.com' AND r.name = 'STAFF'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id
  );
INSERT INTO user_roles (id, user_id, role_id, createdAt)
SELECT gen_random_uuid(), u.id, r.id, now()
FROM users u, roles r
WHERE u.email = 'uma.staff3@example.com' AND r.name = 'STAFF'
  AND NOT EXISTS (
    SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id
  );
