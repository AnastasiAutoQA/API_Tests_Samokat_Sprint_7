package order;
import org.example.api_config.OrderApiConfig;
import org.example.test_data_models.OrderAutoGenerator;
import org.example.test_data_models.OrderRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class TestCreateOrder {
    private final String[] color;
    private final boolean orderIsCreated;
    private OrderApiConfig orderCreate;
    private OrderRequest orderRequest;
    private int orderTrack;
    private int statusCode;

    public TestCreateOrder(String[] color, boolean orderIsCreated) {
        this.color = color;
        this.orderIsCreated = orderIsCreated;
    }

    @Parameterized.Parameters(name = "Доступные цвета. Тестовые данные: {0} {1}")
    public static Object[][] getColorData() {
        return new Object[][]{
                {new String[]{"BLACK", "GREY"}, true},
                {new String[]{"BLACK"}, true},
                {new String[]{"GREY"}, true},
                {new String[]{""}, true},
        };
    }

    @Before
    public void setUp() {
        orderCreate = new OrderApiConfig();
    }

    @DisplayName("Создание заказа и проверка ответа с указанием разных цветов")
    @Description("Проверить, что можно указать один из цветов — BLACK или GREY;\n" +
            "    - можно указать оба цвета;\n" +
            "    - можно совсем не указывать цвет;\n" +
            "    - тело ответа содержит track")
    @Test
    public void shouldCreateNewOrder() {
        orderRequest = OrderAutoGenerator.getRandomDataWithBlankColor(); // Создали заказ
        orderRequest.setColor(this.color); // Выбрали цвет из параметров (2 цвета, черный, серый, никакой)
        ValidatableResponse createResponse = orderCreate.createNewOrder(orderRequest);
        statusCode = createResponse.extract().statusCode();
        orderTrack = createResponse.extract().path("track");

        assertEquals("The status code is invalid", HTTP_CREATED, statusCode);
        assertTrue("The order is not created", orderTrack != 0);
    }

    // Удаляем (отменяем) заказ
    @After
    public void clearDown() {
        orderCreate.cancelOrder(orderTrack);
    }
}