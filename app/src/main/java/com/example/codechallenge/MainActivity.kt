package com.example.codechallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.codechallenge.databinding.ActivityMainBinding
import org.json.JSONArray
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var itemArray = ArrayList<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val urlAPI = "https://api.jsonbin.io/b/6151c4c94a82881d6c5666f6"
        val activity = this
        // get json file from url in another thread
        val thread = Thread {
            val data = URL(urlAPI).readText()

            if (data.isNotEmpty()) {
                var jsonArray = JSONArray(data)
                for (i in 0 until jsonArray.length()) {
                    Log.i("tag", "${jsonArray.getJSONObject(i)["name"]}")

                    val jsonObject = jsonArray.getJSONObject(i)
                    val item = Item(jsonObject["name"] as String, jsonObject["description"] as String, jsonObject["image"] as String, jsonObject["latitude"] as Double, jsonObject["longitude"] as Double)

                    itemArray.add(item)
                }
                Log.i("tag", "DONE")

                runOnUiThread(object : Runnable {
                    override fun run() {
                        binding.listView.adapter = CustomAdaptor(activity, itemArray)
                    }
                })
            }
        }
        thread.start()
    }
}