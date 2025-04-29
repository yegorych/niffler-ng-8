package guru.qa.niffler.data.dao.impl.springJdbc;

import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.model.Authority;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoSpringJdbc implements AuthorityDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public void create(AuthorityEntity... authority) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    jdbcTemplate.batchUpdate(
        "INSERT INTO authority (user_id, authority) VALUES (? , ?)",
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setObject(1, authority[i].getUser().getId());
            ps.setString(2, authority[i].getAuthority().name());
          }

          @Override
          public int getBatchSize() {
            return authority.length;
          }
        }
    );
  }

    @Override
    public List<AuthorityEntity> findByUserId(UUID id) {
        return List.of();
    }

    @Override
    public void delete(AuthorityEntity authority) {

    }

    @Override
    public List<AuthorityEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return jdbcTemplate.query(
                "SELECT * from authority",
                (rs, rowNum) -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    AuthUserEntity user = new AuthUserEntity();
                    ae.setId(rs.getObject("id", UUID.class));
                    ae.setAuthority(Authority.valueOf(rs.getString("authority")));
                    user.setId(rs.getObject("user_id", UUID.class));
                    ae.setUser(user);
                    return ae;
                }
        );
    }
}
