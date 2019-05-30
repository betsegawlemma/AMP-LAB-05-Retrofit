package et.edu.aait.retrofitexample.network

data class Course (
    val id: Long,
    val code: String,
    val title: String,
    val ects: Int,
    val description: String
)
