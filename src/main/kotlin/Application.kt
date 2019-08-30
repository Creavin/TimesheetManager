import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import javafx.scene.control.Tab
import javafx.scene.paint.Stop
import tornadofx.*
import tornadofx.Stylesheet.Companion.tab
import java.io.File
import java.lang.Thread.sleep
import java.util.*
import kotlin.reflect.KMutableProperty


class Application : View("Timesheet Manager") {
    val name = SimpleStringProperty()

    var mainTab: Tab? = null

    val mainTabPane = tabpane{
        mainTab = tab("") {
            var sw = StopWatch()
            content = sw.root

            textProperty().unbind() //required to change tab title
            text = "Hello"

            subscribe<SaveTabEvent> { event ->
                print(sw.lview.items)
            }
        }


        // Add tabs on demand when NewDocumentEvent is emitted
        subscribe<NewSheetEvent> { event ->
            tab(event.message){
                var sw = StopWatch()
                content = sw.root

                subscribe<SaveTabEvent> { event ->
                    print(sw.lview.items)
                }
            }
        }
    }

    override val root = borderpane{
        left = vbox {
            button("New Timesheet").setOnAction{
                //openInternalWindow<NewModalWindow>(modal = true)
                find<NewModalWindow>().openModal()
            }

            button("Write to file").setOnAction {
                fire(SaveTabEvent())
                var newFile = File("tmp.text")
                newFile.writeText("Hello there")
            }
        }
        center = mainTabPane
    }

    init {
        //mainTab?.content.getChildList().
        subscribe<SaveTabEvent> { _ ->
            print(mainTabPane.selectionModel.selectedIndex)
        }
    }



}


