# Manager für Rinderherden

Die folgenden Hinweise sollen bei der Lösung von Aufgaben helfen.
Sie beschreiben die in den jeweiligen Klassen implementierten
Fähigkeiten der Objekte.

## Was kann ein Rindvieh?

Bevor ein Rindvieh Aktionen ausführt, sollte man es erzeugen und auf einen Acker stellen:

```java
Rindvieh vera = new Rindvieh("Vera");
acker.lassRindWeiden(vera);
```

Es kann sich bewegen: 

```java
vera.geheVor();
vera.dreheDichLinksRum();
vera.dreheDichRechtsRum();
vera.geheZurueck();
```

Es kann Gras fressen oder rauchen:

```java
vera.frissGras();
vera.raucheGras();
```
        
Es kann, bevor man es in eine Richtung schickt, Fragen zu dieser mit boolean-Werten beantworten: 

```java
boolean gehtsDaWeiter = vera.gehtsDaWeiterVor();
gehtsDaWeiter = vera.gehtsDaWeiterZurueck();
```

Ein Rindvieh kann eventuell Milch geben. Wenn das möglich ist, tut es dies auch gern:

```java
if (vera.istMilchImEuter()) {
    int milchMenge = vera.gibMilch();
}
```

Nach dem Fressen produziert das Rindvieh Milch. Wieviel im Euter ist,
kann man abfragen:

```java
vera.frissGras();
int milchImEuter = vera.messeMilchImEuter();
```

Es kann die Frage nach seinem Namen mit einem String
beantworten:

```java
String name = vera.gibName();
```

Es kann über seinen aktuellen Status (WARTET, FRISST, RAUCHT)
informieren:

```java
Rindvieh.StatusTyp status = vera.gibStatus();
```

Es kann über seine Blickrichtung (NORD, OST, SUED, WEST)
informieren:

```java
Rindvieh.RichtungsTyp status = vera.gibRichtung();
```

Es kann über seine Position auf dem Acker informieren. Diese
Position ist selbst ein Objekt, das eine X- und Y-Koordinate besitzt:

```java
Position p = vera.gibPosition();
int xKoordinate = p.x;
```

## Was kann der Acker?

Bevor man einen Acker benutzen kann, sollte man ihn erzeugen und mit einem 
Herdenmanager verknüpfen:

```java
Acker acker = new Acker(7,7);
herdenManager.setzeAcker(acker);
```

Der Acker kann Rinder weiden lassen. Ist der Acker selbst bereits sichtbar
auf dem Smartphone, werden diese Rinder auch sofort sichtbar. Das Rindvieh
muss hierfür bereits existieren und wird der Methode
übergeben: 

```java
Rindvieh eumel = new Rindvieh("Eumel");
acker.lassRindWeiden(eumel);
```

Der Acker kann Kälber und Gräser an einer bestimmten Position erzeugen und
sichtbar machen. Kalb und Gras werden mit der Aktion erstellt und müssen
nicht wie das Rindvieh bei lassRindWieden(...) vorher existieren.
Die Methode benötigt die Position für das Kalb bzw. das Gras als
Parameter:

```java
Position p = new Position(3, 2);
Kalb k = acker.lassKalbWeiden(p);
```

Der Acker kann über die bereits dargestellten Rinder, Kälber und Gräser
informieren. Die Methoden liefern entsprechende
Listen: getKaelber(), getGraeser(), getViecher()

```java
List<Kalb> kaelberListe = acker.getKaelber();
```

Wächst an einer Position Gras, kann es entfernt werden: 

```java
Position p = new Position(14, 1);
boolean grasEntfernt = acker.entferneGras(p);
```

Da der Acker eine bestimmte Größe hat, sind einige Positionen ungültig.
Die Frage, ob eine Position gültig ist, kann der Acker mit einem
boolean-Wert beantworten:

```java
Position p = new Position(10, 23);
boolean gueltig = acker.istPositionGueltig(p);
```

Der Acker kann prüfen, ob derzeit an einer Position Gras wächst oder ein
Kalb steht. Die Antwort ist jeweils
ein boolean-Wert: istDaEinKalb(), istDaGras()

```java
Position p = new Position(1, 3);
boolean kalbInfo = acker.istDaEinKalb(p);
```

## Was können Kälber und Gras?

Das Kalb kann wie das Rindvieh und das Gras über seine Position informieren. Diese
Position hat eine X- und eine Y-Koordinate: gibPosition().x, gibPosition().y

```java
Position p = gras.gibPosition();
int yKoordinate = p.y;
```

Diese Position lässt sich auch ändern. Hierfür nutzt man die Methode: setzePosition(...)

```java
Position p = new Position(6, 8);
kalb.setzePosition(p);
```
