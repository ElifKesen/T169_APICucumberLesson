Feature: Restfulbooker sayfasindan bir ID rezervasyonu dogrulamak
  @GetID
  Scenario: Bir rezervasyon ID si ile test
    Given Kullanici IDsi 10 olan booking kaydina GET sorgusu yapar
    Then Kullanici Status kodun 200 oldugunu ve firstname'in "Eric" oldugunu dogrular
    Then Kullanici response bilgilerini dogrular