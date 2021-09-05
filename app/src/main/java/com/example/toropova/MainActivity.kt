package com.example.toropova

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.toropova.retrofit.ApiService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy { ApiService.create() }
    private lateinit var gif: ImageView
    private lateinit var  description:TextView
    private lateinit var  error:TextView
    private var size= 0
    var curIndex=0
    var gifs= ArrayList<Post>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gif=findViewById(R.id.iv_image)
        description=findViewById(R.id.tv_description)
        error=findViewById(R.id.tv_error)
        val fabBack: View =findViewById(R.id.fab_back)
        val fabForward:View=findViewById(R.id.fab_forward)
if(size==0){
        runBlocking {
            withContext(Dispatchers.IO){
            addPost()}
        }
        setGif()}

        fabBack.setOnClickListener { view->
            curIndex--
            setGif()
        }
        fabForward.setOnClickListener { view->
            runBlocking {
                withContext(Dispatchers.IO){
                    addPost()}
            }
            curIndex++
            setGif()
        }
    }

    private suspend fun addPost() {
        try {
            val post=apiService.getRandomGif()
            gifs.add(post)
            size++
        }
        catch (e:Exception){
            Log.d("exception1",e.toString())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("curIndex",curIndex)
        outState.putParcelableArrayList("gifs",gifs)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        curIndex=savedInstanceState.getInt("curIndex")
        gifs= savedInstanceState.getParcelableArrayList("gifs")!!
        size=gifs.size
        setGif()
    }
    fun setGif(){
        val fab=findViewById<FloatingActionButton>(R.id.fab_back)
        if(curIndex==0)
            fab.setEnabled(false)
        else fab.setEnabled(true)
        if(size>curIndex){
            error.visibility=View.GONE
            val circularProgressDrawable=CircularProgressDrawable(this)
            circularProgressDrawable.strokeWidth=5f
            circularProgressDrawable.centerRadius=30f
            circularProgressDrawable.start()

            val postgif=gifs.get(curIndex)
            description.text = postgif.description
            Glide.with(this)
                .asGif()
                .load(postgif.gifURL)
                .placeholder(circularProgressDrawable)
                .into(gif)
        }else{error.visibility=View.VISIBLE}
    }
}

@GlideModule
class MyGlideModule:AppGlideModule()