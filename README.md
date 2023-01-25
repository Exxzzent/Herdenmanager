# Manager für Rinderherden

Die folgenden Hinweise sollen bei der Lösung von Aufgaben helfen.
Sie beschreiben die in den jeweiligen Klassen implementierten
Fähigkeiten der Objekte.

## Was kann ein Rindvieh?

Bevor ein Rindvieh Aktionen ausführt, sollte man es erzeugen und auf einen Acker stellen:

```kotlin
val vera = acker.lassRindWeiden("Vera")
```

Es kann sich bewegen:

```kotlin
vera.geheVor()
vera.dreheDichLinksRum()
vera.dreheDichRechtsRum()
```

Es kann Gras fressen oder rauchen:

```kotlin
vera.frissGras()
vera.raucheGras()
```

Es kann, bevor man es in eine Richtung schickt, Fragen zu dieser mit boolean-Werten beantworten:

```kotlin
var gehtsDaWeiter = vera.gehtsDaWeiterVor
gehtsDaWeiter = vera.gehtsDaWeiterZurueck
```

Ein Rindvieh kann eventuell Milch geben. Wenn das möglich ist, tut es dies auch gern:

```kotlin
if (vera.istMilchImEuter) {
    vera.gibMilch()
}
```

Es kann die Frage nach seinem Namen mit einem String
beantworten:

```kotlin
val name = vera.name
```

Es kann über seinen aktuellen Status (WARTET, FRISST, RAUCHT)
informieren:

```kotlin
val status = vera.status
```

Es kann über seine Blickrichtung (NORD, OST, SUED, WEST)
informieren:

```kotlin
val richtung = vera.richtung
```

Es kann über seine Position auf dem Acker informieren. Diese
Position ist selbst ein Objekt, das eine X- und Y-Koordinate besitzt:

```kotlin
val position = vera.position
val xKoordinate = position.x
```

## Was kann der Acker?

Jeder MainActivity des HerdenManagers ist bereits ein Acker zugeordnet.

```kotlin
fun manageHerde(mainActivity: MainActivity) {
    val acker = mainActivity.acker
}
```

Der Acker kann Rinder weiden lassen. Ist der Acker selbst bereits sichtbar
auf dem Smartphone, werden diese Rinder auch sofort sichtbar. Das Rindvieh
muss hierfür bereits existieren und wird der Methode
übergeben:

```kotlin
val eumel = acker.lassRindWeiden("Eumel")
```

Der Acker kann Kälber und Gräser an einer bestimmten Position erzeugen und
sichtbar machen. Kalb und Gras werden mit jeweils einer Aktion erstellt.
Die Methode benötigt die Position für das Kalb bzw. das Gras als
Parameter:

```kotlin
val position = Position(2, 2)
acker.lassKalbWeiden(position)
```

Der Acker kann über die bereits dargestellten Rinder, Kälber und Gräser
informieren. Die Methoden liefern entsprechende
Listen: getKaelber(), getGraeser(), getViecher()

```kotlin
val kaelber = acker.kaelber
```

Wächst an einer Position Gras, kann es entfernt werden:

```kotlin
val position = Position(1, 2)
acker.entferneGras(position)
```

Da der Acker eine bestimmte Größe hat, sind einige Positionen ungültig.
Die Frage, ob eine Position gültig ist, kann der Acker mit einem
boolean-Wert beantworten:

```kotlin
val position = Position(5, 5)
val gueltig = acker.istGueltig(position)
```

Der Acker kann prüfen, ob derzeit an einer Position Gras wächst oder ein
Kalb steht. Die Antwort ist jeweils ein boolean-Wert:

```kotlin
val position = Position(1, 3)
acker.istDaEinKalb(position)
```

## Was können Kälber und Gras?

Das Kalb kann wie das Rindvieh und das Gras über seine Position informieren. Diese
Position hat eine X- und eine Y-Koordinate:

```kotlin
val position = gras.position
val yKoordinate = position.y
```

Diese Position lässt sich auch ändern:

```kotlin
val kalb = acker.lassKalbWeiden(Position(3, 1))
kalb.position = Position(2, 3)
```
