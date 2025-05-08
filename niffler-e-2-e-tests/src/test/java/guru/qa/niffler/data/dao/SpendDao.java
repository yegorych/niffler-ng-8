package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDao {
  SpendEntity create(SpendEntity spend);
  SpendEntity update(SpendEntity spend);
  Optional<SpendEntity> findSpendById(UUID id);
  Optional<SpendEntity> findByUsernameAndDescription(String username, String description);
  List<SpendEntity> findAllByUsername(String username);
  void delete(SpendEntity spend);
  List<SpendEntity> findAll();


}
