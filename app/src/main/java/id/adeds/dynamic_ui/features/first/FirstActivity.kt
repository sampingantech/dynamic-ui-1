package id.adeds.dynamic_ui.features.first

import android.app.Activity
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FirstActivity : AppCompatActivity(R.layout.activity_first), FormInterface,
    FullScreenDialog.DialogListener, ViewOnDebounceListener {
    private val viewModel: FirstViewModel by viewModel()
    private lateinit var questionAdapter: TaskSubmissionAdapter
    private lateinit var chooseFilePhotoDialog: ChooseFilePhotoDialog

    private var data = ArrayList<DynamicView>()
    private var criteriaSubmissions = ArrayList<CriteriaSubmission>()
    private var uploadFile = 0
    private var submissionId: Int? = null

    private lateinit var question: Form

    override fun onStart() {
        super.onStart()

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener{ onBackPressed() }

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
        questionAdapter = TaskSubmissionAdapter { widget, key ->  itemClicked(widget, key) }
        recyclerFormSubmission.apply {
            layoutManager = LinearLayoutManager(this@FirstActivity)
            adapter = questionAdapter
        }
        viewModel.getReposFromGitHub()
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
        nestedScrollView.visible = true
        questionAdapter.setQuestions(this.data)
    }

    private fun createCriteriaSubmissions(data: ArrayList<DynamicView>): List<CriteriaSubmission> {
        return data.filter { it.uiSchemaRule.uiHelp != null }
            .map { CriteriaSubmission(it.jsonSchema.title, it.uiSchemaRule.uiHelp, it.uiSchemaRule.uiHelpImage) }
    }

    private fun createDynamicView(data: Form): ArrayList<DynamicView> {
        val dynamicViews = ArrayList<DynamicView>()
        val properties =
            data.questionSchema?.properties?.let { Converts.convertMapToJsonObject(it) }
        val uiSchema = data.uiSchema?.let { Converts.convertMapToJsonObject(it) }
        properties?.forEach { entry ->
            val jsonRule = provideJsonSchema(entry.value.jsonObject)
            val uiSchemaRule = provideUiSchema(uiSchema?.get(entry.key)?.jsonObject?: JsonObject(
                mapOf()))
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
            val base64 = if (uri != null) viewModel.convertUriToBase64(this, uri) else null
            if (base64 == null)
                Toast.makeText(this, "File tidak dapat di proses", Toast.LENGTH_SHORT).show()
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

}