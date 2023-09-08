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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UserDetailsTest {
    private String accessToken;
    private String email;

    @DisplayName("Настройка теста")
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api/auth/";

        email = UserData.getEmail();
        Auth user = new Auth(email, UserData.PASSWORD, UserData.NAME);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("register");
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
                    .delete("user").then()
                    .assertThat().body("success", is(true)).and().statusCode(202);
        }
    }

    @DisplayName("Изменение данных пользователя (имя) с авторизацией")
    @Description("Позитивный тест - изменение имени пользователя с авторизацией дает ответ в соответствии с " +
            "описанным в документации")
    @Test
    public void changeUserNameDataWithAuthorizationGivesCorrectResponse(){
        User newUser = new User();
        String newUserName = "New" + UserData.NAME;
        newUser.setName(newUserName);
        Response userChangeResponse = given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", accessToken)
                .and()
                .body(newUser)
                .when()
                .patch("user");
        AuthRegisterResponse userResponseBody = userChangeResponse.as(AuthRegisterResponse.class);

        Assert.assertTrue(userResponseBody.isSuccess());

        User actualUser = userResponseBody.getUser();

        assertThat(actualUser.getEmail(), is(email));
        assertThat(actualUser.getName(), is(newUserName));
    }

    @DisplayName("Изменение данных пользователя (email) с авторизацией")
    @Description("Позитивный тест - изменение email пользователя с авторизацией дает ответ в соответствии с " +
            "описанным в документации")
    @Test
    public void changeUserEmailDataWithAuthorizationGivesCorrectResponse(){
        User newUser = new User();
        String newUserEmail = "new" + email;
        newUser.setEmail(newUserEmail);

        Response userChangeResponse = given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", accessToken)
                .and()
                .body(newUser)
                .when()
                .patch("user");
        AuthRegisterResponse userResponseBody = userChangeResponse.as(AuthRegisterResponse.class);

        Assert.assertTrue(userResponseBody.isSuccess());

        User actualUser = userResponseBody.getUser();

        assertThat(actualUser.getEmail(), is(newUserEmail));
        assertThat(actualUser.getName(), is(UserData.NAME));
    }

    @DisplayName("Изменение данных пользователя (имя) без авторизации")
    @Description("Позитивный тест - изменение имени пользователя без авторизации дает ответ 401")
    @Test
    public void changeUserNameDataWithoutAuthorizationGives401(){
        User newUser = new User();
        String newUserName = "New" + UserData.NAME;
        newUser.setName(newUserName);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(newUser)
                .when()
                .patch("user")
                .then().body("success", is(false))
                .and().body("message", is("You should be authorised"))
                .and().statusCode(401);
    }

    @DisplayName("Изменение данных пользователя (email) без авторизации")
    @Description("Позитивный тест - изменение email пользователя без авторизации дает 401")
    @Test
    public void changeUserEmailDataWithoutAuthorizationGives401(){
        User newUser = new User();
        String newUserEmail = "new" + email;
        newUser.setEmail(newUserEmail);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(newUser)
                .when()
                .patch("user")
                .then().body("success", is(false))
                .and().body("message", is("You should be authorised"))
                .and().statusCode(401);
    }
}