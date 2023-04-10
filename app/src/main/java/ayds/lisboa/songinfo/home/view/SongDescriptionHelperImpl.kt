package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.home.model.entities.Song.EmptySong
import ayds.lisboa.songinfo.home.model.entities.Song
import ayds.lisboa.songinfo.home.model.entities.Song.SpotifySong
import java.text.DateFormatSymbols

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl : SongDescriptionHelper {
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Date: ${CalculatorDate.getDate(song)}"
            else -> "Song not found"
        }
    }
}

interface IMonths {
    fun months(): List<String>
}

abstract class Converter {
    abstract fun converter(date: String): String
}

object ConverterDay : Converter() {
    override fun converter(date: String): String {
        return date.replace("-", "/").split("/").reversed().joinToString(separator = "/")
    }
}
object ConverterMonth : Converter(), IMonths {
    override fun converter(date: String): String {
        val month = date.split("-")[1].toInt() - 1
        val monthName = months()[month]
        val year = date.split("-").first()
        return "$monthName, $year"
    }

    override fun months(): List<String> {
        val symbols = DateFormatSymbols()
        var listmonths = symbols.months.filter { it.isNotEmpty() }
        return listmonths.map { it.replaceFirstChar { char -> char.uppercase() } }
    }
}

object ConverterYear : Converter() {
    override fun converter(date: String): String {
        val year = date.split("-").first().toInt()
        var result: String

        if( (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0) )
            result = "$year (leap year)"
        else
            result = "$year (not a leap year)"

        return result
    }
}

object CalculatorDate {
    fun getDate(song: SpotifySong): String =
        when(song.releaseDatePrecision){
            "day" -> ConverterDay.converter(song.releaseDate)
            "month" -> ConverterMonth.converter(song.releaseDate)
            "year" -> ConverterYear.converter(song.releaseDate)
            else -> "Date not found"
        }
}