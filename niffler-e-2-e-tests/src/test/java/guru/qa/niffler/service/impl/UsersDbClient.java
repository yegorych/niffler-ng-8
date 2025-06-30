package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.Authority;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.client.UsersClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;


@ParametersAreNonnullByDefault
public class UsersDbClient implements UsersClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
  private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHibernate();


  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.authJdbcUrl(),
      CFG.userdataJdbcUrl()
  );



    @NotNull
    public UserJson createUser(String username, String password) {
    return Objects.requireNonNull(xaTransactionTemplate.execute(() -> {
                AuthUserEntity authUser = authUserEntity(username, password);
                authUserRepository.create(authUser);
                return UserJson.fromEntity(
                        userdataUserRepository.create(userEntity(username)),
                        null
                );
            }
    ));
  }

    @Override
    public List<UserJson> allUsersWithout(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<UserJson> getFriends(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<UserJson> getIncomeInvitations(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<UserJson> getOutcomeInvitations(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<UserJson> addFriends(UserJson targetUser, int count) {
        List<UserJson> friends = new ArrayList<>();
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            String username = randomUsername();
                            AuthUserEntity authUser = authUserEntity(username, "12345");
                            authUserRepository.create(authUser);
                            UserEntity addressee = userdataUserRepository.create(userEntity(username));
                            userdataUserRepository.addFriend(targetEntity, addressee);
                            UserJson friendJson = UserJson.fromEntity(addressee, FriendshipStatus.FRIEND);
                            targetUser.testData().friends().add(friendJson);
                            friends.add(friendJson);
                            return friends;
                        }
                );
            }
        }
        return friends;
    }

    public List<UserJson> addOutcomeInvitations(UserJson targetUser, int count) {
        List<UserJson> outcomeInvitations = new ArrayList<>();
        if (count > 0) {
          UserEntity targetEntity = userdataUserRepository.findById(
              targetUser.id()
          ).orElseThrow();

          for (int i = 0; i < count; i++) {
            xaTransactionTemplate.execute(() -> {
                  String username = randomUsername();
                  AuthUserEntity authUser = authUserEntity(username, "12345");
                  authUserRepository.create(authUser);
                  UserEntity addressee = userdataUserRepository.create(userEntity(username));
                  userdataUserRepository.sendInvitation(targetEntity, addressee);
                  UserJson outcomeInvitationsUser = UserJson.fromEntity(addressee, FriendshipStatus.INVITE_RECEIVED);
                  targetUser.testData().outcomeInvitations().add(outcomeInvitationsUser);
                  outcomeInvitations.add(outcomeInvitationsUser);
                  return outcomeInvitations;
                }
            );
          }
        }
        return outcomeInvitations;
  }

  public List<UserJson> addIncomeInvitations(UserJson targetUser, int count) {
    List<UserJson> incomeInvitations = new ArrayList<>();
    if (count > 0) {
      UserEntity targetEntity = userdataUserRepository.findById(
          targetUser.id()
      ).orElseThrow();

      for (int i = 0; i < count; i++) {
        xaTransactionTemplate.execute(() -> {
              String username = randomUsername();
              AuthUserEntity authUser = authUserEntity(username, "12345");
              authUserRepository.create(authUser);
              UserEntity addressee = userdataUserRepository.create(userEntity(username));
              userdataUserRepository.sendInvitation(addressee, targetEntity);
              UserJson incomeInvitationsUser = UserJson.fromEntity(addressee, FriendshipStatus.INVITE_SENT);
              targetUser.testData().incomeInvitations().add(incomeInvitationsUser);
              incomeInvitations.add(incomeInvitationsUser);
              return incomeInvitations;
            }
        );
      }
    }
    return incomeInvitations;
  }

  private UserEntity userEntity(String username) {
    UserEntity ue = new UserEntity();
    ue.setUsername(username);
    ue.setCurrency(CurrencyValues.RUB);
    return ue;
  }

  private AuthUserEntity authUserEntity(String username, String password) {
    AuthUserEntity authUser = new AuthUserEntity();
    authUser.setUsername(username);
    authUser.setPassword(pe.encode(password));
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
