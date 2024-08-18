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
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName) // 文件名
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png") // 文件类型
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyApp") // 存储路径
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



    // 获取用户目录中的 PNG 文件并转换为 Drawable 列表
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
        // 获取用户文件夹
        val userDir = getUserDirectory(context, account)

        // 找到保存的文件（例如最后保存的文件）
        val savedFile = File(userDir, "my.png") // 替换为实际文件名

        if (savedFile.exists()) {
            // 从文件中读取 Bitmap
            val bitmap = loadBitmapFromFile(savedFile.absolutePath)

            // 将 Bitmap 显示在 ImageView 中
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap)
            } else {
                Log.e("LoadBitmap", "Failed to load bitmap from file.")
            }
        } else {
            Log.e("LoadBitmap", "File does not exist.")
        }
    }
    fun loadBitmapFromFile(filePath: String): Bitmap? {
        return try {
            BitmapFactory.decodeFile(filePath)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    // 获取用户的文件夹
    fun getUserDirectory(context: Context, account: String): File {
        val userDir = File(context.getExternalFilesDir(null), account)
        if (!userDir.exists()) {
            userDir.mkdirs()  // 如果文件夹不存在，则创建
        }
        return userDir
    }

    // 生成唯一的文件名
    fun generateUniqueFileName(): String {
        // 获取当前时间戳
        val timestamp = System.currentTimeMillis()

        // 将时间戳转换为日期格式 yyyyMMdd_HHmmss
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val date = Date(timestamp)
        val formattedDate = sdf.format(date)

        // 使用格式化的日期生成唯一文件名
        return "my$formattedDate.png"
    }

    // 将 DrawView 保存到指定路径
    fun saveDrawViewToUserFolder(context: Context, drawView: View, account: String) {
        val userDir = getUserDirectory(context, account)
        val fileName = generateUniqueFileName()
        val filePath = File(userDir, fileName).absolutePath

        val bitmap = getBitmapFromView(drawView)
        saveBitmapToFile(bitmap, filePath)

        Log.d("SaveDrawView", "DrawView saved to $filePath")
    }

    // 获取 View 的 Bitmap
    fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    // 保存 Bitmap 到文件
    fun saveBitmapToFile(bitmap: Bitmap, filePath: String) {
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