import tornadofx.*

class Application : View() {
    private val mainView: StopWatch by inject()

    override val root = borderpane{
        left = vbox {
            button("REFRESH")
            button("COMMIT")
        }
        center = mainView.root
    }

    init {


    }

}