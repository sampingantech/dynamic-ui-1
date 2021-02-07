package id.adeds.dynamic_ui.data.model

data class DynamicView(
    val componentName: String,
    val jsonSchema: JsonSchemaRule,
    val uiSchemaRule: UiSchemaRule,
    val answerSchemaRule: AnswerSchemaRule?,
    val isRequired: Boolean,
    var isValidated: Boolean = true,
    var value: Any? = null,
    var fileName: String? = null,
    var preview: Any? = null,
    var isError: Boolean = false,
    var errorValue: String? = null,
    var isEnable: Boolean = true
)