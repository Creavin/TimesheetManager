/*
    Pop up window that asks user for name of new timesheet
 */
import javafx.animation.AnimationTimer
import javafx.beans.binding.Bindings
import javafx.beans.property.*
import javafx.scene.layout.VBox
import tornadofx.*

class NewModalWindow() : Fragment("New Timesheet") {
    override val root = VBox()
    var newSheetName = SimpleStringProperty()

    init {
        form{
            fieldset("New Sheet") {
                field("New Sheet Name:") {
                    textfield().bind(newSheetName)

                }
            }
        }

        button("save").setOnAction {
            val sheetName = if (newSheetName.get().isNullOrBlank()) "New Sheet" else newSheetName.get()
            fire(NewSheetEvent(sheetName))
            close()
        }


    }

}