package guru.qa.niffler.data.dao.impl.jdbc;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoJdbc implements CategoryDao {

  private final Connection connection;

  public CategoryDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public CategoryEntity create(CategoryEntity category) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO category (username, name, archived) " +
            "VALUES (?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, category.getUsername());
      ps.setString(2, category.getName());
      ps.setBoolean(3, category.isArchived());

      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can`t find id in ResultSet");
        }
      }
      category.setId(generatedKey);
      return category;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM category WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                return rs.next() ? Optional.of(mapResultSetToCategoryEntity(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

  @Override
  public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
      try(PreparedStatement ps = connection.prepareStatement(
              "SELECT * FROM category WHERE username = ? AND name = ?"
      )) {
        ps.setString(1, username);
        ps.setString(2, categoryName);
        ps.execute();
        try(ResultSet rs = ps.getResultSet()){
          return rs.next() ? Optional.of(mapResultSetToCategoryEntity(rs)) : Optional.empty();
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
  }

  @Override
  public List<CategoryEntity> findAllByUsername(String username) {
    List<CategoryEntity> ceList = new ArrayList<>();
      try(PreparedStatement ps = connection.prepareStatement(
              "SELECT * FROM category WHERE username = ?"
      )){
        ps.setString(1, username);
        ps.execute();
        try(ResultSet rs = ps.getResultSet()){
          while(rs.next()){
            ceList.add(mapResultSetToCategoryEntity(rs));
          }
          return ceList;
        }
      }
    catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

      @Override
  public void deleteCategory(CategoryEntity category) {
      try(PreparedStatement ps = connection.prepareStatement(
          "DELETE FROM category WHERE id = ?"
      )){
        ps.setObject(1, category.getId());
        ps.execute();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
  }

  @Override
  public List<CategoryEntity> findAll() {
    List<CategoryEntity> ceList = new ArrayList<>();
    try(PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM category"
    )){
      ps.execute();
      try(ResultSet rs = ps.getResultSet()){
        while(rs.next()){
          ceList.add(mapResultSetToCategoryEntity(rs));
        }
        return ceList;
      }
    }
    catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static CategoryEntity mapResultSetToCategoryEntity(ResultSet rs)  {
    try {
      CategoryEntity ce = new CategoryEntity();
      ce.setId(rs.getObject("id", UUID.class));
      ce.setUsername(rs.getString("username"));
      ce.setName(rs.getString("name"));
      ce.setArchived(rs.getBoolean("archived"));
      return ce;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }





}
