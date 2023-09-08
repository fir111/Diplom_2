package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserOrdersTest {
    private String accessToken;

    @DisplayName("Настройка теста")
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api/";

        String email = UserData.getEmail();
        Auth user = new Auth(email, UserData.PASSWORD, UserData.NAME);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("auth/register");
        AuthRegisterResponse authRegisterResponse = response.as(AuthRegisterResponse.class);
        accessToken = authRegisterResponse.getAccessToken();
    }

    @DisplayName("Завершение теста")
    @Description("Удаляет зарегистрированного пользователя")
    @After
    public void tearDown(){
        if (accessToken != null){
            given()
                    .header("Authorization", accessToken)
                    .when()
                    .delete("auth/user").then()
                    .assertThat().body("success", is(true)).and().statusCode(202);
        }
    }

    @DisplayName("Получения списка заказов пользователя с авторизацией")
    @Description("Позитивный тест - получения списка заказов пользователя нужна авторизация " +
            "дает ответ согласно документации")
    @Test
    public void getOrdersWithAuthorizationGivesOrderList(){
        Response response = given()
                .get("ingredients");
        Ingredients ingredients = response.as(Ingredients.class);
        Ingredient ingredient = ingredients.getData().get(0);
        String ingredientHash = ingredient.get_id();

        IngredientList ingredientList = new IngredientList(List.of(ingredientHash));

        Response orderCreateResponse = given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", accessToken)
                .and()
                .body(ingredientList)
                .when()
                .post("orders");
        OrderResponseAuth orderResponseAuthBody = orderCreateResponse.as(OrderResponseAuth.class);
        int orderNumber = orderResponseAuthBody.getOrder().getNumber();

        UserOrderResponse userOrders = given()
                .header("Authorization", accessToken)
                .when()
                .get("orders").as(UserOrderResponse.class);
        assertThat(userOrders.isSuccess(), is(true));
        Assert.assertTrue(userOrders.getTotal() > 0);
        Assert.assertTrue(userOrders.getTotalToday() > 0);

        List<UserOrder> orders = userOrders.getOrders();
        Assert.assertTrue(orders.size() >= 1);
        List<Integer> orderNumbers = orders.stream()
                .map(UserOrder::getNumber)
                .collect(Collectors.toList());
        assertThat(orderNumbers, is(hasItem(orderNumber)));
    }

    @DisplayName("Получения списка заказов пользователя без авторизации")
    @Description("Негативный тест - для получения списка заказов пользователя нужна авторизация")
    @Test
    public void getOrdersWithoutAuthorizationGives401(){
        given()
                .get("orders")
                .then()
                .assertThat().body("success", is(false))
                .and().body("message", is("You should be authorised"))
                .and().statusCode(401);
    }
}
