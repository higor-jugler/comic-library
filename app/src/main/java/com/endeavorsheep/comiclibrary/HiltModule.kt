package com.endeavorsheep.comiclibrary

import android.content.Context
import androidx.room.Room
import com.endeavorsheep.comiclibrary.model.api.ApiService
import com.endeavorsheep.comiclibrary.model.api.MarvelApiRepo
import com.endeavorsheep.comiclibrary.model.db.CharacterDao
import com.endeavorsheep.comiclibrary.model.db.CollectionDb
import com.endeavorsheep.comiclibrary.model.db.CollectionDbRepo
import com.endeavorsheep.comiclibrary.model.db.CollectionDbRepoImpl
import com.endeavorsheep.comiclibrary.model.db.Constants.DB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {
    @Provides
    fun provideApiRepo() = MarvelApiRepo(ApiService.api)

    @Provides
    fun provideCollectionDb(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, CollectionDb::class.java, DB).build()

    @Provides
    fun provideCharacterDao(characterDao: CharacterDao) : CollectionDbRepo =
        CollectionDbRepoImpl(characterDao)
}