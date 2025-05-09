package no.ntnu.stud.idatt2106.backend.config;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import no.ntnu.stud.idatt2106.backend.model.base.Event;
import no.ntnu.stud.idatt2106.backend.model.base.ExtraResident;
import no.ntnu.stud.idatt2106.backend.model.base.ExtraResidentType;
import no.ntnu.stud.idatt2106.backend.model.base.Food;
import no.ntnu.stud.idatt2106.backend.model.base.FoodType;
import no.ntnu.stud.idatt2106.backend.model.base.GroupHousehold;
import no.ntnu.stud.idatt2106.backend.model.base.Household;
import no.ntnu.stud.idatt2106.backend.model.base.InfoPage;
import no.ntnu.stud.idatt2106.backend.model.base.MapObject;
import no.ntnu.stud.idatt2106.backend.model.base.MapObjectType;
import no.ntnu.stud.idatt2106.backend.model.base.User;
import no.ntnu.stud.idatt2106.backend.model.request.RegisterRequest;
import no.ntnu.stud.idatt2106.backend.repository.EventRepositoryImpl;
import no.ntnu.stud.idatt2106.backend.repository.ExtraResidentRepositoryImpl;
import no.ntnu.stud.idatt2106.backend.repository.ExtraResidentTypeRepositoryImpl;
import no.ntnu.stud.idatt2106.backend.repository.FoodRepository;
import no.ntnu.stud.idatt2106.backend.repository.FoodTypeRepository;
import no.ntnu.stud.idatt2106.backend.repository.GroupHouseholdRepositoryImpl;
import no.ntnu.stud.idatt2106.backend.repository.HouseholdRepositoryImpl;
import no.ntnu.stud.idatt2106.backend.repository.InfoPageRepositoryImpl;
import no.ntnu.stud.idatt2106.backend.repository.KitRepositoryImpl;
import no.ntnu.stud.idatt2106.backend.repository.MapObjectRepositoryImpl;
import no.ntnu.stud.idatt2106.backend.repository.MapObjectTypeRepositoryImpl;
import no.ntnu.stud.idatt2106.backend.repository.SeverityRepository;
import no.ntnu.stud.idatt2106.backend.repository.UserRepositoryImpl;
import no.ntnu.stud.idatt2106.backend.service.AuthService;
import no.ntnu.stud.idatt2106.backend.service.HouseholdService;
import no.ntnu.stud.idatt2106.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * .
 * TestDataInitializer is responsible for initializing test data for the
 * application
 * when the "test-e2e" profile is active. It implements the CommandLineRunner
 * interface to execute code at application startup.
 */
@Component
@Profile("test-e2e")
public class TestDataInitializer implements CommandLineRunner {

  private final AuthService authService;
  private final EventRepositoryImpl eventRepository;
  private final InfoPageRepositoryImpl infoPageRepository;
  private final UserRepositoryImpl userRepositoryImpl;
  private final UserService userService;
  private final HouseholdService householdService;
  private final HouseholdRepositoryImpl householdRepository;
  private final FoodRepository foodRepository;
  private final FoodTypeRepository foodTypeRepository;
  private final ExtraResidentRepositoryImpl extraResidentRepository;
  private final ExtraResidentTypeRepositoryImpl extraResidentTypeRepository;
  private final GroupHouseholdRepositoryImpl groupHouseholdRepository;
  private final KitRepositoryImpl kitRepository;
  private final MapObjectTypeRepositoryImpl mapObjectTypeRepository;
  private final MapObjectRepositoryImpl mapObjectRepository;
  private static final Logger logger = LoggerFactory.getLogger(TestDataInitializer.class);

