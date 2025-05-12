<h1 align="center">Krisefikser backend</h1>

## Info
Krisefikser er en web applikation som hjælper med å øke beredskapen og forståelsen for kriser og katastrofer. Backenden er bygget med Apache Maven og Java, og den håndterer databasetilkoblinger, API-anmodninger og forretningslogik. Se [wikien](https://github.com/aTrueYety/idatt2106-2025-09-backend/wiki) for uttligere dokumentation og detaljer.

## Kildekode

## Oppsett
### Avhengigheter
- Java 21
- Apache Maven 3.9.6


### Installering
1. Klon prosjektet
```
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

4. Bygg
```bash
mvn clean install
```

## Kjøring av applikasjonen
For å kjøre applikasjonen lokalt, kan du bruke følgende kommando fra rotmappen:
```bash
mvn spring-boot:run
```

For å kjøre med predefinert testdata kan du bruke følgende kommando:
```bash
mvn spring-boot:run -Ptest-e2e -D"spring-boot.run.profiles=test-e2e"
```

### Testbrukere
Om man kjører applikasjonen med predefinerte testdata får man noen testbrukere:

Superadmin
- Brukernavn: adminAdminsen
- Passord: Password12345

Admin:
- Brukernavn: adminJunior
- Passord: Password12345

Bruker:
- Brukernavn: olaNormann
- Passord: Password12345

## Kjøre tester
Kjør alle tester:
```bash
mvn test
```
Generer en JaCoCo coverage rapport:
```bash
mvn verify
```

## Andre kommandoer
- Kjør checkstyle: `mvn checkstyle:check`

## Dokumentasjon
For å se en mer detaljert beskrivelse av prosjektet se [wikien](https://github.com/aTrueYety/idatt2106-2025-09-backend/wiki).

### API Dokumentasjon
Dokumentasjon av API-endepunkt finner du ved å gå til http://localhost:8080/swagger-ui/index.html mens du kjører applikasjonen.

## Frontend
Dette er backend repoet. Frontend finner du [her](https://github.com/nikolaitandberg/idatt2106-2025-09-frontend).

## Bidrag
For å bidra til prosjektet må man lage en branch fra dev og gjøre endringer på branchen. Når man er ferdig lager man
en pull request til dev. Så om ingen pipelines feiler kan man merge endringene sine. For å sørge for at pull requests 
går raskest mulig burde man kjøre tester, checkstyle og kjøre backenden lokalt før man oppretter en pull request. Dersom 
man oppdager en bug eller manglene funksjonalitet kan man opprette en ny issue.
