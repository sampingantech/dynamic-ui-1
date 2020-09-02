package id.adeds.dynamic_ui.util

import id.adeds.dynamic_ui.data.model.AnswerSchemaRule
import id.adeds.dynamic_ui.data.model.JsonSchemaRule
import id.adeds.dynamic_ui.data.model.UiSchemaRule
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

fun provideJsonSchema(jsonRule: JsonObject) =
    JsonSchemaRule(
        title = jsonRule["title"]?.toString()!!,
        type = jsonRule["type"]?.toString()!!,
        minimum = jsonRule["minimum"]?.toString()?.toInt(),
        maximum = jsonRule["maximum"]?.toString()?.toInt(),
        items = jsonRule["items"]?.jsonObject,
        enum = jsonRule["enum"]?.jsonArray
    )

fun provideUiSchema(uiRule: JsonObject) =
    UiSchemaRule(
        order = uiRule["ui:order"]?.toString()?.toInt(),
        uiTitle = uiRule["ui:title"]?.toString(),
        uiWidget = uiRule["ui:widget"]?.toString(),
        uiHelp = uiRule["ui:help"]?.toString(),
        uiHelpImage = uiRule["ui:help_image"]?.toString(),
        uiPlaceholder = uiRule["ui:placeholder"]?.toString(),
        uiDescription = uiRule["ui:description"]?.toString(),
        uiOptions = uiRule["ui:options"]?.jsonObject
    )

fun provideAnswerSchema(answerRule: JsonElement?, answerStatusRule: JsonObject?): AnswerSchemaRule =
    AnswerSchemaRule(
        answer = answerRule?.handled,
        status = answerStatusRule?.get("status")?.toString(),
        rejectionReason = answerStatusRule?.get("rejectionReason")?.toString()
    )