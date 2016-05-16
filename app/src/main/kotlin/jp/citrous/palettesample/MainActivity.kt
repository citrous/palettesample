package jp.citrous.palettesample

import android.os.Bundle
import jp.citrous.tool.ExternalAppLauncher
import android.content.Intent
import android.app.Activity
import android.widget.ImageView
import android.support.v7.graphics.Palette
import android.util.Log
import android.graphics.BitmapFactory
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.support.v7.graphics.Palette.Swatch
import java.io.FileNotFoundException

/**
 * Created by citrous on 2015/03/07.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById(R.id.button)?.setOnClickListener {
            ExternalAppLauncher.openPhotoChooser(this, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            resultCode == Activity.RESULT_OK && data != null ->
                loadImageFromUri(data)
            else ->
                return
        }
    }

    fun loadImageFromUri(data: Intent) {
        val bitmap = try {
            contentResolver.openInputStream(data.data);
        } catch(e: FileNotFoundException) {
            e.printStackTrace()
            null
        }?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream);
        } ?: return

        (findViewById(R.id.image) as? ImageView)?.setImageBitmap(bitmap)
        Palette.Builder(bitmap).generate { extractColors(it) }
    }

    fun extractColors(palette: Palette?) {
        if (palette == null) return

        for (swatch in palette.swatches) {
            Log.i("Swatch RGB List", swatch.rgb.convertToStringColorCodeOrNull())
        }

        drawColumns(palette)
    }

    fun drawColumns(palette: Palette) {
        playWithColor(findTextView(R.id.normal_vibrant), palette.vibrantSwatch)
        playWithColor(findTextView(R.id.normal_muted), palette.mutedSwatch)
        playWithColor(findTextView(R.id.light_vibrant), palette.lightVibrantSwatch)
        playWithColor(findTextView(R.id.light_muted), palette.lightMutedSwatch)
        playWithColor(findTextView(R.id.dark_vibrant), palette.darkVibrantSwatch)
        playWithColor(findTextView(R.id.dark_muted), palette.darkMutedSwatch)
    }

    fun playWithColor(textView: TextView?, swatch: Swatch?) {
        if (swatch == null) return
        textView?.apply {
            text = swatch.rgb.convertToStringColorCodeOrNull()
            setTextColor(swatch.bodyTextColor)
            setBackgroundColor(swatch.rgb)
        }
    }

    fun Int.convertToStringColorCodeOrNull(): String? = try {
        java.lang.String.format("%06x", this)
    } catch (e: Exception) { null }

    fun findTextView(@LayoutRes resId: Int): TextView? = findViewById(resId) as? TextView
}
