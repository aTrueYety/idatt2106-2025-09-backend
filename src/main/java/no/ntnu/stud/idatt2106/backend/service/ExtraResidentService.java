package no.ntnu.stud.idatt2106.backend.service;

import no.ntnu.stud.idatt2106.backend.mapper.ExtraResidentMapper;
import no.ntnu.stud.idatt2106.backend.model.base.ExtraResident;
import no.ntnu.stud.idatt2106.backend.model.request.ExtraResidentRequest;
import no.ntnu.stud.idatt2106.backend.model.response.ExtraResidentResponse;
import no.ntnu.stud.idatt2106.backend.model.update.ExtraResidentUpdate;
import no.ntnu.stud.idatt2106.backend.repository.ExtraResidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExtraResidentService {

  private final ExtraResidentRepository repository;

  public ExtraResidentService(ExtraResidentRepository repository) {
    this.repository = repository;
  }

  public void create(ExtraResidentRequest request) {
    ExtraResident resident = ExtraResidentMapper.toModel(request);
    repository.save(resident);
  }

  public List<ExtraResidentResponse> getAll() {
    return repository.findAll().stream()
        .map(ExtraResidentMapper::toResponse)
        .collect(Collectors.toList());
  }

  public Optional<ExtraResidentResponse> getById(int id) {
    return repository.findById(id).map(ExtraResidentMapper::toResponse);
  }

  public boolean update(int id, ExtraResidentUpdate request) {
    if (repository.findById(id).isEmpty()) return false;
    ExtraResident resident = ExtraResidentMapper.toModel(request);
    resident.setId(id);
    repository.update(resident);
    return true;
  }
  

  

  public boolean delete(int id) {
    if (repository.findById(id).isEmpty()) return false;
    repository.deleteById(id);
    return true;
  }
}
