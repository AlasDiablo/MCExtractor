package fr.alasdiablo.mcextrator

import javafx.concurrent.Task
import javafx.scene.control.ProgressBar
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.TextArea
import org.json.JSONObject
import java.io.File
import java.lang.StringBuilder
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class FileCopy(version: String) {

    private val json: JSONObject

    private val fileMap: MutableMap<String, String>;

    init {
        val sb = StringBuilder()
        Files.lines(Paths.get("${FileLocation.MC_DIR}/assets/indexes/$version"), StandardCharsets.UTF_8).forEach{e -> sb.append(e)}
        this.json = JSONObject(sb.toString()).getJSONObject("objects")
        this.fileMap = HashMap()
    }

    fun copy(): Task<Any> {
        return object : Task<Any>() {
            @Throws(Exception::class)
            override fun call(): Any? {
                // get all file and create file location
                updateProgress(ProgressIndicator.INDETERMINATE_PROGRESS, 100.0)
                var step = 100.0 / json.length().toDouble()
                var pos = 0.0
                json.keys().forEach { e ->
                    val originalFile = "${FileLocation.MC_DIR}/assets/objects/${json.getJSONObject(e).getString("hash").substring(0, 2)}/${json.getJSONObject(e).getString("hash")}"
                    val newFileLocation = "${FileLocation.OUT_DIR}/$e"
                    fileMap[originalFile] = newFileLocation
                    Main.addTextInLogger("Original file: $originalFile, New File: $newFileLocation\n")
                    pos += step
                    updateProgress(pos, 100.0)
                    updateMessage(Main.getLoggerText())
                }
                updateProgress(ProgressIndicator.INDETERMINATE_PROGRESS, 100.0)

                //Do copy
                step = 100.0 / fileMap.size.toDouble()
                pos = 0.0
                fileMap.forEach { (originalFile, newFileLocation) ->
                    Main.addTextInLogger("Copy $originalFile to $newFileLocation\n")
                    val file = File(originalFile)
                    try {
                        file.copyTo(File(newFileLocation), true)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }

                    pos += step
                    updateProgress(pos, 100.0)
                    updateMessage(Main.getLoggerText())
                }

                Main.addTextInLogger("Copy done!\nThank for using this software\n")
                updateMessage(Main.getLoggerText())
                return null
            }
        }
    }
}