package happy.mjstudio.concurrencysample.screen

interface VideoDownloader {
    fun download()
    fun cancel()
    fun reset()
}