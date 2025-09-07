-------------------- TABLES -----------------------

-- Table roles
CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(255) NOT NULL
);

-- Table users
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    user_name VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    second_name VARCHAR(100) NOT NULL,
    first_last_name VARCHAR(100) NOT NULL,
    second_last_name VARCHAR(100) NOT NULL,
    identification_number VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(50),
    birth_date DATE,
    base_salary NUMERIC(10,2) NOT NULL,
    role_id INT,
    CONSTRAINT fk_users_roles
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

-------------------------- INSERTIONS --------------

-- Insertions table roles
INSERT INTO roles (name, description)
VALUES ('ADMINISTRADOR', 'Administrador general'),
       ('ASESOR', 'Asesor de la plataforma'),
       ('CLIENTE', 'Cliente de la plataforma')
ON CONFLICT (name) DO NOTHING;  -- Avoid duplicates

-- Insertions table users
INSERT INTO users (
    user_name,
    password,
    first_name,
    second_name,
    first_last_name,
    second_last_name,
    identification_number,
    email,
    base_salary,
    role_id
) VALUES (
    'jcamilo',
    '$2a$10$0sSJCE.LfrT3n2.KmDtyl.y8WOgn/78fP2G0N.5jlX7B3ywaUuyEW',
    'Juan',
    'Camilo',
    'Torres',
    'Beltrán',
    '123',
    'jua@email.com',
    2500.00,
    1
)
ON CONFLICT (identification_number) DO NOTHING; -- Avoid duplicates
