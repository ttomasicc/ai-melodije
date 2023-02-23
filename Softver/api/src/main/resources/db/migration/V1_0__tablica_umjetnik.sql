/* umjetnik tablica */

CREATE TABLE IF NOT EXISTS umjetnik
(
    id                 BIGSERIAL,
    rola               VARCHAR(30)               NOT NULL,
    korime             VARCHAR(50)               NOT NULL UNIQUE,
    email              VARCHAR(70)               NOT NULL UNIQUE,
    lozinka            VARCHAR(60)               NOT NULL,
    ime                VARCHAR(70),
    prezime            VARCHAR(70),
    opis               TEXT,
    slika              VARCHAR(50),
    datum_registracije DATE DEFAULT CURRENT_DATE NOT NULL,

    PRIMARY KEY (id)
);