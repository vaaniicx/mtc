# Protocol

## Design
Die Applikation besteht aus den Paketen `config`, `controller`, `database`, `dto`, `entity`, `exception`, `repository`, `server`, `service` und `util`.
>**config:** Dient zum Verwalten von allgemeinen Konfigurationen und Konstanten für den Betrieb der Applikation<br>
>**controller:** Hier befinden sich alle Controller für die verschiedenen Endpunkte der Rest-Schnittstelle<br>
>**database:** Hier befindet sich die Logik für die Datenbankverbindung und Senden von Queries an die Datenbank<br>
>**dto**: Hier befinden sich die Datentransferobjekte der Applikation, die zum Austausch über die Schnittstelle erforderlich sind<br>
>**entity**: Hier befinden sich die Entity und Enumeration-Klassen der Applikation<br>
>**exception**: Hier befinden sich diverse Exceptions<br>
>**repository**: Hier befinden sich alle Repository-Klassen, wo Anfragen an die Datenbank gesendet und das Ergebnis weiter an den Service-Layer geleitet werden<br>
>**server**: Implementiert Server-Logik<br>
>**service**: Hier befinden sich alle Service-Klassen. Stehen in direkter Verbindung und Austausch mit Repository-Klassen<br>
>**util**: Verschiedene Utility-Klassen, die logisch zusammengelegte Funktionen zur Vereinfachung des restlichen Codes bieten<br>

## Lessions learned
- Entwicklung wird enorm erleichtert durch
    - Klare Anforderungen
    - Klar definierte Testfälle/-Scripts
- Guter Code erfordert ausreichend Zeit (Aber: Diamanten entstehen nur unter Druck ;))
- Es gibt noch viel zu lernen in Anbetracht auf Architektur (beispielsweise Onion-Architecture) und das Umsetzen von manchen Patterns (Builder, Prinzipien aus SOLID) macht Spaß und entwickelt einen weiter
- Multithread-Umgebungen machen mir keinen Spaß

## Unique Feature
Ich habe mich um ein eher fortgeschritteneres ELO-System entschieden.
> Das ELO-System dient dazu, die relativen Spielniveaus zwischen zweier Spielern zu berechnen.<br>
> **K-Faktor**: Der K-Faktor (eine Konstante mit dem Wert 32) bestimmt die Empfindlichkeit des Systems gegenüber Bewertungsunterschieden<br>
> **calculateExpectedOutcome-Methode**: Berechnet die erwartete Ergebniswahrscheinlichkeit für Spieler A in einem Match gegen Spieler B mithilfe der Elo-Formel<br>
> **updateRatings-Methode**: Aktualisiert die Bewertungen von zwei Spielern basierend auf dem tatsächlichen Ergebnis eines Spiels. Es verwendet die erwarteten Ergebnisse und die tatsächlichen Ergebnisse, um neue Elo-Bewertungen für beide Spieler zu berechnen<br>

## Unit Test Design
Folgende Test-Klassen wurden implementiert: `BalanceOperationTest`, `CardMapperTest`, `TradingDealMapperTest`, `UserMapperTest`, `BattleTest`. Diese wurden mithilfe der Frameworks `JUnit` und `Mockito` umgesetzt. In den Test-Klassen befinden sich insgesamt **20 Tests**.


## Time spent
Es wurden insgesamt **104 Commits an 6 verschiedenen Tagen** getätigt. Damit ergibt sich eine Arbeitszeit von ungefähr **35 bis 40 Stunden**.

## Link to Git
[MonsterTradingCards - GitHub Link (vaaniicx)](https://github.com/vaaniicx/mtc)
