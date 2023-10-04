package order;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.api_config.OrderApiConfig;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.test_data_models.Order;
import org.example.test_data_models.OrderAutoGenerator;
import org.example.test_data_models.OrderRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static java.lang.Integer.parseInt;
import static java.net.HttpURLConnection.*;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class TestGetOrderWrongTrackNumber {
    private final String[] color;
    private final boolean orderIsCreated;
    private OrderApiConfig orderCreate;
    private OrderRequest orderRequest;
    private int orderTrack;
    private int statusCode;
    private Order order;
    private String message;

    public TestGetOrderWrongTrackNumber(String[] color, boolean orderIsCreated) {
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

    @DisplayName("Проверка ошибки получения заказа, если ввести не его трэк номер")
    @Description("Проверить, что система возвращает ошибку, если трэк номер не найден")
    @Test
    public void shouldReturnErrorIfNotExistingTrackNumber() {
        orderRequest = OrderAutoGenerator.getRandomDataWithBlankColor(); // Создали заказ
        orderRequest.setColor(this.color); // Выбрали цвет из параметров (2 цвета, черный, серый, никакой)
        ValidatableResponse createResponse = orderCreate.createNewOrder(orderRequest);
        statusCode = createResponse.extract().statusCode();
        int initialOrderTrack = createResponse.extract().path("track");
        assertEquals("The status code is invalid", HTTP_CREATED, statusCode);
        assertTrue("The courier is not created", initialOrderTrack != 0);

        // Поменяли трэк нормер
        orderTrack = parseInt(RandomStringUtils.randomNumeric(8));

        // Пытаемся получить заказ по измененному трэк номеру
        ValidatableResponse createResponseByTrackNumber = orderCreate.getOrderByTrackNumber(orderTrack);
        statusCode = createResponseByTrackNumber.extract().statusCode();
        order = createResponseByTrackNumber.extract().body().as(Order.class);
        message = createResponseByTrackNumber.extract().path("message"); // Получили сообщ об ошибке
        assertEquals("The status code is invalid", HTTP_NOT_FOUND, statusCode);
        assertEquals("The login is still enabled", "Заказ не найден", message);

        orderTrack = initialOrderTrack; // Вернули трэк номер в исходное состояние
    }

    // Удаляем (отменяем) заказ
    @After
    public void clearDown() {
        orderCreate.cancelOrder(orderTrack);
    }

}



