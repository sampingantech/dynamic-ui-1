package id.adeds.dynamic_ui.util

import kotlinx.serialization.json.*

val JsonElement?.handled: JsonElement?
    get() = when {
        this == null  -> JsonObject(mapOf())
        this is List<*> -> this.jsonArray
        else -> this
    }
val Boolean?.handled: Boolean
get() = when{
    this == null -> false
    else -> this
}