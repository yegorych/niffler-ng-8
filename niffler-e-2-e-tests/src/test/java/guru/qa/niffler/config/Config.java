package guru.qa.niffler.config;

import javax.annotation.Nonnull;

public interface Config {

  static Config getInstance() {
    return LocalConfig.instance;
  }

  @Nonnull
  String frontUrl();
  @Nonnull
  String authUrl();
  @Nonnull
  String authJdbcUrl();
  @Nonnull
  String gatewayUrl();
  @Nonnull
  String userdataUrl();
  @Nonnull
  String userdataJdbcUrl();
  @Nonnull
  String spendUrl();
  @Nonnull
  String spendJdbcUrl();
  @Nonnull
  String currencyJdbcUrl();
  @Nonnull
  String ghUrl();
  @Nonnull
  String currencyGrpcAddress();

  default int currencyGrpcPort() {
    return 8092;
  }

}
