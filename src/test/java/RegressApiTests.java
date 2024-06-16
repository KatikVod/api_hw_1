import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class RegressApiTests extends TestBase {
    @Test
    @DisplayName("Успешная регистрация пользователя")
    void successfulRegistrationTest() {
        String registrationData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\"}";

        given()
                .body(registrationData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test
    @DisplayName("Регистрация пользователя без пароля")
    void registrationWithoutPasswordTest() {
        String registrationData = "{\"email\": \"john.holt@reqres.in\"}";

        given()
                .body(registrationData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    @DisplayName("Регистрация пользователя без email")
    void registrationWithoutEmailTest() {
        String registrationData = "{\"password\": \"mypassword\"}";

        given()
                .body(registrationData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    @DisplayName("Регистрация пользователя с некорректным email")
    void registrationWithWrongEmailTest() {
        String registrationData = "{\"email\": \"1\", \"password\": \"pass\"}";

        given()
                .body(registrationData)
                .contentType(JSON)
                .log().uri()
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Note: Only defined users succeed registration"));
    }

    @Test
    @DisplayName("Успешное получение пользователя по id")
    void getSingleUserTest() {

        given()
                .log().uri()
                .when()
                .get("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.id", is(2));
    }

    @Test
    @DisplayName("Неуспешное получение пользователя по id")
    void getNonexistentUserTest() {

        given()
                .log().uri()
                .when()
                .get("/users/30")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }

    @Test
    @DisplayName("Успешное удаление пользователя")
    public void deleteUserTest() {

        given()
                .log().all()
                .when()
                .delete("api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }
}
