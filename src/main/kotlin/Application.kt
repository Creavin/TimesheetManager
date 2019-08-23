import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Stop
import tornadofx.*
import tornadofx.Stylesheet.Companion.tab



class Application : View("Timesheet Manager") {
    val name = SimpleStringProperty()


    override val root = borderpane{
        left = vbox {
            button("New Timesheet").setOnAction{
                //openInternalWindow<NewModalWindow>(modal = true)
                find<NewModalWindow>().openModal()
            }

            button("Open Timesheet")
        }
        center = tabpane{
            tab<StopWatch>{
            }

            // Add tabs on demand when NewDocumentEvent is emitted
            subscribe<NewSheetEvent> { event ->
                tab<StopWatch>{
                    textProperty().unbind() //required to change tab title
                    text = event.message
                }
            }
        }
    }



}


