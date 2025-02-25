# Manager für Rinderherden

Diese README bietet einen Überblick über die zentralen Fähigkeiten der Model-Klassen und des HerdenManagers. 
Hier erfährst Du, was Rindviecher, Kälber und Gräser können und wie der HerdenManager als Controller im 
MVC-Muster das Acker-Modell organisiert. 

---

## Was kann ein Rindvieh?

Bevor ein Rindvieh in Aktion tritt, muss es zunächst erzeugt und auf einen Acker gestellt werden:

```kotlin
val vera = acker.lassRindWeiden("Vera")
```

### Bewegung

Ein Rindvieh kann sich bewegen – vorwärts marschieren und dabei auch mal elegant die Richtung wechseln:

```kotlin
vera.geheVor()            // Vorwärts bewegen
vera.dreheDichLinksRum()  // Drehung um 90° nach links
vera.dreheDichRechtsRum() // Drehung um 90° nach rechts
```

### Gras fressen und rauchen

Je nach Laune kann ein Rindvieh Gras fressen (und dabei Milch produzieren) oder Gras rauchen:

```kotlin
vera.frissGras()  // Gras fressen
vera.raucheGras() // Gras rauchen (aber Vorsicht, das ist nicht ganz gesund!)
```

### Bewegungsprüfung

Bevor das Rindvieh in eine bestimmte Richtung geschickt wird, kann geprüft werden, ob der Weg frei ist:

```kotlin
val gehtsVor = vera.gehtsDaWeiterVor  // Kann Vera vorwärts gehen?
val gehtsZurueck = vera.gehtsDaWeiterZurueck  // Oder auch rückwärts?
```

### Milchproduktion

Wenn an der aktuellen Position ein Kalb steht, kann das Rindvieh Milch geben werden – natürlich nur, wenn auch wirklich Milch im Euter ist:

```kotlin
if (vera.istMilchImEuter) {
    vera.gibMilch() // Frisch gemolkene Milch!
}
```

### Name, Status und Blickrichtung

Ein Rindvieh teilt Dir seinen Namen, Status und Blickrichtung mit:

```kotlin
val name = vera.name       // Zum Beispiel "Vera"
val status = vera.status   // Mögliche Werte: WARTET, FRISST, RAUCHT
```

Standardmäßig schaut ein neues Rindvieh nach rechts (Richtung.OST). Möchte es mal was anderes sehen, kann die Blickrichtung angepasst werden:

```kotlin
if (vera.richtung == Richtung.OST) {
    vera.richtung = vera.richtung.umgekehrt
}
```

### Position

Die Position eines Rindviehs wird als Objekt mit X- und Y-Koordinate dargestellt:

```kotlin
val position = vera.position
val xKoordinate = position.x
```

Möchte Vera in das westliche Feld rutschen, so lässt sich ihre Position wie folgt anpassen:

```kotlin
vera.position = vera.position.naechste(Richtung.WEST)
```

---

## Was kann der Acker?

Der Acker bildet das zentrale Model, auf dem Rinder, Kälber und Gräser positioniert werden.
Jede MainActivity im HerdenManager ist bereits mit einem Acker verknüpft:

```kotlin
fun manageHerde(mainActivity: MainActivity) {
    val acker = mainActivity.acker
}
```

### Rinder weiden lassen

Sobald ein Rindvieh erzeugt und dem Acker zugewiesen wird, erscheint es automatisch in der grafischen Darstellung:

```kotlin
val eumel = acker.lassRindWeiden("Eumel")
```

### Kälber und Gräser erzeugen

Kälber und Gräser werden an einer bestimmten Position auf dem Acker erzeugt. Dabei wird jeweils eine Aktion ausgeführt:

```kotlin
val position = Position(2, 2)
acker.lassKalbWeiden(position)
```

### Informationsabfrage

Der Acker bietet auch Methoden, um über die aktuell platzierten Objekte zu informieren:

```kotlin
val kaelber = acker.kaelber   // Liste aller Kälber
```

Gras, das an einer bestimmten Position wächst, kann auch entfernt werden:

```kotlin
val position = Position(1, 2)
acker.entferneGras(position)
```

Da der Acker eine begrenzte Größe hat, kann er auch überprüfen, ob eine bestimmte Position gültig ist:

```kotlin
val position = Position(5, 5)
val gueltig = acker.istGueltig(position)
```

Außerdem kann abgefragt werden, ob an einer Position bereits ein Kalb steht oder Gras wächst:

```kotlin
acker.istDaEinKalb(Position(1, 3))
acker.istDaGras(Position(1, 3))
```

---

## Was können Kälber und Gräser?

Auch Kälber und Gräser sind eigenständige Model-Objekte, die ihre Position auf dem Acker angeben:

```kotlin
val position = gras.position
val yKoordinate = position.y
```

Die Position eines Kalbs kann natürlich auch verändert werden:

```kotlin
val kalb = acker.lassKalbWeiden(Position(3, 1))
kalb.position = Position(2, 3)
```

---

## Der HerdenManager – Das Herzstück des Systems

Der HerdenManager ist der Controller im MVC-Muster und steuert, wie der Acker bespielt wird.
Er verbindet das Model (Acker, Rindvieh, Kalb, Gras) mit der grafischen Darstellung (AckerView)
und koordiniert die Aktionen, die auf dem Acker ablaufen.

### Einrichtung des Ackers

Der HerdenManager kann den Acker initial bestücken – etwa Gras wachsen lassen oder Kälber aufstellen.
Diese Methode ist optional, da sie in Aufgaben auch entfallen kann:

```kotlin
herdenManager.richteAckerEin(mainActivity)
```

### Management der Herde

Die Methode `manageHerde` im HerdenManager koordiniert die dynamischen Aktionen im Acker.
So wird zum Beispiel ein Rind auf die Weide geschickt und bewegt:

```kotlin
fun manageHerde(mainActivity: MainActivity) {
    val acker = mainActivity.acker
    val vera = acker.lassRindWeiden("Vera")
    vera.geheVor()
    mainActivity.toast("Vera läuft!")
}
```

Hier fließen alle zentralen Model-Funktionen zusammen: Das Rindvieh bewegt sich, interagiert mit
Gras und Kälbern und gibt – falls vorhanden – Milch. Gleichzeitig sorgt der HerdenManager dafür,
dass alle Aktionen animiert und nachvollziehbar ablaufen.
