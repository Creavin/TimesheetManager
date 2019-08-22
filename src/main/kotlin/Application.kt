import javafx.scene.paint.Stop
import tornadofx.*
import tornadofx.Stylesheet.Companion.tab



class Application : View("Timesheet Manager") {

    override val root = borderpane{
        left = vbox {
            button("New Timesheet").setOnAction{
                fire(NewSheetEvent("foo"))
            }

            button("Open Timesheet")
        }
        center = tabpane{
            tab<StopWatch>(){

            }

            // Add tabs on demand when NewDocumentEvent is emitted
            subscribe<NewSheetEvent> { _ ->
                tab("New doc") {

                }
            }
        }
    }



}


