package login;
import org.example.apiConfig.CourierApiConfig;
import org.example.testDataModels.Courier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.testDataModels.CourierAutoGenerator;
import org.example.testDataModels.CourierLogin;
import org.junit.Before;
import org.junit.Test;
import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;

public class TestNotExistingCourierLogin {
    private CourierApiConfig courierLogin;
    private Courier courier;
    private int courierId;
    private int statusCode;

    @Before
    public void setUp() {
        courierLogin = new CourierApiConfig();
    }

    @DisplayName("Авторизация несуществующего или удаленного курьера не пройдет")
    @Description("Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    @Test
    public void shouldNotLoginUnderDeletedCourier() {
        courier = CourierAutoGenerator.getRandomData(); // Получили рандомные значение логина, пароля и имени курьера
        ValidatableResponse createResponse = courierLogin.createNewCourier(courier); // Создали нового курьера с этими данными и получили ответ
        statusCode = createResponse.extract().statusCode(); // Получили статус код ответа при Создании курьера - должен быть 201
        assertEquals("The status code is invalid", HTTP_CREATED, statusCode); // Сравнили коды, задали сообщ, если они не равны

        ValidatableResponse loginResponse = courierLogin.loginCourierAndCheckResponse(CourierLogin.fromCourier(courier));
        statusCode = loginResponse.extract().statusCode(); // Получили статус код ответа при Логине курьера
        courierId = loginResponse.extract().path("id"); // Вытащили id курьера

        // Удаляем созданного курьера по его id методом deleteCourier() из класса CourierApiConfig,
        // получаем и сравниваем статус коды ответа при Удалении курьера (должен быть 200 при успешном)
        ValidatableResponse deleteResponse = courierLogin.deleteCourier(courierId);
        statusCode = deleteResponse.extract().statusCode();
        assertEquals("The status code is invalid", HTTP_OK, statusCode);

        // Пытаемся залогиниться под удаленным (несуществующим ) курьером
        // получаем и сравниваем статус коды ответа при попытки авторизации с несуществующими данными (должно быть 404)
        loginResponse = courierLogin.loginCourierAndCheckResponse(CourierLogin.fromCourier(courier));
        statusCode = loginResponse.extract().statusCode(); // Получаем код респонса
        String courierMessage = loginResponse.extract().path("message"); // Достаем сообщ об ошибке

        assertEquals("The status code is invalid", HTTP_NOT_FOUND, statusCode);
        assertEquals("The login is still enabled", "Учетная запись не найдена", courierMessage);
    }
}