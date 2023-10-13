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
import static org.hamcrest.Matchers.notNullValue;

public class AuthTest {
    private String accessToken;

    @DisplayName("Настройка теста")
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api/auth/";
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

    @DisplayName("Регистрация пользователя")
    @Description("Позитивный тест - регистрация пользователя дает ответ в соответствии с описанным в документации")
    @Test
    public void createUniqueUserGivesCorrectResponse(){
        String email = UserData.getEmail();
        Auth user = new Auth(email, UserData.PASSWORD, UserData.NAME);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("register");
        AuthRegisterResponse authRegisterResponse = response.as(AuthRegisterResponse.class);
        accessToken = authRegisterResponse.getAccessToken();

        Assert.assertTrue(authRegisterResponse.isSuccess());
        assertThat(authRegisterResponse.getAccessToken(), is(notNullValue(String.class)));
        assertThat(authRegisterResponse.getRefreshToken(), is(notNullValue(String.class)));

        User actualUser = authRegisterResponse.getUser();

        assertThat(actualUser.getEmail(), is(email));
        assertThat(actualUser.getName(), is(UserData.NAME));
    }

    @DisplayName("Регистрация неуникального пользователя")
    @Description("Негативный тест - повторная регистрация пользователя дает 403")
    @Test
    public void createNotUniqueUserGives403(){
        String email = UserData.getEmail();
        Auth user = new Auth(email, UserData.PASSWORD, UserData.NAME);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("register");
        AuthRegisterResponse authRegisterResponse = response.as(AuthRegisterResponse.class);
        accessToken = authRegisterResponse.getAccessToken();

        given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("register").then().assertThat().body("success", is(false))
                .and().body("message", is("User already exists"))
                .and().statusCode(403);
    }
    @DisplayName("Регистрация пользователя без поля password")
    @Description("Негативный тест - регистрация пользователя без поля password дает 403")
    @Test
    public void createUserWithoutPasswordGives403(){
        String email = UserData.getEmail();

        Auth user = new Auth();
        user.setEmail(email);
        user.setName(UserData.NAME);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("register").then().assertThat().body("success", is(false))
                .and().body("message", is("Email, password and name are required fields"))
                .and().statusCode(403);
    }

    @DisplayName("Регистрация пользователя без поля email")
    @Description("Негативный тест - регистрация пользователя без поля email дает 403")
    @Test
    public void createUserWithoutEmailGives403(){

        Auth user = new Auth();
        user.setPassword(UserData.PASSWORD);
        user.setName(UserData.NAME);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("register").then().assertThat().body("success", is(false))
                .and().body("message", is("Email, password and name are required fields"))
                .and().statusCode(403);
    }
}