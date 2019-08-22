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
            var elapsedTime: Long = 0 //when the timer is paused the elapsed time is updated and the start time is reset

            override fun start() { //resets start and elapsed time
                elapsedTime = 0
                startTime = System.nanoTime()
                running.set(true)
                super.start()
            }

            override fun stop() {
                running.set(false)
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

        val startStopButton = button()
        val resumeButton = button("Pause"){opacity = 0.5}

        //todo improve button text; refactor Start Stop
        startStopButton.textProperty().bind(
            Bindings.`when`(running)
                .then("Stop")
                .otherwise("Start")
        )


        startStopButton.setOnAction {
            if (running.get()) {
                timer.stop() //timer is paused
                resumeButton.opacity = 1.0
                paused = true
            } else {
                if (paused) {//if the timer is stopped and not paused, we're done here
                    times.add(elapsedTimeLabel.text)
                    paused = false
                }
                timer.start()  //timer is started / restarted
                resumeButton.opacity = 0.5
            }
        }

        resumeButton.setOnAction {
            if (!running.get()){
                timer.resume()
                resumeButton.opacity = 0.5
            } else {
                resumeButton.text = "resume"
                timer.stop() //timer is paused
                paused = true
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