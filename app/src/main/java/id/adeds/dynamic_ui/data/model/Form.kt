package id.adeds.dynamic_ui.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Form(
    @SerialName("project_id")
    val projectId: Int? = 0,
    @SerialName("question_schema")
    val questionSchema: QuestionSchema? = QuestionSchema(),
    @SerialName("ui_schema")
    val uiSchema: Map<String, JsonElement>? = null,
    @SerialName("max_submission")
    val maxSubmission: Int? = 0,
    @SerialName("approval_type")
    val approvalType: String? = "",
    @SerialName("prefered_answer")
    val preferedAnswer: PreferedAnswer? = PreferedAnswer(),
    @SerialName("criteria_schema")
    val criteriaSchema: CriteriaSchema? = CriteriaSchema(),
    /*@SerialName("unique_questions")
    val uniqueQuestions: List<Any?>? = listOf(),*/
    @SerialName("has_assignment")
    val hasAssignment: Boolean? = false,
    @SerialName("id")
    val id: Int? = 0,
    @SerialName("created_at")
    val createdAt: String? = "",
    @SerialName("updated_at")
    val updatedAt: String? = "",
    @SerialName("project_title")
    val projectTitle: String? = ""
) {
    @Serializable
    data class QuestionSchema(
        @SerialName("type")
        val type: String? = "",
        @SerialName("title")
        val title: String? = "",
        @SerialName("required")
        val required: List<String?>? = listOf(),
        @SerialName("properties")
        val properties: Map<String, JsonElement>? = null,
        @SerialName("description")
        val description: String? = ""
    )


    @Serializable
    class PreferedAnswer(
    )

    @Serializable
    class CriteriaSchema(
    )
}