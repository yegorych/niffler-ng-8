package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.springJdbc.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.springJdbc.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.model.Authority;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository {
    private static final Config CFG = Config.getInstance();
    private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        AuthUserEntity aue = new AuthUserDaoSpringJdbc().create(user);
        user.setId(aue.getId());
        new AuthAuthorityDaoSpringJdbc().create(
                user.getAuthorities().toArray(AuthorityEntity[]::new));
        return user;
    }

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
                                Objects.requireNonNull(user).getAuthorities().add(authority);
                            }
                            return userMap.get(userId);
                        }
                )
        );
    }
}
