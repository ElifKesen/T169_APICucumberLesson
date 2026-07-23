Feature: restfulBooker API testleri
  @POSTCreate
  Scenario: Yeni bir rezervasyon olusturma testi
    Given Kullanici yeni bir rezervasyon olusturur
    Then Status kodunun 200 oldugunu ve booking ID'nin geldigini dogrular