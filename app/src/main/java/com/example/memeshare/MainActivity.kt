package com.example.memeshare

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var currentImageUrl: String?= null
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Change Colour of Action bar
        val actionBar: ActionBar?
        actionBar = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#FFBB86FC"))
        actionBar!!.setBackgroundDrawable(colorDrawable)

        //Change Colour of Status Bar
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.yellow)

        }

        //Change Colour of Text of Status Bar
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR: make the text of status bar to black


        loadMeme()
    }


    private fun loadMeme(){
        progressbar.visibility=View.VISIBLE
        val url = "https://meme-api.herokuapp.com/gimme"

       val jsonObjectRequest=JsonObjectRequest(
           Request.Method.GET, url, null,
           Response.Listener { response ->
               currentImageUrl = response.getString("url")
               Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable> {
                   override fun onLoadFailed(
                       e: GlideException?,
                       model: Any?,
                       target: Target<Drawable>?,
                       isFirstResource: Boolean
                   ): Boolean {
                       progressbar.visibility = View.GONE
                       return false
                   }

                   override fun onResourceReady(
                       resource: Drawable?,
                       model: Any?,
                       target: Target<Drawable>?,
                       dataSource: DataSource?,
                       isFirstResource: Boolean
                   ): Boolean {
                       progressbar.visibility = View.GONE
                       return false
                   }
               }).into(memeImageView)
           },
           Response.ErrorListener {
               Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
           })
MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }


    fun shareMeme(view: View) {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Ye dank meme dekh lavde $currentImageUrl"
        )
        val chooser=Intent.createChooser(intent, "Share this meme using...")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}