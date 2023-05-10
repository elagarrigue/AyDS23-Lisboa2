package ayds.lisboa.songinfo.moredetails.presentation


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.MoreDetailsInjector
import com.squareup.picasso.Picasso
import java.util.*
import ayds.lisboa.songinfo.moredetails.data.*
import ayds.lisboa.songinfo.moredetails.domain.*
import ayds.lisboa.songinfo.moredetails.domain.entities.Artist
import ayds.observer.Observable
import ayds.observer.Subject

class OtherInfoView: AppCompatActivity(){
    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        const val IMAGE_LASTFM_LOGO = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    }

    private lateinit var textMoreDetails: TextView
    private lateinit var imageView: ImageView
    private lateinit var urlButton: Button

    private val onActionSubject = Subject<OtherInfoUiEvent>()
    val uiEventObservable: Observable<OtherInfoUiEvent> = onActionSubject
    var uiState: OtherInfoUiState = OtherInfoUiState()

    private lateinit var formatterInfo: FormatterInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initModule()
        initProperties()
        initListeners()
        searchAction()
    }

    private fun initListeners(){
        urlButton.setOnClickListener{ notifyOpenSongAction() }
    }

    private fun searchAction(){
        updateSearchTermState()
        notifyGetInfoAction()
    }

    private fun updateSearchTermState(){
        var artistName = intent.getStringExtra(ARTIST_NAME_EXTRA)
        artistName = artistName.toString()
        uiState = uiState.copy(searchTerm = artistName)
    }

    private fun notifyOpenSongAction(){
        onActionSubject.notify(OtherInfoUiEvent.OpenInfoUrl)
    }
    private fun notifyGetInfoAction(){
        onActionSubject.notify(OtherInfoUiEvent.GetInfo)
    }

    private fun initModule() {
        MoreDetailsInjector.init(this)
    }

    private fun initProperties(){
        textMoreDetails = findViewById(R.id.textMoreDetails)
        imageView = findViewById(R.id.imageView)
        urlButton = findViewById(R.id.openUrlButton)
    }

   fun updateViewInfo(artistInfo: Artist){
        val info = formatterInfo.getInfoFromArtistInfo(artistInfo)
        val infoHtml = formatterInfo.textToHtml(info)
        setTextInfoView(infoHtml)
    }

    @Suppress("DEPRECATION")
    private fun setTextInfoView(info: String?) {
        runOnUiThread {
            val picasso =  Picasso.get()
            val requestCreator = picasso.load(IMAGE_LASTFM_LOGO)
            requestCreator.into(imageView)
            textMoreDetails.text = Html.fromHtml(info)
        }
    }

    fun setFormatterInfo(formatterInfo: FormatterInfo){
        this.formatterInfo = formatterInfo
    }

    fun openExternalLink(songUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(songUrl)
        startActivity(intent)
    }
}