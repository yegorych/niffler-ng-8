package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.service.client.SpendClient;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class SpendDbClient implements SpendClient {
    private final SpendRepository spendRepository = new SpendRepositoryHibernate();
    private static final Config CFG = Config.getInstance();
    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );


    @NotNull
    @Override
    public SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> {
            if (spendEntity.getCategory().getId() == null) {
                spendRepository.findCategoryByUsernameAndSpendName(
                                spendEntity.getCategory().getUsername(),
                                spendEntity.getCategory().getName())
                        .ifPresent(spendEntity::setCategory);
            }
            return SpendJson.fromEntity(spendRepository.create(spendEntity));
        }));
    }

    @NotNull
    @Override
    public SpendJson updateSpend(SpendJson spend) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            if (spendEntity.getCategory().getId() == null) {
                spendRepository.findCategoryByUsernameAndSpendName(
                        spendEntity.getCategory().getUsername(),
                        spendEntity.getCategory().getName()
                ).ifPresentOrElse(
                        spendEntity::setCategory,
                        () -> spendEntity.setCategory(
                                spendRepository.createCategory(spendEntity.getCategory())));
            }
            return SpendJson.fromEntity(spendRepository.update(SpendEntity.fromJson(spend)));
        }));

    }

    @Override
    public void deleteSpend(SpendJson spend) {
        xaTransactionTemplate.execute(()->{
            spendRepository.findById(spend.id()).ifPresent(
                    spendRepository::remove);
            return null;
        });

    }

    @Nullable
    @Override
    public SpendJson findSpendById(UUID id) {
        return xaTransactionTemplate.execute(()->
                spendRepository.findById(id)
                        .map(SpendJson::fromEntity)
                        .orElse(null)
        );

    }

    @Nullable
    @Override
    public SpendJson findSpend(SpendJson spendJson) {
        return findSpendById(spendJson.id());
    }

    @NotNull
    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() ->
                CategoryJson.fromEntity(spendRepository.createCategory(CategoryEntity.fromJson(category)))));
    }

    @Override
    public void deleteCategory(CategoryJson category) {
         xaTransactionTemplate.execute(() -> {
            spendRepository.removeCategory(CategoryEntity.fromJson(category));
            return null;
        });
    }
}
