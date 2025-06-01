package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.EntityManagers.em;

@ParametersAreNonnullByDefault
public class SpendRepositoryHibernate implements SpendRepository {
    private final EntityManager entityManager = em(CFG.spendJdbcUrl());
    private static final Config CFG = Config.getInstance();

    @NotNull
    @Override
    public SpendEntity create(SpendEntity spend) {
        entityManager.joinTransaction();
        entityManager.persist(spend);
        return spend;
    }

    @NotNull
    @Override
    public SpendEntity update(SpendEntity spend) {
        entityManager.joinTransaction();
        entityManager.merge(spend);
        return spend;
    }

    @NotNull
    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        entityManager.joinTransaction();
        entityManager.persist(category);
        return category;
    }

    @NotNull
    @Override
    public CategoryEntity updateCategory(CategoryEntity category) {
        entityManager.joinTransaction();
        entityManager.merge(category);
        return category;
    }


    @NotNull
    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return Optional.ofNullable(entityManager.find(CategoryEntity.class, id));
    }

    @NotNull
    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name) {
        try {
            return Optional.of(
                    entityManager.createQuery("select ce from CategoryEntity ce " +
                                    "where ce.username =: username and ce.name =: name",
                                    CategoryEntity.class)
                            .setParameter("username", username)
                            .setParameter("name", name)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @NotNull
    @Override
    public Optional<SpendEntity> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(SpendEntity.class, id));
    }

    @NotNull
    @Override
    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
        try {
            return Optional.of(
                    entityManager.createQuery("select se from SpendEntity se " +
                                            "where se.username =: username and se.description =: description",
                                    SpendEntity.class)
                            .setParameter("username", username)
                            .setParameter("description", description)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void remove(SpendEntity spend) {
        entityManager.joinTransaction();
        entityManager.remove(spend);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        entityManager.remove(category);
    }
}
