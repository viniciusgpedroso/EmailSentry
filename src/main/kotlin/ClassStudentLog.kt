import com.google.gson.GsonBuilder

class ClassStudentLog(var entries: MutableList<ClassStudentLogEntry>) {

    companion object {
        private val gson = GsonBuilder().disableHtmlEscaping().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .serializeNulls().create()

        fun decode(logJson: String): ClassStudentLog? {
            return gson.fromJson(logJson, ClassStudentLog::class.java)
        }
    }

    fun encode(): String {
        return gson.toJson(this).toString()
    }
}