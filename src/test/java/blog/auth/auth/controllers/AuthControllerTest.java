package blog.auth.auth.controllers;

import blog.auth.auth.user.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @LocalServerPort
    private int port;

    private WebClient webClient;

    @BeforeEach
    public void setup() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:" + port)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Test
    void requestLogin() {
        UserDto dto = new UserDto();
        dto.setUsername("User123");
        dto.setPassword("123");
        String response = webClient.post()
                .uri("/login")
                .body(Mono.just(dto), UserDto.class)
                .retrieve()
                .bodyToMono(String.class)
                        .block();
        assertThat(response).isNotNull();
        assertThat(response).startsWith("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"); 
        assertThat(response.split("\\.")).hasSize(3);

    }

}
