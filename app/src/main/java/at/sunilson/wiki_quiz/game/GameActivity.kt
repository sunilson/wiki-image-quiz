package at.sunilson.wiki_quiz.game

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import at.sunilson.wiki_quiz.R
import at.sunilson.wiki_quiz.game.models.Question
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_game.*
import javax.inject.Inject
import android.view.ViewAnimationUtils
import android.animation.Animator
import android.view.animation.AccelerateInterpolator
import at.sunilson.wiki_quiz.game.models.Highscore
import at.sunilson.wiki_quiz.shared.*


/**
 * @author Linus Weiss
 */

class GameActivity : GameView, BaseActivity(), View.OnClickListener {

    @Inject
    lateinit var gamePresenter: GamePresenter
    private lateinit var currentQuestion: Question
    private var startTime: Long = 0
    private var progressDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //Create a game from the presenter
        gamePresenter.onCreate(this)
        gamePresenter.startGame()

        //Set click listeners of game buttons to the activity
        game_activity_button_1.setOnClickListener(this)
        game_activity_button_2.setOnClickListener(this)
        game_activity_button_3.setOnClickListener(this)
        game_activity_button_4.setOnClickListener(this)
        game_activity_button_5.setOnClickListener(this)
    }

    override fun onBackPressed() {
        val dialog = InfoDialog.newInstance(QUIT_DIALOG)
        dialog.show(fragmentManager, "dialog")
    }

    override fun onDestroy() {
        super.onDestroy()
        //Cleanup
        gamePresenter.onDestroy()
    }


    override fun showAnswer(correct: Boolean, nextQuestion: Question) {
        //Preload the image of the next question as soon as the answer to the last one is displayed
        Picasso.with(this).load(nextQuestion.solution.imageURL).fetch()

        InfoDialog.newInstance(ANSWERED_DIALOG, currentQuestion, null, correct).show(fragmentManager, "dialog")
        currentQuestion = nextQuestion
    }

    override fun displayNextQuestion(nextQuestion: Question?) {
        if (nextQuestion != null) {
            currentQuestion = nextQuestion
        }

        startTime = System.currentTimeMillis()
        circularReveal(true)
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            if(progressDialog == null) progressDialog = LoadingDialog.newInstance()
            progressDialog?.show(fragmentManager, "dialog")
        } else {
            progressDialog?.dismiss()
        }
    }

    override fun gameError(e: Throwable) {
        Toast.makeText(this, "Error while fetching game data!", Toast.LENGTH_LONG).show()
        finish()
    }

    override fun updateHighscoreValue(highscore: Int) {
    }

    /** Gets called when the last question has been answered **/
    override fun gameOver(correct: Boolean, highscore: Highscore) {
        InfoDialog.newInstance(GAME_OVER_DIALOG, currentQuestion, highscore, correct).show(fragmentManager, "dialog")
    }

    override fun onClick(view: View?) {
        if (view != null) {

            //Disable all buttons until the next question starts
            enableDisableButtons(false)

            //Tell the presenter what answer has been clicked and how long it took
            when (view.id) {
                R.id.game_activity_button_1 -> gamePresenter.answerClicked(currentQuestion, System.currentTimeMillis() - startTime, currentQuestion.options[0])
                R.id.game_activity_button_2 -> gamePresenter.answerClicked(currentQuestion, System.currentTimeMillis() - startTime, currentQuestion.options[1])
                R.id.game_activity_button_3 -> gamePresenter.answerClicked(currentQuestion, System.currentTimeMillis() - startTime, currentQuestion.options[2])
                R.id.game_activity_button_4 -> gamePresenter.answerClicked(currentQuestion, System.currentTimeMillis() - startTime, currentQuestion.options[3])
                R.id.game_activity_button_5 -> gamePresenter.answerClicked(currentQuestion, System.currentTimeMillis() - startTime)
            }
        }
    }

    private fun enableDisableButtons(value: Boolean) {
            game_activity_button_1.isEnabled = value
            game_activity_button_2.isEnabled = value
            game_activity_button_3.isEnabled = value
            game_activity_button_4.isEnabled = value
            game_activity_button_5.isEnabled = value
    }

    //Reveal the next question
    private fun circularReveal(value: Boolean) {

        if (value) {
            val x = game_activity_button_container.right
            val y = game_activity_button_container.bottom

            game_activity_reveal_view.visibility = View.INVISIBLE
            val startRadius = 0
            val endRadius = Math.hypot(game_activity_button_container.width.toDouble(), game_activity_button_container.height.toDouble()).toInt()
            val anim = ViewAnimationUtils.createCircularReveal(game_activity_reveal_view, x, y, startRadius.toFloat(), endRadius.toFloat())
            anim.duration = 500
            game_activity_reveal_view.visibility = View.VISIBLE

            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {}
                override fun onAnimationEnd(p0: Animator?) {
                    //Set new data to Content view
                    Picasso.with(this@GameActivity).load(currentQuestion.solution.imageURL).into(game_activity_image)
                    for (i in 0..(game_activity_button_container.childCount - 2)) {
                        (game_activity_button_container.getChildAt(i) as Button).text = currentQuestion.options[i].title
                    }

                    game_activity_main_content.alpha = 0.0f
                    game_activity_main_content.visibility = View.VISIBLE
                    game_activity_reveal_view.alpha = 1.0f

                    //Animate in and out the content and reveal view
                    enableDisableButtons(true)
                    game_activity_main_content
                            .animate()
                            .alpha(1.0f)
                            .setDuration(1000)
                            .setInterpolator(AccelerateInterpolator())
                            .setListener(object: Animator.AnimatorListener {
                        override fun onAnimationRepeat(p0: Animator?) {}
                        override fun onAnimationEnd(p0: Animator?) {
                            game_activity_main_content.visibility = View.VISIBLE
                        }
                        override fun onAnimationCancel(p0: Animator?) {}
                        override fun onAnimationStart(p0: Animator?) {}

                    })
                    game_activity_reveal_view
                            .animate()
                            .alpha(0.0f)
                            .setDuration(1000)
                            .setInterpolator(AccelerateInterpolator())
                            .setListener(object: Animator.AnimatorListener {
                        override fun onAnimationRepeat(p0: Animator?) {}
                        override fun onAnimationEnd(p0: Animator?) {
                            //game_activity_reveal_view.visibility = View.INVISIBLE
                        }
                        override fun onAnimationCancel(p0: Animator?) {}
                        override fun onAnimationStart(p0: Animator?) {}
                    })
                }
                override fun onAnimationCancel(p0: Animator?) {}
                override fun onAnimationStart(p0: Animator?) {}
            })
            anim.start()
        } else {

        }
    }
}