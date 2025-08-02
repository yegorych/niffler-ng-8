package guru.qa.niffler.page.component;


import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.Bubble;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.ScreenshotConditions.image;
import static guru.qa.niffler.condition.StatConditions.*;
import static java.util.Objects.requireNonNull;

@ParametersAreNonnullByDefault
public class StatComponent extends BaseComponent<StatComponent> {

    private final ElementsCollection bubbles = $("#legend-container").$$("li");
    private final SelenideElement chart = $("canvas[role='img']");

    public StatComponent() {
        super($("#stat"));
    }

    @Step("Check that statistic bubbles contain texts {0}")
    @Nonnull
    public StatComponent checkStatisticBubblesContains(String... texts) {
        bubbles.should(CollectionCondition.texts(texts));
        return this;
    }
    @Step("Check that statistic image matches the expected image")
    @Nonnull
    public StatComponent checkStatisticImage(BufferedImage expectedImage) throws IOException {
        chart.should(image(expectedImage));
        return this;
    }

    @Step("Get screenshot of stat chart")
    @Nonnull
    public BufferedImage chartScreenshot() throws IOException {
        return ImageIO.read(requireNonNull(chart.screenshot()));
    }


    @Step("Check stat bubbles")
    @Nonnull
    public StatComponent checkBubbles(Bubble... expectedBubbles) {
        bubbles.should(statBubbles(expectedBubbles));
        return this;
    }

    @Step("Check stat bubbles any order")
    @Nonnull
    public StatComponent checkBubblesAnyOrder(Bubble... expectedBubbles) {
        bubbles.should(statBubblesAnyOrder(expectedBubbles));
        return this;
    }

    @Step("Check stat bubbles contains ")
    @Nonnull
    public StatComponent checkBubblesContains(Bubble... expectedBubbles) {
        bubbles.should(statBubblesContains(expectedBubbles));
        return this;
    }

}
