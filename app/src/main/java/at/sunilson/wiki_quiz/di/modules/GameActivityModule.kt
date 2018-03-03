package at.sunilson.wiki_quiz.di.modules

import at.sunilson.wiki_quiz.game.GamePresenter
import at.sunilson.wiki_quiz.game.GamePresenterImplementation
import dagger.Binds
import dagger.Module

/**
 * Created by linus on 24.02.2018.
 */


@Module
abstract class GameActivityModule {

    @Binds
    abstract fun bindGamePresenterImplementation(gamePresenterImplementation: GamePresenterImplementation) : GamePresenter

}


/*
@Module
class GameActivityModule {

    @Provides
    fun bindGamePresenterImplementation() : GamePresenter {
        return GamePresenterImplementation()
    }

}

        */