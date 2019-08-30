/*
    Event that fires when user creates new timesheet
 */
import tornadofx.FXEvent

class NewSheetEvent(var message: String = "New Sheet") : FXEvent()
