package at.sunilson.wiki_quiz

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import at.sunilson.wiki_quiz.game.GameActivity
import at.sunilson.wiki_quiz.highscore.HighscoreDialog
import at.sunilson.wiki_quiz.repository.GameRepository
import at.sunilson.wiki_quiz.shared.BaseActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    private lateinit var slideInLeft: Animation
    private lateinit var slideInRight: Animation
    private lateinit var scaleUp: Animation
    private lateinit var hoverLoop: Animation

    @Inject
    lateinit var gameRepository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set click listeners
        home_activity_play_button.setOnClickListener { startActivity(Intent(this, GameActivity::class.java)) }
        home_activity_highscore_button.setOnClickListener {
            HighscoreDialog.newInstance(gameRepository.getHighscores()).show(fragmentManager, "dialog")
        }
        home_activity_info_button.setOnClickListener { AboutDialog.newInstance().show(fragmentManager, "dialog") }

        //Load animations for buttons
        slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left)
        slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right)
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        hoverLoop = AnimationUtils.loadAnimation(this, R.anim.hover_loop)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    override fun onStart() {
        super.onStart()

        //Hide views if they have allready been animated in
        home_activity_play_button.visibility = View.INVISIBLE
        home_activity_play_button.scaleX = 0f
        home_activity_play_button.scaleY = 0f

        //Start the small button aniamtions
        home_activity_info_button.startAnimation(slideInRight)
        home_activity_highscore_button.startAnimation(slideInLeft)

        //Delay the animation of the big button
        Handler().postDelayed({
            home_activity_play_button.scaleX = 1f
            home_activity_play_button.scaleY = 1f
            home_activity_highscore_button.visibility = View.VISIBLE
            scaleUp.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationEnd(p0: Animation?) {
                    //Play a looping animatin for the big button
                    home_activity_play_button.startAnimation(hoverLoop)
                }

                override fun onAnimationStart(p0: Animation?) {
                }

                override fun onAnimationRepeat(p0: Animation?) {
                }

            })
            home_activity_play_button.startAnimation(scaleUp)
        }, 800)
    }
}
