package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import static guru.qa.niffler.data.Databases.transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {

  private static final Config CFG = Config.getInstance();

  public SpendJson createSpend(SpendJson spend) {
    return transaction(connection -> {
          SpendEntity spendEntity = SpendEntity.fromJson(spend);
          if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                .create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
          }
          return SpendJson.fromEntity(
              new SpendDaoJdbc(connection).create(spendEntity)
          );
        },
        CFG.spendJdbcUrl()
    );
  }


  public Optional<SpendJson> findSpendById(UUID id) {
    return spendDao.findSpendById(id)
            .map(spendEntity -> {
              categoryDao.findCategoryById(spendEntity.getCategory().getId())
                      .ifPresent(spendEntity::setCategory);
              return SpendJson.fromEntity(spendEntity);
            });
  }


  public List<SpendJson> findAllSpendsByUsername(String username) {
    return spendDao.findAllByUsername(username).stream()
            .map(se -> {
              categoryDao.findCategoryById(se.getCategory().getId())
                      .ifPresent(se::setCategory);
              return SpendJson.fromEntity(se);
            }).toList();
  }


  public void deleteSpend(SpendJson spend){
    spendDao.delete(SpendEntity.fromJson(spend));
  }

  public CategoryJson createCategory(CategoryJson category) {
    CategoryEntity ce = CategoryEntity.fromJson(category);
    return CategoryJson.fromEntity(categoryDao.create(ce));
  }

  public Optional<CategoryJson> findCategoryById(UUID id){
    return categoryDao.findCategoryById(id).map(CategoryJson::fromEntity);
  }

  public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName){
    return categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName)
            .map(CategoryJson::fromEntity);
  }

  public List<CategoryJson> findAllCategoriesByUsername(String username){
    return categoryDao.findAllByUsername(username)
            .stream()
            .map(CategoryJson::fromEntity)
            .toList();
  }

  public void deleteCategory(CategoryJson category){
    categoryDao.deleteCategory(CategoryEntity.fromJson(category));
  }
}
