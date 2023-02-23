/* album_melodija tablica */

CREATE TABLE IF NOT EXISTS album_melodija
(
    album_id    BIGINT,
    melodija_id BIGINT,

    PRIMARY KEY (album_id, melodija_id),
    FOREIGN KEY (album_id) REFERENCES album (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE ,
    FOREIGN KEY (melodija_id) REFERENCES melodija (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);