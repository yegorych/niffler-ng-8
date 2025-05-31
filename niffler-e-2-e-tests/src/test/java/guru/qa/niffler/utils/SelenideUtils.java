package guru.qa.niffler.utils;

import com.codeborne.selenide.SelenideConfig;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.safari.SafariOptions;

import java.util.Map;

public class SelenideUtils {


    public static final SelenideConfig chromeConfig = new SelenideConfig()
            .browser("chrome")
            .pageLoadStrategy("eager")
            .browserSize("1920x1080")
            .timeout(5000L);

    public static final SelenideConfig firefoxConfig = new SelenideConfig()
            .browser("firefox")
            .pageLoadStrategy("eager")
            .browserSize("1920x1080")
            .timeout(5000L);


}
