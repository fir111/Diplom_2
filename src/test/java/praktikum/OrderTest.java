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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class OrderTest {
    private String accessToken;
    private Ingredient ingredient;
    private String ingredientHash;
    private String email;

    @DisplayName("Настройка теста")
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api/";

        email = UserData.getEmail();
        Auth user = new Auth(email, UserData.PASSWORD, UserData.NAME);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("auth/register");
        AuthRegisterResponse authRegisterResponse = response.as(AuthRegisterResponse.class);
        accessToken = authRegisterResponse.getAccessToken();

        Response response1 = given()
                .get("ingredients");
        Ingredients ingredients = response1.as(Ingredients.class);
        ingredient = ingredients.getData().get(0);
        ingredientHash = ingredient.get_id();
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

    @DisplayName("Создание заказа без авторизации")
    @Description("Успешное создание заказа без авторизации")
    @Test
    public void createOrderWithoutAuthorizationGivesCorrectResponse(){
        IngredientList ingredientList = new IngredientList(List.of(ingredientHash));

        Response orderCreateResponse = given()
                .header("Content-type", "application/json")
                .and()
                .body(ingredientList)
                .when()
                .post("orders");

        OrderResponseNoAuth orderResponseNoAuthBody = orderCreateResponse.as(OrderResponseNoAuth.class);
        Assert.assertTrue(orderResponseNoAuthBody.isSuccess());
        assertThat(orderResponseNoAuthBody.getName(), notNullValue(String.class));

        OrderNoAuth orderNoAuthResponse = orderResponseNoAuthBody.getOrder();
        Assert.assertTrue(orderNoAuthResponse.getNumber() > 0);
    }

    @DisplayName("Создание заказа c авторизацией")
    @Description("Успешное создание заказа c авторизацией")
    @Test
    public void createOrderWithAuthorizationGivesCorrectResponse(){
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
        Assert.assertTrue(orderResponseAuthBody.isSuccess());
        assertThat(orderResponseAuthBody.getName(), notNullValue(String.class));

        OrderAuth actualOrder = orderResponseAuthBody.getOrder();

        Assert.assertEquals(ingredient, actualOrder.getIngredients().get(0));

        assertThat(actualOrder.get_id(), notNullValue(String.class));
        assertThat(actualOrder.getOwner().getEmail(), is(email));
        assertThat(actualOrder.getOwner().getName(), is(UserData.NAME));
        assertThat(actualOrder.getStatus(), is("done"));
        assertThat(actualOrder.getName(), notNullValue(String.class));
        assertThat(actualOrder.getCreatedAt(), notNullValue(String.class));
        assertThat(actualOrder.getUpdatedAt(), notNullValue(String.class));
        Assert.assertTrue(actualOrder.getPrice() > 0);
        Assert.assertTrue(actualOrder.getNumber() > 0);
    }

    @DisplayName("Создание заказа без ингредиентов")
    @Description("Негативный тест - нельзя создать заказ без указания ингредиентов")
    @Test
    public void createOrderWithoutIngredientsGives400(){
        IngredientList ingredientList = new IngredientList(List.of());

        given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", accessToken)
                .and()
                .body(ingredientList)
                .when()
                .post("orders").then()
                .assertThat().body("success", is(false))
                .and().body("message", is("Ingredient ids must be provided"))
                .and().statusCode(400);
    }

    @DisplayName("Создание заказа с некорректным хэшем ингредиентов")
    @Description("Негативный тест - нельзя создать заказ с некорректным хэшем ингредиентов")
    @Test
    public void createOrderWithIncorrectIngredientsGives500(){
        IngredientList ingredientList = new IngredientList(List.of("invalid" + ingredientHash));

        given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", accessToken)
                .and()
                .body(ingredientList)
                .when()
                .post("orders").then()
                .assertThat().statusCode(500);
    }
}