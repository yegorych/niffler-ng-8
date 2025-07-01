package guru.qa.niffler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerTest {

  final UserJson updatedUser = new UserJson(
          UUID.fromString("a9165b45-a4aa-47d6-ac50-43611d624421"),
          "dima",
          null,
          null,
          null,
          CurrencyValues.USD,
          null,
          null,
          null
  );


  @Autowired
  private MockMvc mockMvc;

  @Sql(scripts = "/currentUserShouldBeReturned.sql")
  @Test
  void currentUserShouldBeReturned() throws Exception {
    mockMvc.perform(get("/internal/users/current")
            .contentType(MediaType.APPLICATION_JSON)
            .param("username", "dima")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("dima"))
        .andExpect(jsonPath("$.fullname").value("Dmitrii Tuchs"))
        .andExpect(jsonPath("$.currency").value("RUB"))
        .andExpect(jsonPath("$.photo").isNotEmpty())
        .andExpect(jsonPath("$.photoSmall").isNotEmpty());
  }

  @Sql(scripts = "/allUserShouldBeReturned.sql")
  @Test
  void allUsersShouldBeReturned() throws Exception {
    mockMvc.perform(get("/internal/users/all")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("username", "dima")
                    .param("searchQuery", "duck1")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].username").value("duck1"))
            .andExpect(jsonPath("$[0].fullname").value("Duck1 d"))
            .andExpect(jsonPath("$[0].currency").value("RUB"))
            .andExpect(jsonPath("$[0].photoSmall").isNotEmpty())
            .andExpect(jsonPath("$.length()").value(1));
  }

  @Sql(scripts = "/currentUserShouldBeReturned.sql")
  @Test
  void userShouldBeUpdated() throws Exception {
    mockMvc.perform(post("/internal/users/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(updatedUser))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.currency").value(String.valueOf(updatedUser.currency())));

  }


  @Test
  void newUserShouldBeCreated() throws Exception {
    mockMvc.perform(post("/internal/users/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(updatedUser))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("dima"))
            .andExpect(jsonPath("$.currency").value(String.valueOf(updatedUser.currency())));

  }

}