package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.springJdbc.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UdUserEntityRowMapper;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserdataUserRepositorySpringJdbc implements UserdataUserRepository {
    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity create(UserEntity user) {
        return new UdUserDaoSpringJdbc().create(user);
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
        return Optional.ofNullable(jdbcTemplate.query(
                "SELECT u.*, f.*" +
                        "FROM \"user\" u " +
                        "LEFT JOIN friendship f ON u.id = f.requester_id OR u.id = f.addressee_id " +
                        "WHERE u.id = ?",
                new Object[]{id},
                rs -> {
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
                            friendship.setCreatedDate(new Date(rs.getTimestamp("created_date").getTime()));
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
                    return userMap.get(userId);
                }
        ));

    }

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
        jdbcTemplate.update(
                "INSERT INTO friendship (requester_id, addressee_id, status, created_date) " +
                        "VALUES (?, ?, ?, ?)",
                getFriendshipRecord(requester.getId(), addressee.getId(), FriendshipStatus.PENDING)
        );
    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
        jdbcTemplate.update(
                "INSERT INTO friendship (requester_id, addressee_id, status, created_date) " +
                        "VALUES (?, ?, ?, ?)",
                getFriendshipRecord(addressee.getId(), requester.getId(), FriendshipStatus.PENDING)
        );
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
        jdbcTemplate.batchUpdate(
                "INSERT INTO friendship (requester_id, addressee_id, status, created_date) " +
                        "VALUES (?, ?, ?, ?)",
                Arrays.asList(
                        getFriendshipRecord(requester.getId(), addressee.getId(), FriendshipStatus.ACCEPTED),
                        getFriendshipRecord(addressee.getId(), requester.getId(), FriendshipStatus.ACCEPTED))
        );
    }

    public Object[] getFriendshipRecord(UUID requesterId, UUID addresseerId, FriendshipStatus status) {
        return new Object[]{requesterId, addresseerId, String.valueOf(status), new java.sql.Date(System.currentTimeMillis())};
    }

}
