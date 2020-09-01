package id.adeds.dynamic_ui.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Scheme(
    @SerialName("message")
    val message: String? = "",
    @SerialName("data")
    val `data`: Data? = Data()
) {
    @Serializable
    data class Data(
        @SerialName("title")
        val title: String? = "",
        @SerialName("questionSchema")
        val questionSchema: QuestionSchema? = QuestionSchema(),
        @SerialName("questionUISchema")
        val questionUISchema: QuestionUISchema? = QuestionUISchema(),
        @SerialName("questionIsActive")
        val questionIsActive: QuestionIsActive? = QuestionIsActive()
    ) {
        @Serializable
        data class QuestionSchema(
            @SerialName("title")
            val title: String? = "",
            @SerialName("description")
            val description: String? = "",
            @SerialName("type")
            val type: String? = "",
            @SerialName("required")
            val required: List<String?>? = listOf(),
            @SerialName("properties")
            val properties: Properties? = Properties()
        ) {
            @Serializable
            data class Properties(
                @SerialName("filePicker1")
                val filePicker1: FilePicker1? = FilePicker1(),
                @SerialName("imagePicker")
                val imagePicker: ImagePicker? = ImagePicker(),
                @SerialName("radio")
                val radio: Radio? = Radio(),
                @SerialName("numberCounter")
                val numberCounter: NumberCounter? = NumberCounter(),
                @SerialName("dropdownMenu")
                val dropdownMenu: DropdownMenu? = DropdownMenu(),
                @SerialName("checkbox")
                val checkbox: Checkbox? = Checkbox(),
                @SerialName("textfield")
                val textfield: Textfield? = Textfield(),
                @SerialName("textArea")
                val textArea: TextArea? = TextArea()
            ) {
                @Serializable
                data class FilePicker1(
                    @SerialName("type")
                    val type: String? = "",
                    @SerialName("title")
                    val title: String? = "",
                    @SerialName("format")
                    val format: String? = ""
                )

                @Serializable
                data class ImagePicker(
                    @SerialName("type")
                    val type: String? = "",
                    @SerialName("title")
                    val title: String? = "",
                    @SerialName("format")
                    val format: String? = ""
                )

                @Serializable
                data class Radio(
                    @SerialName("type")
                    val type: String? = "",
                    @SerialName("title")
                    val title: String? = "",
                    @SerialName("enum")
                    val `enum`: List<String?>? = listOf()
                )

                @Serializable
                data class NumberCounter(
                    @SerialName("type")
                    val type: String? = "",
                    @SerialName("title")
                    val title: String? = ""
                )

                @Serializable
                data class DropdownMenu(
                    @SerialName("type")
                    val type: String? = "",
                    @SerialName("title")
                    val title: String? = "",
                    @SerialName("enum")
                    val `enum`: List<String?>? = listOf()
                )

                @Serializable
                data class Checkbox(
                    @SerialName("type")
                    val type: String? = "",
                    @SerialName("title")
                    val title: String? = "",
                    @SerialName("items")
                    val items: Items? = Items()
                ) {
                    @Serializable
                    data class Items(
                        @SerialName("type")
                        val type: String? = "",
                        @SerialName("enum")
                        val `enum`: List<String?>? = listOf()
                    )
                }

                @Serializable
                data class Textfield(
                    @SerialName("type")
                    val type: String? = "",
                    @SerialName("title")
                    val title: String? = "",
                    @SerialName("minLength")
                    val minLength: Int? = 0,
                    @SerialName("maxLength")
                    val maxLength: Int? = 0
                )

                @Serializable
                data class TextArea(
                    @SerialName("type")
                    val type: String? = "",
                    @SerialName("title")
                    val title: String? = "",
                    @SerialName("minLength")
                    val minLength: Int? = 0,
                    @SerialName("maxLength")
                    val maxLength: Int? = 0
                )
            }
        }

        @Serializable
        data class QuestionUISchema(
            @SerialName("filePicker1")
            val filePicker1: FilePicker1? = FilePicker1(),
            @SerialName("imagePicker")
            val imagePicker: ImagePicker? = ImagePicker(),
            @SerialName("radio")
            val radio: Radio? = Radio(),
            @SerialName("numberCounter")
            val numberCounter: NumberCounter? = NumberCounter(),
            @SerialName("dropdownMenu")
            val dropdownMenu: DropdownMenu? = DropdownMenu(),
            @SerialName("checkbox")
            val checkbox: Checkbox? = Checkbox(),
            @SerialName("textfield")
            val textfield: Textfield? = Textfield(),
            @SerialName("textArea")
            val textArea: TextArea? = TextArea()
        ) {
            @Serializable
            data class FilePicker1(
                @SerialName("ui:widget")
                val uiWidget: String? = "",
                @SerialName("ui:options")
                val uiOptions: UiOptions? = UiOptions(),
                @SerialName("ui:help")
                val uiHelp: String? = ""
            ) {
                @Serializable
                data class UiOptions(
                    @SerialName("accept")
                    val accept: String? = ""
                )
            }

            @Serializable
            data class ImagePicker(
                @SerialName("ui:widget")
                val uiWidget: String? = "",
                @SerialName("ui:options")
                val uiOptions: UiOptions? = UiOptions(),
                @SerialName("ui:help")
                val uiHelp: String? = ""
            ) {
                @Serializable
                data class UiOptions(
                    @SerialName("accept")
                    val accept: String? = ""
                )
            }

            @Serializable
            data class Radio(
                @SerialName("ui:widget")
                val uiWidget: String? = "",
                @SerialName("ui:help")
                val uiHelp: String? = ""
            )

            @Serializable
            data class NumberCounter(
                @SerialName("ui:widget")
                val uiWidget: String? = "",
                @SerialName("ui:help")
                val uiHelp: String? = ""
            )

            @Serializable
            data class DropdownMenu(
                @SerialName("ui:widget")
                val uiWidget: String? = "",
                @SerialName("ui:placeholder")
                val uiPlaceholder: String? = "",
                @SerialName("ui:help")
                val uiHelp: String? = ""
            )

            @Serializable
            data class Checkbox(
                @SerialName("ui:widget")
                val uiWidget: String? = "",
                @SerialName("ui:help")
                val uiHelp: String? = ""
            )

            @Serializable
            data class Textfield(
                @SerialName("ui:widget")
                val uiWidget: String? = "",
                @SerialName("ui:help")
                val uiHelp: String? = ""
            )

            @Serializable
            data class TextArea(
                @SerialName("ui:widget")
                val uiWidget: String? = "",
                @SerialName("ui:options")
                val uiOptions: UiOptions? = UiOptions(),
                @SerialName("ui:help")
                val uiHelp: String? = ""
            ) {
                @Serializable
                data class UiOptions(
                    @SerialName("rows")
                    val rows: Int? = 0
                )
            }
        }

        @Serializable
        data class QuestionIsActive(
            @SerialName("filePicker1")
            val filePicker1: Boolean? = false,
            @SerialName("imagePicker")
            val imagePicker: Boolean? = false,
            @SerialName("radio")
            val radio: Boolean? = false,
            @SerialName("numberCounter")
            val numberCounter: Boolean? = false,
            @SerialName("dropdownMenu")
            val dropdownMenu: Boolean? = false,
            @SerialName("checkbox")
            val checkbox: Boolean? = false,
            @SerialName("textfield")
            val textfield: Boolean? = false,
            @SerialName("textArea")
            val textArea: Boolean? = false
        )
    }
}