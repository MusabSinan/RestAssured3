package GoRest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class GoRestUsersTests {


    @BeforeClass
    void Setup() {

        //restAsured kendi static değişkeni tanımlı değer atanıyor
        baseURI = "https://gorest.co.in/public/v2/";

    }

    public String getRandomEmail() {

        return RandomStringUtils.randomAlphabetic(8).toLowerCase() + "@gmail.com";

    }


    int userID = 0;

    @Test(priority = 1)
    public void createUserObject() {

        User newUser = new User();

        newUser.setName("güllü");
        newUser.setGender("male");
        newUser.setEmail(getRandomEmail());
        newUser.setStatus("active");


        userID =
                given()

                        //header da bulunan kısımdan authorization yerinden tokenı aldık token ın türü neyse o türden  verdik
                        .header("Authorization", "Bearer d52f67b3b1b20ccc614a3f0e68de1db11c450eb1d344f813e8ffecd7988a374d")
                        .contentType(ContentType.JSON)// göndermek istediğimiz body türünü seçtik
                        //body ne ise buraya  göndermek istediğimiz değerlerri ilk başta classsa attık nesne oluşturup  oradan çağırdık
                        .body(newUser)


                        .when()
                        //create methodu post olduğu için postu kullandık
                        .post("users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        //.extract().path("id")
                        .extract().jsonPath().getInt("id")


        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.


        ;
        System.out.println("userID = " + userID);


    }


    @Test(dependsOnMethods = "createUserObject")
    public void updateUserObject() {            //https://gorest.co.in/public/v2/users

        Map<String, String> upDateUser = new HashMap<>();

        upDateUser.put("name", "bergen");


        given()

                //header da bulunan kısımdan authorization yerinden tokenı aldık token ın türü neyse o türden  verdik
                .header("Authorization", "Bearer d52f67b3b1b20ccc614a3f0e68de1db11c450eb1d344f813e8ffecd7988a374d")
                .contentType(ContentType.JSON)// göndermek istediğimiz body türünü seçtik
                //body ne ise buraya  göndermek istediğimiz değerlerri ilk başta mape attık oradan çağırdık
                .body(upDateUser)
                .log().body()
                .pathParam("userID", userID) //burada userID yi bir değişkene atadık


                .when()
                //update metodu put olduğu için onu kullandık
                .put("users/{userID}")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo("bergen"))


        ;
        System.out.println("userID = " + userID);


    }

    @Test(dependsOnMethods = "createUserObject", priority = 2)
    public void getUserByID() {            //https://gorest.co.in/public/v2/users


        given()

                //header da bulunan kısımdan authorization yerinden tokenı aldık token ın türü neyse o türden  verdik
                .header("Authorization", "Bearer d52f67b3b1b20ccc614a3f0e68de1db11c450eb1d344f813e8ffecd7988a374d")
                .contentType(ContentType.JSON)// göndermek istediğimiz body türünü seçtik
                //.log().body()
                .pathParam("userID", userID)


                .when()
                .get("users/{userID}")

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))


        ;
        System.out.println("userID = " + userID);


    }

    @Test(dependsOnMethods = "createUserObject", priority = 3)
    public void deleteUserById() {            //https://gorest.co.in/public/v2/users

        given()


                .header("Authorization", "Bearer d52f67b3b1b20ccc614a3f0e68de1db11c450eb1d344f813e8ffecd7988a374d")
                .contentType(ContentType.JSON)
                .log().body()
                .pathParam("userID", userID)


                .when()
                .delete("users/{userID}")

                .then()
                .log().body()
                .statusCode(204)


        ;


    }

    @Test(dependsOnMethods = "deleteUserById")
    public void deleteUserByIDNegative() {            //https://gorest.co.in/public/v2/users


        given()


                .header("Authorization", "Bearer d52f67b3b1b20ccc614a3f0e68de1db11c450eb1d344f813e8ffecd7988a374d")
                .contentType(ContentType.JSON)
                .log().body()
                .pathParam("userID", userID)


                .when()
                .delete("users/{userID}")

                .then()
                .log().body()
                .statusCode(404)


        ;


    }

    @Test
    public void getUsers() {
        Response response =
                given()
                        .header("Authorization", "Bearer d52f67b3b1b20ccc614a3f0e68de1db11c450eb1d344f813e8ffecd7988a374d")

                        .when()
                        .get("users")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().response();


        // TODO : 3 usersın id sini alınız (path ve jsonPath ile ayrı ayrı yapınız)

        int idUser3 = response.path("[2].id");
        int idUser3JsonPath = response.jsonPath().getInt("[2].id");

        System.out.println("idUser3 = " + idUser3);
        System.out.println("idUser3JsonPath = " + idUser3JsonPath);


        // TODO : Tüm gelen veriyi bir nesneye atınız (google araştırması)

        User[] userspath = response.as(User[].class);

        System.out.println(Arrays.toString(userspath));

        List<User> usersJsonPath = response.jsonPath().getList("", User.class);
        System.out.println(usersJsonPath);


    }


    // TODO : GetUserByID testinde dönen user ı bir nesneye atınız.
    @Test
    public void getUserByIDExtract() {            //https://gorest.co.in/public/v2/users


        User user =
                given()


                        .header("Authorization", "Bearer d52f67b3b1b20ccc614a3f0e68de1db11c450eb1d344f813e8ffecd7988a374d")
                        .contentType(ContentType.JSON)
                        .pathParam("userID", 3414)


                        .when()
                        .get("users/{userID}")

                        .then()
                        .log().body()
                        .statusCode(200)
                        //.extract().as(User.class)
                        .extract().jsonPath().getObject("", User.class);

        System.out.println("user = " + user);


    }


    @Test(enabled = false)
    public void createUser() {            //https://gorest.co.in/public/v2/users


        int usersID =
                given()

                        //header da bulunan kısımdan authorization yerinden tokenı aldık token ın türü neyse o türden  verdik
                        .header("Authorization", "Bearer d52f67b3b1b20ccc614a3f0e68de1db11c450eb1d344f813e8ffecd7988a374d")
                        .contentType(ContentType.JSON)// göndermek istediğimiz body türünü seçtik
                        //body ne ise buraya yada göndermek istediğimiz
                        .body("{\"name\":\"musabsinan\", \"gender\":\"male\", \"email\":\"" + getRandomEmail() + "\", \"status\":\"active\"}")


                        .when()
                        //create methodu post olduğu için postu kullandık
                        .post("users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");
        System.out.println("usersID = " + usersID);


    }


    @Test(enabled = false)
    public void createUserMap() {            //https://gorest.co.in/public/v2/users


        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", "hösün");
        newUser.put("gender", "male");
        newUser.put("email", getRandomEmail());
        newUser.put("status", "active");

        int usersID =
                given()

                        //header da bulunan kısımdan authorization yerinden tokenı aldık token ın türü neyse o türden  verdik
                        .header("Authorization", "Bearer d52f67b3b1b20ccc614a3f0e68de1db11c450eb1d344f813e8ffecd7988a374d")
                        .contentType(ContentType.JSON)// göndermek istediğimiz body türünü seçtik
                        //body ne ise buraya  göndermek istediğimiz değerlerri ilk başta mape attık oradan çağırdık
                        .body(newUser)


                        .when()
                        //create methodu post olduğu için postu kullandık
                        .post("users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");
        System.out.println("usersID = " + usersID);


    }


}

class User {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String gender;
    private String email;
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}