package com.example.android.popularmovies.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.android.popularmovies.Utils.MovieUtils;
import com.example.android.popularmovies.database.MovieDao;
import com.example.android.popularmovies.database.MovieDatabase;
import com.example.android.popularmovies.remote.MovieApiService;
import com.example.android.popularmovies.remote.MovieRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {
    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    Application providesApplication() {
        return application;
    }

    // --- Database Injection ---
    @Provides
    @Singleton
    MovieDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, MovieDatabase.class, "movieDatabase.db")
                .build();
    }

    @Provides
    @Singleton
    MovieDao provideMovieDao(MovieDatabase database) {
        return database.movieDao();
    }

    // -- Repository Injection ---

    @Provides
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @Singleton
    MovieRepository provideUserRepository(MovieApiService apiService, MovieDao movieDao,
                                          Executor executor) {
        return new MovieRepository(apiService, movieDao, executor);
    }

    // --- Network Injection ---

    @Provides
    Retrofit provideRetrofit() {
        // build retrofit client
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    MovieApiService provideApiWebService(Retrofit retro) {
        return retro.create(MovieApiService.class);
    }

}
