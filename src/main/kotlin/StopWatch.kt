/*
    Calculates time elapsed between a user starting and completing a task
 */

import javafx.animation.AnimationTimer
import javafx.beans.binding.Bindings
import javafx.beans.property.*
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import tornadofx.*


class StopWatch : View() {
    override val root = VBox()

    fun timeFormatter(difference: Long): String{
        var seconds = difference

        val hours = (difference/3600).toInt()
        seconds -= hours*3600
        val minutes = (difference/60).toInt()
        seconds -= minutes*60

        val precedingZero = fun(quantum: Int): String { return if (quantum < 60 ) "0$quantum" else "$quantum"}
        return "${precedingZero(hours)} : ${precedingZero(minutes)} : ${precedingZero(seconds.toInt())}"
    }


    init{
        val elapsedTimeLabel = Label("Default")
        val time = SimpleStringProperty()
        elapsedTimeLabel.textProperty().bind(time)

        val running = SimpleBooleanProperty()


        val timer = object : AnimationTimer() {
            //private var startTime: Long = 0
            private var startTime: Long = 0

            override fun start() {
                //startTime = System.currentTimeMillis()
                startTime = System.nanoTime()
                running.set(true)
                super.start()
            }

            override fun stop() {
                running.set(false)
                super.stop()
            }

            override fun handle(timestamp: Long) {
                val now = System.nanoTime()

                time.set( timeFormatter((now-startTime)/1000000000) ) //Dividing converts nano to seconds
            }
        }

        val startStop = Button("Start Stop Button")

        startStop.textProperty().bind(
            Bindings.`when`(running)
                .then("Stop")
                .otherwise("Start")
        )

        startStop.setOnAction {
            if (running.get()) {
                timer.stop()
            } else {
                timer.start()
            }
        }


        with(root){
            root += elapsedTimeLabel
            root += startStop
        }

    }


}