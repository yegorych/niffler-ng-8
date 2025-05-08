package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;

import java.util.UUID;

public class SpendDbClient implements SpendClient {
    private final SpendRepository spendRepository;
    private static final Config CFG = Config.getInstance();
    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendDbClient(SpendRepository spendRepository) {
        this.spendRepository = spendRepository;
    }

    @Override
    public SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        return xaTransactionTemplate.execute(() -> {
            if (spendEntity.getCategory().getId() == null) {
                spendRepository.findCategoryByUsernameAndSpendName(
                        spendEntity.getCategory().getUsername(),
                        spendEntity.getCategory().getName())
                        .ifPresent(spendEntity::setCategory);
            }
            return SpendJson.fromEntity(spendRepository.create(spendEntity));
        });
    }

    @Override
    public SpendJson updateSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(()->{
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
        });

    }

    @Override
    public void deleteSpend(SpendJson spend) {
        xaTransactionTemplate.execute(()->{
            spendRepository.findById(spend.id()).ifPresent(
                    spendRepository::remove);
            return null;
        });

    }

    @Override
    public SpendJson findSpendById(UUID id) {
        return xaTransactionTemplate.execute(()->
                spendRepository.findById(id)
                        .map(SpendJson::fromEntity)
                        .orElse(null)
        );

    }
}
