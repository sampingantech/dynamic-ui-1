package id.adeds.dynamic_ui.features.first

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import id.adeds.dynamic_ui.R
import id.adeds.dynamic_ui.adapter.TaskSubmissionAdapter
import id.adeds.dynamic_ui.data.model.CriteriaSubmission
import id.adeds.dynamic_ui.data.model.DynamicView
import id.adeds.dynamic_ui.data.model.Form
import id.adeds.dynamic_ui.dialog.ChooseFilePhotoDialog
import id.adeds.dynamic_ui.dialog.FullScreenDialog
import id.adeds.dynamic_ui.util.*
import kotlinx.android.synthetic.main.activity_first.*
import kotlinx.coroutines.launch
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.fromJson
import kotlinx.serialization.json.jsonObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FirstActivity : AppCompatActivity(R.layout.activity_first), FormInterface,
    FullScreenDialog.DialogListener, ViewOnDebounceListener {

    companion object{
        private const val SAVE_QUESTION = "saveQuestion"
        private const val SAVE_STATE_VALUES = "saveStateValue"
    }

    private val viewModel: FirstViewModel by viewModel()
    private lateinit var questionAdapter: TaskSubmissionAdapter
    private lateinit var chooseFilePhotoDialog: ChooseFilePhotoDialog

    private var data = mutableListOf<DynamicView>()
    private var criteriaSubmissions = ArrayList<CriteriaSubmission>()
    private var uploadFile = 0
    private var submissionId: Int? = null

    private lateinit var question: Form
    private var savedInstanceState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
        if (savedInstanceState == null) viewModel.getReposFromGitHub()
        else showSaveInstanceState(savedInstanceState)
    }

    private fun showSaveInstanceState(savedInstanceState: Bundle) {
        this.savedInstanceState = savedInstanceState
        try {
            val data = savedInstanceState.getString(SAVE_QUESTION)
            if (!data.isNullOrEmpty()) {
                this.question = Json.decodeFromString(data)
                setForm(question)
            }
        } catch (e: Exception) {
            viewModel.getReposFromGitHub()
        }
    }

    private fun updateDataFromSaveInstance() {
        if (savedInstanceState != null) {
            try {
                val instanceState = savedInstanceState
                val stateValues = instanceState?.getString(SAVE_STATE_VALUES)?:""
                val type: java.lang.reflect.Type = object : HashMap<String, Any>() {}.javaClass.genericSuperclass
                val serial = serializer(type) as KSerializer<HashMap<String, Any>>

                val test: Map<String, Any> = Json.decodeFromString(serial, stateValues)
                data.forEach {
                    it.value = test[it.componentName]
                    it.preview = instanceState?.get(it.componentName)
                }
            } catch (e: Exception) {
            }
        }
    }

    private fun setup() {
        flipper.displayedChild = 1
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        buttonSubmit.debouncedListener(this)
        checkBox.setOnCheckedChangeListener { _, isChecked -> buttonSubmit.isEnabled = isChecked }

        setupForm()

        viewModel.observeData().observe(this) {
            when (it) {
                is MyResult.Success -> {
                    flipper.displayedChild = 0
                    setForm(it.data)
                }
                is MyResult.Error.RecoverableError -> {
                    flipper.displayedChild = 1
                    infoText.text = it.exception.message
                }
                is MyResult.Error.NonRecoverableError -> {
                    flipper.displayedChild = 1
                    infoText.text = it.exception.message
                }
                is MyResult.Loading -> {
                    flipper.displayedChild = 1
                    infoText.text = "TUNGGU ..."
                }
            }
        }
        questionAdapter = TaskSubmissionAdapter { widget, key -> itemClicked(widget, key) }
        recyclerFormSubmission.apply {
            layoutManager = LinearLayoutManager(this@FirstActivity)
            adapter = questionAdapter
        }
    }

    private fun itemClicked(widget: String, key: String) {
        when (widget) {
            "file", "photo", "camera" -> showFileDialog(widget, key)
//            "fixedlocation" -> showLocation(key, true)
//            "dynamiclocation" -> showLocation(key)
        }
    }

    private fun setForm(questions: Form) {
        question = questions
        data = createDynamicView(questions)
        criteriaSubmissions = ArrayList(createCriteriaSubmissions(data))
        showViewSubmission()
    }

    private fun showViewSubmission() {
        updateDataFromSaveInstance()
        nestedScrollView.visible = true
        questionAdapter.setQuestions(this.data)
    }

    private fun createCriteriaSubmissions(data: MutableList<DynamicView>): List<CriteriaSubmission> {
        return data.filter { it.uiSchemaRule.uiHelp != null }
            .map {
                CriteriaSubmission(
                    it.jsonSchema.title,
                    it.uiSchemaRule.uiHelp,
                    it.uiSchemaRule.uiHelpImage
                )
            }
    }

    private fun createDynamicView(data: Form): ArrayList<DynamicView> {
        val dynamicViews = ArrayList<DynamicView>()
        val properties =
            data.questionSchema?.properties?.let { Converts.convertMapToJsonObject(it) }
        val uiSchema = data.uiSchema?.let { Converts.convertMapToJsonObject(it) }
        properties?.forEach { entry ->
            val jsonRule = provideJsonSchema(entry.value.jsonObject)
            val uiSchemaRule = provideUiSchema(
                uiSchema?.get(entry.key)?.jsonObject ?: JsonObject(
                    mapOf()
                )
            )
            val isRequired = data.questionSchema.required?.any { it == entry.key }
            dynamicViews.add(
                DynamicView(
                    componentName = entry.key,
                    jsonSchema = jsonRule,
                    uiSchemaRule = uiSchemaRule,
                    answerSchemaRule = null,
                    isRequired = isRequired.handled
                )
            )
        }
        dynamicViews.sortBy { it.uiSchemaRule.order }
        return dynamicViews
    }

    override fun setupForm() {
        chooseFilePhotoDialog = ChooseFilePhotoDialog(this) { _, key, uri ->
            onFileSelected(key, uri)
        }
    }

    private fun onFileSelected(key: String, uri: Uri?) {
        data.find { it.componentName == key }?.let {
            val base64 = if (uri != null) viewModel.convertUriToBase64(this@FirstActivity, uri) else null
            if (base64 == null)
                Toast.makeText(this@FirstActivity, "File tidak dapat di proses", Toast.LENGTH_SHORT).show()
            else {
                it.value = uri
                it.preview = uri
            }
        }
        questionAdapter.updateItem(key)
    }

    override fun showFileDialog(widget: String, key: String) {
        chooseFilePhotoDialog.show(widget, key)
    }

    override fun onDialogFinish() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onClickDebounce(view: View?) {
        when (view) {
            buttonSubmit -> submit()
        }
    }

    private fun submit() {
        data.forEach { it.isError = (it.value == null && it.isRequired) || !it.isValidated }
        val isError = data.any { it.isError }
        if (isError) {
            questionAdapter.refreshAdapter()
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
        } else {
            val files = getDataTypeFile()
            uploadFile = files.size
            files.forEach {
                if (it.value is Uri) {
                    it.value = viewModel.convertUriToBase64(this, it.value as Uri)
                }
            }

            postSubmissionData()
        }
    }

    private fun getDataTypeFile(): List<DynamicView> {
        return data.filter {
            when (it.uiSchemaRule.uiWidget) {
                "file", "photo", "camera" -> true
                else -> false
            }
        }
    }

    private fun postSubmissionData() {
        val submission = mutableMapOf<String, Any?>()
        data.forEach {
            when (val value = if (it.value == null) "" else it.value) {
                is Int -> submission[it.componentName] = value
                is String -> submission[it.componentName] = value
                is Boolean -> submission[it.componentName] = value
                is Float -> submission[it.componentName] = value
                is List<*> -> {
                    val listString = mutableListOf<String>()
                    value.forEach { item -> listString.add(item as String) }
                    submission[it.componentName] = listString
                }
                else -> submission[it.componentName] =
                    it.value.toString() //other type to string type
            }
        }
        viewModel.postSubmission(question.id, submissionId, submission)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        chooseFilePhotoDialog.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        chooseFilePhotoDialog.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val mapValues  = mutableMapOf<String, Any>()
        data.forEach {
            if (it.value != null) mapValues[it.componentName] = it.value.toString()
            if (it.preview != null) outState.putString(it.componentName, it.preview.toString())
        }
        val question = Json.encodeToString(question)
        val jsonMapValues = mapValues.toString()
        outState.putString(SAVE_QUESTION, question)
        outState.putString(SAVE_STATE_VALUES, jsonMapValues)
    }

}