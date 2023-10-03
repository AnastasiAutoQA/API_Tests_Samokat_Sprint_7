package org.example.testDataModels;
import org.apache.commons.lang3.RandomStringUtils;

// Методы для автогенерации данных Курьера
public class CourierAutoGenerator {
    public static Courier getRandomData() {
        String login = RandomStringUtils.randomAlphanumeric(10);
        String password = RandomStringUtils.randomAlphanumeric(10);
        String firstName = RandomStringUtils.randomAlphanumeric(10);
        return new Courier(login, password, firstName);
    }

    public static Courier getRandomLoginAndPassword() {
        String login = RandomStringUtils.randomAlphanumeric(10);
        String password = RandomStringUtils.randomAlphanumeric(10);
        return new Courier(login, password);
    }

    public static Courier getRandomPassword() {
        String password = RandomStringUtils.randomAlphanumeric(5);
        Courier courier = new Courier();
        courier.setPassword(password);
        return courier;
    }

    public static Courier getRandomLogin() {
        String login = RandomStringUtils.randomAlphanumeric(5);
        Courier courier = new Courier();
        courier.setLogin(login);
        return courier;
    }
}

