/* album tablica */

CREATE TABLE IF NOT EXISTS album
(
    id           BIGSERIAL,
    umjetnik_id  BIGINT,
    slika        VARCHAR(50),
    naziv        VARCHAR(100) DEFAULT 'Moj novi album' NOT NULL,
    datum_dodano DATE         DEFAULT CURRENT_DATE     NOT NULL,
    opis         TEXT,

    PRIMARY KEY (id),
    FOREIGN KEY (umjetnik_id) REFERENCES umjetnik (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);