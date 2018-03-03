package at.sunilson.wiki_quiz.shared

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import at.sunilson.wiki_quiz.R
import at.sunilson.wiki_quiz.game.GameView
import at.sunilson.wiki_quiz.game.models.Highscore
import at.sunilson.wiki_quiz.game.models.Question
import com.google.gson.Gson
import kotlinx.android.synthetic.main.info_dialog_body.view.*

/**
 * @author Linus Weiss
 */

class InfoDialog : DialogFragment() {

    lateinit var builder: AlertDialog.Builder
    lateinit var content: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        builder = AlertDialog.Builder(activity)
        content = activity.layoutInflater.inflate(R.layout.info_dialog_body, null)

        when (arguments.get("dialogType")) {
            ANSWERED_DIALOG -> {
                val solution = Gson().fromJson(arguments.get("question") as String, Question::class.java).solution
                val answer = arguments.getBoolean("answer")

                content.info_dialog_continue_button.setOnClickListener {
                    if (activity is GameView) (activity as GameView).displayNextQuestion()
                    dialog.dismiss()
                }

                content.info_dialog_info_button.setOnClickListener {
                    val uri: Uri = Uri.parse("https://en.wikipedia.org/?curid=" + solution.pageID)
                    startActivity(Intent(Intent.ACTION_VIEW, uri))
                }

                content.info_dialog_title_icon.setImageDrawable(if (answer) resources.getDrawable(R.drawable.ic_check_black_24dp) else resources.getDrawable(R.drawable.ic_close_black_24dp))

                content.info_dialog_title.text = if (answer) resources.getString(R.string.congratulations) else resources.getString(R.string.whoops)

                content.info_dialog_answer_text.text = if (answer) {
                    '"' + solution.title + '"' + resources.getString(R.string.correct_answer)
                } else {
                    resources.getString(R.string.wrong_answer) + " " + '"' + solution.title + '"'
                }

                if (solution.extract != null && (solution.extract as String).length > 150) {
                    content.info_dialog_answer_extract.text = (solution.extract as String).substring(0, 150) + "..."
                } else content.info_dialog_answer_extract.text = solution.extract
                builder.setView(content)
            }
            QUIT_DIALOG -> {
                builder.setTitle(resources.getString(R.string.quit_question))
                builder.setNegativeButton(R.string.cancel, null)
                builder.setPositiveButton(R.string.quit, null)
                builder.setMessage(resources.getString(R.string.quit_body))
            }
            GAME_OVER_DIALOG -> {
                builder.setPositiveButton(R.string.quit, null)

                //Same as answer dialog
                val solution = Gson().fromJson(arguments.get("question") as String, Question::class.java).solution
                val answer = arguments.getBoolean("answer")

                content.info_dialog_continue_button.visibility = View.GONE
                content.info_dialog_title_icon.visibility = View.GONE

                content.info_dialog_info_button.setOnClickListener {
                    val uri: Uri = Uri.parse("https://en.wikipedia.org/?curid=" + solution.pageID)
                    startActivity(Intent(Intent.ACTION_VIEW, uri))
                }

                content.info_dialog_title.text = resources.getString(R.string.game_over)
                content.info_dialog_answer_text.text = if (answer) {
                    '"' + solution.title + '"' + resources.getString(R.string.correct_answer)
                } else {
                    resources.getString(R.string.wrong_answer) + '"' + solution.title + '"'
                }

                if (solution.extract != null && (solution.extract as String).length > 150) {
                    content.info_dialog_answer_extract.text = (solution.extract as String).substring(0, 150) + "..."
                } else content.info_dialog_answer_extract.text = solution.extract

                //Specific for Game Over
                content.info_dialog_game_over_container.visibility = View.VISIBLE
                val highscore: Highscore = Gson().fromJson(arguments["highscore"] as String, Highscore::class.java)
                content.info_dialog_game_over_ranking.text = "${highscore.points} ${resources.getString(R.string.points)} - ${resources.getString(R.string.rank)} ${highscore.place}"
                content.info_dialog_game_over_questions.text = "${highscore.correctAnswers} ${resources.getString(R.string.game_over_questions)} ${(highscore.time / 10).toInt()} ${resources.getString(R.string.game_over_questions2)}"
                builder.setView(content)
            }
        }

        this.activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        return builder.create()
    }

    override fun onStart() {
        super.onStart()

        val dialog: AlertDialog = dialog as AlertDialog

        when (arguments.get("dialogType")) {
            ANSWERED_DIALOG -> {
                dialog.setCanceledOnTouchOutside(false)

                dialog.setOnKeyListener { p1, p2, p3 ->
                    true
                }
            }
            QUIT_DIALOG -> {
                dialog.setCanceledOnTouchOutside(true)
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                    activity.finish()
                }

                dialog.setOnKeyListener { p1, p2, p3 ->
                    dialog.dismiss()
                    true
                }
            }
            GAME_OVER_DIALOG -> {
                dialog.setCanceledOnTouchOutside(false)

                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                    activity.finish()
                }

                dialog.setOnKeyListener { p1, p2, p3 ->
                    activity.finish()
                    true
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Apply open and close animation to Dialog
        if (dialog.window != null) {
            val answer : Boolean = arguments.getBoolean("answer")
            if(answer) {
                dialog.window.attributes.windowAnimations = R.style.dialogAnimation
            } else {
                dialog.window.attributes.windowAnimations = R.style.dialogErrorAnimation
            }
        }
    }

    companion object {
        fun newInstance(dialogType: Int, question: Question? = null, highscore: Highscore? = null, answer: Boolean = false): InfoDialog {
            val masterPasswordDialog = InfoDialog()
            val args = Bundle()
            args.putInt("dialogType", dialogType)
            if (dialogType != QUIT_DIALOG) args.putBoolean("answer", answer)
            if (question != null) args.putString("question", Gson().toJson(question, Question::class.java))
            if (highscore != null) args.putString("highscore", Gson().toJson(highscore, Highscore::class.java))
            masterPasswordDialog.arguments = args
            return masterPasswordDialog
        }
    }
}