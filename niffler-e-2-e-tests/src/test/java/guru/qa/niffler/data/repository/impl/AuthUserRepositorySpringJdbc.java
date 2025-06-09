package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.AuthorityDao;
import guru.qa.niffler.data.dao.impl.springJdbc.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.springJdbc.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.jdbc.DataSources;
import guru.qa.niffler.model.Authority;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ParametersAreNonnullByDefault
public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();
    private static final Config CFG = Config.getInstance();
    @NotNull
    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        AuthUserEntity aue = authUserDao.create(user);
        user.setId(aue.getId());
        authAuthorityDao.create(
                user.getAuthorities().toArray(AuthorityEntity[]::new));
        return user;
    }

    @NotNull
    @Override
    public AuthUserEntity update(AuthUserEntity user) {
        AuthUserEntity aue = authUserDao.update(user);
        authAuthorityDao.update(
                user.getAuthorities().toArray(AuthorityEntity[]::new));
        return aue;
    }

    @NotNull
    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.query(
                        "select * from \"user\" u join authority a on u.id = a.user_id where u.id = ?",
                        new Object[]{id},
                        rs -> {
                            Map<UUID, AuthUserEntity> userMap = new ConcurrentHashMap<>();
                            UUID userId = null;
                            List<AuthorityEntity> authorityEntities = new ArrayList<>();
                            while (rs.next()) {
                                userId = rs.getObject("id", UUID.class);
                                AuthUserEntity user = userMap.computeIfAbsent(userId, key -> {
                                    try {
                                        return AuthUserEntityRowMapper.instance.mapRow(rs, 1);
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                                AuthorityEntity authority = new AuthorityEntity();
                                authority.setId(rs.getObject("authority_id", UUID.class));
                                authority.setAuthority(Authority.valueOf(rs.getString("authority")));
                                authority.setUser(user);
                                authorityEntities.add(authority);
                            }
                            userMap.get(userId).setAuthorities(authorityEntities);
                            return userMap.get(userId);
                        }
                )
        );
    }

    @NotNull
    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.query(
                        "select * from \"user\" u join authority a on u.id = a.user_id where u.username = ?",
                        new Object[]{username},
                        rs -> {
                            Map<UUID, AuthUserEntity> userMap = new ConcurrentHashMap<>();
                            UUID userId = null;
                            List<AuthorityEntity> authorityEntities = new ArrayList<>();
                            while (rs.next()) {
                                userId = rs.getObject("id", UUID.class);
                                AuthUserEntity user = userMap.computeIfAbsent(userId, key -> {
                                    try {
                                        return AuthUserEntityRowMapper.instance.mapRow(rs, 1);
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                                AuthorityEntity authority = new AuthorityEntity();
                                authority.setId(rs.getObject("authority_id", UUID.class));
                                authority.setAuthority(Authority.valueOf(rs.getString("authority")));
                                authority.setUser(user);
                                authorityEntities.add(authority);
                            }
                            userMap.get(userId).setAuthorities(authorityEntities);
                            return userMap.get(userId);
                        }
                )
        );
    }

    @Override
    public void remove(AuthUserEntity user) {
        authUserDao.delete(user);
        authAuthorityDao.delete(user.getAuthorities().toArray(AuthorityEntity[]::new));
    }
}
