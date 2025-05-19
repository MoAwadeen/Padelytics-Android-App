package grad.project.padelytics.features.analysis.data

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast

object PdfExporter {
    fun exportBitmapAsPdf(context: Context, bitmap: Bitmap, filename: String = "Match Analysis.pdf") {
        try {
            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
            val page = document.startPage(pageInfo)
            val canvas = page.canvas
            canvas.drawBitmap(bitmap.copy(Bitmap.Config.ARGB_8888, false), 0f, 0f, null)
            document.finishPage(page)

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI
                else
                    MediaStore.Files.getContentUri("external"),
                contentValues
            )

            uri?.let {
                resolver.openOutputStream(it)?.use { output ->
                    document.writeTo(output)
                }

                Toast.makeText(context, "PDF saved to Downloads!", Toast.LENGTH_SHORT).show()

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(it, "application/pdf")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY)
                }
                context.startActivity(Intent.createChooser(intent, "Open PDF"))
            }

            document.close()
        } catch (e: Exception) {
            Toast.makeText(context, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
            Log.e("PDFExport", "Error", e)
        }
    }
}