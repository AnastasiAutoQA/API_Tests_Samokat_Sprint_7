package org.example.test_data_models;
import org.apache.commons.lang3.RandomStringUtils;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class OrderAutoGenerator {
    public static OrderRequest getRandomDataWithBlankColor() {
        String firstName = RandomStringUtils.randomAlphanumeric(3);
        String lastName = RandomStringUtils.randomAlphanumeric(3);
        String address = RandomStringUtils.randomAlphanumeric(10);
        String metroStation = RandomStringUtils.randomAlphanumeric(10);
        String phone = RandomStringUtils.randomNumeric(11);
        int rentTime = ThreadLocalRandom.current().nextInt(1, 365);
        String deliveryDate = LocalDate.of(4, 11, 28).toString();
        String comment = RandomStringUtils.randomAlphanumeric(3);
        String[] color = {};
        return new OrderRequest(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }

}