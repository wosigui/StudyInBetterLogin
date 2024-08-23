package com.example.studyinbetterlogin.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.studyinbetterlogin.adapter.DrawableItem
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FileUtil {
    fun shareImage(context: Context, uri: Uri) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/png"
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }

    fun notifyGallery(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = uri
        context.sendBroadcast(intent)
    }
    fun saveFilePathToGallery(context: Context, filePath: String, fileName: String): Uri? {
        val file = File(filePath)
        if (!file.exists()) {
            Log.e("Error", "File not found")
            return null
        }
        val inputStream: InputStream = FileInputStream(file)
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyApp")
        }
        val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            val outputStream = context.contentResolver.openOutputStream(uri)
            inputStream.copyTo(outputStream!!)
            outputStream.close()
            inputStream.close()
        }
        return uri
    }
    fun getPngDrawablesInAccountDir(context: Context, account: String): MutableList<DrawableItem> {
        val accountDir = getUserDirectory(context, account)
        return if (accountDir.exists() && accountDir.isDirectory) {
            accountDir.listFiles { _, name ->
                name.endsWith(".png")
            }?.mapNotNull { file ->
                Drawable.createFromPath(file.absolutePath)?.let { DrawableItem(it, file.absolutePath) }
            } ?.toMutableList()?: mutableListOf()
        } else {
            mutableListOf()
        }
    }
    fun displaySavedDrawView(context: Context, account: String, imageView: ImageView) {
        val userDir = getUserDirectory(context, account)
        val savedFile = File(userDir, "my.png")
        if (savedFile.exists()) {
            val bitmap = loadBitmapFromFile(savedFile.absolutePath)
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap)
            } else {
                Log.e("LoadBitmap", "Failed to load bitmap from file.")
            }
        } else {
            Log.e("LoadBitmap", "File does not exist.")
        }
    }
    fun saveDrawViewToUserFolder(context: Context, drawView: View, account: String) {
        val userDir = getUserDirectory(context, account)
        val fileName = generateUniqueFileName()
        val filePath = File(userDir, fileName).absolutePath
        val bitmap = getBitmapFromView(drawView)
        saveBitmapToFile(bitmap, filePath)
        Log.d("SaveDrawView", "DrawView saved to $filePath")
    }
    private fun loadBitmapFromFile(filePath: String): Bitmap? {
        return try {
            BitmapFactory.decodeFile(filePath)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    // 获取用户的文件夹
    private fun getUserDirectory(context: Context, account: String): File {
        val userDir = File(context.getExternalFilesDir(null), account)
        if (!userDir.exists()) {
            userDir.mkdirs()  // 如果文件夹不存在，则创建
        }
        return userDir
    }
    private fun generateUniqueFileName(): String {
        val timestamp = System.currentTimeMillis()
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val date = Date(timestamp)
        val formattedDate = sdf.format(date)
        return "my$formattedDate.png"
    }
    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
    private fun saveBitmapToFile(bitmap: Bitmap, filePath: String) {
        val file = File(filePath)
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("SaveBitmap", "Failed to save bitmap: ${e.message}")
        }
    }
}