package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.jdbc.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;

import java.util.Optional;
import java.util.UUID;

public class SpendRepositoryJdbc implements SpendRepository {
    private static final Config CFG = Config.getInstance();
    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    @Override
    public SpendEntity create(SpendEntity spend) {
        if (spend.getCategory().getId() == null) {
            categoryDao.create(spend.getCategory());
        }
        return spendDao.create(spend);
    }

    @Override
    public SpendEntity update(SpendEntity spend) {
        return spendDao.update(spend);
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDao.create(category);
    }

    @Override
    public CategoryEntity updateCategory(CategoryEntity category) {
        return categoryDao.update(category);
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findCategoryById(id);
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name) {
        return categoryDao.findCategoryByUsernameAndCategoryName(username, name);
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {
        Optional<SpendEntity> spendEntity = spendDao.findSpendById(id);
        spendEntity.ifPresent(se ->
                categoryDao.findCategoryById(se.getCategory().getId()).ifPresent(se::setCategory));
        return spendEntity;
    }

    @Override
    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
        Optional<SpendEntity> spendEntity = spendDao.findByUsernameAndDescription(username, description);
        spendEntity.ifPresent(se ->
                categoryDao.findCategoryById(se.getCategory().getId()).ifPresent(se::setCategory));
        return spendEntity;
    }

    @Override
    public void remove(SpendEntity spend) {
        spendDao.delete(spend);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        categoryDao.deleteCategory(category);
    }
}
