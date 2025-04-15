package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.jdbc.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.SpendDaoJdbc;
import guru.qa.niffler.data.dao.impl.springJdbc.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.springJdbc.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.TransactionIsolation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.transaction;

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
                CFG.spendJdbcUrl(), TransactionIsolation.READ_UNCOMMITTED
        );
    }

    public SpendJson createSpendSpring(SpendJson spend) {
        return SpendJson.fromEntity(
                new SpendDaoSpringJdbc(dataSource(CFG.spendJdbcUrl()))
                        .create(SpendEntity.fromJson(spend))
        );
    }


    public Optional<SpendJson> findSpendById(UUID id) {
        return transaction(connection -> {
                    return new SpendDaoJdbc(connection).findSpendById(id)
                            .map(spendEntity -> {
                                new CategoryDaoJdbc(connection).findCategoryById(spendEntity.getCategory().getId())
                                        .ifPresent(spendEntity::setCategory);
                                return SpendJson.fromEntity(spendEntity);
                            });
                }, CFG.spendJdbcUrl(), TransactionIsolation.READ_UNCOMMITTED
        );

    }


    public List<SpendJson> findAllSpendsByUsername(String username) {
        return transaction(connection -> {
            return new SpendDaoJdbc(connection).findAllByUsername(username).stream().map(se -> {
                new CategoryDaoJdbc(connection).findCategoryById(se.getCategory().getId()).ifPresent(se::setCategory);
                return SpendJson.fromEntity(se);
            }).toList();
        }, CFG.spendJdbcUrl(), TransactionIsolation.READ_UNCOMMITTED);

    }


    public void deleteSpend(SpendJson spend) {
        transaction(connection -> {
            new SpendDaoJdbc(connection).delete(SpendEntity.fromJson(spend));
        }, CFG.spendJdbcUrl(), TransactionIsolation.READ_UNCOMMITTED);

    }

    public CategoryJson createCategory(CategoryJson category) {
        return transaction(connection -> {
            CategoryEntity ce = CategoryEntity.fromJson(category);
            return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).create(ce));
        }, CFG.spendJdbcUrl(), TransactionIsolation.READ_UNCOMMITTED);

    }

    public CategoryJson createCategorySpring(CategoryJson category) {
        return CategoryJson.fromEntity(
                new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl()))
                        .create(CategoryEntity.fromJson(category))
        );
    }

    public Optional<CategoryJson> findCategoryById(UUID id) {
        return transaction(connection -> {
            return new CategoryDaoJdbc(connection).findCategoryById(id).map(CategoryJson::fromEntity);
        }, CFG.spendJdbcUrl(), TransactionIsolation.READ_UNCOMMITTED);

    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return transaction(connection -> {
            return new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(username, categoryName)
                    .map(CategoryJson::fromEntity);
        }, CFG.spendJdbcUrl(), TransactionIsolation.READ_UNCOMMITTED);

    }

    public List<CategoryJson> findAllCategoriesByUsername(String username) {
        return transaction(connection -> {
            return new CategoryDaoJdbc(connection).findAllByUsername(username)
                    .stream()
                    .map(CategoryJson::fromEntity)
                    .toList();
        }, CFG.spendJdbcUrl(), TransactionIsolation.READ_UNCOMMITTED);

    }

    public void deleteCategory(CategoryJson category) {
        transaction(connection -> {
            new CategoryDaoJdbc(connection).deleteCategory(CategoryEntity.fromJson(category));
        }, CFG.spendJdbcUrl(), TransactionIsolation.READ_UNCOMMITTED);
    }

    public void deleteCategorySpring(CategoryJson category) {
        new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).deleteCategory(CategoryEntity.fromJson(category));
    }
}
