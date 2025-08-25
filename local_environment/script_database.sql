CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    second_name VARCHAR(100) NOT NULL,
    first_last_name VARCHAR(100) NOT NULL,
    second_last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
	address VARCHAR(255),
    phone VARCHAR(50),
    birth_date DATE,
    base_salary NUMERIC(8,2) NOT NULL
);