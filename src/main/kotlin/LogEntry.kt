import com.google.gson.GsonBuilder
import java.time.LocalDateTime

data class LogEntry(
    val updateTime: LocalDateTime,
    val studentsAdded: List<Student>,
    val studentsRemoved: List<Student>
)