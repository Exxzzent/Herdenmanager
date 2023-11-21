package herdenmanagement.model

class TanzRind(name: String) : Rindvieh(name) {

    fun geheSeitwaertsNachLinks() {
    }

    fun geheSeitwaertsNachRechts() {
    }

    val gehtsDaLinksWeiter: Boolean
        get() = true

    val gehtsDaRechtsWeiter: Boolean
        get() = true

    fun chaChaCha() {
        geheVor()
        geheZurueck()
        geheSeitwaertsNachLinks()
        geheSeitwaertsNachLinks()
        geheZurueck()
        geheVor()
        geheSeitwaertsNachRechts()
        geheSeitwaertsNachRechts()
    }
}
