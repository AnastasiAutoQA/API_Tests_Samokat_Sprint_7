package org.example.apiConfig;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.testDataModels.CourierLogin;
import org.example.testDataModels.Courier;

import static io.restassured.RestAssured.given;

// Доки https://qa-scooter.praktikum-services.ru/docs/
public class CourierApiConfig extends MainUrlConfig {
    private static final String NEWCOURIER_URI = URI + "api/v1/courier/";
    private static final String COURIER_LOGIN_URI = URI + "api/v1/courier/login";

    /* Метод createNewCourier() отправляет HTTP POST-запрос для создания нового курьера с данными
     логина, пароля и имени (описанных в классе Courier) на endpoint, указанный в константе NEWCOURIER_URI,
     использует настройки заголовка (метод getHeader() из класса MainUrlConfig) и тела запроса из Courier.
     Возвращаемый объект ValidatableResponse позволяет проверить результаты запроса. */
    @Step("Создание нового курьера courier")
    public ValidatableResponse createNewCourier(Courier courier) {
        return given().log().all()
                .spec(getHeader())
                .body(courier)
                .when()
                .post(NEWCOURIER_URI)
                .then().log().all();
    }
    @Step("Авторизация курьера courierLogin")
    public ValidatableResponse loginCourierAndCheckResponse(CourierLogin courierLogin) {
        return given().log().all()
                .spec(getHeader())
                .body(courierLogin)
                .when()
                .post(COURIER_LOGIN_URI)
                .then().log().all();
    }

    @Step("Удаление созданного курьера по его courierId (в json поле id)")
    public ValidatableResponse deleteCourier(int courierId) {
        return given().log().all()
                .spec(getHeader())
                .when()
                .delete(NEWCOURIER_URI + courierId)
                .then().log().all();
    }
}