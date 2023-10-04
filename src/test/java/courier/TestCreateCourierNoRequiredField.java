package courier;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.api_config.CourierApiConfig;
import org.example.test_data_models.Courier;
import org.junit.Before;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;
import static java.net.HttpURLConnection.*;

@RunWith(Parameterized.class)
public class TestCreateCourierNoRequiredField {
    private final String login;
    private final String password;
    private boolean isCourierCreated;
    private CourierApiConfig courierCreate;
    private Courier courier;
    private int statusCode;

    public TestCreateCourierNoRequiredField(String login, String password, boolean isCourierCreated) {
        this.login = login;
        this.password = password;
        this.isCourierCreated = isCourierCreated;
    }

    // Задаем набор тестовых данных {login, password, isCourierCreated} с провальными значениями
    @Parameterized.Parameters(name = "Логин и пароль. Тестовые данные: {0} {1}. Создан ли курьер: {2}")
    public static Object[][] getCourierData() {
        return new Object[][]{
                {RandomStringUtils.randomAlphanumeric(3), "", false},
                {RandomStringUtils.randomAlphanumeric(3), null, false},
                {"", RandomStringUtils.randomAlphanumeric(3), false},
                {null, RandomStringUtils.randomAlphanumeric(3), false},
                {"", "", false},
                {"", null, false},
                {null, "", false},
                {null, null, false}
        };
    }

    @Before
    public void setUp() {
        courierCreate = new CourierApiConfig();
    }

    @DisplayName("Ошибка в ответе при отсутствии одного или обоих обязательных полей - логина, пароля")
    @Description("Проверка, что запрос возвращает ошибку, если одного из/обоих полей нет")
    @Test
    public void shouldNotCreateCourierWithoutRequiredFields() {
        courier = new Courier();
        courier.setLoginAndPassword(this.login, this.password); // Задали курьеру логин и пароль из параметров
        ValidatableResponse createResponse = courierCreate.createNewCourier(courier);
        statusCode = createResponse.extract().statusCode(); // Вытащили статус код респонса для сравнения
        String courierMessage = createResponse.extract().path("message"); // Вытащили сообщ респонса для сравнения ниже

        assertEquals("The status code is invalid", HTTP_BAD_REQUEST, statusCode);
        assertFalse("The courier is created", isCourierCreated);
        assertEquals("The courier is created", "Недостаточно данных для создания учетной записи", courierMessage);
    }

}