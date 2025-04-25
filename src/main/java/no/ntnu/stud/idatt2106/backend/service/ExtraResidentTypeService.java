package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.mapper.ExtraResidentTypeMapper;
import no.ntnu.stud.idatt2106.backend.model.base.ExtraResidentType;
import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentTypeRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentTypeResponse;
import no.ntnu.stud.idatt2106.backend.repository.ExtraResidentTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExtraResidentTypeService {

  private final ExtraResidentTypeRepository repository;

  public ExtraResidentTypeService(ExtraResidentTypeRepository repository) {
    this.repository = repository;
  }

  public void create(ExtraResidentTypeRequest request) {
    ExtraResidentType type = ExtraResidentTypeMapper.toModel(request);
    repository.save(type);
  }

  public List<ExtraResidentTypeResponse> getAll() {
    return repository.findAll().stream()
        .map(ExtraResidentTypeMapper::toResponse)
        .collect(Collectors.toList());
  }

  public Optional<ExtraResidentTypeResponse> getById(int id) {
    return repository.findById(id).map(ExtraResidentTypeMapper::toResponse);
  }

  public boolean update(int id, ExtraResidentTypeRequest request) {
    if (repository.findById(id).isEmpty()) return false;
    ExtraResidentType type = ExtraResidentTypeMapper.toModel(request);
    type.setId(id);
    repository.update(type);
    return true;
  }

  public boolean delete(int id) {
    if (repository.findById(id).isEmpty()) return false;
    repository.deleteById(id);
    return true;
  }
}
