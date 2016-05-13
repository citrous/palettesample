package jp.citrous.palettesample

import android.os.Bundle
import jp.citrous.tool.ExternalAppLauncher
import android.content.Intent
import android.app.Activity
import android.widget.ImageView
import android.support.v7.graphics.Palette
import android.util.Log
import android.graphics.BitmapFactory
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
            Log.i("Swatch RGB List", convertColorCodeToString(swatch.rgb))
        }

        drawColumns(palette)
    }

    fun drawColumns(palette: Palette) {
        playWithColor(findViewById(R.id.normal_vibrant) as? TextView, palette.vibrantSwatch)
        playWithColor(findViewById(R.id.normal_muted) as? TextView, palette.mutedSwatch)
        playWithColor(findViewById(R.id.light_vibrant) as? TextView, palette.lightVibrantSwatch)
        playWithColor(findViewById(R.id.light_muted) as? TextView, palette.lightMutedSwatch)
        playWithColor(findViewById(R.id.dark_vibrant) as? TextView, palette.darkVibrantSwatch)
        playWithColor(findViewById(R.id.dark_muted) as? TextView, palette.darkMutedSwatch)
    }

    fun playWithColor(textView: TextView?, swatch: Swatch?) {
        if (swatch == null) return
        textView?.apply {
            text = convertColorCodeToString(swatch.rgb)
            setTextColor(swatch.bodyTextColor)
            setBackgroundColor(swatch.rgb)
        }
    }

    fun convertColorCodeToString(colorCode: Int): String {
        return java.lang.String.format("%06x", colorCode)
    }
}
