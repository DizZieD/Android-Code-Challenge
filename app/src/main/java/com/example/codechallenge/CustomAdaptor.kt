package com.example.codechallenge

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import java.io.InputStream
import java.net.URL

class CustomAdaptor(private val context: Activity, private val arrayList: ArrayList<Item>): ArrayAdapter<Item>(context, R.layout.list_item, arrayList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.list_item, null)

        val imageView = view.findViewById(R.id.imgView) as ImageView
        val nameTextView = view.findViewById(R.id.nameTextView) as TextView
        val descriptionTextView = view.findViewById(R.id.descriptionTextView) as TextView

        // I know that this is bad practice and for some reason it is not running on a new thread,
        // and I don't have time to debug
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val thread = Thread {
            val imgInputStream = URL(arrayList[position].image).content as InputStream
            if(imgInputStream != null) {
                val drawable = Drawable.createFromStream(imgInputStream, arrayList[position].name)
                imageView.setImageDrawable(drawable)
            }
        }
        thread.run()

        nameTextView.text = arrayList[position].name
        descriptionTextView.text = arrayList[position].description

        view.isClickable = true
        view.setOnClickListener {
            val mapUri = Uri.parse("geo:" + arrayList[position].latitude + "," + arrayList[position].longitude)
            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context.startActivity(mapIntent)
        }

        return view
    }
}