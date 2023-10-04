package org.example.api_config;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.test_data_models.OrderRequest;

import static io.restassured.RestAssured.given;

// Refer to https://qa-scooter.praktikum-services.ru/docs/
public class OrderApiConfig extends MainUrlConfig {
    private static final String ORDER_LIST_URI = URI + "api/v1/orders/";
    private static final String ORDER_TRACK_URI = URI + "api/v1/orders/track";
    private static final String ORDER_CANCEL_URI = URI + "api/v1/orders/cancel";

    @Step("Получение списка заказов")
    public ValidatableResponse getOrderList() {
        return given().log().all()
                .spec(getHeader())
                .when()
                .get(ORDER_LIST_URI)
                .then().log().all();
    }

    @Step("Создание нового заказа - orderRequest")
    public ValidatableResponse createNewOrder(OrderRequest orderRequest) {
        return given().log().all()
                .spec(getHeader())
                .body(orderRequest)
                .when()
                .post(ORDER_LIST_URI)
                .then().log().all();
    }
    @Step("Получение данных заказа по его трэк номеру")
    public ValidatableResponse getOrderByTrackNumber(int orderTrack){
        return given().log().all()
                .spec(getHeader())
                .when()
                .get(ORDER_TRACK_URI + "?t=" + orderTrack)
                .then().log().all();
    }

    @Step("Отмена созданного заказ по его трэк номеру orderTrack (в json поле track)")
    public ValidatableResponse cancelOrder(int orderTrack) {
        return given().log().all()
                .spec(getHeader())
                .when()
                .put(ORDER_CANCEL_URI + "?track=" + String.valueOf(orderTrack))
                .then().log().all();
    }

}

