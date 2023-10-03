package login;
import org.example.apiConfig.CourierApiConfig;
import org.example.testDataModels.Courier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.testDataModels.CourierAutoGenerator;
import org.example.testDataModels.CourierLogin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestValidCourierLogin {
    private CourierApiConfig courierLogin;
    private Courier courier;
    private int courierId;
    private int statusCode;

    @Before
    public void setUp() {
        courierLogin = new CourierApiConfig();
    }

    @DisplayName("Авторизация курьера с валидными данными")
    @Description("Проверить, что курьер может авторизоваться;\n" +
            "    - для авторизации нужно передать все обязательные поля - login, password;\n" +
            "    - успешный запрос возвращает id курьера")
    @Test
    public void shouldLoginWithCreatedCourier() {
        courier = CourierAutoGenerator.getRandomData(); // Получили рандомные значение логина, пароля и имени курьера
        ValidatableResponse createResponse = courierLogin.createNewCourier(courier); // Создали курьера с этими данными и получили ответ
        statusCode = createResponse.extract().statusCode(); // Получили статус код ответа - должен быть 201
        assertEquals("The status code is invalid", HTTP_CREATED, statusCode); // Сравнили коды, задали сообщ, если они не равны

        ValidatableResponse loginResponse = courierLogin.loginCourierAndCheckResponse(CourierLogin.fromCourier(courier));
        statusCode = loginResponse.extract().statusCode(); // Получаем статус код респонса при авторизации курьера (должно быть 200)
        courierId = loginResponse.extract().path("id"); // Достаем id курьера

        assertEquals("The status code is invalid", HTTP_OK, statusCode); // Сравниеваем статус коды
        assertTrue("The courier ID is not provided", courierId != 0); // Проверяем, что пришел id курьера не нулевой
    }

    // Удаление Курьера по его id
    @After
    public void clearDown() {
        courierLogin.deleteCourier(courierId);
    }
}

