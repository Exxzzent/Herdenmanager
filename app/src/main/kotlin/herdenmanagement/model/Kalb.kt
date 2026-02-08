package herdenmanagement.model

/**
 * Repräsentiert ein Kalb im Kontext des Model-View-Controller (MVC)-Entwurfsmusters.
 *
 * Als Subtyp von [PositionsElement] erbt ein Kalb grundlegende Eigenschaften wie eine eindeutige ID
 * und eine Position auf einem [Acker]. Diese Klasse dient didaktisch dazu, Konzepte wie Vererbung,
 * Zustandsänderung und die Integration in ein Model zu veranschaulichen.
 *
 * Didaktische Hinweise:
 * - **Vererbung:** [Kalb] demonstriert, wie durch Vererbung gemeinsame Funktionalitäten aus [PositionsElement]
 *   genutzt und bei Bedarf erweitert werden können.
 * - **Modellierung im MVC:** Diese Klasse ist Bestandteil des Model und zeigt, wie auch einfache Objekte
 *   in einem größeren architektonischen Konzept eingebettet werden können.
 * - **Humorvolle Note:** Der Kommentar verweist humorvoll auf "Eimer für Alle – Die Musketiere" als
 *   weiterführende Information, was den Unterricht auflockern und das Interesse der Studenten wecken soll.
 *
 * @author Steffen Greiffenberg
 */
class Kalb : PositionsElement(), DekoElement {
    override fun siehGutAus() {
        zeigeNachricht("Ich sehe immer gut aus!")
    }
}
