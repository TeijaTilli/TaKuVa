package com.example.takuukuittivarasto

import android.graphics.Bitmap
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import android.content.Context //tietokanta
import androidx.room.* //tietokanta
import androidx.room.RoomDatabase //tietokanta
import androidx.room.PrimaryKey
import com.google.android.material.textfield.TextInputLayout


@Entity
data class Kuitti(  //luodaan taulu
    @PrimaryKey val id: Int,
    @ColumnInfo(name="tuotenimi") val tuotenimi:String,
    //@ColumnInfo(name="takuupvm") val takuupvm:Date,
    @ColumnInfo(name="takuupvm") val takuupvm:Long, //pitää tallentaa longina päivämäärä
    //@ColumnInfo(name="kuva") val kuva:Bitmap //https://stackoverflow.com/questions/46337519/how-insert-image-in-room-persistence-library
    @ColumnInfo(name="kuva") val kuva: String //pitää muuttaa BLOB:ksi? https://stackoverflow.com/questions/46337519/how-insert-image-in-room-persistence-library
)

//tietokantaliitäntä jonka avullakäsitellään tietokantaa ohjelmassa
@Dao //Databaseaccess -objekti
interface TakuukuittiDBDao{
    @Query("INSERT INTO kuitti (tuotenimi, takuupvm, kuva) VALUES (:tuotenimi, :takuupvm, :kuva);")
    fun lisaaUusiKuitti(tuotenimi:String, takuupvm: Long, kuva: String)

    @Query("SELECT * FROM kuitti ORDER BY takuupvm;")
    fun haeKuitit() : List<Kuitti>

    @Query("DELETE FROM kuitti WHERE id= :id2;")
    fun poistaKuitti(id2: Int)

    @Query("SELECT * FROM kuitti WHERE id= :id;")
    fun haeYksiKuitti(id: Int): List<Kuitti> //palauttaa vain yhden kuitin

}

@Database(entities = [Kuitti::class], version = 1, exportSchema = false)
abstract class TakuukuittiDB : RoomDatabase() {
    abstract val takuukuittiDBDao:TakuukuittiDBDao
    companion object {
        @Volatile
        private var INSTANCE: TakuukuittiDB? = null
        // palautta takuukuittiDB -olion
        fun getInstance(context: Context) : TakuukuittiDB{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TakuukuittiDB::class.java,
                        "takuukuitti.db"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE=instance
                }
                return instance
            }
        }
    }
}



