
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.kslimweb.ipolyglot.R

/**
 * Create Progress Dialog with ease
 */
fun Context.createProgressDialog(): AlertDialog {
    return let {
        val progressBar = ProgressBar(this, null, R.attr.progressBarStyle)
        progressBar.isIndeterminate = true
        AlertDialog.Builder(it)
            .setView(progressBar)
            .setCancelable(false)
            .create()
    }.apply {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}