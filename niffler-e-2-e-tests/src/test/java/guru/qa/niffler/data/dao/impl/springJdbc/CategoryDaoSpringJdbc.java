package guru.qa.niffler.data.dao.impl.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.mapper.CategoryEntityRowMapper;
import guru.qa.niffler.data.jdbc.DataSources;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault

public class CategoryDaoSpringJdbc implements CategoryDao {
    private static final Config CFG = Config.getInstance();

    @NotNull
    @Override
    public CategoryEntity create(CategoryEntity category) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO category (username, name, archived) " +
                            "VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, category.getUsername());
            ps.setString(2, category.getName());
            ps.setBoolean(3, category.isArchived());
            return ps;
        }, keyHolder);
        final UUID generatedKey = (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        category.setId(generatedKey);
        return category;
    }

    @NotNull
    @Override
    public CategoryEntity update(CategoryEntity category) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE category SET username = ?, name = ?, archived = ? WHERE id = ?"
            );
            ps.setString(1, category.getUsername());
            ps.setString(2, category.getName());
            ps.setBoolean(3, category.isArchived());
            ps.setObject(4, category.getId());
            return ps;
        });
        return category;
    }

    @NotNull
    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM category WHERE id = ?",
                        CategoryEntityRowMapper.instance,
                        id
                )
        );
    }

    @NotNull
    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject( //пришлось обернуть в try, т.к queryForObject выбрасывает исключение если результат != 1
                            "SELECT * FROM category WHERE username = ? AND name = ?",
                            CategoryEntityRowMapper.instance,
                            username,
                            categoryName

                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

//    @Override
//    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
//        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
//                "SELECT * FROM category WHERE username = ? AND name = ?"
//        )) {
//            ps.setString(1, username);
//            ps.setString(2, categoryName);
//            ps.execute();
//            try(ResultSet rs = ps.getResultSet()){
//                return rs.next() ? Optional.of(mapResultSetToCategoryEntity(rs)) : Optional.empty();
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @NotNull
    @Override
    public List<CategoryEntity> findAllByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        return jdbcTemplate.query(
                "SELECT * FROM category WHERE username = ?",
                CategoryEntityRowMapper.instance,
                username
        );
    }

    @Override
    public void deleteCategory(CategoryEntity category) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        jdbcTemplate.update(
                "DELETE FROM category WHERE id = ?",
                category.getId()
        );
    }

    @NotNull
    @Override
    public List<CategoryEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        return jdbcTemplate.query(
                "SELECT * FROM category",
                CategoryEntityRowMapper.instance
        );
    }
}
