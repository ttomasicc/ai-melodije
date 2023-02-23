/* melodija tablica */

CREATE TABLE IF NOT EXISTS melodija
(
    id           BIGSERIAL,
    autor_id     BIGINT,
    zanr_id      BIGINT,
    audio        VARCHAR(50)                               NOT NULL,
    naziv        VARCHAR(100) DEFAULT 'Moja nova melodija' NOT NULL,
    datum_dodano DATE         DEFAULT CURRENT_DATE         NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (autor_id) REFERENCES umjetnik (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    FOREIGN KEY (zanr_id) REFERENCES zanr (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);