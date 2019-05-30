package et.edu.aait.retrofitexample

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import et.edu.aait.retrofitexample.network.Course
import et.edu.aait.retrofitexample.network.CourseApiService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var addButton: Button
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var searchButton: Button

    private lateinit var titleEditText: EditText
    private lateinit var codeEditText: EditText
    private lateinit var ectsEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addButton = add_button
        deleteButton = delete_button
        updateButton = update_button
        searchButton = search_button

        titleEditText = title_edit_text
        codeEditText = code_edit_text
        descriptionEditText = description_edit_text
        ectsEditText = ects_edit_text
        searchEditText = search_edit_text

        addButton.setOnClickListener {
            val course = readFields()
            GlobalScope.launch(Dispatchers.IO) {
                if(connected()) {
                    val response: Response<Void> =
                        CourseApiService.getInstance().
                            insertCourseAsync(course).await()
                    Log.d("MainActivity", response.message())
                }
            }
            clearFields()
        }

        updateButton.setOnClickListener {
            val id: Long = searchEditText.text.toString().toLong()
            val course = readFields()
            GlobalScope.launch(Dispatchers.IO) {
                if(connected()) {
                    val response: Response<Void> = CourseApiService.getInstance().updateCourseAsnc(id, course).await()
                    Log.d("MainActivity", response.message())
                }
            }
            clearFields()
        }

        deleteButton.setOnClickListener {
            val id: Long = searchEditText.text.toString().toLong()
            GlobalScope.launch(Dispatchers.IO) {
                if(connected()) {
                    val response: Response<Void> = CourseApiService.getInstance().deleteCourseAsync(id).await()
                    Log.d("MainActivity", response.message())
                }
            }
            clearFields()
        }

        searchButton.setOnClickListener {
            val id: Long = searchEditText.text.toString().toLong()
            GlobalScope.launch(Dispatchers.IO) {
                if(connected()) {
                    val response: Response<Course> =
                        CourseApiService.getInstance().findByIdAsync(id).await()
                    val course = response.body()
                    if (course != null) {
                        withContext(Dispatchers.Main){
                            updateFields(course)
                        }
                    }
                }
            }
        }
    }

    private fun updateFields(course: Course){
        course.run{
            codeEditText.setText(code)
            titleEditText.setText(title)
            ectsEditText.setText(ects.toString())
            descriptionEditText.setText(description)
        }
    }

    private fun readFields() = Course(
        0,
        codeEditText.text.toString(),
        titleEditText.text.toString(),
        ectsEditText.text.toString().toInt(),
        descriptionEditText.text.toString()
    )

    private fun clearFields() {
        searchEditText.setText("")
        codeEditText.setText("")
        titleEditText.setText("")
        ectsEditText.setText("")
        descriptionEditText.setText("")
    }

    private fun connected():Boolean {

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo

        return networkInfo != null && networkInfo.isConnected

    }
}