  /**
   * Constructs a TestDataInitializer with the required services and repositories.
   *
   * @param authService        the authentication service
   * @param eventRepository    the event repository implementation
   * @param infoPageRepository the info page repository implementation
   * @param severityRepository the severity repository
   * @param userRepositoryImpl the user repository implementation
   * @param userService        the user service
   */
  @Autowired
  public TestDataInitializer(AuthService authService,
      EventRepositoryImpl eventRepository,
      InfoPageRepositoryImpl infoPageRepository,
      SeverityRepository severityRepository,
      UserRepositoryImpl userRepositoryImpl,
      UserService userService,
      HouseholdService householdService,
      HouseholdRepositoryImpl householdRepository,
      FoodRepository foodRepository,
      FoodTypeRepository foodTypeRepository,
      ExtraResidentRepositoryImpl extraResidentRepository,
      ExtraResidentTypeRepositoryImpl extraResidentTypeRepository,
      GroupHouseholdRepositoryImpl groupHouseholdRepository,
      KitRepositoryImpl kitRepository,
      MapObjectTypeRepositoryImpl mapObjectTypeRepository,
      MapObjectRepositoryImpl mapObjectRepository) {
    this.extraResidentTypeRepository = extraResidentTypeRepository;
    this.extraResidentRepository = extraResidentRepository;
    this.authService = authService;
    this.eventRepository = eventRepository;
    this.infoPageRepository = infoPageRepository;
    this.userRepositoryImpl = userRepositoryImpl;
    this.userService = userService;
    this.householdService = householdService;
    this.householdRepository = householdRepository;
    this.foodRepository = foodRepository;
    this.foodTypeRepository = foodTypeRepository;
    this.groupHouseholdRepository = groupHouseholdRepository;
    this.kitRepository = kitRepository;
    this.mapObjectTypeRepository = mapObjectTypeRepository;
    this.mapObjectRepository = mapObjectRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    logger.info("Initializing test data...");
    initializeTestData();
  }

  /**
   * .
   * Initializes test data for the application
   */
  public void initializeTestData() {
    initializeExtraResidentTypes();
    initializeFoodTypes();
    initializeTestUsers();
    initializeTestSeveritiesAndInfoPages();
    initializeMapObjectsAndMapObjectTypes();
    initializeTestEvents();
  }

  /**
   * Initializes test users for the application.
   */
  public void initializeTestUsers() {
    // olaNordmann - test user with no special privileges
    // kariNordmann - test user with no special privileges
    // adminJunior - test user with admin privileges
    // adminAdminsen - test user with super admin privileges

    RegisterRequest adminUserRequest = new RegisterRequest("adminJunior",
        "Password12345", "admin@tgmail.com", "captha");
    RegisterRequest superAdminUserRequest = new RegisterRequest("adminAdminsen",
        "Password12345", "superadmin@gmail.com", "captha");
    RegisterRequest testUserRequest = new RegisterRequest("olaNordmann",
        "Password12345", "testme@gmail.com", "captha");
    RegisterRequest testUserRequest2 = new RegisterRequest("kariNordmann",
        "Password12345", "testhim@gmail.com", "captha");
    RegisterRequest testUserRequest3 = new RegisterRequest("nordmannJunior",
        "Password12345", "junior@gmail.com", "captha");

    authService.register(adminUserRequest);
    authService.register(superAdminUserRequest);
    authService.register(testUserRequest);
    authService.register(testUserRequest2);
    authService.register(testUserRequest3);

    User adminUser = userService.getUserByUsername("adminAdminsen");

    adminUser.setAdmin(true);
    adminUser.setSuperAdmin(true);
    userRepositoryImpl.updateUser(adminUser);

    User adminJuniorUser = userService.getUserByUsername("adminJunior");
    adminJuniorUser.setAdmin(true);
    userRepositoryImpl.updateUser(adminJuniorUser);

    Household household1 = new Household(
        null,
        "Eirik jarls gate 2",
        "Nordmannfamilien",
        63.4225,
        10.3911,
        30.0,
        Date.from(Instant.now()));
    householdRepository.save(household1);

    LocalDateTime sixMonthsAgoLdt = LocalDateTime.now().minusMonths(6);
    Date sixMonthsAgo = Date.from(
        sixMonthsAgoLdt
            .atZone(ZoneId.systemDefault())
            .toInstant());
    //
    Household household2 = new Household(
        null,
        "A4-112",
        "Admin Familien",
        63.4158,
        10.4053,
        40.0,
        sixMonthsAgo);
    householdRepository.save(household2);

    long household1Id = householdService.getAll().get(0).getId();
    long household2Id = householdService.getAll().get(1).getId();
    householdService.addUserToHousehold("adminJunior", household2Id);
    householdService.addUserToHousehold("adminAdminsen", household2Id);
    householdService.addUserToHousehold("olaNordmann", household1Id);
    householdService.addUserToHousehold("kariNordmann", household1Id);

    Food food1 = new Food();
    food1.setTypeId(1L);
    food1.setHouseholdId(household2Id);
    food1.setExpirationDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
    food1.setAmount(3000.0);
    foodRepository.save(food1);
    Food food2 = new Food();
    food2.setTypeId(2L);
    food2.setHouseholdId(household2Id);
    food2.setExpirationDate(LocalDate.now().plus(2, ChronoUnit.DAYS));
    food2.setAmount(20.0);
    foodRepository.save(food2);
    Food food3 = new Food();
    food3.setTypeId(3L);
    food3.setHouseholdId(household2Id);
    food3.setExpirationDate(LocalDate.now().plus(3, ChronoUnit.DAYS));
    food3.setAmount(10.0);
    foodRepository.save(food3);

    Food food4 = new Food();
    food4.setTypeId(4L);
    food4.setHouseholdId(household1Id);
    food4.setExpirationDate(LocalDate.now().plus(1, ChronoUnit.DAYS));
    food4.setAmount(30.0);
    foodRepository.save(food4);

    ExtraResident nordmannDog = new ExtraResident();
    nordmannDog.setHouseholdId(household2Id);
    nordmannDog.setTypeId(5L);
    nordmannDog.setName("Hunden Felix");
    extraResidentRepository.save(nordmannDog);

  }

