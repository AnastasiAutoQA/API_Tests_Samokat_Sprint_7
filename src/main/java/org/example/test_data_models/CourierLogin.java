package org.example.test_data_models;

/*JSON for Courier login {
        "login": "ninja",
        "password": "1234"
    }*/

// Создаем класс КурьерЛогин с такими же полями, как в джэйсоне для логина, конструктор, геттеры и сеттеры
public class CourierLogin {
    private String login;
    private String password;

    public CourierLogin(String login, String password) {
        this.login = login;
        this.password = password;
    }
    // Метод fromCourier() для создания объекта класса CourierLogin на основе данных из объекта Courier,
    // в объекте Courier, приходящий в метод, есть login, password, firstname при создании Курьера
    // также используются методы getLogin() и getPassword() класса Courier
    public static CourierLogin fromCourier(Courier courier) {
        return new CourierLogin(courier.getLogin(), courier.getPassword());
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
      this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "CourierLogin{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}