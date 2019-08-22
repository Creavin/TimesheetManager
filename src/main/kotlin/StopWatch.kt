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

    var times = mutableListOf<String>().observable()

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
        val elapsedTimeLabel = label()

        val time = SimpleStringProperty()
        time.set("00 : 00 : 00")
        elapsedTimeLabel.textProperty().bind(time)

        val running = SimpleBooleanProperty()
        var paused = false

        val timer = object : AnimationTimer() {
            private var startTime: Long = 0

            override fun start() {
                startTime = System.nanoTime()
                running.set(true)
                super.start()
            }

            override fun stop() {
                running.set(false)
                super.stop()
            }

            fun resume(){
                running.set(true)
                super.start()
            }

            override fun handle(timestamp: Long) {
                val now = System.nanoTime()

                time.set( timeFormatter((now-startTime)/1000000000) ) //Dividing converts nano to seconds
            }
        }

        val startStopButton = button()
        val resumeButton = button("Resume"){opacity = 0.5}

        //todo improve button text
        startStopButton.textProperty().bind(
            Bindings.`when`(running)
                .then("Stop")
                .otherwise("Start")
        )


        //FIXME paused time is written to list
        startStopButton.setOnAction {
            if (running.get()) {
                timer.stop()
                resumeButton.opacity = 1.0
                paused = true
            } else {
                if (paused) {//if the timer is stopped and not paused, we're done here
                    times.add(elapsedTimeLabel.text)
                    paused = false

                }
                timer.start()
                resumeButton.opacity = 0.5
            }
        }

        //FIXME fix logical error, timer continues to run while paused
        resumeButton.setOnAction {
            if (!running.get()){
                timer.resume()
                resumeButton.opacity = 0.5
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