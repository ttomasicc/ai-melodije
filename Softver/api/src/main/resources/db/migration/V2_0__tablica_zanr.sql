/* zanr tablica */

CREATE TABLE IF NOT EXISTS zanr
(
    id    BIGSERIAL,
    naziv VARCHAR(50) NOT NULL UNIQUE,

    PRIMARY KEY (id)
);