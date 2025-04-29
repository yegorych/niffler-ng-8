package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.jdbc.UdUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UdUserEntityRowMapper;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {
    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity create(UserEntity user) {
        return new UdUserDaoJdbc().create(user);
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT u.*, f.*" +
                        "FROM \"user\" u " +
                        "LEFT JOIN friendship f ON u.id = f.requester_id OR u.id = f.addressee_id " +
                        "WHERE u.id = ?")) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                Map<UUID, UserEntity> userMap = new ConcurrentHashMap<>();
                UUID userId = null;
                while (rs.next()) {
                    userId = rs.getObject("id", UUID.class);
                    UserEntity user = userMap.computeIfAbsent(userId, key -> {
                        try {
                            return UdUserEntityRowMapper.instance.mapRow(rs, 1);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    if (rs.getString("status") != null) {
                        FriendshipEntity friendship = new FriendshipEntity();
                        friendship.setStatus(FriendshipStatus.valueOf(rs.getString("status")));
                        friendship.setCreatedDate(new java.util.Date(rs.getTimestamp("created_date").getTime()));
                        UserEntity userEntity = new UserEntity();
                        UUID addresseeId = rs.getObject("addressee_id", UUID.class);
                        UUID requesterId = rs.getObject("requester_id", UUID.class);
                        if (addresseeId.equals(id)) {
                            friendship.setAddressee(user);
                            userEntity.setId(requesterId);
                            friendship.setRequester(userEntity);
                            user.getFriendshipAddressees().add(friendship);
                        } else {
                            userEntity.setId(addresseeId);
                            friendship.setAddressee(userEntity);
                            friendship.setRequester(user);
                            user.getFriendshipRequests().add(friendship);
                        }
                    }
                }
                return Optional.ofNullable(userMap.get(userId));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO friendship (requester_id, addressee_id, status, created_date)" +
                        "VALUES (?, ?, ?, ?)")) {
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, String.valueOf(FriendshipStatus.PENDING));
            ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO friendship (requester_id, addressee_id, status, created_date)" +
                        "VALUES (?, ?, ?, ?)")) {
            ps.setObject(1, addressee.getId());
            ps.setObject(2, requester.getId());
            ps.setString(3, String.valueOf(FriendshipStatus.PENDING));
            ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO friendship (requester_id, addressee_id, status, created_date)" +
                        "VALUES (?, ?, ?, ?)")) {
            addFriendshipRecord(ps, requester.getId(), addressee.getId());
            addFriendshipRecord(ps, addressee.getId(), requester.getId());
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addFriendshipRecord(PreparedStatement ps, UUID requesterId, UUID addresseeId) throws SQLException {
        ps.setObject(1, requesterId);
        ps.setObject(2, addresseeId);
        ps.setString(3, String.valueOf(FriendshipStatus.ACCEPTED));
        ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));
        ps.addBatch();
        ps.clearParameters();
    }
}
