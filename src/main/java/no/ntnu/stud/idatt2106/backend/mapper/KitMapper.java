package no.ntnu.stud.idatt2106.backend.mapper;

import no.ntnu.stud.idatt2106.backend.model.base.Kit;
import no.ntnu.stud.idatt2106.backend.model.request.KitRequest;
import no.ntnu.stud.idatt2106.backend.model.response.KitResponse;

/**
 * A utility class for mapping between KitResponse objects and Kit models.
 */
public class KitMapper {
  
  /**
   * Converts a KitResponse object to a Kit model.
   *
   * @param request the KitResponse object to convert
   * @return the converted Kit model
   */
  public static Kit toModel(KitRequest request) {
    Kit kit = new Kit();
    kit.setName(request.getName());
    kit.setDescription(request.getDescription());
    return kit;
  }

  /**
   * Converts a Kit model to a KitResponse object.
   *
   * @param kit the Kit model to convert
   * @return the converted KitResponse object
   */
  public static KitResponse toResponse(Kit kit) {
    KitResponse response = new KitResponse();
    response.setName(kit.getName());
    response.setDescription(kit.getDescription());
    return response;
  }
}
