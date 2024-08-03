package igor.second.mycounter.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val price: String,
    val nextCurrency: String,
    val nextCategory: String,
    val showMyDialog: Boolean
)