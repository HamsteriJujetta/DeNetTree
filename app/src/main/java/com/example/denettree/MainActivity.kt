package com.example.denettree

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
    private val ivGoToParentNode: ImageView by lazy { findViewById(R.id.ivGoToParentNode) }

    private val btnAddChild: Button by lazy { findViewById(R.id.btnAddChild) }
    private val btnDeleteTree: Button by lazy { findViewById(R.id.btnDeleteTree) }

    private val rvItemList: RecyclerView by lazy { findViewById(R.id.rvItemList) }
    private val adapter: ItemsAdapter by lazy {
        ItemsAdapter(
            { processItemClick(it) },
            { processItemDelete(it) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAddChild.setOnClickListener {
            processAddItem()
        }
        btnDeleteTree.setOnClickListener {
            applicationContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply()
            structure = Node()
            currentNode = structure
            convertToJSON()
            updateAdapter()
        }
        ivGoToParentNode.setOnClickListener {
            processGoToParentNode()
        }

        setUpData()
    }


    private fun setUpData() {
        parseJSON()
        updateAdapter()
    }

    private fun saveJSON() {
        val sharedPrefs = applicationContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString("json", currentJSON).apply()
        //Log.d("Hamster", "saveJSON, currentJSON = $currentJSON")
    }

    private fun loadJSON() {
        val sharedPrefs = applicationContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
        currentJSON = sharedPrefs.getString("json", "") ?: ""
        //Log.d("Hamster", "loadJSON, currentJSON = $currentJSON")
    }

    private fun parseJSON() {
        loadJSON()
        val gson = Gson()
        try {
            val list = gson.fromJson(currentJSON, Array<Node>::class.java)
            if (list?.size == 1) {
                structure = list[0]
                currentNode = structure
                setParentNodes(currentNode)
                //Log.d("Hamster", "parseJSON OK, currentNode = ${currentNode.name}, ${currentNode.children}")
            } else {
                structure = Node()
                currentNode = structure
                //Log.d("Hamster", "parseJSON, oh noes")
            }
        } catch (e: JsonSyntaxException) {
            Toast.makeText(applicationContext, "Failed to parse Json", Toast.LENGTH_LONG).show()
            //Log.d("Hamster", "parseJSON failed")
            structure = Node()
            currentNode = structure
        }
    }

    private fun convertToJSON() {
        val nodeAdapter = NodeSerializer()
        val gsonBuilder = GsonBuilder().registerTypeAdapter(Node::class.java, nodeAdapter)
        val gson = gsonBuilder.create()
        currentJSON = "[${gson.toJson(structure)}]"
        //Log.d("Hamster", "convertToJSON, currentJSON = $currentJSON")
        saveJSON()
    }

    private fun setParentNodes(parentNode: Node) {
        for (node in parentNode.children) {
            setParentNodes(node)
            node.parent = parentNode
        }
    }

    private fun updateAdapter() {
        adapter.setCurrentItems(currentNode.children)
        rvItemList.adapter = adapter
        if (currentNode.parent == null) {
            tvParentNodeName.text = "Root:\n${currentNode.name}"
        } else {
            tvParentNodeName.text = "Child:\n${currentNode.name}"
        }
    }

    private fun processItemClick(position: Int) {
        currentNode = currentNode.children[position]
        updateAdapter()
    }

    private fun processAddItem() {
        currentNode.children.add(
            Node(parent = currentNode)
        )
        updateAdapter()
        convertToJSON()
    }

    private fun processItemDelete(position: Int) {
        currentNode.children.removeAt(position)
        updateAdapter()
        convertToJSON()
    }

    private fun processGoToParentNode() {
        val parent = currentNode.parent
        if (parent != null) {
            currentNode = parent
        }
        updateAdapter()
    }

}