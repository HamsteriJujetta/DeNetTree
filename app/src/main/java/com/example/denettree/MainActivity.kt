package com.example.denettree

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private var currentJSON: String? = null
    private var structure: Node? = null
    private var currentNode: Node? = null

    private val tvParentNodeName: TextView by lazy { findViewById(R.id.tvParentNodeName) }
    private val rvItemList: RecyclerView by lazy { findViewById(R.id.rvItemList) }
    private val adapter: ItemsAdapter by lazy {
        ItemsAdapter { processItemClick(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpData()
    }

    override fun onPause() {
        super.onPause()

        convertToJSON()
        saveJSON()
    }

    override fun onResume() {
        super.onResume()

        loadJSON()
        parseJSON()
    }


    private fun setUpData() {
        loadJSON()
        parseJSON()
        adapter.setCurrentItems(currentNode?.children ?: emptyList())
        rvItemList.adapter = adapter
        tvParentNodeName.text = currentNode?.name ?: "no..."
    }

    private fun saveJSON() {
        val sharedPrefs = applicationContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString("json", currentJSON).apply()
    }

    private fun loadJSON() {
        val sharedPrefs = applicationContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
        currentJSON = sharedPrefs.getString("json", "")
    }

    private fun parseJSON() {
        val gson = Gson()
        try {
            structure = gson.fromJson(currentJSON, Node::class.java)
        } catch (e: JsonSyntaxException) {
            Toast.makeText(applicationContext, "Failed to parse Json", Toast.LENGTH_LONG).show()
            val str = UUID.randomUUID().toString()
            currentNode = Node(
                id = str,
                parent = null,
                name = str,
                children = mutableListOf()
            )
        }
    }

    private fun convertToJSON() {
        val gson = Gson()
        currentJSON = gson.toJson(structure, Node::class.java)
    }

    private fun processItemClick(position: Int) {
        Log.d("Hamster", "clicked $position")
    }

}