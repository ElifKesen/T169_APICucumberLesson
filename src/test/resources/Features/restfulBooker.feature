Feature:restfulBooker API testleri
  @get
  Scenario:GET request ile rezervasyon kayitlarini listelemek
    Given Kullanici booking entpointine GET istegi yapar
    Then Status kodunun 200 oldugunu dogrular
    Then Detaylari dogrular