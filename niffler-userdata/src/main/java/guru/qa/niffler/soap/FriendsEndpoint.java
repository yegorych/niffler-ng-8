package guru.qa.niffler.soap;

import guru.qa.niffler.model.UserJsonBulk;
import guru.qa.niffler.service.UserService;
import guru.qa.jaxb.userdata.FriendsPageRequest;
import guru.qa.jaxb.userdata.FriendsRequest;
import guru.qa.jaxb.userdata.RemoveFriendRequest;
import guru.qa.jaxb.userdata.UsersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class FriendsEndpoint extends BaseEndpoint {

  private final UserService userService;

  @Autowired
  public FriendsEndpoint(UserService userService) {
    this.userService = userService;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "friendsRequest")
  @ResponsePayload
  public UsersResponse friendsRq(@RequestPayload FriendsRequest request) {
    UsersResponse response = new UsersResponse();
    List<UserJsonBulk> users = userService.friends(request.getUsername(), request.getSearchQuery());
    return enrichUsersResponse(users, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "friendsPageRequest")
  @ResponsePayload
  public UsersResponse friendsPageRq(@RequestPayload FriendsPageRequest request) {
    UsersResponse response = new UsersResponse();
    Page<UserJsonBulk> users = userService.friends(
        request.getUsername(),
        new SpringPageable(request.getPageInfo()).pageable(),
        request.getSearchQuery()
    );
    return enrichUsersResponse(users, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "removeFriendRequest")
  public void removeFriendRq(@RequestPayload RemoveFriendRequest request) {
    userService.removeFriend(
        request.getUsername(),
        request.getFriendToBeRemoved()
    );
  }
}
