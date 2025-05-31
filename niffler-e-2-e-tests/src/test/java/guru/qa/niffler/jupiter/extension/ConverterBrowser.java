package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.model.Browser;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import static guru.qa.niffler.utils.SelenideUtils.*;

public class ConverterBrowser implements ArgumentConverter {
    @Override
    public SelenideDriver convert(Object source, ParameterContext context) throws ArgumentConversionException {
        if (!(source instanceof Browser))
            throw new IllegalArgumentException("The argument should be a Browser: " + source);

        return switch ((Browser) source) {
            case CHROME -> new SelenideDriver(chromeConfig);
            case FIREFOX -> new SelenideDriver(firefoxConfig);
        };
    }
}
