package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.checkerframework.checker.units.qual.N;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements
    BeforeTestExecutionCallback,
    AfterTestExecutionCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

  public record StaticUser(
          String username,
          String password,
          String friend,
          String income,
          String outcome) {
    }

  private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
  private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

  static {
    EMPTY_USERS.add(new StaticUser("cat", "12345", null, null, null));
    WITH_FRIEND_USERS.add(new StaticUser("yegor", "12345", "duck", null, null));
    WITH_INCOME_REQUEST_USERS.add(new StaticUser("test", "12345", null, "dog", null));
    WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("dog", "12345", null, null, "test"));
  }



  @Target(ElementType.PARAMETER)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface UserType {
    Type value() default Type.EMPTY;

    enum Type{
        EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
    }
  }

  @Override
  public void beforeTestExecution(ExtensionContext context) {
    Arrays.stream(context.getRequiredTestMethod().getParameters())
            .filter(parameter -> parameter.isAnnotationPresent(UserType.class))
            .map(parameter -> parameter.getAnnotation(UserType.class)).filter(Objects::nonNull)
            .forEach(ut -> {
                Optional<StaticUser> user = Optional.empty();
                StopWatch sw = StopWatch.createStarted();
                while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30){
                    user = getStaticUser(ut);
                }
                Allure.getLifecycle().updateTestCase(testCase -> testCase.setStart(new Date().getTime()));

                Map<UserType, StaticUser> userMap = (Map<UserType, StaticUser>) context.getStore(NAMESPACE)
                    .getOrComputeIfAbsent(
                        context.getUniqueId(),
                        key -> new HashMap()
                    );

                user.ifPresentOrElse(u ->
                         userMap.put(ut, u),
                        () -> {throw new IllegalStateException("Can`t obtain user after 30s.");}
                );
            });
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(
        context.getUniqueId(), Map.class);
    for (Map.Entry<UserType, StaticUser> e : map.entrySet()) {
        returnUserToQueue(e);
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
        && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
  }

  @Override
  public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
      Optional<UserType> userType = AnnotationSupport.findAnnotation(parameterContext.getParameter(), UserType.class);
      return userType.map(ut -> (StaticUser) extensionContext.getStore(NAMESPACE)
              .get(extensionContext.getUniqueId(), Map.class)
              .get(ut))
              .orElseThrow(() -> new IllegalArgumentException("Annotation UserType is missing on the parameter."));

  }


    private static Optional<StaticUser> getStaticUser(UserType ut) {
        return switch (ut.value()){
            case EMPTY -> Optional.ofNullable(EMPTY_USERS.poll());
            case WITH_FRIEND -> Optional.ofNullable(WITH_FRIEND_USERS.poll());
            case WITH_INCOME_REQUEST -> Optional.ofNullable(WITH_INCOME_REQUEST_USERS.poll());
            case WITH_OUTCOME_REQUEST -> Optional.ofNullable(WITH_OUTCOME_REQUEST_USERS.poll());
        };
    }

    private static void returnUserToQueue(Map.Entry<UserType, StaticUser> e) {
        switch (e.getKey().value()){
            case EMPTY -> EMPTY_USERS.add(e.getValue());
            case WITH_FRIEND -> WITH_FRIEND_USERS.add(e.getValue());
            case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS.add(e.getValue());
            case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS.add(e.getValue());
        }
    }
}
