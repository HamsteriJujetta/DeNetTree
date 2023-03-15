package com.example.denettree

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class NodeDeserializer : JsonDeserializer<Node> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Node? {
        var result: Node? = null
        if (json != null) {
            val t = json.asJsonObject.get("id")
            var type: String = ""
            if (t != null) {
                type = t.asString

                /*when (type) {
                    ""
                }*/
            }
        }
        return result
    }
}