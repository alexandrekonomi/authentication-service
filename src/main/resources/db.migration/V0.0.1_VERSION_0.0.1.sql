CREATE TABLE tab_user (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(150),
    email VARCHAR(150),
    password VARCHAR(50),
    active BOOLEAN,
    document VARCHAR(20),
    creation_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_update_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tab_role (
    id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(30) NOT NULL
);

INSERT INTO tab_role (role_name) VALUES ('ROLE_ADMIN'), ('ROLE_CUSTOMER');

CREATE TABLE tab_user_role (
    user_id UUID,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id)
);

ALTER TABLE tab_user_role ADD CONSTRAINT fk_user
FOREIGN KEY (user_id) REFERENCES tab_user(id);

ALTER TABLE tab_user_role ADD CONSTRAINT fk_role
FOREIGN KEY (role_id) REFERENCES tab_role(id);