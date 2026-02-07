package herdenmanagement

/**
 * Der HerdenManager organisiert und steuert die Rinderherde.
 *
 * Diese Klasse fungiert als Controller im Model-View-Controller (MVC)-Muster. Sie verknüpft das Model,
 * repräsentiert durch [herdenmanagement.model.Acker], [herdenmanagement.model.Rindvieh],
 * [herdenmanagement.model.Kalb] und [herdenmanagement.model.Gras], mit der grafischen Darstellung,
 * die in den View-Klassen (z. B. [herdenmanagement.view.AckerView] und [herdenmanagement.view.RindviehView])
 * realisiert wird.
 *
 * Im Observer-Muster agieren die Model-Klassen als beobachtbare Objekte, während die View-Klassen als Beobachter
 * fungieren. Änderungen am Model (z. B. wenn sich eine Kuh bewegt oder ein Kalb aufgestellt wird) werden an die
 * View weitergeleitet, sodass diese ihre Darstellung automatisch aktualisiert.
 *
 * Didaktische Hinweise:
 * - **MVC-Struktur:**
 *   Diese Klasse illustriert den Controller-Aspekt, indem sie das Model initialisiert und die Steuerung
 *   der Aktionen (z. B. Bewegung der Rinder) übernimmt. Dadurch wird die Trennung von Logik und Darstellung
 *   verdeutlicht.
 * - **Observer-Muster:**
 *   Die Kommunikation zwischen Model und View wird über das Observer-Muster realisiert, was in dieser Klasse
 *   indirekt sichtbar wird, da Änderungen im Model zu aktualisierten Darstellungen in der View führen.
 * - **Animation und Benutzerinteraktion:**
 *   Methoden wie [manageHerde] demonstrieren, wie programmierte Aktionen animiert und in einer nachvollziehbaren
 *   Reihenfolge ausgeführt werden können. Dies ist besonders wichtig für die didaktische Darstellung von
 *   asynchronen Abläufen in einer App.
 *
 * Im Wesentlichen verknüpft der HerdenManager den Acker (Model) mit der AckerView (View) und koordiniert
 * weitere Abläufe, wie das Bewegen von Rindern oder das Fressen von Gras.
 *
 * @author Steffen Greiffenberg
 */
@Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
class HerdenManager {

    /**
     * Initialisiert den Acker.
     *
     * Diese Methode dient dazu, den Acker mit anfänglichen Objekten (z. B. Gras oder Kälbern)
     * auszustatten. Die Einrichtung erfolgt ohne Animation, um einen statischen Ausgangszustand
     * zu gewährleisten.
     *
     * Hinweis: Die Methode kann im Rahmen von Übungsaufgaben auch gestrichen werden, wenn nur
     * [manageHerde] verwendet werden soll.
     *
     * @param mainActivity Die Hauptaktivität der App, über die auch auf die AckerView zugegriffen werden kann.
     */
    fun richteAckerEin(mainActivity: MainActivity) {
        // TODO: Implementieren Sie die Einrichtung des Ackers, z. B. Gras wachsen lassen und Kälber platzieren.
    }

    /**
     * Steuert die Aktionen innerhalb der Herde.
     *
     * In dieser Methode werden vorprogrammierte Aktionen ausgeführt, die die Interaktion mit dem Acker
     * demonstrieren. Beispielsweise wird ein Rind (hier "Vera") auf dem Acker platziert und anschließend
     * bewegt. Die Aktionen werden animiert dargestellt, sodass die Reihenfolge der Bewegungen und Zustandsänderungen
     * nachvollzogen werden kann.
     *
     * @param mainActivity Die Hauptaktivität der App, die Zugriff auf den aktuell angezeigten Acker bietet.
     */
    fun manageHerde(mainActivity: MainActivity) {
        val acker = mainActivity.acker
        val vera = acker.lassRindWeiden("Vera")

        // Bewege die Kuh vorwärts und zeige eine Benachrichtigung
        vera.geheVor()
        vera.geheVor()
        mainActivity.toast("Vera läuft!")

    }
}
