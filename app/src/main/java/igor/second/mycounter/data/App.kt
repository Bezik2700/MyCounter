package igor.second.mycounter.data

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

class App: Application(){
    val database by lazy {
        MainDb.createDataBase(this)
    }
}

@Database(
    entities = [NameEntity:: class], version = 1)
abstract class MainDb: RoomDatabase() {
    abstract val dao: Dao
    companion object {
        fun createDataBase(context: Context): MainDb{
            return Room.databaseBuilder(
                context,
                MainDb::class.java,
                "MainDb"
            ).build()
        }
    }
}



