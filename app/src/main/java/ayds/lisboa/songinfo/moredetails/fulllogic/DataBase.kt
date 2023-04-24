package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val ARTIST_TABLE = "artist"
private const val ID_COLUMN = "id"
private const val ARTIST_COLUMN = "artist"
private const val INFO_COLUMN = "info"
private const val SOURCE_COLUMN = "source"

private const val createTableArtists: String =
    "create table $ARTIST_TABLE ("+
            "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "$ARTIST_COLUMN string, "+
            "$INFO_COLUMN string, "+
            "$SOURCE_COLUMN integer)"

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "dictionary.db"

internal class DataBase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val projection = arrayOf(
        ID_COLUMN,
        ARTIST_COLUMN,
        INFO_COLUMN
        )
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTableArtists)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    fun saveArtist(dbHelper: DataBase, artist: String, info: String) {
        val dataBase = dbHelper.writableDatabase
        val values = createValuesOfArtist(artist, info)
        dataBase.insert("artists", null, values)
    }

    private fun createValuesOfArtist(artist: String, info: String): ContentValues {
        val values = ContentValues()
        values.put(ARTIST_COLUMN, artist)
        values.put(INFO_COLUMN, info)
        values.put(SOURCE_COLUMN, 1)
        return values
    }
    fun getInfo(dbHelper: DataBase, artist: String): String? {
        val cursor = getCursor(dbHelper, artist)
        val itemsOfCursor = map(cursor)
        return itemsOfCursor.getOrNull(0) ?: null
    }

    private fun getCursor(dbHelper: DataBase, artist: String): Cursor {
        val dataBase = dbHelper.readableDatabase
        return dataBase.query(
            ARTIST_TABLE,
            projection,
            "$ARTIST_COLUMN  = ?",
            arrayOf(artist),
            null,
            null,
            "$ARTIST_COLUMN DESC"
        )
    }

    private fun map(cursor: Cursor): List<String> {
        val itemsOfCursor: MutableList<String> = ArrayList()
        while (cursor.moveToNext()) {
            val info = cursor.getString(cursor.getColumnIndexOrThrow(INFO_COLUMN))
            itemsOfCursor.add(info)
        }
        cursor.close()
        return itemsOfCursor
    }
}