package guru.qa.niffler.data.dao.impl.jdbc;

import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoJdbc implements SpendDao {

  private final Connection connection;

  public SpendDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public SpendEntity create(SpendEntity spend) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
            "VALUES ( ?, ?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, spend.getUsername());
      ps.setDate(2, spend.getSpendDate());
      ps.setString(3, spend.getCurrency().name());
      ps.setDouble(4, spend.getAmount());
      ps.setString(5, spend.getDescription());
      ps.setObject(6, spend.getCategory().getId());

      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can`t find id in ResultSet");
        }
      }
      spend.setId(generatedKey);
      return spend;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<SpendEntity> findSpendById(UUID id) {
      try (PreparedStatement ps = connection.prepareStatement(
              "SELECT * FROM spend WHERE id = ?")) {
        ps.setObject(1, id);
        ps.execute();
        try (ResultSet rs = ps.getResultSet()) {
          return rs.next() ? Optional.of(mapResultSetToSpendEntity(rs)) : Optional.empty();
        }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<SpendEntity> findAllByUsername(String username) {
    List<SpendEntity> seList = new ArrayList<>();
      try (PreparedStatement ps = connection.prepareStatement(
              "SELECT * FROM spend WHERE username = ?")) {
        ps.setObject(1, username);
        ps.execute();
        try (ResultSet rs = ps.getResultSet()) {
          while (rs.next()) {
            seList.add(mapResultSetToSpendEntity(rs));
          }
          return seList;
        }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }


  @Override
  public void delete(SpendEntity spend) {
      try (PreparedStatement ps = connection.prepareStatement(
              "DELETE FROM spend WHERE id = ?")) {
        ps.setObject(1, spend.getId());
        ps.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<SpendEntity> findAll() {
    List<SpendEntity> seList = new ArrayList<>();
    try (PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM spend")) {
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          seList.add(mapResultSetToSpendEntity(rs));
        }
        return seList;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }


  private SpendEntity mapResultSetToSpendEntity(ResultSet rs) {
    try {
      SpendEntity spendEntity = new SpendEntity();
      CategoryEntity categoryEntity = new CategoryEntity();
      spendEntity.setId(rs.getObject("id", UUID.class));
      spendEntity.setUsername(rs.getString("username"));
      spendEntity.setSpendDate(rs.getDate("spend_date"));
      spendEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
      spendEntity.setAmount(rs.getDouble("amount"));
      spendEntity.setDescription(rs.getString("description"));
      categoryEntity.setId(rs.getObject("category_id", UUID.class));
      spendEntity.setCategory(categoryEntity);
      return spendEntity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }

}
