Feature: RestfulBooker API testleri
  @PUT
  Scenario: Rezervasyon güncelleme testi
    Given Kullanici IDsi 40 olan booking kaydini günceller
    Then Status kodun 200 ve kaydin güncellendigini dogrular