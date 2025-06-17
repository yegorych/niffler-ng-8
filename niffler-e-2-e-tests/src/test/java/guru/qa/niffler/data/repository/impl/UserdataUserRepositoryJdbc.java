package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataDao;
import guru.qa.niffler.data.dao.impl.jdbc.UdUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UdUserEntityRowMapper;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static guru.qa.niffler.data.jdbc.Connections.holder;

@ParametersAreNonnullByDefault
@SuppressWarnings("resource")
public class UserdataUserRepositoryJdbc implements UserdataUserRepository {
    private static final Config CFG = Config.getInstance();
    private final UserdataDao userdataDao = new UdUserDaoJdbc();

    @NotNull
    @Override
    public UserEntity create(UserEntity user) {
        return userdataDao.create(user);
    }

    @NotNull
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
                return getUserFromRs(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT u.*, f.*" +
                        "FROM \"user\" u " +
                        "LEFT JOIN friendship f ON u.id = f.requester_id OR u.id = f.addressee_id " +
                        "WHERE u.username = ?")) {
            ps.setObject(1, username);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                return getUserFromRs(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    public UserEntity update(UserEntity user) {
        return userdataDao.update(user);
    }

    @Override
    public void remove(UserEntity user) {
        userdataDao.delete(user);
    }

    @Override
    public void sendInvitation(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO friendship (requester_id, addressee_id, status, created_date)" +
                        "VALUES (?, ?, ?, ?)")) {
            addFriendshipRecord(ps, requester.getId(), addressee.getId(), FriendshipStatus.PENDING);
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
            addFriendshipRecord(ps, requester.getId(), addressee.getId(), FriendshipStatus.ACCEPTED);
            ps.addBatch();
            ps.clearParameters();
            addFriendshipRecord(ps, addressee.getId(), requester.getId(), FriendshipStatus.ACCEPTED);
            ps.addBatch();
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void addFriendshipRecord(PreparedStatement ps, UUID requesterId, UUID addresseeId, FriendshipStatus status) throws SQLException {
        ps.setObject(1, requesterId);
        ps.setObject(2, addresseeId);
        ps.setString(3, String.valueOf(status));
        ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));
    }

    private Optional<UserEntity> getUserFromRs(ResultSet rs) throws SQLException {
        Map<UUID, UserEntity> userMap = new ConcurrentHashMap<>();
        UUID userId = null;
        List<FriendshipEntity> friendshipAddressees = new ArrayList<>();
        List<FriendshipEntity> friendshipRequests = new ArrayList<>();
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
                friendship.setCreatedDate(new Date(rs.getTimestamp("created_date").getTime()));
                UserEntity userEntity = new UserEntity();
                UUID addresseeId = rs.getObject("addressee_id", UUID.class);
                UUID requesterId = rs.getObject("requester_id", UUID.class);
                if (addresseeId.equals(userId)) {
                    friendship.setAddressee(user);
                    userEntity.setId(requesterId);
                    friendship.setRequester(userEntity);
                    friendshipAddressees.add(friendship);
                } else {
                    userEntity.setId(addresseeId);
                    friendship.setAddressee(userEntity);
                    friendship.setRequester(user);
                    friendshipRequests.add(friendship);
                }
            }
        }
        userMap.get(userId).setFriendshipAddressees(friendshipAddressees);
        userMap.get(userId).setFriendshipRequests(friendshipRequests);
        return Optional.ofNullable(userMap.get(userId));
    }
}
