package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import utilities.BaseUrl;

import static io.restassured.RestAssured.given;

public class RestfulBookerStepDef extends BaseUrl {

    Response response;

    //------------------rezListesi GET (restfulBooker)---------------
    @Given("Kullanici booking entpointine GET istegi yapar")
    public void kullanici_booking_entpointine_get_istegi_yapar() {
       // System.out.println("Given adimi calisti");
        response=given().spec(spec).when().get("/booking");
        //response.prettyPrint();
    }

    @Then("Status kodunun {int} oldugunu dogrular")
    public void status_kodunun_oldugunu_dogrular(Integer statusCode) {
        response.then().assertThat().statusCode(statusCode);
        //Assert.assertEquals((int)statusCode,response.getStatusCode());
    }

    @Then("Detaylari dogrular")
    public void detaylari_dogrular() {
        response.then().statusCode(200)
                .time(Matchers.lessThan(1500L))
                .body("size()",Matchers.greaterThan(0))
                .body("[0]",Matchers.hasKey("bookingid"));

        System.out.println("Test gecti");

        Assertions.assertTrue(response.getTime() < 1500L, "Yanıt süresi 1.5 saniyeden kisa sürdü!");
    }

    //---------------GETid kodlari--------------
    @Given("Kullanici IDsi {int} olan booking kaydina GET sorgusu yapar")
    public void kullanici_i_dsi_olan_booking_kaydina_get_sorgusu_yapar(Integer bookingID) {
        response=given().spec(spec).when().get("/booking/10");
        response.prettyPrint();
    }

    @Then("Kullanici Status kodun {int} oldugunu ve firstname'in {string} oldugunu dogrular")
    public void kullanici_status_kodun_oldugunu_ve_firstname_in_oldugunu_dogrular(Integer statusCode, String beklenenfirstname) {
        Assert.assertEquals((int)statusCode,response.getStatusCode());
        Assert.assertEquals(beklenenfirstname,response.jsonPath().getString("firstname"));
        //ikisi ayni sorgu oldugundan alttakini yoruma aldik
        //response.then().assertThat().body("firstname",Matchers.equalTo(beklenenfirstname));
    }
    @Then("Kullanici response bilgilerini dogrular")
    public void kullanici_response_bilgilerini_dogrular() {
        //totalprice'in bir numara icerip icermedigini dogrulayalim
        response.then().body("totalprice",Matchers.instanceOf(Integer.class));

        //totalprice degerinin .... oldugunu dogrulayalim
        response.then().body("totalprice",Matchers.equalTo(381));

        //checkin olup olmadigini dogrulayalim
        response.then().body("bookingdates",Matchers.hasKey("checkin"));


    }



}
