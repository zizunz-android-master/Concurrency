package happy.mjstudio.concurrencysample.model

data class Video(
    val name : String,
    val sizeKb : Int
) {
    override fun toString() = "Downloaded - $name(${sizeKb}kb)"
}