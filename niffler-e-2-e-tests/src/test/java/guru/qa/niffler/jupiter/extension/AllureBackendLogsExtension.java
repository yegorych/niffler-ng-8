package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.TestResult;
import lombok.SneakyThrows;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class AllureBackendLogsExtension implements SuiteExtension{
    public static final String caseName = "Niffler backend logs";
    String[] serviceNames = {"niffler-auth", "niffler-spend", "niffler-userdata", "niffler-gateway", "niffler-currency"};

    @SneakyThrows
    @Override
    public void afterSuite() {
        final AllureLifecycle allureLifecycle = Allure.getLifecycle();
        final String caseId = UUID.randomUUID().toString();
        allureLifecycle.scheduleTestCase(new TestResult().setUuid(caseId).setName(caseName));
        allureLifecycle.startTestCase(caseId);
        addAttachmentLogs(allureLifecycle, serviceNames);
        allureLifecycle.stopTestCase(caseId);
        allureLifecycle.writeTestCase(caseId);
    }

    private static void addAttachmentLogs(AllureLifecycle allureLifecycle, String... serviceNames) throws IOException {
        for (String serviceName : serviceNames) {
            allureLifecycle.addAttachment(
                    serviceName + " log",
                    "text/html",
                    ".log",
                    Files.newInputStream(
                            Path.of(String.format("./logs/%s/app.log", serviceName))
                    )
            );
        }
    }
}
