package id.adeds.dynamic_ui.util

import id.adeds.dynamic_ui.data.model.AnswerSchemaRule
import id.adeds.dynamic_ui.data.model.JsonSchemaRule
import id.adeds.dynamic_ui.data.model.UiSchemaRule
import kotlinx.serialization.json.*

fun provideJsonSchema(jsonRule: JsonObject) =
    JsonSchemaRule(
        title = jsonRule["title"]?.jsonPrimitive?.content?:"",
        type = jsonRule["type"]?.jsonPrimitive?.content?:"",
        minimum = jsonRule["minimum"]?.jsonPrimitive?.int,
        maximum = jsonRule["maximum"]?.jsonPrimitive?.int,
        items = jsonRule["items"]?.jsonObject,
        enum = jsonRule["enum"]?.jsonArray
    )

fun provideUiSchema(uiRule: JsonObject) =
    UiSchemaRule(
        order = uiRule["ui:order"]?.jsonPrimitive?.int,
        uiTitle = uiRule["ui:title"]?.jsonPrimitive?.content,
        uiWidget = uiRule["ui:widget"]?.jsonPrimitive?.content,
        uiHelp = uiRule["ui:help"]?.jsonPrimitive?.content,
        uiHelpImage = uiRule["ui:help_image"]?.jsonPrimitive?.content,
        uiPlaceholder = uiRule["ui:placeholder"]?.jsonPrimitive?.content,
        uiDescription = uiRule["ui:description"]?.jsonPrimitive?.content,
        uiOptions = uiRule["ui:options"]?.jsonObject
    )

fun provideAnswerSchema(answerRule: JsonElement?, answerStatusRule: JsonObject?): AnswerSchemaRule =
    AnswerSchemaRule(
        answer = answerRule?.handled,
        status = answerStatusRule?.get("status")?.jsonPrimitive?.content,
        rejectionReason = answerStatusRule?.get("rejectionReason")?.jsonPrimitive?.content
    )