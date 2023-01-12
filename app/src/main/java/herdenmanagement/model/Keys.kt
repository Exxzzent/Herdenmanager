package herdenmanagement.model

/**
 * Dieses Singleton speichert die Schlüssel, unter denen Nachrichten
 * in der App versendet werden. Ändert man zum Beispiel die Position einer Kuh,
 * sendet diese die Nachricht PROPERTY_POSITION an ihre Beobachter.
 */
object Keys {

    /**
     * Schlüssel zur Kommunikation mit einem [PropertyChangeListener].
     * Der Schlüssel wird als property der Methode [.informiereBeobachter]
     * übergeben.
     *
     * Der Schlüssel dient für Nachrichten zum [Acker].
     */
    const val PROPERTY_SIZE = "herdenmanagement.model.Acker.size"

    /**
     * Schlüssel zur Kommunikation mit einem [PropertyChangeListener].
     * Der Schlüssel wird als property der Methode [.informiereBeobachter]
     * übergeben.
     *
     *
     * Der Schlüssel dient für Nachrichten zum Property [.viecher].
     */
    const val PROPERTY_VIECHER = "herdenmanagement.model.Acker.viecher"

    /**
     * Schlüssel zur Kommunikation mit einem [PropertyChangeListener].
     * Der Schlüssel wird als property der Methode [.informiereBeobachter]
     * übergeben.
     *
     *
     * Der Schlüssel dient für Nachrichten zum Property [.kaelber].
     */
    const val PROPERTY_KALB = "herdenmanagement.model.Acker.kaelber"

    /**
     * Schlüssel zur Kommunikation mit einem [PropertyChangeListener].
     * Der Schlüssel wird als property der Methode [.informiereBeobachter]
     * übergeben.
     *
     *
     * Der Schlüssel dient für Nachrichten zum Property [.graeser].
     */
    const val PROPERTY_GRAESER = "herdenmanagement.model.Acker.graeser"

    /**
     * Schlüssel zur Kommunikation mit einem [PropertyChangeListener].
     * Der Schlüssel wird als property der Methode [.informiereBeobachter]
     * übergeben.
     *
     *
     * Der Schlüssel dient für Nachrichten zum Property [.threading].
     */
    const val PROPERTY_THREADING = "herdenmanagement.model.Acker.threading"

    /**
     * Schlüssel zur Kommunikation mit einem [PropertyChangeListener].
     * Der Schlüssel wird als property der Methode [.informiereBeobachter]
     * übergeben.
     *
     *
     * Der Schlüsel dient für Nachrichten zum Property [.nachricht].
     */
    const val PROPERTY_NACHRICHT = "herdenmanagement.model.PositionsElement.nachricht"

    /**
     * Schlüssel zur Kommunikation mit einem [PropertyChangeListener].
     * Der Schlüssel wird als property der Methode [.informiereBeobachter]
     * übergeben.
     *
     *
     * Der Schlüssel dient für Nachrichten zum Property [.position].
     */
    const val PROPERTY_POSITION = "herdenmanagement.model.PositionsElement.position"

    /**
     * Schlüssel zur Kommunikation mit einem [PropertyChangeListener].
     * Der Schlüssel wird als property der Methode [.informiereBeobachter]
     * übergeben.
     *
     *
     * Der Schlüssel dient für Nachrichten zum Property [.richtung].
     */
    const val PROPERTY_RICHTUNG = "herdenmanagement.model.Rindvieh.richtung"

    /**
     * Schlüssel zur Kommunikation mit einem [PropertyChangeListener].
     * Der Schlüssel wird als property der Methode [.informiereBeobachter]
     * übergeben.
     *
     * Der Schlüssel dient für Nachrichten zum Property [.milchImEuter].
     */
    const val PROPERTY_MILCH = "herdenmanagement.model.Rindvieh.milch"

    /**
     * Schlüssel zur Kommunikation mit einem [PropertyChangeListener].
     * Der Schlüssel wird als property der Methode [.informiereBeobachter]
     * übergeben.
     *
     * Der Schlüssel dient für Nachrichten zum Property [.status].
     */
    const val PROPERTY_STATUS = "herdenmanagement.model.Rindvieh.status"
}