package order;
import org.example.apiConfig.OrderApiConfig;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.testDataModels.Order;
import org.example.testDataModels.OrderAutoGenerator;
import org.example.testDataModels.OrderRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static java.net.HttpURLConnection.*;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class TestGetOrderByTrackNumber {
    private final String[] color;
    private final boolean orderIsCreated;
    private OrderApiConfig orderCreate;
    private OrderRequest orderRequest;
    private int orderTrack;
    private int statusCode;
    private Order order;

    public TestGetOrderByTrackNumber(String[] color, boolean orderIsCreated) {
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

    @DisplayName("Получение заказа по его трэк номеру")
    @Description("Проверить, что возвращается тело ответа по номеру заказа")
    @Test
    public void shouldGetOrderByTrackNumber() {
        orderRequest = OrderAutoGenerator.getRandomDataWithBlankColor(); // Создали заказ
        orderRequest.setColor(this.color); // Выбрали цвет из параметров (2 цвета, черный, серый, никакой)
        ValidatableResponse createResponse = orderCreate.createNewOrder(orderRequest);
        statusCode = createResponse.extract().statusCode();
        orderTrack = createResponse.extract().path("track");
        assertEquals("The status code is invalid", HTTP_CREATED, statusCode);
        assertTrue("The courier is not created", orderTrack != 0);

        ValidatableResponse createResponseByTrackNumber = orderCreate.getOrderByTrackNumber(orderTrack);
        statusCode = createResponseByTrackNumber.extract().statusCode();
        order = createResponseByTrackNumber.extract().body().as(Order.class);
        assertEquals("The status code is invalid", HTTP_OK, statusCode);
        assertNotNull("The list of orders is not provided", order);
    }

    // Удаляем (отменяем) заказ
    @After
    public void clearDown() {
        orderCreate.cancelOrder(orderTrack);
    }

}
