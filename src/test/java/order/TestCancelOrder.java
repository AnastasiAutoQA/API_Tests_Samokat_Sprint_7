package order;
import org.example.api_config.OrderApiConfig;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.test_data_models.Order;
import org.example.test_data_models.OrderAutoGenerator;
import org.example.test_data_models.OrderRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static java.net.HttpURLConnection.*;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class TestCancelOrder {
    private final String[] color;
    private final boolean orderIsCreated;
    private OrderApiConfig orderCreate;
    private OrderRequest orderRequest;
    private int orderTrack;
    private int statusCode;
    private Order order;

    public TestCancelOrder(String[] color, boolean orderIsCreated) {
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

    @DisplayName("Отмена заказа по его трэк номеру")
    @Description("Проверить, что заказ оменяется по его трэк номеру")
    @Test
    public void shouldCancelOrder(){
        orderRequest = OrderAutoGenerator.getRandomDataWithBlankColor(); // Создали заказ
        orderRequest.setColor(this.color); // Выбрали цвет из параметров (2 цвета, черный, серый, никакой)
        ValidatableResponse createResponse = orderCreate.createNewOrder(orderRequest);
        statusCode = createResponse.extract().statusCode();
        orderTrack = createResponse.extract().path("track");
        assertEquals("The status code is invalid", HTTP_CREATED, statusCode);
        assertTrue("The courier is not created", orderTrack != 0);

        ValidatableResponse createResponseCancelOrder = orderCreate.cancelOrder(orderTrack);
        statusCode = createResponseCancelOrder.extract().statusCode();
        order = createResponseCancelOrder.extract().body().as(Order.class);
        assertEquals("The status code is invalid", HTTP_OK, statusCode);
        assertNotNull("The list of orders is not provided", order);
    }

}
