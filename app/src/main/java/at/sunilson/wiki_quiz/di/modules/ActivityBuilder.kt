package at.sunilson.wiki_quiz.di.modules

import at.sunilson.wiki_quiz.MainActivity
import at.sunilson.wiki_quiz.game.GameActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector



/**
 * @author Linus Weiss
 */

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = arrayOf(MainActivityModule::class))
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = arrayOf(GameActivityModule::class))
    abstract fun bindGameActivity() : GameActivity
}