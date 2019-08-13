package happy.mjstudio.concurrencysample.model

data class Video(
    val name : String,
    val sizeKb : Int
) {
    override fun toString() = "내 비디오의 이름 : $name 크기 : ${sizeKb}kb"
}