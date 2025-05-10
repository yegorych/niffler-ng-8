package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.UUID;

public interface SpendClient {
    SpendJson createSpend(SpendJson spend);
    SpendJson updateSpend(SpendJson spend);
    void deleteSpend(SpendJson spendJson);
    SpendJson findSpendById(UUID id);
    SpendJson findSpend(SpendJson spendJson);
    CategoryJson createCategory(CategoryJson category);
    void deleteCategory(CategoryJson category);
}
