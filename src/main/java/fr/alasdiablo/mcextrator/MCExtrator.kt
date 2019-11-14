package fr.alasdiablo.mcextrator

import java.io.File
import java.lang.Exception

class MCExtrator {

    fun getFolder() {
        val e = getOperatingSystem()

        when {
            e.contains("win", true) -> {
                FileLocation.MC_DIR = "C:/Users/${getUserName()}/appdata/roaming/.minecraft/"
                FileLocation.OUT_DIR = "C:/Users/${getUserName()}/Desktop/MCExtractor/"
            }
            e.contains("mac", true) -> {
                FileLocation.MC_DIR = "/Users/${getUserName()}/library/Application Support/minecraft/"
                FileLocation.OUT_DIR = "/Users/${getUserName()}/Desktop/MCExtractor/"
            }
            else -> {
                FileLocation.MC_DIR = "/home/${getUserName()}/.minecraft/"
                FileLocation.OUT_DIR = "/home/${getUserName()}/MCExtractor/"
            }
        }
    }

    fun getVersion() {
        try {
            val versionFolder = File("${FileLocation.MC_DIR}/assets/indexes/")
            FileLocation.MC_VERSION_LIST = versionFolder.list()
        } catch (e: Exception) {
            FileLocation.MC_VERSION_LIST = null
        }
    }

    private fun getOperatingSystem(): String = System.getProperty("os.name")

    private fun getUserName(): String = System.getProperty("user.name")
}