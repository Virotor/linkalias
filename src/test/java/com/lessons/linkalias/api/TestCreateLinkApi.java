package com.lessons.linkalias.api;


import com.lessons.linkalias.dto.LinkRequest;
import com.lessons.linkalias.dto.PrettyLinkRequest;
import com.lessons.linkalias.util.ShortLinkCreater;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConfiguration("fixedport")
public class TestCreateLinkApi {

    @TestConfiguration
    public static class TestEmployeeServiceConfig {
        @Bean
        @Primary
        public ShortLinkCreater employeeService() {
            return new ShortLinkCreater() {
                @Override
                public String createShortLink(String baseLink) {
                    return  baseLink.substring(18);
                }

                @Override
                public String getBase() {
                    return "http://localhost:8081/redirect/";
                }
            };
        }
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${server.port}")
    private int port;


    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgreSQLContainer =  new PostgreSQLContainer<>("postgres")
            .withPassword("inmemory")
            .withUsername("inmemory");

    @BeforeAll
    static public void setContainer(){
        postgreSQLContainer.start();
    }

    @AfterAll
    public static void  closeContainer(){
        postgreSQLContainer.close();
    }
    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("server.port", ()->8081);
        registry.add("short.address", ()->"http://localhost:8081");
        registry.add("short.link", ()->"/redirect/");
    }

    @Test
    void createNewShortLink() {
        var res = this.restTemplate.postForEntity("http://localhost:" + port + "/link/create",
                new LinkRequest("https://spring.io/guides/gs/rest-service", 0L),
                String.class);
        assertThat(Objects.requireNonNull(res.getBody())).startsWithIgnoringCase("http://localhost:8081/redirect/guides/gs/rest-service");
    }

    @Test
    void createNewShortLinkWithTTL() throws InterruptedException {
        var res = this.restTemplate.postForEntity("http://localhost:" + port + "/link/create",
                new LinkRequest("https://spring.io/guides/gs/rest-service", 100L),
                String.class);
        String url = Objects.requireNonNull(res.getBody());
        Thread.sleep(200);
        res = this.restTemplate.getForEntity(url,String.class);
        assertThat(Objects.requireNonNull(res.getStatusCode())).isEqualTo(HttpStatus.REQUEST_TIMEOUT);
    }

    @Test
    void createNewPrettyLink() {
        var res = this.restTemplate.postForEntity("http://localhost:" + port + "/link/create/pretty",
                new PrettyLinkRequest("https://spring.io/guides/gs", 0L, "pretty"),
                String.class);
        assertThat(Objects.requireNonNull(res.getBody())).startsWithIgnoringCase("http://localhost:8081/redirect/pretty");
    }

    @Test
    void createNewPrettyLinkResolve() {
        var res = this.restTemplate.postForEntity("http://localhost:" + port + "/link/create/pretty",
                new PrettyLinkRequest("https://spring.io/guides/gs/rest", 0L, "newPretty"),
                String.class);
        assertThat(Objects.requireNonNull(res.getBody())).startsWithIgnoringCase("http://localhost:8081/redirect/newPretty");

        var error = this.restTemplate.postForEntity("http://localhost:" + port + "/link/create/pretty",
                new PrettyLinkRequest("https://spring.io/guides/gs/", 0L, "newPretty"),
                String.class);
        assertThat(Objects.requireNonNull(error.getStatusCode())).isEqualTo(HttpStatus.CONFLICT);
    }

}
