CREATE TABLE authorities (
                             id VARCHAR(36) NOT NULL PRIMARY KEY,
                             authority_name VARCHAR(255) NOT NULL
);

CREATE TABLE users (
                       id VARCHAR(36) NOT NULL PRIMARY KEY,
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       email VARCHAR(255),
                       password VARCHAR(255),
                       active BOOLEAN NOT NULL,
                       phone_number VARCHAR(255),
                       birth_date DATE,
                       creation_date DATETIME(6),
                       authority_id VARCHAR(36),
                       CONSTRAINT fk_users_authority FOREIGN KEY (authority_id) REFERENCES authorities(id)
);

CREATE TABLE promotion_request (
                                   id VARCHAR(36) NOT NULL PRIMARY KEY,
                                   user_id VARCHAR(255) NOT NULL,
                                   hr_notes VARCHAR(1000),
                                   admin_notes VARCHAR(1000),
                                   status VARCHAR(50) NOT NULL,
                                   processed_at DATETIME(6)
);

CREATE TABLE oauth2_registered_client (
                                          id varchar(100) NOT NULL PRIMARY KEY,
                                          client_id varchar(100) NOT NULL,
                                          client_id_issued_at timestamp NOT NULL,
                                          client_secret varchar(200),
                                          client_secret_expires_at timestamp,
                                          client_name varchar(200) NOT NULL,
                                          client_authentication_methods varchar(1000) NOT NULL,
                                          authorization_grant_types varchar(1000) NOT NULL,
                                          redirect_uris varchar(1000),
                                          post_logout_redirect_uris varchar(1000),
                                          scopes varchar(1000) NOT NULL,
                                          client_settings varchar(2000) NOT NULL,
                                          token_settings varchar(2000) NOT NULL
);

CREATE TABLE oauth2_authorization (
                                      id varchar(100) NOT NULL PRIMARY KEY,
                                      registered_client_id varchar(100) NOT NULL,
                                      principal_name varchar(200) NOT NULL,
                                      authorization_grant_type varchar(100) NOT NULL,
                                      authorized_scopes varchar(1000),
                                      attributes blob,
                                      state varchar(500),
                                      authorization_code_value blob,
                                      authorization_code_issued_at timestamp,
                                      authorization_code_expires_at timestamp,
                                      access_token_value blob,
                                      access_token_issued_at timestamp,
                                      access_token_expires_at timestamp,
                                      access_token_scopes varchar(1000),
                                      refresh_token_value blob,
                                      refresh_token_issued_at timestamp,
                                      refresh_token_expires_at timestamp,
                                      oidc_id_token_value blob,
                                      oidc_id_token_issued_at timestamp,
                                      oidc_id_token_expires_at timestamp
);

CREATE TABLE oauth2_authorization_consent (
                                              registered_client_id varchar(100) NOT NULL,
                                              principal_name varchar(200) NOT NULL,
                                              authorities varchar(1000) NOT NULL,
                                              PRIMARY KEY (registered_client_id, principal_name)
);