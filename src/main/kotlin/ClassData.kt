import java.time.LocalDate

data class ClassData(
    val classPrefix: String,
    val classCode: String,
    val classIndex: String,
    val undergrad: Boolean = true,
    val semester: Int = getCurrentSemester(),
    val year: Int = LocalDate.now().year,
) {
    companion object {
        fun getCurrentSemester(): Int {
            return when(LocalDate.now().monthValue) {
                in 1..6 -> 1
                else -> 2
            }
        }
    }

    val id = "$classPrefix$classCode$classIndex-${semester}s$year-${gradPos()}"

    fun getFormData(): Map<String, String> {
        return mapOf(
            "sigla" to classPrefix,
            "disciplina" to classCode,
            "turma" to classIndex,
            "gradpos" to gradPos(),
            "periodo" to semester.toString(),
            "ano" to year.toString()
        )
    }

    private fun gradPos() = if(undergrad) "grad" else "pos"
}