package Campus;

import Campus.Model.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
 import static org.hamcrest.Matchers.*;

public class CountryTest {


    Cookies cookies;

    @BeforeClass
    public void LoginCampus(){

       baseURI="https://demo.mersys.io/";

        Map<String,String> credential=new HashMap<>();

        credential.put("username","richfield.edu");
        credential.put("password","Richfield2020!");
        credential.put("remembrMe","true");



        cookies=
        given()

                .contentType(ContentType.JSON)
                .body(credential)


                .when()
                .post("auth/login")

                .then()
                .log().cookies()
                .statusCode(200)
                .extract().response().getDetailedCookies()


                ;

    }


    String countryID;
    String countryName;
    String countryCode;

    @Test
    public void createCountry(){

        countryName=getRandomName();
        countryCode=getRandomCode();

        Country country=new Country();
        country.setName(countryName);
        country.setCode(countryCode);

        countryID=
        given()

                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)


                .when()
                .post("school-service/api/countries")


                .then()
                .log().body()
                .statusCode(201)
                .extract().jsonPath().getString("id")

        ;


    }



    public String getRandomName() {

        return RandomStringUtils.randomAlphabetic(8).toLowerCase();

    }


    public String getRandomCode() {

        return RandomStringUtils.randomAlphabetic(3).toLowerCase();

    }

    @Test(dependsOnMethods = "createCountry")
    public void createCountryNegative(){


        Country country=new Country();
        country.setName(countryName);
        country.setCode(countryCode);


                given()

                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(country)


                        .when()
                        .post("school-service/api/countries")


                        .then()
                        .log().body()
                        .statusCode(400)
                        .body("message",equalTo("The Country with Name \""+countryName+"\" already exists."))

        ;


    }



    @Test(dependsOnMethods = "createCountry")
    public void UpdateCountry(){

        countryName=getRandomName();

        Country country=new Country();
        country.setId(countryID);
        country.setName(countryName);
        country.setCode(countryCode);


        given()

                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)


                .when()
                .put("school-service/api/countries")


                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(countryName))

        ;


    }



    @Test(dependsOnMethods = "UpdateCountry")
    public void DeleteCountryById(){



        given()

                .cookies(cookies)
                .pathParam("countryID",countryID)


                .when()
                .delete("school-service/api/countries/{countryID}")


                .then()
                .log().body()
                .statusCode(200)


        ;


    }



    @Test(dependsOnMethods = "DeleteCountryById")
    public void DeleteCountryNegative(){



        given()

                .cookies(cookies)
                .pathParam("countryID",countryID)


                .when()
                .delete("school-service/api/countries/{countryID}")


                .then()
                .log().body()
                .statusCode(400)


        ;


    }

    @Test(dependsOnMethods = "DeleteCountryById")
    public void UpdateCountryNegative(){

        countryName=getRandomName();

        Country country=new Country();
        country.setId(countryID);
        country.setName(countryName);
        country.setCode(countryCode);


        given()

                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)


                .when()
                .put("school-service/api/countries")


                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Country not found"))


        ;


    }

}
