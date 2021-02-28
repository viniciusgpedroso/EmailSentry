import java.lang.Exception

fun main(args: Array<String>) {
    // TODO create data file if it doesn't exist
    // TODO make options to continue/exit
    // TODO Add option to change location of data file
    val sentry = EmailSentry()
    val classData = ClassData("mc", "714", "a") // TODO remove

    parseArgs(args, sentry, classData)
}

fun parseArgs(args: Array<String>, sentry: EmailSentry, classData: ClassData) {
    val help = """Available commands:
        |--gn [class], --get-new [class]: Get new class info
        |--e [class], --emails [class]: Get the e-mails of all the current students of a given class
        |--le [class], --last-emails [class]: Get the e-mails from the last update of a given class
        |--u [class], --update [class]: Updates the info about a single class
        |--ua, --update-all: Updates all stored classes and students info
        |--exit: Exists the program
        |Check README.md for more sample commands
    """.trimMargin()


    when (args[0].toLowerCase()) {
        "--u", "--update" -> validateClassData(args, sentry::update)
        "--ua", "--update-all" -> sentry.updateAll()
        "--gn", "--get-new" -> validateClassData(args, sentry::getNewClass)
        "--e", "--emails" -> validateClassData(args, sentry::emails)
        "--le", "--last-emails" -> validateClassData(args, sentry::lastEmails)
        // arguments
        else -> println(help)
    }
}

fun validateClassData(
    args: Array<String>,
    functionToApply: (c: ClassData) -> Unit,
) {
    try {
        var classData: ClassData? = when(args.size) {
            4 -> ClassData(
                classPrefix = args[1],
                classCode = args[2],
                classIndex = args[3])
            7 -> ClassData(
                classPrefix = args[1],
                classCode = args[2],
                classIndex = args[3],
                undergrad = args[4].toBoolean(),
                semester = args[5].toInt(),
                year = args[6].toInt()
            )
            else -> null
        }
        if (classData == null) println("Invalid arguments: $args. Check README.")
        classData?.let { functionToApply(it) }
    } catch (ex: Exception) {
        println(ex)
    }

}