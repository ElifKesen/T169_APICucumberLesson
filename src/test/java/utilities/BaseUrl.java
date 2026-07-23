package utilities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BaseUrl {
   protected RequestSpecification spec;

   public BaseUrl(){
      // System.out.println("Constructor calisti");

       String baseUrl=ConfigReader.getProperty("base_url");
       spec=new RequestSpecBuilder().setBaseUri(baseUrl).build();
   }
}
