package no.ntnu.stud.idatt2106.backend.repository;

import no.ntnu.stud.idatt2106.backend.model.base.ExtraResident;

import java.util.List;
import java.util.Optional;

public interface ExtraResidentRepository {
  Optional<ExtraResident> findById(int id);
  List<ExtraResident> findAll();
  void save(ExtraResident extraResident);
  void update(ExtraResident extraResident);
  void deleteById(int id);
}
