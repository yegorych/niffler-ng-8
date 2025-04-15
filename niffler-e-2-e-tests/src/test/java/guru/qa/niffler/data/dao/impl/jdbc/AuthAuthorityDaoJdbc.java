package guru.qa.niffler.data.dao.impl.jdbc;

import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.model.Authority;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthorityDao {
    private final Connection connection;
    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(AuthorityEntity... authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO authority (user_id, authority) " +
                        "VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            for (AuthorityEntity ae : authority) {
                ps.setObject(1, ae.getUserId());
                ps.setString(2, String.valueOf(ae.getAuthority()));
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthorityEntity> findByUserId(UUID id) {
        List<AuthorityEntity> aeList = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM authority WHERE user_id = ?"
        )){
            ps.setObject(1, id);
            ps.execute();
            try(ResultSet rs = ps.getResultSet()){
                while(rs.next()){
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setId(rs.getObject("id", UUID.class));
                    ae.setAuthority(rs.getObject("authority", Authority.class));
                    ae.setUserId(rs.getObject("user_id", UUID.class));
                    aeList.add(ae);
                }
                return aeList;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(AuthorityEntity authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM authority WHERE id = ?")) {
            ps.setObject(1, authority.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthorityEntity> findAll() {
        List<AuthorityEntity> aeList = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM authority"
        )){
            ps.execute();
            try(ResultSet rs = ps.getResultSet()){
                while(rs.next()){
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setId(rs.getObject("id", UUID.class));
                    ae.setAuthority(rs.getObject("authority", Authority.class));
                    ae.setUserId(rs.getObject("user_id", UUID.class));
                    aeList.add(ae);
                }
                return aeList;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
