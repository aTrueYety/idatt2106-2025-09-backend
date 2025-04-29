package no.ntnu.stud.idatt2106.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;

import no.ntnu.stud.idatt2106.backend.model.base.MapObjectType;
import no.ntnu.stud.idatt2106.backend.model.request.MapObjectTypeRequest;
import no.ntnu.stud.idatt2106.backend.repository.MapObjectTypeRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MapObjectTypeServiceTest {

  @Mock
  private MapObjectTypeRepositoryImpl repository;

  @Mock
  private JwtService jwtService;

  @InjectMocks
  private MapObjectTypeService service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldGetAllMapObjectTypes() {
    MapObjectType type = new MapObjectType(1L, "Shelter", "icon.png");
    when(repository.findAll()).thenReturn(List.of(type));

    List<MapObjectType> result = service.getAllMapObjectTypes();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getName()).isEqualTo("Shelter");
    verify(repository).findAll();
  }

  @Test
  void shouldGetMapObjectTypeById() {
    MapObjectType type = new MapObjectType(1L, "Shelter", "icon.png");
    when(repository.findById(1L)).thenReturn(type);

    MapObjectType result = service.getMapObjectTypeById(1L);

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("Shelter");
    verify(repository).findById(1L);
  }

  @Test
  void shouldCreateMapObjectType() {
    when(jwtService.extractIsAdmin(anyString())).thenReturn(true);

    MapObjectTypeRequest request = new MapObjectTypeRequest();
    request.setName("Shelter");
    request.setIcon("icon.png");

    service.createMapObjectType(request, "Bearer valid-token");

    verify(repository).save(any(MapObjectType.class));
  }

  @Test
  void shouldUpdateMapObjectType() {
    when(jwtService.extractIsAdmin(anyString())).thenReturn(true);

    MapObjectType type = new MapObjectType(1L, "Updated Shelter", "new-icon.png");

    service.updateMapObjectType(type, "Bearer valid-token");

    verify(repository).update(type);
  }

  @Test
  void shouldDeleteMapObjectType() {
    when(jwtService.extractIsAdmin(anyString())).thenReturn(true);

    service.deleteMapObjectType(1L, "Bearer valid-token");

    verify(repository).deleteById(1L);
  }
}
