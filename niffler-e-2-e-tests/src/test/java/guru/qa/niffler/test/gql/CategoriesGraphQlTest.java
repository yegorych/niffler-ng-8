package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.FriendCategoriesQuery;
import guru.qa.FriendsOfFriendsOfFriendsQuery;
import guru.qa.FriendsOfFriendsQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoriesGraphQlTest extends BaseGraphQlTest {
    @User(friends = 1, categories = @Category)
    @Test
    @ApiLogin
    void friendCategoriesShouldNotBeReturnedFromGateway(@Token String bearerToken) {
        final ApolloCall<FriendCategoriesQuery.Data> currenciesCall = apolloClient.query(FriendCategoriesQuery.builder()
                        .page(0)
                        .size(10)
                        .build())
                .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<FriendCategoriesQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
        Assertions.assertTrue(response.hasErrors());
        Assertions.assertEquals(
                "Can`t query categories for another user",
                response.errors.getFirst().getMessage()
        );

    }

    @User(friends = 1)
    @Test
    @ApiLogin
    void friendsOfFriendsShouldNotBeReturnedFromGateway (@Token String bearerToken) {
        final ApolloCall<FriendsOfFriendsQuery.Data> currenciesCall = apolloClient.query(FriendsOfFriendsQuery.builder()
                        .page(0)
                        .size(10)
                        .build())
                .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<FriendsOfFriendsQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
        Assertions.assertTrue(response.hasErrors());
        Assertions.assertEquals(
                "Can`t fetch over 2 friends sub-queries",
                response.errors.getFirst().getMessage()
        );
    }
}
