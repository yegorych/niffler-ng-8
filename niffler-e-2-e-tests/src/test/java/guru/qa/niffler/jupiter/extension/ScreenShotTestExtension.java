package guru.qa.niffler.jupiter.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.model.allure.ScreenDif;
import io.qameta.allure.Allure;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

@ParametersAreNonnullByDefault
public class ScreenShotTestExtension implements ParameterResolver, TestExecutionExceptionHandler {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ScreenShotTestExtension.class);
    public static final String ASSERT_SCREEN_MESSAGE = "Screen comparison failure";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Base64.Encoder encoder = Base64.getEncoder();

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return AnnotationSupport.isAnnotated(extensionContext.getRequiredTestMethod(), ScreenShotTest.class) &&
                parameterContext.getParameter().getType().isAssignableFrom(BufferedImage.class);
    }

    @SneakyThrows
    @Override
    @Nonnull
    public BufferedImage resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return ImageIO.read(
                new ClassPathResource(
                        AnnotationSupport.findAnnotation(
                                extensionContext.getRequiredTestMethod(), ScreenShotTest.class)
                                .orElseThrow().value())
                        .getInputStream());
    }

  @Override
  public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
    final ScreenShotTest screenShotTest = context.getRequiredTestMethod().getAnnotation(ScreenShotTest.class);
    if (screenShotTest != null) {
      if (screenShotTest.rewriteExpected()) {
        final BufferedImage actual = getActual();
        if (actual != null) {
          ImageIO.write(
              actual,
              "png",
              new File("src/test/resources/" + screenShotTest.value())
          );
        }
      }

      if (throwable.getMessage().contains(ASSERT_SCREEN_MESSAGE)) {
        ScreenDif screenDif = new ScreenDif(
            "data:image/png;base64," + encoder.encodeToString(imageToBytes(getExpected())),
            "data:image/png;base64," + encoder.encodeToString(imageToBytes(getActual())),
            "data:image/png;base64," + encoder.encodeToString(imageToBytes(getDiff()))
        );

        Allure.addAttachment(
            "Screenshot diff",
            "application/vnd.allure.image.diff",
            objectMapper.writeValueAsString(screenDif)
        );
      }
    }
    throw throwable;
  }
//    @Override
//    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
//        if (throwable.getMessage().contains(ASSERT_SCREEN_MESSAGE)) {
//            AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), ScreenShotTest.class).ifPresent(
//                    annotation -> {
//                        if (annotation.rewriteExpected()){
//                            try {
//                                ImageIO.write(
//                                        getActual(),
//                                        "png",
//                                        new File("src/test/resources/" + annotation.value()));
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                    }
//            );
//            ScreenDif screenDif = new ScreenDif(
//                    "data:image/png;base64," + encoder.encodeToString(imageToBytes(getExpected())),
//                    "data:image/png;base64," + encoder.encodeToString(imageToBytes(getActual())),
//                    "data:image/png;base64," + encoder.encodeToString(imageToBytes(getDiff()))
//            );
//            Allure.addAttachment(
//                    "Screenshot diff",
//                    "application/vnd.allure.image.diff",
//                    objectMapper.writeValueAsString(screenDif)
//            );
//        }
//        throw throwable;
//
//    }

    public static void setExpected(BufferedImage expected) {
        TestsMethodContextExtension.context().getStore(NAMESPACE).put("expected", expected);
    }
    @Nullable
    public static BufferedImage getExpected() {
        return TestsMethodContextExtension.context().getStore(NAMESPACE).get("expected", BufferedImage.class);
    }

    public static void setActual(BufferedImage actual) {
        TestsMethodContextExtension.context().getStore(NAMESPACE).put("actual", actual);
    }
    @Nullable
    public static BufferedImage getActual() {
        return TestsMethodContextExtension.context().getStore(NAMESPACE).get("actual", BufferedImage.class);
    }

    public static void setDiff(BufferedImage diff) {
        TestsMethodContextExtension.context().getStore(NAMESPACE).put("diff", diff);
    }
    @Nullable
    public static BufferedImage getDiff() {
        return TestsMethodContextExtension.context().getStore(NAMESPACE).get("diff", BufferedImage.class);
    }

    private static byte[] imageToBytes(BufferedImage image) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
