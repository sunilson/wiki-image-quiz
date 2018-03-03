package at.sunilson.wiki_quiz.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import javax.inject.Singleton
import dagger.Provides


/**
 * @author Linus Weiss
 */

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }
}