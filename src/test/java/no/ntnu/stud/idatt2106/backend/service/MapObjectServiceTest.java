package no.ntnu.stud.idatt2106.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import no.ntnu.stud.idatt2106.backend.model.base.MapObject;
import no.ntnu.stud.idatt2106.backend.model.request.MapObjectRequest;
import no.ntnu.stud.idatt2106.backend.model.response.MapObjectResponse;
import no.ntnu.stud.idatt2106.backend.repository.MapObjectRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for the MapObjectService class.
 */
public class MapObjectServiceTest {

  @Mock
  private MapObjectRepositoryImpl repositoryImpl;

  @Mock
  private JwtService jwtService;

  @Mock
  private MapObjectWebSocketService webSocketService;

  @InjectMocks
  private MapObjectService service;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldCreateMapObjectWithValidTokenAndBroadcast() {
    when(jwtService.extractIsAdmin(any(String.class))).thenReturn(true);

    MapObjectRequest request = new MapObjectRequest();
    request.setTypeId(1L);
    request.setContactEmail("Jacob@gmail.com");
    request.setContactName("Jacob");
    request.setContactPhone("12345678");
    request.setDescription("Test description");
    request.setLatitude(1.0f);
    request.setLongitude(1.0f);
    request.setOpening(new java.sql.Timestamp(System.currentTimeMillis()));
    request.setClosing(new java.sql.Timestamp(System.currentTimeMillis()));

    doAnswer(invocation -> {
      MapObject arg = invocation.getArgument(0);
      arg.setId(1L); // simulerer at databasen setter ID
      return null;
    }).when(repositoryImpl).save(any(MapObject.class));

    MapObjectResponse response = new MapObjectResponse();
    response.setId(1L);
    response.setDescription("Test description");
    when(repositoryImpl.findByIdWithDetail(1L)).thenReturn(response);

    String token = "Bearer valid-token";
    service.createMapObject(request, token);

    verify(repositoryImpl).save(any(MapObject.class));
    verify(webSocketService).broadcastCreated(request);
  }

  @Test
  void shouldNotCreateMapObjectWithInvalidToken() {
    when(jwtService.extractIsAdmin(any(String.class))).thenReturn(false);

    MapObjectRequest request = new MapObjectRequest();
    request.setTypeId(1L);
    request.setContactEmail("Jacob@gmail.com");
    request.setContactName("Jacob");
    request.setContactPhone("12345678");
    request.setDescription("Test description");
    request.setLatitude(1.0f);
    request.setLongitude(1.0f);
    request.setOpening(new java.sql.Timestamp(System.currentTimeMillis()));
    request.setClosing(new java.sql.Timestamp(System.currentTimeMillis()));

    String token = "Invalid token";
    assertThrows(IllegalArgumentException.class, () -> {
      service.createMapObject(request, token);
    });
  }

  @Test
  void shouldReturnAllMapObjects() {
    when(repositoryImpl.findAll()).thenReturn(Collections.singletonList(new MapObject()));
    List<MapObject> result = service.getAllMapObjects();
    assertThat(result).hasSize(1);
    verify(repositoryImpl).findAll();
  }

  @Test
  void shouldReturnMapObjectById() {
    MapObjectResponse response = new MapObjectResponse();
    when(repositoryImpl.findByIdWithDetail(1L)).thenReturn(response);
    MapObjectResponse result = service.getMapObjectById(1L);
    assertThat(result).isEqualTo(response);
    verify(repositoryImpl).findByIdWithDetail(1L);
  }

  @Test
  void shouldCreateMapObjectIfAdmin() {
    when(jwtService.extractIsAdmin(any())).thenReturn(true);
    MapObjectRequest request = new MapObjectRequest();
    when(repositoryImpl.findByIdWithDetail(anyLong())).thenReturn(new MapObjectResponse());
    service.createMapObject(request, "Bearer token");
    verify(repositoryImpl).save(any(MapObject.class));
  }

  @Test
  void shouldThrowWhenUpdatingMapObjectIfNotAdmin() {
    when(jwtService.extractIsAdmin(any())).thenReturn(false);
    MapObject mapObject = new MapObject();
    assertThrows(IllegalArgumentException.class,
        () -> service.updateMapObject(mapObject, "Bearer token"));
  }

  @Test
  void shouldDeleteMapObjectIfAdmin() {
    when(jwtService.extractIsAdmin(any())).thenReturn(true);
    service.deleteMapObject(1L, "Bearer token");
    verify(repositoryImpl).deleteById(1L);
  }

  @Test
  void shouldThrowWhenDeletingMapObjectIfNotAdmin() {
    when(jwtService.extractIsAdmin(any())).thenReturn(false);
    assertThrows(IllegalArgumentException.class, () -> service.deleteMapObject(1L, "Bearer token"));
  }

  @Test
  void shouldFindMapObjectsInBounds() {
    when(repositoryImpl
        .findAllInBoundsWithDetail(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
        .thenReturn(Collections.emptyList());
    List<MapObjectResponse> result = service.getMapObjectsInBounds(0, 0, 0, 0);
    assertThat(result).isEmpty();
    verify(repositoryImpl).findAllInBoundsWithDetail(0, 0, 0, 0);
  }

  @Test
  void shouldFindMapObjectsInBoundsWithResults() {
    MapObjectResponse response1 = new MapObjectResponse();
    response1.setId(1L);
    response1.setTypeId(1L);
    response1.setLatitude(63.421057f);
    response1.setLongitude(10.393674f);
    response1.setDescription("Test Description 1");

    MapObjectResponse response2 = new MapObjectResponse();
    response2.setId(2L);
    response2.setTypeId(2L);
    response2.setLatitude(63.422000f);
    response2.setLongitude(10.394000f);
    response2.setDescription("Test Description 2");

    when(repositoryImpl
        .findAllInBoundsWithDetail(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
        .thenReturn(List.of(response1, response2));

    List<MapObjectResponse> result = service.getMapObjectsInBounds(63.420000,
        63.423000, 10.390000, 10.400000);

    assertThat(result).hasSize(2);
    assertThat(result.get(0).getId()).isEqualTo(1L);
    assertThat(result.get(0).getDescription()).isEqualTo("Test Description 1");
    assertThat(result.get(1).getId()).isEqualTo(2L);
    assertThat(result.get(1).getDescription()).isEqualTo("Test Description 2");

    verify(repositoryImpl).findAllInBoundsWithDetail(63.420000,
        63.423000, 10.390000, 10.400000);
  }

  @Test
  void shouldFindClosestMapObject() {
    MapObjectResponse response = new MapObjectResponse();
    when(repositoryImpl.findClosestWithDetail(anyDouble(),
        anyDouble(), anyLong())).thenReturn(response);
    MapObjectResponse result = service.getClosestMapObject(0, 0, 1);
    assertThat(result).isEqualTo(response);
    verify(repositoryImpl).findClosestWithDetail(0, 0, 1);
  }
}
