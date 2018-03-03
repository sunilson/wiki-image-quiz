package at.sunilson.wiki_quiz

import android.app.Activity
import android.app.Application
import at.sunilson.wiki_quiz.di.DaggerAppComponent
import dagger.android.HasActivityInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject



/**
 * @author Linus Weiss
 */

class GameApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
        super.onCreate()
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return activityDispatchingAndroidInjector
    }
}