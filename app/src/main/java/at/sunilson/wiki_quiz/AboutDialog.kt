package at.sunilson.wiki_quiz

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.View
import android.view.WindowManager

/**
 * Created by linus on 03/03/2018.
 */


/**
 * @author Linus Weiss
 */

class AboutDialog : DialogFragment() {

    lateinit var builder: AlertDialog.Builder
    lateinit var content: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        builder = AlertDialog.Builder(activity)
        content = activity.layoutInflater.inflate(R.layout.about_dialog_body, null)
        builder.setNegativeButton(getString(R.string.close), null)
        builder.setView(content)
        this.activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        return builder.create()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Apply open and close animation to Dialog
        if (dialog.window != null) {
            dialog.window.attributes.windowAnimations = R.style.dialogAnimation
        }
    }

    companion object {
        fun newInstance(): AboutDialog {
            return AboutDialog()
        }
    }

}