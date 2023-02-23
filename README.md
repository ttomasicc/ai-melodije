# AI Melodije :notes:

Online platforma za pretraživanje, slušanje i objavljivanje AI generiranih melodija (AI glazbenih albuma).

# Pozadinski dio aplikacije (Web REST API)

### Produkcija

##### 1. Pozicioniranje u direktorij izvornog koda
```bash
cd Softver/api/
```

##### 2. Kreiranje Docker slike (*eng.* image) Spring Boot aplikacije (API)
```bash
./gradlew bootBuildImage
```

##### 3. Pokretanje Docker kontejnera (*eng.* containers) pomoću Docker-compose
```bash
docker-compose up [--detach]
```

### Razvojno okruženje

##### 1. Pozicioniranje u direktorij izvornog koda
```bash
cd Softver/api/
```

##### 2. Pokretanje PostgreSQL i Redis Docker kontejnera
```bash
docker-compose up postgres redis [--detach]
```

##### 3. Pokretanje Spring aplikacije
```bash
./gradlew bootRun
```

### Postman API

Popis Web API resursa (*eng.* endpoints), zajedno s parametrima, dostupan je putem [Postman API kolekcije](./Dokumentacija/AI-Melodije-API.postman_collection.json).

### ERA Model

![ERA Model](./Dokumentacija/ERA-Model.png)

### Korišteni alati, okviri i moduli

#### [Spring okvir](https://spring.io/) (Spring Boot `3.0.2`)
- Spring Boot Web
- Spring Boot WebFlux
- Spring Boot HATEOAS
- Spring Boot Validation
- Spring Boot Data JPA
- Spring Boot Data Redis
- Spring Boot JOOQ
- Spring Boot Session Core
- Spring Boot Session Data Redis
- Spring Boot Security
- Spring Boot Test
- Spring Boot Security Test

#### Eksterni moduli
- [Flyway](https://flywaydb.org/) - migracija baze
- [Ktlint](https://github.com/pinterest/ktlint) - stil i format koda
- [Kover](https://github.com/Kotlin/kotlinx-kover) - pokrivenost Kotlin testova
- [JOOQ](https://www.jooq.org/) - objektno-orijenitrani SQL upiti
- [Auth0 Java JWT](https://github.com/auth0/java-jwt) - sigurnost
- [Shedlock](https://github.com/lukas-krecan/ShedLock) - distribuirano zaključavanje zadataka
- [kotlin-logging](https://github.com/oshai/kotlin-logging) - Kotlin logiranje
- [PostgreSQL](https://hub.docker.com/_/postgres) - SQL baza podatka
- [Redis](https://hub.docker.com/_/redis) - brza NoSQL (ključ/vrijednost) baza podataka
- [MockK](https://mockk.io/) - kreiranje lažnih (*eng.* mock) objekata u Kotlinu

#### Alati
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) `2022.3.2` (Ultimate Edition)
- [DataGrip](https://www.jetbrains.com/datagrip/) `2022.3.2`
- [Kotlin](https://kotlinlang.org/) `1.7.22`
- [OpenJDK](https://openjdk.org/projects/jdk/17/) `17`
- [Docker](https://www.docker.com/) `23.0.1`
- [Docker Compose](https://docs.docker.com/compose/) `2.15.1`
- [Postman](https://www.postman.com/) `10.10.8`
- [Git](https://git-scm.com/) `2.34.1` i [GitHub](https://github.com/)

Aplikacija razvijena i testirana na Linux Mint 21.1 (5.15.0-60-generic).

# Klijentski dio aplikacije

Vrlo grubi inicijalni prototip dostupan je putem sljedećeg [linka](https://www.figma.com/file/YR1ROBbX0QlgVe0RadPViT/AI-Melodije?node-id=0%3A1&t=Yt3Mkb70sWJwkv66-1).

### Korišteni alati, okviri i moduli

- Figma

// TODO
