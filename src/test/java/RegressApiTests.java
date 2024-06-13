import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class RegressApiTests extends TestBase {
    @Test
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
}
