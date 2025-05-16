package grad.project.padelytics.features.analysis.viewModel

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import grad.project.padelytics.features.analysis.data.FullAnalysisData
import grad.project.padelytics.features.results.data.MatchData
import grad.project.padelytics.features.results.data.PlayerInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class AnalysisViewModel: ViewModel() {
    private val _matchData = MutableStateFlow<List<MatchData>>(emptyList())
    //val matchData: StateFlow<List<MatchData>> = _matchData

    private val _isFetching = MutableStateFlow(false)
    val isFetching: StateFlow<Boolean> = _isFetching.asStateFlow()

    private val _analysisData = MutableStateFlow<FullAnalysisData?>(null)
    val analysisData: StateFlow<FullAnalysisData?> = _analysisData.asStateFlow()

    private val _players = MutableStateFlow<List<PlayerInfo>>(emptyList())
    val players: StateFlow<List<PlayerInfo>> = _players

    private val _playerFirstNames = MutableStateFlow<List<String>>(emptyList())
    val playerFirstNames: StateFlow<List<String>> = _playerFirstNames

    private val _playerPhotos = MutableStateFlow<List<String>>(emptyList())
    val playerPhotos: StateFlow<List<String>> = _playerPhotos

    private val _playerLevels = MutableStateFlow<List<String>>(emptyList())
    val playerLevels: StateFlow<List<String>> = _playerLevels

    fun saveBitmapAsPdf(context: Context, bitmap: Bitmap, filename: String = "analysis.pdf") {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        pdfDocument.finishPage(page)

        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
        }

        val collectionUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Downloads.EXTERNAL_CONTENT_URI
        } else {
            MediaStore.Files.getContentUri("external")
        }

        val uri = resolver.insert(collectionUri, contentValues)

        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                pdfDocument.writeTo(outputStream)
                Toast.makeText(context, "PDF saved!", Toast.LENGTH_SHORT).show()

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(it, "application/pdf")
                    flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                context.startActivity(Intent.createChooser(intent, "Open PDF"))
            }
        }

        pdfDocument.close()
    }

    fun captureComposableToBitmap(view: View): Bitmap {
        val bitmap = createBitmap(view.width, view.height)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun fetchMatchById(matchId: String?) {
        if (matchId == null) return

        val db = FirebaseFirestore.getInstance()
        _isFetching.value = true

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("users")
            .document(currentUserId)
            .collection("uploadedMatches")
            .document(matchId)
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) {
                    _matchData.value = emptyList()
                    _players.value = emptyList()
                    _isFetching.value = false
                    return@addOnSuccessListener
                }

                val playerIds = doc.get("players") as? List<*>
                if (playerIds == null || playerIds.size != 4) {
                    _matchData.value = emptyList()
                    _players.value = emptyList()
                    _isFetching.value = false
                    return@addOnSuccessListener
                }

                val playersInfo = MutableList<PlayerInfo?>(4) { null }
                var fetchedCount = 0

                playerIds.forEachIndexed { index, uid ->
                    db.collection("users").document(uid.toString()).get()
                        .addOnSuccessListener { userDoc ->
                            val firstName = userDoc.getString("firstName") ?: "Unknown"
                            val photo = userDoc.getString("photo") ?: ""
                            val level = userDoc.getString("level") ?: "N/A"
                            playersInfo[index] = PlayerInfo(uid.toString(), firstName, photo, level)
                            fetchedCount++

                            if (fetchedCount == 4) {
                                val finalPlayers = playersInfo.map { it!! } // safe since count == 4
                                _players.value = finalPlayers
                                _playerFirstNames.value = finalPlayers.map { it.firstName }
                                _playerPhotos.value = finalPlayers.map { it.photo }
                                _playerLevels.value = finalPlayers.map { it.level }

                                val match = MatchData(
                                    matchId = doc.id,
                                    players = finalPlayers,
                                    court = doc.getString("court") ?: "Unknown",
                                    formattedTime = doc.getString("formattedTime") ?: "",
                                    timestamp = doc.getLong("timestamp") ?: 0,
                                    matchUrl = doc.getString("matchUrl") ?: ""
                                )
                                _matchData.value = listOf(match)
                                _isFetching.value = false

                                // Load JSON if URL exists
                                if (match.matchUrl.isNotBlank()) {
                                    loadAnalysisDataFromUrl(match.matchUrl)
                                }
                            }
                        }
                        .addOnFailureListener {
                            fetchedCount++
                            if (fetchedCount == 4) {
                                _matchData.value = emptyList()
                                _players.value = emptyList()
                                _playerFirstNames.value = emptyList()
                                _playerPhotos.value = emptyList()
                                _playerLevels.value = emptyList()
                                _isFetching.value = false
                            }
                        }
                }
            }
            .addOnFailureListener {
                Log.e("MatchFetch", "Failed to fetch match by ID", it)
                _matchData.value = emptyList()
                _players.value = emptyList()
                _isFetching.value = false
            }
    }

    private suspend fun fetchJsonFromUrl(url: String?): String {
        if (url.isNullOrBlank()) return ""

        return withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connect()
                val inputStream = connection.inputStream
                inputStream.bufferedReader().use { it.readText() }
            } catch (e: Exception) {
                Log.e("FetchJSON", "Failed to load JSON from URL", e)
                ""
            }
        }
    }

    private fun loadAnalysisDataFromUrl(url: String) {
        viewModelScope.launch {
            val json = fetchJsonFromUrl(url)
            try {
                val gson = Gson()
                val type = object : TypeToken<FullAnalysisData>() {}.type
                val parsed = gson.fromJson<FullAnalysisData>(json, type)
                _analysisData.value = parsed
            } catch (e: Exception) {
                Log.e("JSON Parsing", "Error parsing JSON from URL", e)
                _analysisData.value = null
            }
        }
    }
}