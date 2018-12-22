package com.example.android.popularmovies.di;

import com.example.android.popularmovies.Utils.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, ActivityModule.class, FragmentModule.class,
        AppModule.class, ViewModelModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(App application);
        Builder appModule(AppModule appModule);
        AppComponent build();
    }

    void inject(App app);
}
