package guru.qa.niffler.config;

import org.jetbrains.annotations.NotNull;

public class DockerConfig implements Config {

    @NotNull
    @Override
    public String frontUrl() {
        return "";
    }

    @NotNull
    @Override
    public String authUrl() {
        return "";
    }

    @NotNull
    @Override
    public String authJdbcUrl() {
        return "";
    }

    @NotNull
    @Override
    public String gatewayUrl() {
        return "";
    }

    @NotNull
    @Override
    public String userdataUrl() {
        return "";
    }

    @NotNull
    @Override
    public String userdataJdbcUrl() {
        return "";
    }

    @NotNull
    @Override
    public String spendUrl() {
        return "";
    }

    @NotNull
    @Override
    public String spendJdbcUrl() {
        return "";
    }

    @NotNull
    @Override
    public String currencyJdbcUrl() {
        return "";
    }

    @NotNull
    @Override
    public String ghUrl() {
        return "";
    }

    @NotNull
    @Override
    public String currencyGrpcAddress() {
        return "";
    }
}
