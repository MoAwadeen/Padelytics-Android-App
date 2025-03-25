package grad.project.padelytics.appComponents

import android.content.Context

object AppUtils{

    fun showToast(context: Context, message: String) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show()
    }
}