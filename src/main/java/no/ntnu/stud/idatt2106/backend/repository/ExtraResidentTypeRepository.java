package no.ntnu.stud.idatt2106.backend.repository;

import no.ntnu.stud.idatt2106.backend.model.base.ExtraResidentType;

import java.util.List;
import java.util.Optional;

public interface ExtraResidentTypeRepository {
  Optional<ExtraResidentType> findById(int id);
  List<ExtraResidentType> findAll();
  void save(ExtraResidentType type);
  void update(ExtraResidentType type);
  void deleteById(int id);
}
