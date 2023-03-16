package com.example.denettree

import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

class Node(
    val id: String = UUID.randomUUID().toString(),
    var parent: Node? = null,
    var name: String = "",
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