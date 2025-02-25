package herdenmanagement.model

/**
 * Dieses Singleton verwaltet alle Schlüssel, die zur Identifikation von Nachrichten im
 * Observer-Muster genutzt werden.
 *
 * Im Kontext des Model-View-Controller (MVC)-Entwurfsmusters ermöglichen diese Schlüssel,
 * dass Änderungen an den Eigenschaften von Model-Objekten (wie z. B. Position, Größe oder Status)
 * konsistent an registrierte PropertyChangeListener übermittelt werden. Jeder Schlüssel repräsentiert
 * eine bestimmte Eigenschaft, sodass Beobachter gezielt auf relevante Änderungen reagieren können.
 *
 * Didaktische Hinweise:
 * - **Zentrale Verwaltung:** Durch die zentrale Definition der Schlüssel in diesem Objekt wird
 *   die Wiederverwendbarkeit und Wartbarkeit des Codes erhöht.
 * - **Typische Anwendungsfälle:** Die Schlüssel werden als Property-Parameter in der Methode
 *   [BeobachtbaresElement.informiereBeobachter] verwendet, um unterschiedliche Ereignisse
 *   (z. B. Änderung der Größe, Position oder eines spezifischen Attributs) zu signalisieren.
 * - **Konventionen:** Die Namenskonvention der Schlüssel (z. B. "herdenmanagement.model.Acker.size")
 *   verdeutlicht den Ursprung und Anwendungsbereich der jeweiligen Nachricht.
 *
 *   @author Steffen Greiffenberg
 */
object Keys {

    /**
     * Schlüssel für Nachrichten zum Property [Acker.spalten] und [Acker.zeilen].
     *
     * Wird verwendet, wenn die Größe des Ackers geändert wird. Dies erlaubt Observern,
     * auf Änderungen in der Dimension des Ackers zu reagieren.
     */
    const val PROPERTY_SIZE = "herdenmanagement.model.Acker.size"

    /**
     * Schlüssel für Nachrichten, die das Property [Acker.viecher] betreffen.
     *
     * Dieser Schlüssel wird verwendet, um Änderungen an der Liste der [Rindvieh]-Instanzen
     * auf dem Acker zu kommunizieren.
     */
    const val PROPERTY_VIECHER = "herdenmanagement.model.Acker.viecher"

    /**
     * Schlüssel für Nachrichten, die das Property [Acker.kaelber] betreffen.
     *
     * Wird verwendet, um Observer zu informieren, wenn ein [Kalb] auf dem Acker platziert
     * oder entfernt wird.
     */
    const val PROPERTY_KALB = "herdenmanagement.model.Acker.kaelber"

    /**
     * Schlüssel für Nachrichten, die das Property [Acker.graeser] betreffen.
     *
     * Dieser Schlüssel wird genutzt, um Observer über Änderungen an der Gras-Instanz auf dem Acker
     * zu benachrichtigen.
     */
    const val PROPERTY_GRAESER = "herdenmanagement.model.Acker.graeser"

    /**
     * Schlüssel für Nachrichten, die das Property [Acker.animation] betreffen.
     *
     * Dieser Schlüssel signalisiert Änderungen in der Art der Animation, die für die Darstellung
     * von Zustandsänderungen auf dem Acker verwendet wird.
     */
    const val PROPERTY_THREADING = "herdenmanagement.model.Acker.threading"

    /**
     * Schlüssel für Nachrichten, die das Property [PositionsElement.nachricht] betreffen.
     *
     * Wird verwendet, um Meldungen (wie Fehlermeldungen oder Informationshinweise) an
     * Observer zu übermitteln, die sich auf das [PositionsElement] beziehen.
     */
    const val PROPERTY_NACHRICHT = "herdenmanagement.model.PositionsElement.nachricht"

    /**
     * Schlüssel für Nachrichten, die das Property [PositionsElement.position] betreffen.
     *
     * Dieser Schlüssel wird genutzt, um Änderungen an der Position eines Elements zu signalisieren.
     */
    const val PROPERTY_POSITION = "herdenmanagement.model.PositionsElement.position"

    /**
     * Schlüssel für Nachrichten, die das Property [Rindvieh.richtung] betreffen.
     *
     * Wird verwendet, um Observer zu informieren, wenn sich die Bewegungsrichtung eines [Rindvieh]
     * ändert.
     */
    const val PROPERTY_RICHTUNG = "herdenmanagement.model.Rindvieh.richtung"

    /**
     * Schlüssel für Nachrichten, die das Property [Rindvieh.milchImEuter] betreffen.
     *
     * Mit diesem Schlüssel werden Änderungen bezüglich der Milchproduktion eines [Rindvieh]
     * an die Observer kommuniziert.
     */
    const val PROPERTY_MILCH = "herdenmanagement.model.Rindvieh.milch"

    /**
     * Schlüssel für Nachrichten, die das Property [Rindvieh.status] betreffen.
     *
     * Dieser Schlüssel signalisiert Änderungen im Status eines [Rindvieh], beispielsweise wenn
     * es bestimmte Aktionen ausführt oder sich in einen anderen Zustand begibt.
     */
    const val PROPERTY_STATUS = "herdenmanagement.model.Rindvieh.status"
}
