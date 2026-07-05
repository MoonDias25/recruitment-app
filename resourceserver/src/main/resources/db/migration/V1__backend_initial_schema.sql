CREATE TABLE user_profile (
                              id VARCHAR(36) NOT NULL PRIMARY KEY,
                              first_name VARCHAR(255),
                              last_name VARCHAR(255),
                              email VARCHAR(255),
                              phone_number VARCHAR(255),
                              birth_date DATE,
                              creation_date DATETIME(6)
);

CREATE TABLE job_offer (
                           id VARCHAR(36) NOT NULL PRIMARY KEY,
                           job_title VARCHAR(255) NOT NULL,
                           status VARCHAR(50),
                           salary_max INT,
                           salary_min INT,
                           description TEXT,
                           application_start DATETIME(6),
                           application_end DATETIME(6),
                           reviewed_by VARCHAR(255)
);

CREATE TABLE cv (
                    id VARCHAR(36) NOT NULL PRIMARY KEY,
                    user_id VARCHAR(255),
                    file_name VARCHAR(255),
                    file_path VARCHAR(255),
                    uploaded_at DATETIME(6)
);

CREATE TABLE candidate_application (
                                       id VARCHAR(36) NOT NULL PRIMARY KEY,
                                       user_id VARCHAR(255),
                                       job_id VARCHAR(36),
                                       cv_id VARCHAR(255),
                                       applied_at DATETIME(6),
                                       status VARCHAR(50),
                                       reviewed_by VARCHAR(255),
                                       recruiter_notes VARCHAR(255),
                                       reviewed_at DATETIME(6),
                                       CONSTRAINT fk_application_job FOREIGN KEY (job_id) REFERENCES job_offer(id)
);

CREATE TABLE promotion_requests (
                                    id VARCHAR(36) NOT NULL PRIMARY KEY,
                                    target_user_id VARCHAR(255) NOT NULL,
                                    hr_id VARCHAR(255) NOT NULL,
                                    created_at DATETIME(6) NOT NULL,
                                    reviewed_at DATETIME(6),
                                    request_status VARCHAR(50) NOT NULL,
                                    hr_notes VARCHAR(1000),
                                    admin_notes VARCHAR(255)
);