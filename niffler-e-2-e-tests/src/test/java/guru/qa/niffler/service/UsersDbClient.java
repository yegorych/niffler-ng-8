package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositorySpringJdbc;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositorySpringJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.Authority;
import guru.qa.niffler.model.UserJson;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;


public class UsersDbClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final AuthUserRepository authUserRepository = new AuthUserRepositorySpringJdbc();
  private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryJdbc();
  private final UserdataUserRepository userdataUserSpringRepository = new UserdataUserRepositorySpringJdbc();


  private final TransactionTemplate txTemplate = new TransactionTemplate(
      new JdbcTransactionManager(
          DataSources.dataSource(CFG.authJdbcUrl())
      )
  );

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.authJdbcUrl(),
      CFG.userdataJdbcUrl()
  );

    public UserJson createUser(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = getAuthUserEntity(user);
                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            userdataUserRepository.create(UserEntity.fromJson(user)),
                            null
                    );

                }
        );
    }

    //для теста
    public UserJson createUserWithFriend(UserJson user, UserJson friend) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = getAuthUserEntity(user);
                    AuthUserEntity friendAuthUser = getAuthUserEntity(friend);
                    authUserRepository.create(authUser);
                    authUserRepository.create(friendAuthUser);
                    UserEntity friendUserEntity = userdataUserSpringRepository.create(UserEntity.fromJson(friend));
                    UserEntity userEntity = userdataUserSpringRepository.create(UserEntity.fromJson(user));
                    userdataUserSpringRepository.addFriend(userEntity, friendUserEntity);
                    return UserJson.fromEntity(
                            userEntity,
                            null
                    );

                }
        );
    }

    private static AuthUserEntity getAuthUserEntity(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(authUser);
                            ae.setAuthority(e);
                            return ae;
                        }
                ).toList()
        );
        return authUser;
    }


}
