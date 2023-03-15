package com.example.denettree

import android.util.Log
import com.google.gson.annotations.SerializedName
import java.math.BigInteger
import java.security.MessageDigest
import java.util.UUID

class Node(

    @SerializedName("id")
    val id: String = UUID.randomUUID().toString(),

    @SerializedName("parent")
    var parent: Node? = null,

    @SerializedName("name")
    var name: String = "",

    @SerializedName("children")
    val children: MutableList<Node> = mutableListOf()
) {

    init {
        name = nodeHashCode()
    }

    fun nodeHashCode(): String {
        val md = MessageDigest.getInstance("SHA-512")
        val digest = md.digest(this.id.toByteArray())
        val bi = BigInteger(1, digest)
        var fullHash = bi.toString(2)
        var hash20Bytes = "failed to evaluate hash"
        if (fullHash.length > 160) {
            // 20 bytes = 20 * 8 bits = 160 bits
            hash20Bytes = fullHash.substring(fullHash.length - 160)
        }
        val result = BigInteger(hash20Bytes, 2).toString(16)
        //Log.d("Hamster", "nodeHashCode, $result")
        return result
    }

}