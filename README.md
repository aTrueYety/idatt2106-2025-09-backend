<h1 align="center">Krisefikser backend</h1>
Backend til krisefikser en applikasjon laget i sammenheng med faget Systemutvikling 2 (IDATT2106) vår 2025.

## Info
Oppgaven gikk ut på å utvikle en web-applikasjon som skal hjelpe med å øke beredskapsgraden og forståelsen for kriser i Norge.
Dette er backend repositoriet til prosjektet, frontend finner du [her](https://github.com/nikolaitandberg/idatt2106-2025-09-frontend).
For mer infomasjon om prosjektet se wiki.

## Kjør lokalt
### Avhengigheter
- Java 21
- Maven 3.8+
- MySQL 8+ (ekstern database brukes i praksis, testdata er med h2)

### Installer og kjør backend
1. Klon prosjektet
```bash
git clone git@github.com:aTrueYety/idatt2106-2025-09-backend.git
```

2. Naviger til rotmappen til prosjektet
```bash
cd idatt2106-2025-09-backend
```

3. Opprett en .env fil og legg til i rotmappen med følgende innhold:
```env
DATABASE_URL=
DATABASE_USER=
DATABASE_PASSWORD=
SPRING_DATABASE_DRIVER=

GMAIL_HOST=
GMAIL_PORT=
GMAIL_USER=
GMAIL_PASSWORD=
```

4. Kjør applikasjonen
```bash
mvn clean install
mvn spring-boot:run
```

### Kjør med testdata
Følg steg 1-3 fra vanlig installasjon, deretter kjør med:
```bash
mvn clean install
mvn spring-boot:run -Ptest-e2e -D"spring-boot.run.profiles=test-e2e"
```

### Testbrukere
Superadmin:
- Brukernavn: adminAdminsen
- Passord: Password12345

Admin:
- Brukernavn: adminJunior
- Passord: Password12345

Bruker:
- Brukernavn: olaNormann
- Passord: Password12345

## Andre kommandoer
- Kjør tester: `mvn test`
- Generer testingsgrad: `mvn verify`
- Kjør checkstyle: `mvn checkstyle:check`
