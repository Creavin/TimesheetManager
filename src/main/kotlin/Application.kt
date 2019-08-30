/*
    Main class that controls the application
 */

import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.io.File


class Application : View("Timesheet Manager") {
    val name = SimpleStringProperty()

    private val mainTabPane = tabpane{
        tab("New Sheet") {
            val sw = StopWatch()
            content = sw.root

            subscribe<SaveTabEvent> { _ ->
                print(sw.listOfTimes.items)
            }
        }

        // Add tabs on demand when NewDocumentEvent is emitted
        subscribe<NewSheetEvent> { event ->
            tab(event.message){
                val sw = StopWatch()
                content = sw.root

                subscribe<SaveTabEvent> { _ ->
                    print(sw.listOfTimes.items)
                }
            }
        }
    }

    override val root = borderpane{
        left = vbox {
            button("New Timesheet").setOnAction{
                find<NewModalWindow>().openModal()
            }

            button("Write to file").setOnAction {
                fire(SaveTabEvent())
                val newFile = File("tmp.text")
                newFile.writeText("Hello there")
            }
        }
        center = mainTabPane
    }

    init {
        subscribe<SaveTabEvent> { _ ->
            print(mainTabPane.selectionModel.selectedIndex)
            mainTabPane.selectionModel.selectedItem.text = "lol"
        }
    }



}


