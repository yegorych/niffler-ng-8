package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.service.impl.GhApiClient;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.SearchOption;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Method;
import java.util.Optional;


@ParametersAreNonnullByDefault
public class IssueExtension implements ExecutionCondition {

  private static final GhApiClient ghApiClient = new GhApiClient();

  @SneakyThrows
  @Override
  @Nonnull
  public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
    final Optional<Method> method = context.getTestMethod();
    final Class<?> clazz = context.getRequiredTestClass();
    final Optional<DisabledByIssue> annotation;
    if (method.isPresent()) {
      annotation = AnnotationSupport.findAnnotation(
          method.get(),
          DisabledByIssue.class
      );
    } else {
      annotation = AnnotationSupport.findAnnotation(
          clazz,
          DisabledByIssue.class,
          SearchOption.INCLUDE_ENCLOSING_CLASSES
      );
    }

    return annotation.map(
        byIssue -> "open".equals(ghApiClient.issueState(byIssue.value()))
            ? ConditionEvaluationResult.disabled("Disabled by issue #" + byIssue.value())
            : ConditionEvaluationResult.enabled("Issue closed")
    ).orElseGet(
        () -> ConditionEvaluationResult.enabled("Annotation @DisabledByIssue not found")
    );
  }
}
