INSERT INTO authorities (id, authority_name)
SELECT UUID(), 'ROLE_ADMIN' WHERE NOT EXISTS (SELECT 1 FROM authorities WHERE authority_name = 'ROLE_ADMIN');

INSERT INTO authorities (id, authority_name)
SELECT UUID(), 'ROLE_HR' WHERE NOT EXISTS (SELECT 1 FROM authorities WHERE authority_name = 'ROLE_HR');

INSERT INTO authorities (id, authority_name)
SELECT UUID(), 'ROLE_USER' WHERE NOT EXISTS (SELECT 1 FROM authorities WHERE authority_name = 'ROLE_USER');

INSERT INTO authorities (id, authority_name)
SELECT UUID(), 'ROLE_SUPER_ADMIN' WHERE NOT EXISTS (SELECT 1 FROM authorities WHERE authority_name = 'ROLE_SUPER_ADMIN');

INSERT INTO users (
    id, first_name, last_name, email, password, active, phone_number, birth_date, creation_date, authority_id
)
SELECT
    UUID(),
    'Super',
    'Admin',
    'admin@mail.com',
    '{bcrypt}$2a$10$tRKtLxd9xD/QeEW1Dje8peGAGKNPafyaynSLuKid5IwSyIsa5TwRu',
    true,
    '0000000000',
    '2000-01-01',
    NOW(),
    id
FROM authorities
WHERE authority_name = 'ROLE_SUPER_ADMIN'
  AND NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@mail.com');