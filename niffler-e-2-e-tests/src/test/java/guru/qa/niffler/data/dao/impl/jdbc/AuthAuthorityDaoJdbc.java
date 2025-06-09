package guru.qa.niffler.data.dao.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.model.Authority;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.jdbc.Connections.holder;

@ParametersAreNonnullByDefault
@SuppressWarnings("resource")
public class AuthAuthorityDaoJdbc implements AuthorityDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public void create(AuthorityEntity... authority) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (AuthorityEntity a : authority) {
                ps.setObject(1, a.getUser().getId());
                ps.setString(2, a.getAuthority().name());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(AuthorityEntity... authority) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "UPDATE \"authority\" SET user_id = ?, authority = ? WHERE id = ?")) {
            for (AuthorityEntity a : authority) {
                ps.setObject(1, a.getUser().getId());
                ps.setString(2, a.getAuthority().name());
                ps.setObject(3, a.getId());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    @Override
    @SuppressWarnings("resource")
    public List<AuthorityEntity> findByUserId(UUID id) {
        List<AuthorityEntity> aeList = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM authority WHERE user_id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    AuthorityEntity ae = new AuthorityEntity();
                    AuthUserEntity user = new AuthUserEntity();
                    ae.setId(rs.getObject("id", UUID.class));
                    ae.setAuthority(Authority.valueOf(rs.getString("authority")));
                    user.setId(rs.getObject("user_id", UUID.class));
                    ae.setUser(user);
                    aeList.add(ae);
                }
                return aeList;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(AuthorityEntity... authority) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "DELETE FROM authority WHERE id = ?")) {
            for (AuthorityEntity ae : authority) {
                ps.setObject(1, ae.getId());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    @Override
    public List<AuthorityEntity> findAll() {
        List<AuthorityEntity> aeList = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM authority"
        )) {
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    AuthorityEntity ae = new AuthorityEntity();
                    AuthUserEntity user = new AuthUserEntity();
                    ae.setId(rs.getObject("id", UUID.class));
                    ae.setAuthority(Authority.valueOf(rs.getString("authority")));
                    user.setId(rs.getObject("user_id", UUID.class));
                    ae.setUser(user);
                    aeList.add(ae);
                }
                return aeList;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
