package at.sunilson.wiki_quiz.di.modules

import at.sunilson.wiki_quiz.repository.WikipediaService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Singleton

/**
 * @author Linus Weiss
 */

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideOkHTTP() : OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient().newBuilder().addInterceptor(interceptor).build()
    }

    @Provides
    @Singleton
    fun provideRertofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://en.wikipedia.org/w/")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun provideWikipediaService(retrofit: Retrofit) : WikipediaService {
        return retrofit.create(WikipediaService::class.java)
    }
}