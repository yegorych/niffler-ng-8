package guru.qa.niffler.data.dao.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataDao;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jdbc.Connections.holder;

@ParametersAreNonnullByDefault
@SuppressWarnings("resource")
public class UdUserDaoJdbc implements UserdataDao {
    private static final Config CFG = Config.getInstance();

    @NotNull
    @Override
    public UserEntity create(UserEntity user) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setBytes(5, user.getPhoto());
            ps.setBytes(6, user.getPhotoSmall());
            ps.setString(7, user.getFullname());
            ps.executeUpdate();
            final UUID generatedUserId;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedUserId = rs.getObject("id", UUID.class);
                } else {
                    throw new IllegalStateException("Can`t find id in ResultSet");
                }
            }
            user.setId(generatedUserId);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    public UserEntity update(UserEntity user) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "UPDATE \"user\" SET username = ?, currency = ?, firstname = ?, surname = ?, photo = ?, " +
                        "photo_small = ?, full_name = ? WHERE id = ?")) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setBytes(5, user.getPhoto());
            ps.setBytes(6, user.getPhotoSmall());
            ps.setString(7, user.getFullname());
            ps.executeUpdate();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @NotNull
    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                return rs.next() ? Optional.of(mapResultSetToUserdataEntity(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE username = ?"
        )) {
            ps.setObject(1, username);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                return rs.next() ? Optional.of(mapResultSetToUserdataEntity(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UserEntity user) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "DELETE FROM \"user\" WHERE id = ?")) {
            ps.setObject(1, user.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    public List<UserEntity> findAll() {
        List<UserEntity> users = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\""
        )) {
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    users.add(mapResultSetToUserdataEntity(rs));
                }
                return users;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    private static UserEntity mapResultSetToUserdataEntity(ResultSet rs) {
        try {
            UserEntity ue = new UserEntity();
            ue.setId(rs.getObject("id", UUID.class));
            ue.setUsername(rs.getString("username"));
            ue.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
            ue.setFirstname(rs.getString("firstname"));
            ue.setSurname(rs.getString("surname"));
            ue.setPhoto(rs.getBytes("photo"));
            ue.setPhotoSmall(rs.getBytes("photo_small"));
            ue.setFullname(rs.getString("full_name"));
            return ue;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
