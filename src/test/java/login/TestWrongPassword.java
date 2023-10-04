package login;
import org.example.api_config.CourierApiConfig;
import org.example.test_data_models.Courier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.test_data_models.CourierAutoGenerator;
import org.example.test_data_models.CourierLogin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;

public class TestWrongPassword {
    private CourierApiConfig courierConfig;
    private Courier courier;
    private int courierId;
    private String courierMessage;
    private int statusCode;

    @Before
    public void setUp() {
        courierConfig = new CourierApiConfig();
    }

    @DisplayName("Авторизация не пройдет при неправильном Пароле")
    @Description("Система должна возвращать ошибку при неправильном пароле")
    @Test
    public void shouldNotAuthorizeIncorrectPassword() {
        courier = CourierAutoGenerator.getRandomData(); // Получили рандомные значение для логина, пароля и имени курьера
        ValidatableResponse createResponse = courierConfig.createNewCourier(courier); // Создали нового курьера с этими данными и получили ответ
        String initialPassword = courier.getPassword(); // Получили пароль курьера
        statusCode = createResponse.extract().statusCode(); // Получили статус код (должен быть 201)
        assertEquals("The status code is invalid", HTTP_CREATED, statusCode); // Сравнили коды, задали сообщ, если они не равны
        // Задали другой рандомный пароль
        courier.setPassword(CourierAutoGenerator.getRandomPassword().getPassword());
        // Залогинились с другим паролем, получили статус код и сообщ
        ValidatableResponse loginResponse = courierConfig.loginCourierAndCheckResponse(CourierLogin.fromCourier(courier));
        statusCode = loginResponse.extract().statusCode();
        courierMessage = loginResponse.extract().path("message"); // Получили сообщ об ошибке

        //Сравнили полученный код и сообщ - должна быть ошибка, тк Пароль не совпадает
        assertEquals("The status code is invalid", HTTP_NOT_FOUND, statusCode);
        assertEquals("The login is still enabled", "Учетная запись не найдена", courierMessage);

        // Вернули Курьеру изначальный пароль
        courier.setPassword(initialPassword);
        loginResponse = courierConfig.loginCourierAndCheckResponse(CourierLogin.fromCourier(courier));
        statusCode = loginResponse.extract().statusCode();
        courierId = loginResponse.extract().path("id");
    }

    // Удаление Курьера по его id
    @After
    public void clearDown() {
        courierConfig.deleteCourier(courierId);
    }
}