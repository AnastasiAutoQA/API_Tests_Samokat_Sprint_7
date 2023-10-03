package org.example.apiConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

// Доки https://qa-scooter.praktikum-services.ru/docs/
// Создание настроек HTTP-запросов для тестирования с использованием методов и классов библиотеки REST Assured
public class MainUrlConfig {
    protected static final String URI = "https://qa-scooter.praktikum-services.ru/";
    protected RequestSpecification getHeader() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(URI)
                .build();
    }
}
