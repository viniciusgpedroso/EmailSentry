import java.io.File
import java.lang.Exception
import java.time.LocalDateTime

class EmailSentry {
    companion object {
        private const val DEFAULT_FILENAME = "classStudentLog.json"
        private const val DEFAULT_PATH = "./src/main/resources/$DEFAULT_FILENAME"

        fun loadLog(filename: String = DEFAULT_FILENAME): ClassStudentLog? {
            val file = this::class.java.getResource(filename)
            val jsonString = file?.let { it.readText(Charsets.UTF_8) } ?: ""
            return try {
                println("Trying to read $filename")
                ClassStudentLog.decode(jsonString)
            } catch (ex: Exception) {
                println("An error occurred when reading the decoding data. Error: $ex")
                null
            }
        }
    }

    private var classStudentLog = loadLog()

    fun update(classData: ClassData) {
        println("Updating info for class ${classData.id}.")
        val entry = findClass(classData)
        if (entry == null) {
            println("Class ${classData.id} doesn't exist in storage. Getting new info for it.")
            getNewClass(classData)
            return
        }
        val updatedStudentsList = EmailScraper.scrapeClassData(classData)
        val updatedStudents = updatedStudentsList.toSet()
        val currentStudents = entry.currentStudents.toSet()

        val addedStudents = updatedStudents.filter { it !in currentStudents }
        val removedStudents = currentStudents.filter { it !in updatedStudents }

        if (addedStudents.isNotEmpty() || removedStudents.isNotEmpty()) {
            if (entry.log == null) entry.log = ArrayList()
            entry.log?.let{ it.add(LogEntry(LocalDateTime.now(), addedStudents, removedStudents)) }

            entry.currentStudents = updatedStudentsList

            println("Updating storage.")
            updateStorage()
        }
        println("New students found: $addedStudents\nRemoved students found: $removedStudents")
    }

    fun updateAll() {
        println("Updating info for all classes stored.")
        classStudentLog?.entries?.forEach {
            update(it.logClassData)
        }
    }

    fun emails(classData: ClassData) {
        println("Getting emails from class ${classData.id}.")
        val entry = findClass(classData)

        println(entry?.currentStudents?.joinToString { it.dacEmail } ?: "")
    }

    fun lastEmails(classData: ClassData) {
        println("Getting emails added in the last update of class ${classData.id}.")
        val entry = findClass(classData)
        val noUpdate = "There is no update log available"
        if (entry?.log.isNullOrEmpty()) return

        println(entry?.log?.last()?.studentsAdded?.joinToString { it.dacEmail }
            ?: noUpdate)
    }

    fun getNewClass(classData: ClassData) {
        val entry = findClass(classData)
        if (entry != null) {
            println("Class ${classData.id} already exists. Updating the info for it.")
            update(classData)
            return
        }
        println("Getting info about new class ${classData.id}.")

        val studentsList = EmailScraper.scrapeClassData(classData)
        val classStudentLogEntry = ClassStudentLogEntry(
            classData,
            LocalDateTime.now(),
            studentsList,
            null
        )

        if (classStudentLog == null) {
            val list = ArrayList<ClassStudentLogEntry>()
            list.add(classStudentLogEntry)
            classStudentLog = ClassStudentLog(list)
        } else {
            classStudentLog?.let { it.entries.add(classStudentLogEntry) }
        }

        updateStorage()
    }

    private fun findClass(classData: ClassData): ClassStudentLogEntry? {
        if (classStudentLog == null) {
            println("There is no data available.")
            return null
        }
        val entry = classStudentLog?.entries?.find { it.logClassData.id == classData.id }
        if (entry == null) {
            println("The class ${classData.id} has no data available.")
            return null
        }
        return entry
    }

    private fun updateStorage() {
        val file = File(DEFAULT_PATH)
        classStudentLog?.let { file.writeText(it.encode()) }
    }
}