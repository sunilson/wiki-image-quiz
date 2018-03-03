package at.sunilson.wiki_quiz.shared

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import at.sunilson.wiki_quiz.R

/**
 * Created by linus on 28/02/2018.
 */

class LoadingDialog : DialogFragment() {

    lateinit var builder: AlertDialog.Builder
    lateinit var content: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        builder = AlertDialog.Builder(activity)
        content = activity.layoutInflater.inflate(R.layout.loading_dialog_body, null)
        builder.setView(content)
        this.activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        return builder.create()
    }

    override fun onStart() {
        super.onStart()

        dialog.setCanceledOnTouchOutside(false)

        dialog.setOnKeyListener { p1, p2, p3 ->
            true
        }
    }

    companion object {
        fun newInstance(): LoadingDialog {
            val loadingDialog = LoadingDialog()
            return loadingDialog
        }
    }
}