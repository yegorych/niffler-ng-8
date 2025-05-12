package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.model.FriendshipStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Friendship {
    int count() default 0;
    FriendshipStatus status() default FriendshipStatus.EMPTY;
}
