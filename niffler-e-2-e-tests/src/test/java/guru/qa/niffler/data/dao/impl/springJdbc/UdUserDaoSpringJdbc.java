package guru.qa.niffler.data.dao.impl.springJdbc;

import guru.qa.niffler.data.dao.UserdataDao;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UdUserEntityRowMapper;
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
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class UdUserDaoSpringJdbc implements UserdataDao {

  private static final Config CFG = Config.getInstance();

  @NotNull
  @Override
  public UserEntity create(UserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    KeyHolder kh = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
          "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
              "VALUES (?,?,?,?,?,?,?)",
          Statement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getCurrency().name());
      ps.setString(3, user.getFirstname());
      ps.setString(4, user.getSurname());
      ps.setBytes(5, user.getPhoto());
      ps.setBytes(6, user.getPhotoSmall());
      ps.setString(7, user.getFullname());
      return ps;
    }, kh);

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    user.setId(generatedKey);
    return user;
  }

  @NotNull
  @Override
  public Optional<UserEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    try{
      return Optional.ofNullable(
              jdbcTemplate.queryForObject(
                      "SELECT * FROM \"user\" WHERE id = ?",
                      UdUserEntityRowMapper.instance,
                      id
              )
      );
    } catch (EmptyResultDataAccessException e){
      return Optional.empty();
    }

  }

  @NotNull
  @Override
  public Optional<UserEntity> findByUsername(String username) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    try{
      return Optional.ofNullable(
              jdbcTemplate.queryForObject(
                      "SELECT * FROM \"user\" WHERE username = ?",
                      UdUserEntityRowMapper.instance,
                      username
              )
      );
    } catch (EmptyResultDataAccessException e){
      return Optional.empty();
    }
  }

  @Override
  public void delete(UserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    jdbcTemplate.update("DELETE FROM \"user\" WHERE id = ?", user.getId());
  }

  @NotNull
  @Override
  public List<UserEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    return jdbcTemplate.query(
            "SELECT * from \"user\"",
            UdUserEntityRowMapper.instance
    );
  }

  @NotNull
  @Override
  public UserEntity update(UserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
              "UPDATE \"user\" SET username = ?, currency = ?, firstname = ?, " +
                      "surname = ?, photo = ?, photo_small = ?, full_name = ? " +
                      "WHERE id = ?"
      );
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getCurrency().name());
      ps.setString(3, user.getFirstname());
      ps.setString(4, user.getSurname());
      ps.setBytes(5, user.getPhoto());
      ps.setBytes(6, user.getPhotoSmall());
      ps.setString(7, user.getFullname());
      return ps;
    });
    return user;
  }
}
