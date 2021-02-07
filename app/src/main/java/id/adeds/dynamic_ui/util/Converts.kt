package id.adeds.dynamic_ui.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.toJson

object Converts {
    private val gson = Json
    fun convertMapToJsonObject(data: Map<String, JsonElement>) : JsonObject {
        return JsonObject(data)
    }
}