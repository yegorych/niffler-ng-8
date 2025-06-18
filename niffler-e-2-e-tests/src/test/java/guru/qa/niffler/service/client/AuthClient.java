package guru.qa.niffler.service.client;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface AuthClient {
    void requestRegisterForm();
    void register(String username, String password);
}
