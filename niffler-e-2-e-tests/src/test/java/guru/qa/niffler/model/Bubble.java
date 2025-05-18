package guru.qa.niffler.model;

import guru.qa.niffler.condition.Color;
import org.jetbrains.annotations.NotNull;


public record Bubble(Color color, String text) implements Comparable<Bubble> {

    @Override
    public String toString() {
        return "Bubble{" +
                "color=" + color +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NotNull Bubble o) {
        return this.color.compareTo(o.color);
    }
}
