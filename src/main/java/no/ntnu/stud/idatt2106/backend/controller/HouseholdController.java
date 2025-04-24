package no.ntnu.stud.idatt2106.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import no.ntnu.stud.idatt2106.backend.model.request.HouseholdRequest;
import no.ntnu.stud.idatt2106.backend.service.HouseholdService;

@Tag(
  name = "Households",
  description = "Endpoints for household operations"
)
@RestController
@RequestMapping("api/households")
public class HouseholdController {
  private final static Logger logger = LoggerFactory.getLogger(HouseholdController.class);
  
  @Autowired
  private HouseholdService householdService;

  /**
   * Handles request to register a new household.
   *  
   * @param householdRequest the request containing the details of the household.
   * @return a ResponseEntity containing the registration response or an error message
   */
  @Operation(
    summary = "Creates a new household",
    description = "Creates a new household with the user creating it as a member"
  )
  @PostMapping("/register")
  public ResponseEntity<?> registerHousehold(@RequestBody HouseholdRequest householdRequest) {
    householdService.registerHousehold(householdRequest);
    logger.info("Household created successfully {}");
    return ResponseEntity.ok("Household create successfully");
  }

}
