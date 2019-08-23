/*
    Calculates time elapsed between a user starting and completing a task
 */
import javafx.animation.AnimationTimer
import javafx.beans.binding.Bindings
import javafx.beans.property.*
import javafx.scene.layout.VBox
import tornadofx.*

class NewModalWindow() : Fragment() {
    override val root = VBox()
    var newSheetName = SimpleStringProperty()

    init {
        form() {
            fieldset("New Sheet") {
                field("New Sheet Name:") {
                    textfield().bind(newSheetName)

                }
            }
        }

        button("save").setOnAction {
            fire(NewSheetEvent(newSheetName.get()))
            close()
        }


    }

}