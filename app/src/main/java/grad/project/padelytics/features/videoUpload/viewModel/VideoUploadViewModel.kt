package grad.project.padelytics.features.videoUpload.viewModel
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VideoUploadViewModel : ViewModel() {
    private val _videoUri = MutableStateFlow<Uri?>(null)
    val videoUri: StateFlow<Uri?> = _videoUri

    private val _permissionGranted = MutableStateFlow(false)
    val permissionGranted: StateFlow<Boolean> = _permissionGranted

    fun updatePermissionStatus(granted: Boolean) {
        _permissionGranted.value = granted
    }

    fun selectVideo(uri: Uri?) {
        viewModelScope.launch {
            _videoUri.value = uri
            // You can add additional processing here
        }
    }
}