-------------------- TABLES-----------------------

-- Table users
CREATE TABLE users (
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

-- Table roles
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(255) NOT NULL
);

--------------------------INSERTIONS--------------

-- Insertions table roles
INSERT INTO roles (name, description)
VALUES ('ADMINISTRADOR', 'Administrador general'),
       ('ASESOR', 'Asesor de la plataforma'),
       ('CLIENTE', 'Cliente de la plataforma');