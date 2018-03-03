package at.sunilson.wiki_quiz.di

import android.app.Application
import at.sunilson.wiki_quiz.GameApplication
import at.sunilson.wiki_quiz.di.modules.ActivityBuilder
import at.sunilson.wiki_quiz.di.modules.AppModule
import at.sunilson.wiki_quiz.di.modules.RepositoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton



/**
 * @author Linus Weiss
 */

@Singleton
@Component(modules = arrayOf(AndroidSupportInjectionModule::class, AppModule::class, RepositoryModule::class, ActivityBuilder::class))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent

    }

    fun inject(app: GameApplication)

}