package courier;
import org.example.apiConfig.CourierApiConfig;
import org.example.testDataModels.Courier;
import org.example.testDataModels.CourierAutoGenerator;
import org.example.testDataModels.CourierLogin;
import org.junit.After;
import org.junit.Before;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCreateCourierValidData {
    private CourierApiConfig courierCreate;
    private int courierId;
    private int statusCode;
    private boolean isCourierCreated;

    @Before
    public void setUp() {
        courierCreate = new CourierApiConfig();
    }

    @DisplayName("Создание нового курьера с логином, паролем и именем, и проверка тело ответа")
    @Description("Проверить, что курьера можно создать;\n" +
            "    - чтобы создать курьера, нужно передать на эндпоинт все обязательные поля;\n" +
            "    - запрос возвращает правильный код ответа;\n" +
            "    - успешный запрос возвращает ok: true;")
    @Test
    public void shouldCreateNewCourierAndCheckResponse() {
        Courier courier = CourierAutoGenerator.getRandomData(); // Создание курьера с логином, паролем и именем
        ValidatableResponse createResponse = courierCreate.createNewCourier(courier); // и получение респонса
        statusCode = createResponse.extract().statusCode(); // Вытащили статус код ответа при создании курьера
        isCourierCreated = createResponse.extract().path("ok"); // Вытащили сообщение при создании курьера

        assertEquals("The status code is invalid", HTTP_CREATED, statusCode); // Сравнили статус коды и ответ
        assertTrue("The courier is not created", isCourierCreated);

        ValidatableResponse loginResponse = courierCreate.loginCourierAndCheckResponse(CourierLogin.fromCourier(courier));
        courierId = loginResponse.extract().path("id"); // Взяли id курьера - id возвращается при логине под курьером

        assertTrue("The courier ID is not provided", courierId != 0);
    }

    // Удалили курьера по его id
    @After
    public void clearDown() {
        courierCreate.deleteCourier(courierId);
    }
}