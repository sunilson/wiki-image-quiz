package at.sunilson.wiki_quiz.shared

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

/**
 * Created by linus on 25.02.2018.
 */

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}