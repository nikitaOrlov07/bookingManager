\echo 'Starting initialization script'

-- Create tables if they don't exist
CREATE TABLE IF NOT EXISTS roles (
                                     id INT PRIMARY KEY,
                                     name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     username VARCHAR(50) NOT NULL,
                                     email VARCHAR(100) NOT NULL,
                                     password VARCHAR(100) NOT NULL,
                                     role_id INT REFERENCES roles(id),
                                     town VARCHAR(100),
                                     phone_number BIGINT
);

-- Create the join table for many-to-many relationship (UserEntity - Rols)
CREATE TABLE IF NOT EXISTS users_role (
                                          user_id INT REFERENCES users(id),
                                          role_id INT REFERENCES roles(id),
                                          PRIMARY KEY (user_id, role_id)
);

-- Add unique constraints if they don't exist
DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'roles_name_key') THEN
            ALTER TABLE roles ADD CONSTRAINT roles_name_key UNIQUE (name);
        END IF;

        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'users_username_key') THEN
            ALTER TABLE users ADD CONSTRAINT users_username_key UNIQUE (username);
        END IF;
    END $$;

-- Insert roles
INSERT INTO roles (id, name) VALUES (0, 'USER'), (1, 'ADMIN') ON CONFLICT (name) DO NOTHING;

-- Insert admin user
INSERT INTO users (id,username, email, password, role_id, town, phone_number)
VALUES (0,'ADMIN', 'ADMIN@gmail.com', '$2a$10$CtnkjDK6RKEr8gu9e5KamOgy4E/7ThUgrWjw6SWxsm/8UmSDrKMoy', 1, 'Bratislava', 555555555555)
ON CONFLICT (username) DO NOTHING;

-- Insert into users_roles
INSERT INTO users_role (user_id,role_id) VALUES (0,1);

\echo 'Initialization script completed'
