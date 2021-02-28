import org.jsoup.Jsoup
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets


class EmailScraper {
    companion object {
        private const val POST_URL = "https://fenix.lab.ic.unicamp.br/listador/email.php"

        fun scrapeClassData(classData: ClassData): MutableList<Student> {
            val formDataMap = classData.getFormData()
            val httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build()
            val request = HttpRequest.newBuilder()
                .POST(ofFormData(formDataMap))
                .uri(URI.create(POST_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build()

            val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
            println(response.statusCode()) // TODO check status code and report to terminal

            val body = Jsoup.parse(response.body()).body()
            val table = body.select("table")[0]
            val rows = table.select("tr")
            val studentsList = ArrayList<Student>()

            for (i in 1 until rows.size) { //skip title row
                val row = rows[i]
                val cols = row.select("td")
                studentsList.add(Student(name = cols[2].ownText(), dacEmail = cols[4].ownText()))
            }
            return studentsList
        }

        private fun ofFormData(data: Map<String, String>): HttpRequest.BodyPublisher? {
            val result = StringBuilder()
            for ((key, value) in data) {
                if (result.isNotEmpty()) {
                    result.append("&")
                }
                val encodedName = URLEncoder.encode(key, StandardCharsets.UTF_8)
                val encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8)
                result.append(encodedName)
                if (encodedValue != null) {
                    result.append("=")
                    result.append(encodedValue)
                }
            }
            return HttpRequest.BodyPublishers.ofString(result.toString())
        }
    }
}