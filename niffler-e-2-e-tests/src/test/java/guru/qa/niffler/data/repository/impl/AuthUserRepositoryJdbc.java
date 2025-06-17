package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.jdbc.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.model.Authority;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jdbc.Connections.holder;


@ParametersAreNonnullByDefault
public class AuthUserRepositoryJdbc implements AuthUserRepository {

  private static final Config CFG = Config.getInstance();
  private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
  private final AuthAuthorityDaoJdbc authAuthorityDao = new AuthAuthorityDaoJdbc();

  @NotNull
  @Override
  public AuthUserEntity create(AuthUserEntity user) {
      AuthUserEntity authUser = authUserDao.create(user);
      user.setId(authUser.getId());
      authAuthorityDao.create(user.getAuthorities().toArray(AuthorityEntity[]::new));
      return user;
  }

  @NotNull
  @Override
  public AuthUserEntity update(AuthUserEntity user) {
      AuthUserEntity authUser = authUserDao.update(user);
      authAuthorityDao.update(user.getAuthorities().toArray(AuthorityEntity[]::new));
      return authUser;
  }

  @Override
  public void remove(AuthUserEntity user) {
      authUserDao.delete(user);
      authAuthorityDao.delete(user.getAuthorities().toArray(AuthorityEntity[]::new));
  }

  @NotNull
  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "select * from \"user\" u join authority a on u.id = a.user_id where u.id = ?"
    )) {
      ps.setObject(1, id);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        AuthUserEntity user = null;
        List<AuthorityEntity> authorityEntities = new ArrayList<>();
        while (rs.next()) {
          if (user == null) {
            user = AuthUserEntityRowMapper.instance.mapRow(rs, 1);
          }
          AuthorityEntity ae = new AuthorityEntity();
          ae.setUser(user);
          ae.setId(rs.getObject("a.id", UUID.class));
          ae.setAuthority(Authority.valueOf(rs.getString("authority")));
          authorityEntities.add(ae);
        }
        if (user == null) {
          return Optional.empty();
        } else {
          user.setAuthorities(authorityEntities);
          return Optional.of(user);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @NotNull
  @Override
  public Optional<AuthUserEntity> findByUsername(String username) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "select * from \"user\" u join authority a on u.id = a.user_id where u.username = ?"
    )) {
      ps.setString(1, username);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        AuthUserEntity user = null;
        List<AuthorityEntity> authorityEntities = new ArrayList<>();
        while (rs.next()) {
          if (user == null) {
            user = AuthUserEntityRowMapper.instance.mapRow(rs, 1);
          }

          AuthorityEntity ae = new AuthorityEntity();
          ae.setUser(user);
          ae.setId(rs.getObject("a.id", UUID.class));
          ae.setAuthority(Authority.valueOf(rs.getString("authority")));
          authorityEntities.add(ae);
        }
        if (user == null) {
          return Optional.empty();
        } else {
          user.setAuthorities(authorityEntities);
          return Optional.of(user);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }


}
