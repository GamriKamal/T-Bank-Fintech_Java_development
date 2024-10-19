package tbank.mr_irmag.tbank_kudago_task.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Category;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Location;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class KudaGoApiIntegrationTest {
    @Container
    public static GenericContainer<?> wireMockContainer =
            new GenericContainer<>("wiremock/wiremock:latest")
                    .withExposedPorts(8080)
                    .waitingFor(Wait.forListeningPort());

    private RestTemplate restTemplate;
    private ReadKudaGo<Category> readKudaGoCategory;
    private ReadKudaGo<Location> readKudaGoLocation;

    @DynamicPropertySource
    static void wireMockProperties(DynamicPropertyRegistry registry) {
        registry.add("kudago.api.url", () -> "http://" + wireMockContainer.getHost() + ":" + wireMockContainer.getMappedPort(8080));
    }

    @BeforeAll
    public static void setup() {
        String url = "http://" + wireMockContainer.getHost() + ":" + wireMockContainer.getMappedPort(8080);
        configureFor(wireMockContainer.getHost(), wireMockContainer.getMappedPort(8080));

        stubFor(get(urlEqualTo("/public-api/v1.4/place-categories"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\": 89, \"slug\": \"amusement\", \"name\": \"Развлечения\"}, {\"id\": 114, \"slug\": \"animal-shelters\", \"name\": \"Питомники\"}]")));

        stubFor(get(urlEqualTo("/public-api/v1.4/locations"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"slug\":\"spb\",\"name\":\"Санкт-Петербург\"}, {\"slug\":\"msk\",\"name\":\"Москва\"}]")));
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    public void init() {
        readKudaGoCategory = new ReadKudaGo<>();
        readKudaGoCategory.setRestTemplate(restTemplate);

        readKudaGoLocation = new ReadKudaGo<>();
        readKudaGoLocation.setRestTemplate(restTemplate);
    }

    @Test
    void convertJsonToList_SuccessGetCategories_ShouldReturnList() {
        // Arrange
        String url = "http://" + wireMockContainer.getHost() + ":" + wireMockContainer.getMappedPort(8080) + "/public-api/v1.4/place-categories";

        // Act
        List<Category> categories = readKudaGoCategory.convertJsonToList(url, Category.class);

        // Assert
        assertNotNull(categories);
        assertEquals(2, categories.size());
        assertEquals("Развлечения", categories.get(0).getName());
        assertEquals("Питомники", categories.get(1).getName());
    }

    @Test
    void convertJsonToList_SuccessGetLocations_ShouldReturnList() {
        // Arrange
        String url = "http://" + wireMockContainer.getHost() + ":" + wireMockContainer.getMappedPort(8080) + "/public-api/v1.4/locations";

        // Act
        List<Location> locations = readKudaGoLocation.convertJsonToList(url, Location.class);

        // Assert
        assertNotNull(locations);
        assertEquals(2, locations.size());
        assertEquals("Санкт-Петербург", locations.get(0).getName());
        assertEquals("Москва", locations.get(1).getName());
        assertEquals("spb", locations.get(0).getSlug());
        assertEquals("msk", locations.get(1).getSlug());
    }
}
