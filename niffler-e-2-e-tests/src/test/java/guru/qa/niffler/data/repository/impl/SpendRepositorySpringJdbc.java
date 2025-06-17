package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.springJdbc.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.springJdbc.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class SpendRepositorySpringJdbc implements SpendRepository {
    private static final Config CFG = Config.getInstance();
    private final SpendDao spendDao = new SpendDaoSpringJdbc();
    private final CategoryDao categoryDao = new CategoryDaoSpringJdbc();
    @NotNull
    @Override
    public SpendEntity create(SpendEntity spend) {
        if (spend.getCategory().getId() == null) {
            categoryDao.create(spend.getCategory());
        }
        return spendDao.create(spend);
    }

    @NotNull
    @Override
    public SpendEntity update(SpendEntity spend) {
        return spendDao.update(spend);
    }

    @NotNull
    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDao.create(category);
    }

    @NotNull
    @Override
    public CategoryEntity updateCategory(CategoryEntity category) {
        return categoryDao.update(category);
    }

    @NotNull
    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findCategoryById(id);
    }

    @NotNull
    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name) {
        return categoryDao.findCategoryByUsernameAndCategoryName(username, name);
    }

    @NotNull
    @Override
    public Optional<SpendEntity> findById(UUID id) {
        Optional<SpendEntity> spendEntity = spendDao.findSpendById(id);
        spendEntity.ifPresent(se ->
                categoryDao.findCategoryById(se.getCategory().getId()).ifPresent(se::setCategory));
        return spendEntity;
    }

    @NotNull
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
