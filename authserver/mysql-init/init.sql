CREATE DATABASE IF NOT EXISTS authdb;
CREATE DATABASE IF NOT EXISTS businessdb;

CREATE USER IF NOT EXISTS 'auth_user'@'%' IDENTIFIED BY 'authpass';
GRANT ALL PRIVILEGES ON authdb.* TO 'auth_user'@'%';

CREATE USER IF NOT EXISTS 'backend_user'@'%' IDENTIFIED BY 'backendpass';
GRANT ALL PRIVILEGES ON businessdb.* TO 'backend_user'@'%';

FLUSH PRIVILEGES;