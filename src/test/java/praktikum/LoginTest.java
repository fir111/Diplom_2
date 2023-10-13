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

public class LoginTest {

    private Auth user;
    private String accessToken;
    private String email;

    @DisplayName("Настройка теста")
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api/auth/";

        email = UserData.getEmail();

        user = new Auth(email, UserData.PASSWORD, UserData.NAME);

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

    @DisplayName("Логин существующим пользователем")
    @Description("Позитивный тест - логин существующим пользователем дает ответ в соответствии с " +
            "описанным в документации")
    @Test
    public void loginWithCorrectUserGivesCorrectResponse(){

        Response loginResponse = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("login");
        AuthRegisterResponse loginResponseBody = loginResponse.as(AuthRegisterResponse.class);

        Assert.assertTrue(loginResponseBody.isSuccess());
        assertThat(loginResponseBody.getAccessToken(), is(notNullValue(String.class)));
        assertThat(loginResponseBody.getRefreshToken(), is(notNullValue(String.class)));

        User actualUser = loginResponseBody.getUser();

        assertThat(actualUser.getEmail(), is(email));
        assertThat(actualUser.getName(), is(UserData.NAME));
    }

    @DisplayName("Логин пользователем с некорректным email")
    @Description("Негативный тест - логин не существующим пользователем дает 401")
    @Test
    public void loginUserWithInCorrectEmailGives401(){
        Auth incorrectUser = new Auth();
        incorrectUser.setEmail("Incorrect_"+email);
        incorrectUser.setPassword(UserData.PASSWORD);
        incorrectUser.setName(UserData.NAME);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(incorrectUser)
                .when()
                .post("login").then()
                .assertThat().body("success", is(false))
                .and().body("message", is("email or password are incorrect"))
                .and().statusCode(401);
    }

    @DisplayName("Логин пользователем с некорректным password")
    @Description("Негативный тест - логин существующим пользователем с некорректным паролем дает 401")
    @Test
    public void loginUserWithInCorrectPasswordGives401(){
        Auth incorrectUser = new Auth();
        incorrectUser.setEmail(email);
        incorrectUser.setPassword("Incorrect"+UserData.PASSWORD);
        incorrectUser.setName(UserData.NAME);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(incorrectUser)
                .when()
                .post("login").then()
                .assertThat().body("success", is(false))
                .and().body("message", is("email or password are incorrect"))
                .and().statusCode(401);
    }
}