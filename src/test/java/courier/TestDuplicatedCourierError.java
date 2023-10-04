package courier;
import org.example.api_config.CourierApiConfig;
import org.example.test_data_models.Courier;
import org.example.test_data_models.CourierAutoGenerator;
import org.example.test_data_models.CourierLogin;
import org.junit.After;
import org.junit.Before;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import static java.net.HttpURLConnection.*;
import static org.junit.Assert.*;

public class TestDuplicatedCourierError {
    private CourierApiConfig courierCreate;
    private Courier courier;
    private int statusCode;
    private int courierId;

    @Before
    public void setUp() {
        courierCreate = new CourierApiConfig();
    }

    @DisplayName("Тело ответа содержит ошибку, если создается дубликат курьера")
    @Description("Нельзя создать двух одинаковых курьеров")
    @Test
    public void shouldNotCreateDuplicatedCourier() {
        courier = CourierAutoGenerator.getRandomData();
        ValidatableResponse createResponse = courierCreate.createNewCourier(courier);
        statusCode = createResponse.extract().statusCode();
        assertEquals("The status code is invalid", HTTP_CREATED, statusCode); // Курьер успешно создан

        // Создаем курьера-дубликата с такими же данными
        ValidatableResponse createDuplicatedCourierResponse = courierCreate.createNewCourier(courier);
        statusCode = createDuplicatedCourierResponse.extract().statusCode();
        String isCourierCreated = createDuplicatedCourierResponse.extract().path("message");

        assertEquals("The status code is invalid", HTTP_CONFLICT, statusCode);
        assertEquals("The courier is already created", "Этот логин уже используется. Попробуйте другой.", isCourierCreated);

        ValidatableResponse loginResponse = courierCreate.loginCourierAndCheckResponse(CourierLogin.fromCourier(courier));
        courierId = loginResponse.extract().path("id"); // Взяли id курьера - id возвращается при логине под курьером
        assertTrue("The courier ID is not provided", courierId != 0);

    }

    @DisplayName("Тело ответа содержит ошибку, если используется тот же логин")
    @Description("Если создать пользователя с логином, который уже есть, возвращается ошибка")
    @Test
    public void shouldNotCreateCourierWithExistingLogin() {
        Courier courier1 = CourierAutoGenerator.getRandomData(); //Создаем курьера-1 с логином, паролем, именем
        String courier1Login = courier1.getLogin();
        Courier courier2 = CourierAutoGenerator.getRandomData(); // Создаем курьера-2 и присваиваем ему логин от курьера-1
        courier2.setLogin(courier1Login);

        // Получаем ответ и статус код при создании курьера-1 - успешно 200
        ValidatableResponse createResponse = courierCreate.createNewCourier(courier1);
        statusCode = createResponse.extract().statusCode();
        assertEquals("The status code is invalid", HTTP_CREATED, statusCode);

        // Получаем ответ и статус код при создании курьера-2 - ошибка конфликт 409, так как одинаковый логин
        ValidatableResponse createCourierWithExistingLoginResponse = courierCreate.createNewCourier(courier2);
        statusCode = createCourierWithExistingLoginResponse.extract().statusCode();
        String isCourierCreated = createCourierWithExistingLoginResponse.extract().path("message");

        assertEquals("The status code is invalid", HTTP_CONFLICT, statusCode);
        assertEquals("The courier is already created", "Этот логин уже используется. Попробуйте другой.", isCourierCreated);

        ValidatableResponse loginResponse = courierCreate.loginCourierAndCheckResponse(CourierLogin.fromCourier(courier1));
        courierId = loginResponse.extract().path("id"); // Взяли id курьера - id возвращается при логине под курьером
    }
    // Удалили курьера по его id
    @After
    public void clearDown() {
        courierCreate.deleteCourier(courierId);
    }
}

