package com.endeavorsheep.comiclibrary.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.endeavorsheep.comiclibrary.comicsToString
import com.endeavorsheep.comiclibrary.model.CharacterResult
import com.endeavorsheep.comiclibrary.model.db.Constants.CHARACTER_TABLE

@Entity(tableName = CHARACTER_TABLE)
data class DbCharacter(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val apiId: Int?,
    val name: String?,
    val thumbnail: String?,
    val comics: String?,
    val description: String?
) {
    companion object {
        fun fromCharacters(character: CharacterResult) =
            DbCharacter(
                id = 0,
                apiId = character.id,
                name = character.name,
                thumbnail = character.thumbnail?.path + "." + character.thumbnail?.extension,
                comics = character.comics?.items?.mapNotNull { it.name }?.comicsToString()
                    ?: "No comics",
                description = character.description
            )
    }
}
