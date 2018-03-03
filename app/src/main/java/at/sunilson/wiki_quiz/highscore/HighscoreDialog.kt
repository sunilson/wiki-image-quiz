package at.sunilson.wiki_quiz.highscore

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.WindowManager
import at.sunilson.wiki_quiz.R
import at.sunilson.wiki_quiz.game.models.Highscore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.highscore_dialog_body.view.*

/**
 * Created by linus on 26.02.2018.
 */

class  HighscoreDialog : DialogFragment() {

    lateinit var builder: AlertDialog.Builder
    lateinit var content: View
    lateinit var highscoreAdapter: HighscoreRecyclerAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        builder = AlertDialog.Builder(activity)
        content = activity.layoutInflater.inflate(R.layout.highscore_dialog_body, null)
        builder.setNegativeButton(R.string.close, null)
        val highscores : List<Highscore> = Gson().fromJson(arguments.get("highscores") as String)

        //Check if there already are any highscores
        if(highscores.size > 0) {
            //Initialize recyclerview
            content.highscore_dialog_recyclerview.layoutManager = LinearLayoutManager(activity)
            highscoreAdapter = HighscoreRecyclerAdapter(highscores.toMutableList())
            content.highscore_dialog_recyclerview.adapter = highscoreAdapter
        } else {
            //Show placeholder
            content.highscore_dialog_recyclerview.visibility = View.GONE
            content.highscore_dialog_empty.visibility = View.VISIBLE
        }
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

    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

    companion object {
        fun newInstance(highScores: List<Highscore>): HighscoreDialog {
            val highscoreDialog = HighscoreDialog()
            val args = Bundle()
            args.putString("highscores", Gson().toJson(highScores))
            highscoreDialog.arguments = args
            return highscoreDialog
        }
    }

}