  /**
   * Initializes test severities and informational pages for the application.
   * This includes setting up severity levels and creating informational pages
   * for various scenarios like nuclear explosions, terror actions, and more.
   */
  public void initializeTestSeveritiesAndInfoPages() {
    // Initialize severities:
    // - Critical severity: Represents the most severe events that require
    // - High severity: Represents critical events requiring immediate attention.
    // - Medium severity: Represents significant events with moderate urgency.
    // - Low severity: Represents minor events with low urgency.

    // Initialize informational pages:
    // - Nuclear reactor explosion: Detailed description of nuclear incidents and
    // safety measures.
    // - Terror action: Overview of terror attacks, their impact, and preventive
    // measures.
    // - Forest fire: Information about forest fires, their consequences, and safety
    // precautions.
    // - Storm warning: Description of storm warnings, their effects, and safety

    // Severity levels
    // severity0 - Critical
    // severity1 - High
    // severity2 - Medium
    // severity3 - Low

    // Nuclear explosion warning
    final String nuclearExplosionDesc = """
        Reaktorkjerner og kjerneenergi
        - Inne i en reaktor er kjernebrensel (typisk uran eller plutonium)
          stablet i staver og omgitt av kjølevæske. Ved normal drift kontrolleres
          kjedereaksjonen av styringsstaver som absorberer nøytroner.

        Hva går galt?
        - Hvis styringsstavene ikke kan senkes inn tilstrekkelig (pga. teknisk svikt
          eller menneskelig feil), øker nøytronfluxen dramatisk. Brå temperatur-
          økning kan koke bort kjølevæsken, slik at brenselet overopphetes. I
          tillegg kan hydrogen dannet ved reaksjon mellom varmt damp og
          zircalloy-hylsene eksplodere.

        Selve “eksplosjonen”
        - Dampeksplosjon: Når vann raskt omdannes til damp under høyt trykk, kan
          selve trykkbølgen rive av reaktortopp eller bygningstak.
        - Hydrogeneksplosjon: Frigjort hydrogen kan antennes og gi sekundære
          eksplosjoner (slik det skjedde i Fukushima i 2011).
        - Kjerneeksplosjon? En fullblods kjernefysisk “atombombeliknende”
          eksplosjon er i praksis umulig i en sivil reaktor, fordi brenselet ikke har
          tilstrekkelig beriket andel fissilt materiale.

        Radioaktiv spredning
        - Når reaktordeler eller dampskyer løsner, sprer de radioaktive isotoper
          (Cs-137, I-131, Sr-90) i omgivelsene. Dette fører til både akutt strålesyke
          for de som er nærmest og langvarige jord- og vannforurensninger.

        Historiske eksempler
        - Tsjernobyl (1986): Grafitteksplosjonen kastet ut en sky av mange
          radioisotoper over store deler av Europa. Evakuering og konstante
          helsestudier har pågått i tiår etterpå.
        - Fukushima Daiichi (2011): Jordskjelv og tsunami førte til dampeksplosjoner
          og hydrogeneksplosjoner, med lekkasje av radioaktivt kjølevann ut i
          Stillehavet.

        Forebygging og sikkerhet
        - Flere backup-kjølesystemer (dieselaggregater, batterier)
        - Passive sikkerhetssystemer som bruker naturlig konveksjon
        - Isolasjon og trykkavlastningsventiler som hindrer brå trykkoppbygging
        - Streng regulering og jevnlig øvelser i nødsituasjoner
              """;

    InfoPage infoPageNuclear = new InfoPage(
        1L,
        "Atomreaktor eksplosjon",
        "Atomreaktor eksplosjon kan være en alvorlig hendelse som kan føre til"
            + "betydelig skade på mennesker og miljø.",
        nuclearExplosionDesc,
        Timestamp.valueOf(LocalDateTime.now()),
        Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
    infoPageRepository.save(infoPageNuclear);

    // Terror warning
    final String terrorAksjonBeskrivelse = """
        Terrorangrep er bevisste, voldelige handlinger utført for
        å skape frykt og påvirke en stat eller en sivil befolkning
        gjennom terror. De kan ha politiske, religiøse eller ideologiske
        motiver, og rammer ofte uskyldige sivile for størst mulig
        psykologisk effekt.

        Vanlige kjennetegn:
        - Koordinerteangrep mot offentlige steder (transportknutepunkter,
          regjeringsbygg, shoppingsentra).
        - Bruk av eksplosiver, skytevåpen eller kjøretøy for å skade
          mennesker og infrastruktur.
        - Planlegging over lengre tid, ofte med logistikk- og
          etterretningsstøtte.
        - Målrettet propagandabruk for å spre budskap og rekruttere
          støttespillere i etterkant.

        Konsekvenser:
        - Akutte tap av menneskeliv og alvorlige skader.
        - Psykologisk terroreffekt i befolkningen, med langvarig
          angst og mistenksomhet.
        - Ødeleggelse av kritisk infrastruktur og økonomiske tap.
        - Økt sikkerhetspress, strengere lover og risiko for
          sivile friheter blir innskrenket.

        Forebygging og beredskap:
        - Etterretning og overvåking av ekstreme grupper.
        - Samarbeid mellom politi, etterretningstjenester og
          internasjonale partnere.
        - Regelmessige sikkerhetsøvelser for nødetater og helsevesen.
        - Informasjonskampanjer og samfunnsopplysning for å styrke
          motstandsdyktighet i befolkningen.
        """;

    InfoPage infoPageTerror = new InfoPage(
        2L,
        "Terror aksjon",
        "Terroraksjon er bevisste, koordinerte voldshandlinger "
            + "rettet mot sivile for å spre frykt og destabilisere samfunnet.",
        terrorAksjonBeskrivelse,
        Timestamp.valueOf(LocalDateTime.now()),
        Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
    infoPageRepository.save(infoPageTerror);

    // Fire warning
    final String skogbrannBeskrivelse = """
        Skogbranner oppstår når vegetasjon antennes og brenner ukontrollert.
        Under tørre, varme og vindfulle forhold kan brannen spre seg svært raskt,
        og true både skog, bebyggelse og mennesker.

        Kjennetegn:
        - Rask flamme- og glødeutvikling i tett vegetasjon.
        - Kraftig røykutvikling som reduserer sikt og helse.
        - Vanskelig slukking uten tunge redningsressurser.

        Konsekvenser:
        - Ødelegger plante- og dyreliv.
        - Skade på bygninger og infrastruktur.
        - Helsefare ved røykinnånding.

        Forebygging:
        - Røykeforbud i skog og utmark under høy brannfare.
        - Bruk av branngater og skogrydding.
        - Tidlig varsling og kontinuerlig værovervåkning.
        """;

    InfoPage infoPageSkogbrann = new InfoPage(
        3L,
        "Skogbrann",
        "Skogbrann kan spre seg raskt under varme og vind, true både natur, bebyggelse "
            + "og menneskers sikkerhet.",
        skogbrannBeskrivelse,
        Timestamp.valueOf(LocalDateTime.now()),
        Timestamp.valueOf(LocalDateTime.now().plusDays(1)));

    infoPageRepository.save(infoPageSkogbrann);

    // Storm warning
    final String stormBeskrivelse = """
        Storm er kraftige værfenomener med svært sterke vindkast, ofte kombinert
        med regn eller snø, og kan føre til betydelige ødeleggelser på både
        natur og menneskeskapte konstruksjoner.

        Kjennetegn:
        - Vindhastigheter over 20 m/s (eller > Beaufort-skala 8).
        - Mest utbredt langs kysten, men kan også ramme innlandsområder.
        - Regnbyger, snøbyger eller hagl kan følge med.

        Konsekvenser:
        - River ned trær og strømledninger.
        - Taksikring og byggfasader kan bli skadet.
        - Fare for flom når vinder presser sjøvann mot kysten.
        - Avlyste ferjer, fly og andre transporter.

        Historiske eksempler:
        - Orkanen “Sandy” (USA 2012): Kraftige stormfloer og utbredt flom.
        - Stormen “Dagmar” (Norge 2011): Mesta stoppet, omfattende skogskader.

        Forebygging og beredskap:
        - Sikre løse gjenstander utendørs (hagemøbler, containere osv.).
        - Følge offentlige varsler fra meteorologiske institutter.
        - Ha nødstrøm og vann i reserve ved strømbrudd.
        - Planlegge evakueringsruter i utsatte kyst- og elvesoner.
        """;

    InfoPage infoPageStorm = new InfoPage(
        4L, // id
        "Stormvarsel",
        "Stormvarsel varsler om kraftig vind og mulig stormflo som kan forårsake omfattende "
            + "ødeleggelser på eiendom og infrastruktur.",
        stormBeskrivelse,
        Timestamp.valueOf(LocalDateTime.now()),
        Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
    infoPageRepository.save(infoPageStorm);

  }

  /**
   * Initializes test events for the application.
   */
  public void initializeTestEvents() {

    // Events
    // event1 - Trondheim terror action
    // event2 - Bergen nuclear reactor explosion
    // event3 - Trondheim forest fire
    // event4 - Trondheim storm warning
    // event5 - Trondheim storm warning critical

    Event eventTerrorTrondheim = new Event(
        3L,
        "Trondheim terrorangrep",
        2L,
        63.4305,
        10.3951,
        0.5,
        Timestamp.valueOf(LocalDateTime.now().plusDays(1)),
        Timestamp.valueOf(LocalDateTime.now().plusDays(2)),
        4L,
        "Stay safe!");
    eventRepository.save(eventTerrorTrondheim);

    Event eventNuclearBergen = new Event(
        3L,
        "Bergen atomreaktorekslplosjonen",
        1L,
        60.3913,
        5.3221,
        200.0,
        Timestamp.valueOf(LocalDateTime.now().plusDays(3)),
        Timestamp.valueOf(LocalDateTime.now().plusDays(4)),
        4L,
        "Evakuer umiddelbart!");
    eventRepository.save(eventNuclearBergen);

    Event skogbrannEvent = new Event(
        2L,
        "Brann i Baklidammen",
        3L,
        63.4240,
        10.3274,
        1.0,
        Timestamp.valueOf(LocalDateTime.now().plusHours(2)),
        Timestamp.valueOf(LocalDateTime.now().plusDays(1)),
        2L,
        "Evakuer området og følg ulykkesvarsler.");
    eventRepository.save(skogbrannEvent);

    Event stormEvent = new Event(
        1L,
        "Stormvarsel i Trondheim",
        4L,
        63.4021,
        10.4089,
        1.0,
        Timestamp.valueOf(LocalDateTime.now().plusHours(1)),
        Timestamp.valueOf(LocalDateTime.now().plusDays(1)),
        2L,
        "Sikre løse gjenstander og følg med på værmeldingen.");
    eventRepository.save(stormEvent);

    Event stormEventCritical = new Event(
        4L,
        "Livsfarlig storm i Trondheim",
        4L,
        63.3755,
        10.3402,
        1.0,
        Timestamp.valueOf(LocalDateTime.now().plusHours(1)),
        Timestamp.valueOf(LocalDateTime.now().plusDays(1)),
        4L,
        "Prøv å hold deg ilivet og søk ly!");
    eventRepository.save(stormEventCritical);
  }

  /**
   * Initializes test kit types for the application.
   */
  public void initializeFoodTypes() {
    FoodType cannedTomatoes = new FoodType();
    cannedTomatoes.setName("Hermetiske tomater");
    cannedTomatoes.setUnit("g");
    cannedTomatoes.setCaloriesPerUnit(0.2f);
    foodTypeRepository.save(cannedTomatoes);

    FoodType lentils = new FoodType();
    lentils.setName("Linser");
    lentils.setUnit("g");
    lentils.setCaloriesPerUnit(3.5f);
    foodTypeRepository.save(lentils);

    FoodType rice = new FoodType();
    rice.setName("Ris");
    rice.setUnit("g");
    rice.setCaloriesPerUnit(1.3f);
    foodTypeRepository.save(rice);

    FoodType potatoes = new FoodType();
    potatoes.setName("Poteter");
    potatoes.setUnit("g");
    potatoes.setCaloriesPerUnit(7.7f);
    foodTypeRepository.save(potatoes);

    FoodType tomatoSoup = new FoodType();
    tomatoSoup.setName("Tomat Suppe");
    tomatoSoup.setUnit("g");
    tomatoSoup.setCaloriesPerUnit(0.2f);
    foodTypeRepository.save(tomatoSoup);

    FoodType beans = new FoodType();
    beans.setName("Tørre bønner");
    beans.setUnit("g");
    beans.setCaloriesPerUnit(3.4f);
    foodTypeRepository.save(beans);

    FoodType wetBeans = new FoodType();
    wetBeans.setName("Hermetisk kokte bønner");
    wetBeans.setUnit("g");
    wetBeans.setCaloriesPerUnit(1.2f);
    foodTypeRepository.save(wetBeans);

    FoodType oats = new FoodType();
    oats.setName("Havregryn");
    oats.setUnit("g");
    oats.setCaloriesPerUnit(3.8f);
    foodTypeRepository.save(oats);

    FoodType cannedLentils = new FoodType();
    cannedLentils.setName("Hermetiske linser");
    cannedLentils.setUnit("g");
    cannedLentils.setCaloriesPerUnit(1.1f);
    foodTypeRepository.save(cannedLentils);

    FoodType cannedBeans = new FoodType();
    cannedBeans.setName("Hermetiske bønner");
    cannedBeans.setUnit("g");
    cannedBeans.setCaloriesPerUnit(1.2f);
    foodTypeRepository.save(cannedBeans);

    FoodType cannedSpread = new FoodType();
    cannedSpread.setName("Makrell i tomat");
    cannedSpread.setUnit("g");
    cannedSpread.setCaloriesPerUnit(1.8f);
    foodTypeRepository.save(cannedSpread);

    FoodType energyBars = new FoodType();
    energyBars.setName("Energibarer");
    energyBars.setUnit("stk");
    energyBars.setCaloriesPerUnit(250.0f);
    foodTypeRepository.save(energyBars);

    FoodType driedFruit = new FoodType();
    driedFruit.setName("Tørket frukt");
    driedFruit.setUnit("g");
    driedFruit.setCaloriesPerUnit(3.0f);
    foodTypeRepository.save(driedFruit);

    FoodType chocolate = new FoodType();
    chocolate.setName("Sjokolade");
    chocolate.setUnit("g");
    chocolate.setCaloriesPerUnit(55.0f);
    foodTypeRepository.save(chocolate);

    FoodType honey = new FoodType();
    honey.setName("Honning");
    honey.setUnit("g");
    honey.setCaloriesPerUnit(3.2f);
    foodTypeRepository.save(honey);

    FoodType biscuits = new FoodType();
    biscuits.setName("Kjeks");
    biscuits.setUnit("kg");
    biscuits.setCaloriesPerUnit(4800.0f);
    foodTypeRepository.save(biscuits);

    FoodType nuts = new FoodType();
    nuts.setName("Nøtter");
    nuts.setUnit("g");
    nuts.setCaloriesPerUnit(6.5f);
    foodTypeRepository.save(nuts);
  }

  /**
   * Initializes the extra resident types for the application.
   * This includes various types of residents such as adults, children, and pets,
   * along with their respective water and food consumption rates.
   */
  public void initializeExtraResidentTypes() {
    ExtraResidentType adult = new ExtraResidentType();
    adult.setName("Voksen");
    adult.setConsumptionWater(2.85714285714f);
    adult.setConsumptionFood(2000.0f);
    extraResidentTypeRepository.save(adult);

    ExtraResidentType child = new ExtraResidentType();
    child.setName("Barn (1-3 år)");
    child.setConsumptionWater(2.85714285714f);
    child.setConsumptionFood(1200.0f);
    extraResidentTypeRepository.save(child);
    ExtraResidentType child2 = new ExtraResidentType();
    child2.setName("Barn (4-6 år)");
    child2.setConsumptionWater(2.85714285714f);
    child2.setConsumptionFood(1500.0f);
    extraResidentTypeRepository.save(child2);

    ExtraResidentType child3 = new ExtraResidentType();
    child3.setName("Barn (7-12 år)");
    child3.setConsumptionWater(2.85714285714f);
    child3.setConsumptionFood(1800.0f);
    extraResidentTypeRepository.save(child3);

    ExtraResidentType dogUnder10kg = new ExtraResidentType();
    dogUnder10kg.setName("Hund under 10kg");
    dogUnder10kg.setConsumptionWater(2.85714285714f);
    dogUnder10kg.setConsumptionFood(150.0f);
    extraResidentTypeRepository.save(dogUnder10kg);

    ExtraResidentType dogBetween10And25Kg = new ExtraResidentType();
    dogBetween10And25Kg.setName("Hund mellom 10-25kg");
    dogBetween10And25Kg.setConsumptionWater(1.5f);
    dogBetween10And25Kg.setConsumptionFood(220.0f);
    extraResidentTypeRepository.save(dogBetween10And25Kg);

    ExtraResidentType dogOver25Kg = new ExtraResidentType();
    dogOver25Kg.setName("Hund over 25kg");
    dogOver25Kg.setConsumptionWater(2.5f);
    dogOver25Kg.setConsumptionFood(400.0f);
    extraResidentTypeRepository.save(dogOver25Kg);

    ExtraResidentType cat = new ExtraResidentType();
    cat.setName("Katt");
    cat.setConsumptionWater(0.35f);
    cat.setConsumptionFood(75.0f);
    extraResidentTypeRepository.save(cat);
  }

  /**
   * Initializes mapobjects for the application.
   */
  public void initializeMapObjectsAndMapObjectTypes() {
    MapObjectType heartStarterType = new MapObjectType();
    heartStarterType.setName("Hjertestarter");
    heartStarterType.setIcon("activity");
    mapObjectTypeRepository.save(heartStarterType);

    MapObjectType hospitalType = new MapObjectType();
    hospitalType.setName("Sykehus");
    hospitalType.setIcon("hospital");
    mapObjectTypeRepository.save(hospitalType);

    MapObjectType foodStationType = new MapObjectType();
    foodStationType.setName("Matstasjon");
    foodStationType.setIcon("apple");
    mapObjectTypeRepository.save(foodStationType);

    MapObjectType escapeRoomType = new MapObjectType();
    escapeRoomType.setName("Tilfluktsrom");
    escapeRoomType.setIcon("shield");
    mapObjectTypeRepository.save(escapeRoomType);

    MapObject heartStarterGlos = new MapObject();
    heartStarterGlos.setTypeId(1L);
    heartStarterGlos.setLatitude(63.4194f);
    heartStarterGlos.setLongitude(10.4019f);
    heartStarterGlos.setOpening(Timestamp.valueOf(LocalDate.now().atTime(2, 00)));
    heartStarterGlos.setClosing(Timestamp.valueOf(LocalDate.now().atTime(2, 00)));
    heartStarterGlos.setContactPhone("23026212");
    heartStarterGlos.setContactEmail("hjertestarterregister@ous-hf.no");
    heartStarterGlos.setContactName("Hjertestarterregisteret");
    heartStarterGlos.setDescription("Finnes til høyre i inngangen til"
        + " Hovedbygget på NTNU Gløshaugen.");
    mapObjectRepository.save(heartStarterGlos);

    MapObject escapeRoom = new MapObject();
    escapeRoom.setTypeId(4L);
    escapeRoom.setLatitude(63.4264f);
    escapeRoom.setLongitude(10.4053f);
    escapeRoom.setOpening(Timestamp.valueOf(LocalDate.now().atTime(2, 00)));
    escapeRoom.setClosing(Timestamp.valueOf(LocalDate.now().atTime(2, 00)));
    escapeRoom.setContactPhone("33412500");
    escapeRoom.setContactEmail("postmottak@dsb.no");
    escapeRoom.setContactName("Direktoratet for samfunnssikkerhet og beredskap");
    escapeRoom.setDescription("Bakklandet 3b, 7014 Trondheim");
    mapObjectRepository.save(escapeRoom);

    MapObject foodStation = new MapObject();
    foodStation.setTypeId(3L);
    foodStation.setLatitude(63.4122f);
    foodStation.setLongitude(10.4051f);
    foodStation.setOpening(Timestamp.valueOf(LocalDate.now().atTime(8, 00)));
    foodStation.setClosing(Timestamp.valueOf(LocalDate.now().atTime(20, 00)));
    foodStation.setContactPhone("12345678");
    foodStation.setContactEmail("rosenborg@ballklubb.com");
    foodStation.setContactName("Rosenborg Ballklubb");
    foodStation.setDescription("Lerkendal Stadion, Klæbuveien 125, 7031 Trondheim");
    mapObjectRepository.save(foodStation);

    MapObject hospital = new MapObject();
    hospital.setTypeId(2L);
    hospital.setLatitude(63.4205f);
    hospital.setLongitude(10.3877f);
    hospital.setOpening(Timestamp.valueOf(LocalDate.now().atTime(2, 00)));
    hospital.setClosing(Timestamp.valueOf(LocalDate.now().atTime(2, 00)));
    hospital.setContactPhone("72547260");
    hospital.setContactEmail("trondheim.kommune@gmail.com");
    hospital.setContactName("St. Olavs Hospital");
    hospital.setDescription("Olav Kyrres gate 17, 7030 Trondheim");
    mapObjectRepository.save(hospital);
  }
}