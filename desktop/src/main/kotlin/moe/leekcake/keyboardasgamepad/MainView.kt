package moe.leekcake.keyboardasgamepad

import javafx.scene.Scene
import javafx.scene.input.KeyEvent
import tornadofx.View
import tornadofx.hbox
import tornadofx.keyboard
import tornadofx.label
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.File
import java.lang.Exception
import java.net.Socket

class MainView : View() {
    val remapTable = HashMap<Int, Int>()

    override val root = hbox {
        label("Input Receiver :p")
        keyboard {
            addEventHandler(KeyEvent.KEY_PRESSED) {
                if (remapTable.containsKey(it.code.code)) {
                    socketOutput.writeByte(0)
                    socketOutput.writeInt(remapTable[it.code.code] ?: error(""))
                } else {
                    println("Unmapped: " + it.code.code)
                }
            }
            addEventHandler(KeyEvent.KEY_RELEASED) {
                if (remapTable.containsKey(it.code.code)) {
                    socketOutput.writeByte(1)
                    socketOutput.writeInt(remapTable[it.code.code] ?: error(""))
                }
            }
        }
    }

    val socket: Socket
    val socketOutput: DataOutputStream

    init {
        val table = File("remap_table.txt")
        val tableStream = BufferedReader(table.reader())
        for (line in tableStream.lines()) {
            if(line.startsWith("#")) continue
            val split = line.split("=")
            remapTable[split[0].toInt()] = split[1].toInt()
        }
        tableStream.close()

        var process = ProcessBuilder("adb", "push", "kagp.jar", "/data/local/tmp/kagp.jar")
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT).start()
        process.waitFor()

        process = ProcessBuilder("adb", "shell", "CLASSPATH=/data/local/tmp/kagp.jar", "app_process", "/", "moe.leekcake.keyboardasgamepad.Main", "0", "8000000", "false")
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT).start()

        Thread.sleep(1000)

        process = ProcessBuilder("adb", "forward", "tcp:3940", "tcp:3940")
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT).start()
        process.waitFor()

        socket = Socket("127.0.0.1", 3940)
        socketOutput = DataOutputStream(socket.getOutputStream())
    }
}