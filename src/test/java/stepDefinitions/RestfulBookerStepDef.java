package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import utilities.BaseUrl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

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

//-------------Token Create etme--------------------
    @Given("Endpointe yapilan gerekli POST request ile token alinir")
    public void endpointeYapilanGerekliPOSTRequestIleTokenAlinir() {
       /*
        String requestbody="{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";
        response=given()
                .spec(spec)
                .contentType("application/json")
                .body(requestbody)
                .when()
                .post("/auth");

        String donenToken=response.jsonPath().getString("token");
        System.out.println("Responsetaki token: "+donenToken);

        */

        Map<String, String> reqBodymap=new HashMap<>();
        reqBodymap.put("username","admin");
        reqBodymap.put("password","password123");

        response=given()
                .spec(spec)
                .contentType("application/json")
                .body(reqBodymap)
                .when()
                .post("/auth");

        String donenToken=response.jsonPath().getString("token");
        System.out.println("Dönen token: "+donenToken);

    }


//---------------POST yeni rez olusturma---------------------
    @Given("Kullanici yeni bir rezervasyon olusturur")
    public void kullanici_yeni_bir_rezervasyon_olusturur() {

        Map<String, Object> bookingData=new HashMap<>();
        bookingData.put("firstname","Deniz");
        bookingData.put("lastname","Günes");
        bookingData.put("totalprice", 100);
        bookingData.put("depositpaid", true);

        Map<String, Object> bookingDates=new HashMap<>();
        bookingDates.put("checkin","2026-07-25");
        bookingDates.put("checkout","2026-07-31");

        bookingData.put("bookingdates",bookingDates);
        bookingData.put("additionalneeds","WI-FI");

        response=given()
                .spec(spec)
                .contentType("application/json")
                .body(bookingData)
                .when()
                .post("/booking");

        response.prettyPrint();
    }

    @Then("Status kodunun {int} oldugunu ve booking ID'nin geldigini dogrular")
    public void status_kodunun_oldugunu_ve_booking_id_nin_geldigini_dogrular(Integer StatusCode) {
        Assert.assertEquals((int)StatusCode,response.getStatusCode());
        int olusturulanID=response.jsonPath().getInt("bookingid");
        Assert.assertTrue(olusturulanID>0);

        response.then().body(
                "booking.firstname",Matchers.equalTo("Deniz"),
                "booking.lastname",Matchers.equalTo("Günes"),
                "booking.totalprice",Matchers.equalTo(100)
        );
        System.out.println("Yeni rezervasyon olustu");

    }

    //--------------------PUT Sorgusu---------------------------
    @Given("Kullanici IDsi {int} olan booking kaydini günceller")
    public void kullanici_i_dsi_olan_booking_kaydini_günceller(Integer ID) {
        Map<String, Object> Updatebooking=new HashMap<>();
        Updatebooking.put("firstname","Deniz");
        Updatebooking.put("lastname","Toprak");
        Updatebooking.put("totalprice", 100);
        Updatebooking.put("depositpaid", true);

        Map<String, Object> bookingDates=new HashMap<>();
        bookingDates.put("checkin","2026-07-25");
        bookingDates.put("checkout","2026-07-31");

        Updatebooking.put("bookingdates",bookingDates);
        Updatebooking.put("additionalneeds","WI-FI");

        response=given()
                .spec(spec)
                .auth()
                .preemptive()
                .basic("admin","password123")
                .contentType("application/json")
                .body(Updatebooking)
                .when()
                .put("/booking/"+ID);

        response.prettyPrint();
/*
    Preemptive Authentication, kimlik doğrulama bilgisini istek gönderilmeden önce
    sunucuya proaktif olarak eklememizi sağlar.
Normalde, bir sunucu önce kullanıcıdan bir istek alır ve ardından "401 Unauthorized" yanıtı ile
kimlik doğrulaması talep eder. Preemptive Authentication ile, sunucunun bu tür bir yanıt
döndürmesini beklemeden, kimlik doğrulama bilgisini istekle birlikte hemen gönderiyoruz.
Yani bu, gereksiz bir 401 hata yanıtı almadan direkt kimlik doğrulama bilgisini sunucuya ileterek işlemi hızlandırır.
     */

    }
    @Then("Status kodun {int} ve kaydin güncellendigini dogrular")
    public void status_kodun_ve_kaydin_güncellendigini_dogrular(Integer statusCode) {
        assertEquals((int)statusCode,response.getStatusCode());
        assertEquals("Deniz",response.jsonPath().getString("firstname"));

    }

    // ---------------- DELETE ----------------
    @Given("Kullanıcı ID'si {int} olan booking kaydını siler")
    public void kullanici_id_si_olan_booking_kaydini_siler(int bookingId) {
      //ÖNEMLI: Testi calistirinca 50 ID'yi sildik,
        // yeniden calistirmadan önce hangi ID silecekseniz feature'da o ID'yi yaziniz
        response = given()
                .spec(spec)
                .auth().preemptive().basic("admin", "password123")
                .contentType("application/json")
                .when()
                .delete("/booking/" + bookingId);

        response.prettyPrint();
    }

    @Then("Status kodunun {int} olduğunu ve yanıtın {string} içerdiğini doğrular")
    public void status_kodunun_oldugunu_ve_yanitin_icerdigini_dogrular(int statusCode, String expectedText) {

        response.then()
                .statusCode(statusCode)
                .body(equalTo(expectedText));//Created görmeliyiz
    }


}
