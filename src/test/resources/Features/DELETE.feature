Feature: RestfulBooker API Silme (DELETE) İşlemi
  @DELETE
  Scenario: Kullanıcı mevcut bir booking kaydını siler
    Given Kullanıcı ID'si 50 olan booking kaydını siler
    Then Status kodunun 201 olduğunu ve yanıtın "Created" içerdiğini doğrular