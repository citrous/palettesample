package jp.citrous.palettesample

import android.support.v7.app.ActionBarActivity
import android.os.Bundle
import jp.citrous.tool.ExternalAppLauncher
import android.content.Intent
import android.app.Activity
import android.widget.ImageView
import android.support.v7.graphics.Palette
import android.util.Log
import android.graphics.BitmapFactory
import android.widget.TextView
import android.support.v7.graphics.Palette.Swatch
import java.io.FileNotFoundException

/**
 * Created by citrous on 2015/03/07.
 */
public class MainActivity : ActionBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById(R.id.button).setOnClickListener {
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
        val inputStream = try {
            getContentResolver().openInputStream(data.getData());
        } catch(e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
        if (inputStream == null) return
        val bitmap = BitmapFactory.decodeStream(inputStream);
        inputStream.close();
        (findViewById(R.id.image) as ImageView).setImageBitmap(bitmap)
        Palette.generateAsync(bitmap, { palette -> extractColors(palette) })
    }

    fun extractColors(palette: Palette?) {
        if (palette == null) return

        for (swatch in palette.getSwatches()) {
            Log.i("Swatch RGB List", convertColorCodeToString(swatch.getRgb()))
        }

        drawColumns(palette)
    }

    fun drawColumns(palette: Palette) {
        playWithColor(findViewById(R.id.normal_vibrant) as TextView, palette.getVibrantSwatch())
        playWithColor(findViewById(R.id.normal_muted) as TextView, palette.getMutedSwatch())
        playWithColor(findViewById(R.id.light_vibrant) as TextView, palette.getLightVibrantSwatch())
        playWithColor(findViewById(R.id.light_muted) as TextView, palette.getLightMutedSwatch())
        playWithColor(findViewById(R.id.dark_vibrant) as TextView, palette.getDarkVibrantSwatch())
        playWithColor(findViewById(R.id.dark_muted) as TextView, palette.getDarkMutedSwatch())
    }

    fun playWithColor(textView: TextView, swatch: Swatch?) {
        if (swatch == null) return
        textView.setText(convertColorCodeToString(swatch.getRgb()))
        textView.setTextColor(swatch.getBodyTextColor())
        textView.setBackgroundColor(swatch.getRgb())
    }

    fun convertColorCodeToString(colorCode: Int): String {
        return java.lang.String.format("%06x", colorCode)
    }
}
