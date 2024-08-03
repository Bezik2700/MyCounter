package igor.second.mycounter.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(nameEntity: NameEntity)

    @Delete
    suspend fun deleteItem(nameEntity: NameEntity)

    @Query("SELECT * FROM NameEntity")
    fun getAllItems(): Flow<List<NameEntity>>
}

@Dao
interface Dao2 {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(currency: Currency)

    @Delete
    suspend fun deleteItem(currency: Currency)

    @Query("SELECT * FROM NameEntity")
    fun getAllItems(): Flow<List<Currency>>
}