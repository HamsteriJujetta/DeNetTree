package com.example.denettree

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class NodeSerializer : TypeAdapter<Node>() {
    override fun write(jsonWriter: JsonWriter?, node: Node?) {
        if (node != null && jsonWriter != null) {
            jsonWriter.beginObject()
                ?.name("id")
                ?.jsonValue("\"" + node.id + "\"")
                ?.name("name")
                ?.jsonValue("\"" + node.name + "\"")
                ?.name("children")
                ?.beginArray()

            for (n in node.children) {
                this.write(jsonWriter, n)
            }
            jsonWriter.endArray()
                .endObject()
        }
    }

    /*private fun customWrite(jsonWriter: JsonWriter, node: Node) {
        jsonWriter.beginObject()
            ?.name("id")
            ?.jsonValue("\"" + node.id + "\"")
            ?.name("name")
            ?.jsonValue("\"" + node.name + "\"")
            ?.name("children")
            ?.beginArray()

        for (n in node.children) {
            this.write(jsonWriter, n)
        }
        jsonWriter.endArray()
            .endObject()
    }*/

    override fun read(jsonReader: JsonReader?): Node? {
        return null
    }
}