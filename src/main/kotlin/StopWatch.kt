/*
    Calculates time elapsed between a user starting and completing a task
 */
import javafx.animation.AnimationTimer
import javafx.beans.binding.Bindings
import javafx.beans.property.*
import javafx.scene.control.ListView
import javafx.scene.layout.VBox
import tornadofx.*

class StopWatch: Fragment("Default") {
    override val root = VBox()

    val time = SimpleStringProperty() //stores current time as string
    var times = mutableListOf<String>().observable() //list of times
    private val defaultTime = "00 : 00 : 00"
    val listOfTimes: ListView<String>


    /* Stopwatch states */
    val running = SimpleBooleanProperty()
    var paused = SimpleBooleanProperty()


    /* Formats time string */
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
        val elapsedTimeLabel = label() //displays elapsed time
        val startStopButton = button()
        val resumeButton = button()
        listOfTimes = listview(times)

        elapsedTimeLabel.textProperty().bind(time)

        val timer = object : AnimationTimer() {
            private var startTime: Long = 0
            var elapsedTime: Long = 0 //when the timer is paused the elapsed time is updated and the start time is reset

            override fun start() { //resets start and elapsed time
                elapsedTime = 0
                startTime = System.nanoTime()
                running.set(true)
                super.start()
            }

            override fun stop() {
                running.set(false)
                paused.set(false)
                super.stop()
            }

            fun pause() {
                paused.set(true)
                elapsedTime += System.nanoTime() - startTime
                super.stop()
            }

            fun resume(){
                paused.set(false)
                startTime = System.nanoTime() //time is updated to reflect pause
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
                timer.start()
            }
        }

        resumeButton.setOnAction {
            if (paused.get()){
                timer.resume()
            } else if (!paused.get() && running.get()){
                timer.pause()
            }
        }

        with(root){
            style {
                padding = box(20.px)
            }
            listOfTimes
        }

    }

}
