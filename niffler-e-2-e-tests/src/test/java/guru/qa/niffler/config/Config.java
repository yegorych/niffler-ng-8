package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.instance;
  }

  String frontUrl();

  String spendUrl();

  String ghUrl();

  String authUrl();

  String gatewayUrl();

  String userdataUrl();
}
