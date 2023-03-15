package com.example.denettree

import com.google.gson.annotations.SerializedName

class Node(

    @SerializedName("id")
    val id: String = "",

    @SerializedName("parent")
    var parent: Node? = null,

    @SerializedName("name")
    var name: String = "",

    @SerializedName("children")
    val children: MutableList<Node> = mutableListOf()
) {
}