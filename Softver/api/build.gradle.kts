import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

val auth0JwtVerzija = "4.2.2"
val shedlockVerzija = "5.1.0"

group = "com.aimelodije"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

plugins {
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"

    id("org.flywaydb.flyway") version "9.14.1"
    id("org.jlleitschuh.gradle.ktlint") version "11.1.0"
    id("nu.studer.jooq") version "8.1"
    id("org.jetbrains.kotlinx.kover") version "0.6.1"

    kotlin("jvm") version "1.7.22"

    kotlin("plugin.spring") version "1.7.22"
    kotlin("plugin.jpa") version "1.7.22"
}

dependencies {
    // Spring boot moduli
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.session:spring-session-core")
    implementation("org.springframework.session:spring-session-data-redis")

    // jOOQ
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    jooqGenerator("org.postgresql:postgresql:42.5.1")

    // Jwt
    implementation("com.auth0:java-jwt:$auth0JwtVerzija")

    // Distribuirano zaključavanje zadataka
    implementation("net.javacrumbs.shedlock:shedlock-spring:$shedlockVerzija")
    implementation("net.javacrumbs.shedlock:shedlock-provider-jdbc-template:$shedlockVerzija")

    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Logiranje
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

    // Migracija baze
    implementation("org.flywaydb:flyway-core")
    // PostgreSQL upravljač
    runtimeOnly("org.postgresql:postgresql")

    // Testovi
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }
    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("com.ninja-squad:springmockk:4.0.0")
    testImplementation("org.springframework.security:spring-security-test")
}

flyway {
    url = "jdbc:postgresql://localhost:5432/melodije"
    user = "admin"
    password = "admin"
    validateMigrationNaming = true
    cleanDisabled = false
}

jooq {
    // Postavlja istu verziju jOOQ-a kao i Spring
    version.set(dependencyManagement.importedProperties["jooq.version"])

    configurations {
        create("main") { // Naziv jOOQ konfiguracije
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/melodije"
                    user = "admin"
                    password = "admin"
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        forcedTypes.addAll(
                            listOf(
                                org.jooq.meta.jaxb.ForcedType().apply {
                                    name = "varchar"
                                    includeExpression = ".*"
                                    includeTypes = "JSONB?"
                                },
                                org.jooq.meta.jaxb.ForcedType().apply {
                                    name = "varchar"
                                    includeExpression = ".*"
                                    includeTypes = "INET"
                                }
                            )
                        )
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }
                    target.apply {
                        packageName = "com.aimelodije.modeli.generirano"
                        directory = "src/jooq"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") {
    allInputsDeclared.set(true)
    enabled = false
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    systemProperty("spring.profiles.active", "test")
    useJUnitPlatform()
}

tasks.withType<BootBuildImage> {
    imageName.set("ttomasic/ai-melodije-api")
}