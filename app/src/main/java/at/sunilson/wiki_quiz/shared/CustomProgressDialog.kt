package at.sunilson.wiki_quiz.shared

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager

/**
 * Created by linus on 28/02/2018.
 */

class CustomProgressDialog(val contet: Context) : ProgressDialog(contet) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    }

}