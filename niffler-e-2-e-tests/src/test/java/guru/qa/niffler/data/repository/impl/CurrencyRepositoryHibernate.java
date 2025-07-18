package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.currency.CurrencyEntity;
import guru.qa.niffler.data.repository.CurrencyRepository;
import guru.qa.niffler.model.CurrencyValues;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static guru.qa.niffler.data.jpa.EntityManagers.em;

public class CurrencyRepositoryHibernate implements CurrencyRepository {

    private static final Config CFG = Config.getInstance();
    private final EntityManager entityManager = em(CFG.currencyJdbcUrl());

    @NotNull
    @Override
    public Optional<CurrencyEntity> findByCurrency(@NotNull CurrencyValues currency) {
        try {
            return Optional.of(
                    entityManager.createQuery(
                            "select u from CurrencyEntity u where u.currency =: currency",
                                    CurrencyEntity.class)
                            .setParameter("currency", currency)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @NotNull
    @Override
    public List<CurrencyEntity> findAllCurrencies() {
        return entityManager.createQuery(
                "select c from CurrencyEntity c", CurrencyEntity.class)
                .getResultList();

    }

}
