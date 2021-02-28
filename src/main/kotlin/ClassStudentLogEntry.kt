import java.time.LocalDateTime

data class ClassStudentLogEntry(
    val logClassData: ClassData,
    val lastUpdated: LocalDateTime,
    var currentStudents: MutableList<Student>,
    var log: MutableList<LogEntry>?
)