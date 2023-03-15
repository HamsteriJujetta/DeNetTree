package com.example.denettree

import android.content.Context
import android.os.Bundle
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
    private var operationsCount = 0 // TODO !!!

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
    }

    private fun loadJSON() {
        val sharedPrefs = applicationContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
        currentJSON = sharedPrefs.getString("json", "") ?: ""
    }

    private fun parseJSON() {
        loadJSON()
        val gson = Gson()
        try {
            val list = gson.fromJson(currentJSON, Array<Node>::class.java)
            if (list?.size == 1) {
                structure = list[0]
                setParentNodes(currentNode)
            } else {
                structure = Node()
            }
        } catch (e: JsonSyntaxException) {
            Toast.makeText(applicationContext, "Failed to parse Json", Toast.LENGTH_LONG).show()
            structure = Node()
        }
    }

    private fun convertToJSON() {
        val nodeAdapter = NodeSerializer()
        val gsonBuilder = GsonBuilder().registerTypeAdapter(Node::class.java, nodeAdapter)
        val gson = gsonBuilder.create()
        currentJSON = "[${gson.toJson(structure)}]"
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
        operationsCount++
        currentNode.children.add(
            Node(parent = currentNode)
        )
        updateAdapter()
        if (operationsCount == OPERATIONS_LIMIT) {
            operationsCount = 0
            convertToJSON()
        }
    }

    private fun processItemDelete(position: Int) {
        operationsCount++
        currentNode.children.removeAt(position)
        updateAdapter()
        if (operationsCount == OPERATIONS_LIMIT) {
            operationsCount = 0
            convertToJSON()
        }
    }

    private fun processGoToParentNode() {
        val parent = currentNode.parent
        if (parent != null) {
            currentNode = parent
        }
        updateAdapter()
    }

}