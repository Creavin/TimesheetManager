/*
    Calculates time elapsed between a user starting and completing a task
 */
import javafx.animation.AnimationTimer
import javafx.beans.binding.Bindings
import javafx.beans.property.*
import javafx.scene.layout.VBox
import tornadofx.*

class StopWatch : Fragment("Screen 1") {
    override val root = VBox()

    val time = SimpleStringProperty()

    val running = SimpleBooleanProperty()
    var paused = SimpleBooleanProperty()

    var times = mutableListOf<String>().observable()
    private val defaultTime = "00 : 00 : 00"

    fun timeFormatter(difference: Long): String{
        var seconds = difference

        val hours = (difference/3600).toInt()
        seconds -= hours*3600
        val minutes = (difference/60).toInt()
        seconds -= minutes*60

        // 9 minutes -> 09 minutes
        val precedingZero = fun(quantum: Int): String { return if (quantum < 10 ) "0$quantum" else "$quantum"}
        return "${precedingZero(hours)} : ${precedingZero(minutes)} : ${precedingZero(seconds.toInt())}"
    }

    init{
        time.set(defaultTime)
        val elapsedTimeLabel = label()
        val startStopButton = button()
        val resumeButton = button()

        elapsedTimeLabel.textProperty().bind(time)

        val timer = object : AnimationTimer() {
            private var startTime: Long = 0
            var elapsedTime: Long = 0 //when the timer is paused the elapsed time is updated and the start time is reset

            override fun start() { //resets start and elapsed time
                elapsedTime = 0
                startTime = System.nanoTime()
                running.set(true)
                paused.set(false)
                super.start()
            }

            override fun stop() {
                running.set(false)
                elapsedTime += System.nanoTime() - startTime
                super.stop()
            }

            fun pause() {
                elapsedTime += System.nanoTime() - startTime
                super.stop()
            }

            fun resume(){
                startTime = System.nanoTime() //time is updated to reflect pause
                running.set(true)
                super.start()
            }

            override fun handle(timestamp: Long) {
                val now = System.nanoTime()
                time.set( timeFormatter((now-startTime+elapsedTime)/1000000000) )//Divide converts nano->sec
            }
        }

        startStopButton.textProperty().bind(
            Bindings.`when`(running)
                .then("Stop")
                .otherwise("Start")
        )

        resumeButton.textProperty().bind(
            Bindings.`when`(paused)
                .then("Resume")
                .otherwise("Pause")
        )

        startStopButton.setOnAction {
            if (running.get()) {
                timer.stop()
                times.add(elapsedTimeLabel.text)
                time.set(defaultTime)
            } else {
                timer.start()  //timer is started / restarted
            }
        }

        resumeButton.setOnAction {
            if (paused.get()){
                timer.resume()
                paused.set(false)
            } else if (!paused.get()){
                timer.pause() //timer is paused
                paused.set(true)
            }
        }

        with(root){
            style {
                padding = box(20.px)
            }
            listview(times)
        }

    }

}