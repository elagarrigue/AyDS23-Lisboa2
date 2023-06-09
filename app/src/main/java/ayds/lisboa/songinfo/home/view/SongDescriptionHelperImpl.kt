package ayds.lisboa.songinfo.home.view

import DateConverterFactory
import Converter
import ayds.lisboa.songinfo.home.model.entities.Song.EmptySong
import ayds.lisboa.songinfo.home.model.entities.Song
import ayds.lisboa.songinfo.home.model.entities.Song.SpotifySong

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl(private val dateConverter: DateConverterFactory) : SongDescriptionHelper {

    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong -> {
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Date: ${song.getReleaseDate()}"
            }
            else -> "Song not found"
        }
    }

    private fun getCalculatorPrecision(precision: String): Converter{
        return dateConverter.create(precision)
    }  

    private fun SpotifySong.getReleaseDate(): String{
        val converter = getCalculatorPrecision(releaseDatePrecision)
        return converter.getReleaseDate(releaseDate)
    }
}

