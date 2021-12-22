package com.example.moviesearch

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity() {
    lateinit var requestQueue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appnetwork = BasicNetwork(HurlStack())
        val appcache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap
        requestQueue = RequestQueue(appcache, appnetwork).apply {
            start()
        }
        findViewById<Button>(R.id.search).setOnClickListener {
            var input = findViewById<EditText>(R.id.userinput).text.toString()
            fetchData(input)
        }


    }

    fun fetchData( input: String){
        val url = "http://www.omdbapi.com/?t=${input}&apikey=e2a9a772"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                if(response.get("Response")=="False"){
                    findViewById<TextView>(R.id.name).setText("incorrect detail")
                }else {
                    Picasso.with(this).load(response.getString("Poster")).into(findViewById<ImageView>(R.id.image))
                    findViewById<TextView>(R.id.plot).setText(response.getString("Plot"))
                    findViewById<TextView>(R.id.name).setText(response.getString("Title"))
                    findViewById<TextView>(R.id.info).setText("Writer: "+response.getString("Writer")+"\n\n"+"Director: "+response.getString("Director")+"\n\n"+"Actors: "+response.getString("Actors")+"\n\n"+
                    "Release date: "+response.getString("Released")+"\n\n"+"Genre: "+response.getString("Genre"))
                }
            },
            { error ->
                Log.d("vol",error.toString())
            }
        )

        requestQueue.add(jsonObjectRequest)
    }
}