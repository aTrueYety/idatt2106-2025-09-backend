package no.ntnu.stud.idatt2106.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.GroupInvite;
import no.ntnu.stud.idatt2106.backend.service.GroupInviteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing group invites.
 */
@Tag(name = "Group Invite", description = "API for managing group invites")
@RestController
@RequestMapping("/api/group-invite")
public class GroupInviteController {
  private static final Logger logger = LoggerFactory.getLogger(GroupInviteController.class);

  @Autowired
  private GroupInviteService groupInviteService;

  /**
   * Retrieves all group invites for a user.
   *
   * @param groupId the ID of the group to retrieve invites for
   * @param token the JWT token of the user
   * @return a ResponseEntity containing the list of group invites
   */
  @Operation(summary = "Get group invites for a specific group", 
      description = "Retrieves all group invites for a specific group based on the group ID " 
                  + "and user token.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved group invites", 
                   content = @Content(mediaType = "application/json", 
                   schema = @Schema(implementation = GroupInvite.class))),
      @ApiResponse(responseCode = "204", description = "No group invites found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized access")
  })
  @GetMapping("/group/{groupId}")
  public ResponseEntity<List<GroupInvite>> getGroupInvitesForUser(
      @PathVariable Long groupId,
      @RequestHeader("Authorization") String token) {
    List<GroupInvite> groupInvites = groupInviteService.findGroupInvitesForGroup(groupId, token);
    logger.info("Retrived group invites for group with token: {}", groupId);
    if (groupInvites.isEmpty()) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.ok(groupInvites);
    }
  }

  /**
   * Retrieves all group invites for a household.
   *
   * @param token the JWT token of the user
   * @return a ResponseEntity containing the list of group invites
   */
  @Operation(summary = "Get group invites for a household", 
             description = "Retrieves all group invites for the user's household based on the " 
                        + "user token.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved group invites", 
                   content = @Content(mediaType = "application/json", 
                   schema = @Schema(implementation = GroupInvite.class))),
      @ApiResponse(responseCode = "204", description = "No group invites found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized access")
  })
  @GetMapping("/household")
  public ResponseEntity<List<GroupInvite>> getGroupInvitesForHousehold(
      @RequestHeader("Authorization") String token) {
    List<GroupInvite> groupInvites = groupInviteService.findGroupInvitesForHousehold(token);
    logger.info("Retrived group invites for household with token: {}", token);
    if (groupInvites.isEmpty()) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.ok(groupInvites);
    }
  }
}
