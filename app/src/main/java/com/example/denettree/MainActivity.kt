package com.example.denettree

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import java.util.*

class MainActivity : AppCompatActivity() {

    private var currentJSON: String = ""
    private var structure: Node = Node()
    private var currentNode: Node = structure

    private val tvParentNodeName: TextView by lazy { findViewById(R.id.tvParentNodeName) }
    private val rvItemList: RecyclerView by lazy { findViewById(R.id.rvItemList) }
    private val adapter: ItemsAdapter by lazy {
        ItemsAdapter { processItemClick(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentJSON = "{\"id\":\"a32c5082-9937-44f9-b2ff-ddcc2ddfc791\",\"name\":\"a32c5082-9937-44f9-b2ff-ddcc2ddfc791\",\"children\":[{\"id\":\"123\",\"name\":\"123 name\",\"children\":[]},{\"id\":\"456\",\"name\":\"465 name\",\"children\":[]},{\"id\":\"789\",\"name\":\"789 name\",\"children\":[]}]}"
        parseJSON()
        updateAdapter()
        convertToJSON()
        //setUpData()
    }

    override fun onPause() {
        super.onPause()
        convertToJSON()
        saveJSON()
    }

    override fun onResume() {
        super.onResume()
        //setUpData()
    }


    private fun setUpData() {
        loadJSON()
        parseJSON()
        updateAdapter()
    }

    private fun saveJSON() {
        val sharedPrefs = applicationContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString("json", currentJSON).apply()
    }

    private fun loadJSON() {
        val sharedPrefs = applicationContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
        currentJSON = sharedPrefs.getString("json", "") ?: ""
        Log.d("Hamster", "loadJSON, currentJSON = $currentJSON")
    }

    private fun parseJSON() {
        val gson = Gson()
        try {
            /*if (gson.fromJson(currentJSON, Node::class.java) == null) {
                setDefaultStructure()
            }*/
            val list = gson.fromJson(currentJSON, Array<Node>::class.java)
            Log.d("Hamster", "parsed = ${list.size}")
            for (l in list) {
                Log.d("Hamster", "\tparsing ${l.name}")
            }
        } catch (e: JsonSyntaxException) {
            //Toast.makeText(applicationContext, "Failed to parse Json", Toast.LENGTH_LONG).show()
            Log.d("Hamster", "parseJSON failed ${e.message}")
            setDefaultStructure()
        }
        Log.d("Hamster", "parseJSON, currentNode = ${currentNode.name}")
    }

    private fun convertToJSON() {
        val nodeAdapter = NodeSerializer()
        val gsonBuilder = GsonBuilder().registerTypeAdapter(Node::class.java, nodeAdapter)
        val gson = gsonBuilder.create()
        currentJSON = gson.toJson(structure)
        Log.d("Hamster", "convertToJSON, currentJSON = $currentJSON")
    }

    private fun processItemClick(position: Int) {
        Log.d("Hamster", "clicked $position")
        currentNode = structure.children[position]
        updateAdapter()
    }

    private fun updateAdapter() {
        adapter.setCurrentItems(currentNode.children)
        Log.d("Hamster", "updateAdapter, children = ${currentNode.children.map { it.name }}")
        rvItemList.adapter = adapter
        tvParentNodeName.text = "Current node:\n${currentNode.name}"
    }

    private fun setDefaultStructure() {
        val str = UUID.randomUUID().toString()
        //Log.d("Hamster", "setDefaultStructure, str = $str")
        structure = Node(
            id = str,
            parent = null,
            name = str,
            children = mutableListOf()
        )
        currentNode = structure
        currentNode.children.addAll(
            mutableListOf(
                Node("default", currentNode, "default name"),
                Node("456", currentNode, "465 name"),
                Node("789", currentNode, "789 name")
            )
        )
    }

}