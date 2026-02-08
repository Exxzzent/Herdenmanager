package herdenmanagement.model

/**
 * Repräsentiert Gras als Element auf einem Acker.
 *
 * Diese Klasse ist Teil des Model im Model-View-Controller (MVC)-Entwurfsmuster und
 * erbt von [PositionsElement]. Dadurch erhält sie eine Position auf einem [Acker] und
 * kann in dessen Logik integriert werden. Obwohl Gras in anderen Kontexten (z. B. als
 * Droge) schädliche Wirkungen haben kann, wird hier ausschließlich seine Rolle als
 * fressbares bzw. "rauchbares" Element demonstriert – vor allem zu didaktischen Zwecken.
 *
 * Didaktische Hinweise:
 * - **Erben und Vererbung:** Diese Klasse zeigt, wie durch Vererbung Funktionalitäten (z. B.
 *   ID, Position, Observer-Mechanismen) von [PositionsElement] übernommen und in einem
 *   neuen Kontext genutzt werden können.
 * - **Modellierung im MVC:** Gras dient als Beispiel für ein Model-Objekt, das später durch
 *   eine View visualisiert und durch einen Controller manipuliert werden kann.
 *
 * @author Steffen Greiffenberg
 */
class Gras : PositionsElement(), DekoElement {
    override fun siehGutAus() {
        position = Position(position.x + 1, position.y)
        position = Position(position.x - 1, position.y)
    }
}
