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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCreateCourierRequiredFields {
    private CourierApiConfig courierCreate;
    private Courier courier;
    private int courierId;
    private int statusCode;
    private boolean isCourierCreated;

    @Before
    public void setUp() {
        courierCreate = new CourierApiConfig();
    }

    @DisplayName("Создание нового курьера с логином и паролем")
    @Description("Проверка, что для создания Курьера нужно передать на эндпоинт все обязательные поля")
    @Test
    public void shouldCreateNewCourierOnlyWithLoginAndPassword() {
        courier = CourierAutoGenerator.getRandomLoginAndPassword(); // Создание рандомных логина и пароля для курьера
        ValidatableResponse createResponse = courierCreate.createNewCourier(courier); // Создаем курьера с логином и паролем и получаем респонс
        statusCode = createResponse.extract().statusCode(); // Достаем статус код при создании курьера
        isCourierCreated = createResponse.extract().path("ok"); // Достаем ответ при создании курьера

        assertEquals("The status code is invalid", HTTP_CREATED, statusCode); //Статус код должен быть 201
        assertTrue("The courier is not created", isCourierCreated); // В респонсе должен быть "ok"

        // Авторизуемся под созданным курьером с логином и паролем
        // и получаем id курьера в респонсе на логин, чтоб его удалить в After
        ValidatableResponse loginResponse = courierCreate.loginCourierAndCheckResponse(CourierLogin.fromCourier(courier));
        courierId = loginResponse.extract().path("id");
    }

    // Удаляем курьера по его id
    @After
    public void clearDown() {
        courierCreate.deleteCourier(courierId);
    }
}
