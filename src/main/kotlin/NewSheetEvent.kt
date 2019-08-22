import tornadofx.EventBus.RunOn.*
import tornadofx.FXEvent

class NewSheetEvent(message: String) : FXEvent(){
    init {
        print(message)
    }
}