<h1 align="center">Krisefikser backend</h1>

## Innholdsfortegnelse
- [Info](#info)
- [Oppsett](#oppsett)
  - [Avhengigheter](#avhengigheter)
  - [Installering](#installering)
- [Kjøring av applikasjonen](#kjøring-av-applikasjonen)
  - [Testbrukere](#testbrukere)
- [Kjøre tester](#kjøre-tester)
- [Andre kommandoer](#andre-kommandoer)
- [Dokumentasjon](#dokumentasjon)
  - [API Dokumentasjon](#api-dokumentasjon)
- [Frontend](#frontend)
- [Bidrag](#bidrag)

## Info
Krisefikser er en web applikation som hjælper med å øke beredskapen og forståelsen for kriser og katastrofer. Backenden er bygget med Apache Maven og Java, og den håndterer databasetilkoblinger, API-anmodninger og forretningslogik. Se [wikien](https://github.com/aTrueYety/idatt2106-2025-09-backend/wiki) for uttligere dokumentation og detaljer.

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
#database
DATABASE_URL= Database url. f.eks jdbc:mysql://192.168.0.1:3306/default
DATABASE_USER= Databasebrukeren.
DATABASE_PASSWORD= Databasepassordet til databasebrukeren.
SPRING_DATABASE_DRIVER= Hvilken databasedriver som skal brukes (com.mysql.cj.jdbc.Driver eller org.h2.Driver).
DATABASE_SCHEMA= Hvilken schema databasen skal bruke (default for mysql eller PUBLIC for h2).

#epost
GMAIL_HOST= Epost-serveren som skal benyttes.
GMAIL_PORT= Porten epost serveren kjører på.
GMAIL_USER= Epost-brukernavn.
GMAIL_PASSWORD= Epost-passord.

#turnstile
TURNSTILE_SECRET_KEY= Den hemmelig nøkkelen du får når du setter opp turnstile.

#diverse
CORS_ALLOWED_ORIGINS= Hvilke cross-orgins som er tillatt. Lokalt er dette localhost:3000.
FRONTEND_URL= Hva url-en til frontend er. Lokalt er dette localhost:3000.
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
mvn spring-boot:run -Ptest-e2e -Dspring-boot.run.profiles=test-e2e
```

### Testbrukere
Om man kjører applikasjonen med predefinerte testdata får man noen testbrukere:

Superadmin:
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
