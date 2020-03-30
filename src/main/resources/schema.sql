CREATE TABLE transaction (
    id   INTEGER NOT NULL ,
    type VARCHAR(128) NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount DOUBLE ,
    PRIMARY KEY (id)
);