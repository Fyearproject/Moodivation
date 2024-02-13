package khushi.drashti.moodivation.view

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import khushi.drashti.moodivation.R
import khushi.drashti.moodivation.model.Quote
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class OpenQuote(var quote:Quote): Fragment() {

    var quoteTextView:TextView?=null
    var shareButton: Button?=null
    var parentLayout:ConstraintLayout?=null
    var frameLayout:FrameLayout?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.open_quote,container,false)
        quoteTextView = view.findViewById(R.id.quote)
        shareButton = view.findViewById(R.id.shareButton)
        quoteTextView!!.text = quote.quote+" \n- ${quote.author}"
        parentLayout = view.findViewById(R.id.parent_layout)
        frameLayout = view.findViewById(R.id.QuoteBackground)
        parentLayout!!.setOnClickListener {  }

        shareButton!!.setOnClickListener {
            sendViewViaMail(frameLayout!!,requireActivity(),requireContext(),quoteTextView!!.text.toString())
        }

        return view
    }

    fun sendViewViaMail(
        view: View,
        baseContext: Context?,
        activityContextOnly: Context?,
        textToMail: String?
    ) {
        view.post {
            val heightG = view.height
            val widthG = view.width
            if (baseContext != null) {
                if (activityContextOnly != null) {
                    if (textToMail != null) {
                        sendViewViaMail(view, baseContext, activityContextOnly, widthG, heightG, textToMail)
                    }
                }
            }
        }
    }

    private fun sendViewViaMail(
        view: View,
        baseContext: Context,
        activityContextOnly: Context,
        widthG: Int,
        heightG: Int,
        textToMail: String
    ) {
        val bitmap: Bitmap = createViewBitmap(view, widthG, heightG)
        var imageUri: Uri? = null
        var file: File? = null
        var fos1: FileOutputStream? = null
        try {
            val folder =
                File("/My Temp Files")
            var success = true
            if (!folder.exists()) {
                success = folder.mkdir()
            }
            val filename = "img.jpg"
            file = File(folder.getPath(), filename)
            fos1 = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos1)
            imageUri = file?.let {
                FileProvider.getUriForFile(
                    activityContextOnly,
                    activityContextOnly.packageName + ".my.package.name.provider",
                    it
                )
            }
        } catch (ex: Exception) {
        } finally {
            try {
                if(fos1!=null){
                    fos1.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val emailIntent1 = Intent(Intent.ACTION_SEND)
        emailIntent1.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        emailIntent1.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>())
        emailIntent1.putExtra(Intent.EXTRA_STREAM, imageUri)
        emailIntent1.putExtra(Intent.EXTRA_SUBJECT, "[" + "COMPANY_HEADER" + "]")
        emailIntent1.putExtra(Intent.EXTRA_TEXT, textToMail)
        emailIntent1.data = Uri.parse("mailto:" + "mail@gmail.com") // or just "mailto:" for blank
        emailIntent1.type = "image/jpg"
        activityContextOnly.startActivity(Intent.createChooser(emailIntent1, "Send email using"))
    }

    private fun createViewBitmap(view: View, widthG: Int, heightG: Int): Bitmap {
        val viewBitmap = Bitmap.createBitmap(widthG, heightG, Bitmap.Config.RGB_565)
        val viewCanvas = Canvas(viewBitmap)
        val backgroundDrawable = view.background
        if (backgroundDrawable != null) {
            backgroundDrawable.draw(viewCanvas)
        } else {
            viewCanvas.drawColor(Color.WHITE)
            view.draw(viewCanvas)
        }
        return viewBitmap
    }
}