package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.service.client.UsersClient;
import guru.qa.niffler.service.impl.UsersApiClient;
import guru.qa.niffler.service.impl.UsersDbClient;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;

public class InjectClientExtension implements TestInstancePostProcessor {
  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
    for (Field field : testInstance.getClass().getDeclaredFields()) {
      if (field.getType().isAssignableFrom(UsersClient.class)) {
        field.setAccessible(true);
        field.set(testInstance, "api".equals(System.getProperty("client.impl"))
            ? new UsersApiClient()
            : new UsersDbClient());
      }
    }
  }
}
