package no.ntnu.stud.idatt2106.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import no.ntnu.stud.idatt2106.backend.mapper.HouseholdKitMapper;
import no.ntnu.stud.idatt2106.backend.model.base.HouseholdKit;
import no.ntnu.stud.idatt2106.backend.model.request.HouseholdKitRequest;
import no.ntnu.stud.idatt2106.backend.model.request.MoveHouseholdKitRequest;
import no.ntnu.stud.idatt2106.backend.model.response.HouseholdKitResponse;
import no.ntnu.stud.idatt2106.backend.repository.HouseholdKitRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for managing HouseholdKit relations.
 */
@Service
public class HouseholdKitService {

  private final HouseholdKitRepository repository;

  /**
   * Constructs a HouseholdKitService with the given repository.
   *
   * @param repository the household kit repository
   */
  public HouseholdKitService(HouseholdKitRepository repository) {
    this.repository = repository;
  }

  /**
   * Creates a new household-kit relation.
   *
   * @param request the request containing household and kit IDs
   */
  public void create(HouseholdKitRequest request) {
    HouseholdKit householdKit = HouseholdKitMapper.toEntity(request);
    repository.save(householdKit);
  }

  /**
   * Gets a list of kits for a given household ID.
   *
   * @param householdId the ID of the household
   * @return a list of HouseholdKitResponse
   */
  public List<HouseholdKitResponse> getByHouseholdId(Long householdId) {
    return repository.findByHouseholdId(householdId).stream()
        .map(HouseholdKitMapper::toResponse)
        .collect(Collectors.toList());
  }

  /**
   * Gets a list of households for a given kit ID.
   *
   * @param kitId the ID of the kit
   * @return a list of HouseholdKitResponse
   */
  public List<HouseholdKitResponse> getByKitId(Long kitId) {
    return repository.findByKitId(kitId).stream()
        .map(HouseholdKitMapper::toResponse)
        .collect(Collectors.toList());
  }

  /**
   * Deletes a household-kit relation.
   *
   * @param request the request containing household and kit IDs
   * @return true if deleted, false if relation does not exist
   */
  public boolean delete(HouseholdKitRequest request) {
    Optional<HouseholdKit> relation = repository.findByHouseholdId(
        request.getHouseholdId()).stream()
        .filter(hk -> hk.getKitId().equals(request.getKitId()))
        .findFirst();

    if (relation.isEmpty()) {
      return false;
    }
    repository.delete(relation.get());
    return true;
  }

  /**
   * Moves a kit from one household to another.
   *
   * @param request the request containing old household ID, kit ID, and new household ID
   * @return true if moved successfully, false if relation does not exist
   */
  public boolean moveKitToAnotherHousehold(MoveHouseholdKitRequest request) {
    Optional<HouseholdKit> relation = repository.findByHouseholdId(
        request.getOldHouseholdId()).stream()
        .filter(hk -> hk.getKitId().equals(request.getKitId()))
        .findFirst();

    if (relation.isEmpty()) {
      return false;
    }
    repository.updateHouseholdForKit(request.getOldHouseholdId(), 
        request.getKitId(), request.getNewHouseholdId());
    return true;
  }
}
