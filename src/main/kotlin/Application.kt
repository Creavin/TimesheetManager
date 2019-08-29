import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Stop
import tornadofx.*
import tornadofx.Stylesheet.Companion.tab
import java.io.File


class Application : View("Timesheet Manager") {
    val name = SimpleStringProperty()

    override val root = borderpane{
        left = vbox {
            button("New Timesheet").setOnAction{
                //openInternalWindow<NewModalWindow>(modal = true)
                find<NewModalWindow>().openModal()
            }

            button("Write to file").setOnAction {
                var newFile = File("tmp.text")
                newFile.writeText("Hello there")

            }
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

    init {
        print(root.center.getToggleGroup())
    }



}


