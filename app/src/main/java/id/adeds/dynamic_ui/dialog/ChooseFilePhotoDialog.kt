package id.adeds.dynamic_ui.dialog

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.adeds.dynamic_ui.R
import id.adeds.dynamic_ui.util.*
import kotlinx.android.synthetic.main.dialog_file_photo.view.*

class ChooseFilePhotoDialog(
    val activity: Activity,
    private val imageCallback: (requestCode: Int, key: String, uri: Uri?) -> Unit
): AccessFile {
    private val imageUtils = ImageUtils(activity)
    private lateinit var bottomDialog: BottomSheetDialog
    private var imageUri: Uri? = null
    private var dialogRequestCode: Int = 0
    private var key: String = ""
    private var fileType = ""
    private val sharedPref = activity.getSharedPreferences("ChooseFile", Context.MODE_PRIVATE)

    companion object {
        const val REQUEST_PERMISSION_CAMERA = 1
        const val REQUEST_PERMISSION_GALLERY = 2
        const val REQUEST_PERMISSION_FOLDER = 3
        const val REQUEST_GALLERY = 100
        const val REQUEST_CAMERA = 101
        const val REQUEST_FOLDER = 102
        const val PREF_CHOOSE_FILE = "prefChooseFile"
        const val PREF_IMAGE_URI = "prefImageUri"
    }

    @SuppressLint("InflateParams")
    fun show(widget:String, key: String, requestCode: Int = 0) {
        saveLastChooseFile(key)
        var accessGallery = true
        var accessCamera = true
        dialogRequestCode = requestCode
        this.key = key
        bottomDialog = BottomSheetDialog(activity)
        val layoutChoosePhoto = LayoutInflater.from(activity).inflate(R.layout.dialog_file_photo, null)
        bottomDialog.setContentView(layoutChoosePhoto)
        bottomDialog.show()
        bottomDialog.fullExpanded(layoutChoosePhoto)
        when (widget) {
            "file" -> {
                fileType = "*/*"
                accessCamera = false
                accessGallery = true
            }
            "camera" -> {
                accessCamera = true
                accessGallery = false
            }
            "photo" -> {
                fileType = "image/*"
                accessCamera = true
                accessGallery = true
            }
        }
        layoutChoosePhoto.frameCamera.setOnClickListener { launchCamera() }
        layoutChoosePhoto.frameGallery.setOnClickListener {
            if (widget == "file") launchFolder() else launchGallery()
        }
        layoutChoosePhoto.buttonClosePopupSelectPhoto.setOnClickListener { bottomDialog.dismiss() }
        layoutChoosePhoto.frameCamera.visible = imageUtils.isDeviceSupportCamera && accessCamera
        layoutChoosePhoto.frameGallery.visible = accessGallery
    }

    private fun launchCamera() {
        if (Permission.checkPermission(activity, Manifest.permission.CAMERA, REQUEST_PERMISSION_CAMERA) &&
            Permission.checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSION_CAMERA)
        ) {
            val values = ContentValues()
            imageUri = activity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            setLastChooseImageUri(imageUri)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            activity.startActivityForResult(intent, REQUEST_CAMERA)
            bottomDialog.dismiss()
        }
    }

    private fun launchGallery() {
        if (Permission.checkPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSION_GALLERY)) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = fileType
            activity.startActivityForResult(intent, REQUEST_GALLERY)
            bottomDialog.dismiss()
        }
    }

    private fun launchFolder() {
        if (Permission.checkPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSION_FOLDER)) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = fileType
            activity.startActivityForResult(intent, REQUEST_FOLDER)
            bottomDialog.dismiss()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_CAMERA -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    launchCamera()
                } else {
                    Toast.makeText(activity,"You don't have permission to access camera", Toast.LENGTH_SHORT).show()
                }
                return
            }
            REQUEST_PERMISSION_GALLERY -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    launchGallery()
                } else {
                    Toast.makeText(activity,"You don't have permission to access external storage", Toast.LENGTH_SHORT).show()
                }
                return
            }
            REQUEST_PERMISSION_FOLDER -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    launchFolder()
                } else {
                    Toast.makeText(activity,"You don't have permission to access external storage", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CAMERA -> if (resultCode == Activity.RESULT_OK) {
                onSelectFromCameraResult()
            }
            REQUEST_GALLERY -> if (resultCode == Activity.RESULT_OK) {
                onSelectFromGalleryResult(data)
            }
            REQUEST_FOLDER -> if (resultCode == Activity.RESULT_OK) {
                onSelectFromGalleryResult(data)
            }
        }
    }

    private fun onSelectFromGalleryResult(data: Intent?) {
        val selectedData = data?.data
        if (selectedData != null) {
            try {
                val selectedPath = FileUtils.getRealPath(activity, selectedData)
                if (selectedPath == null) {
                    Toast.makeText(
                        activity,
                        "File tidak dapat dilampirkan, pastikan file ada di media penyimpanan lokal",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                returnIntent(selectedData)
            } catch (e: Exception) {
                Log.e("selectFromGallery", "${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun onSelectFromCameraResult() {
        try {
            if (imageUri == null)
                imageUri = getLastChooseImageUri()
            returnIntent(imageUri)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("selectFromCamera", "${e.message}")
        }
    }

    private fun returnIntent(uri: Uri?) {
        if (key.isEmpty())
            key = getLastChooseFile()
        imageCallback.invoke(dialogRequestCode, key, uri)
    }

    private fun saveLastChooseFile(key: String) {
        sharedPref.edit().putString(PREF_CHOOSE_FILE, key).apply()
    }

    private fun getLastChooseFile(): String {
        return sharedPref.getString(PREF_CHOOSE_FILE, "").orEmpty()
    }

    private fun setLastChooseImageUri(uri: Uri?) {
        sharedPref.edit().putString(PREF_IMAGE_URI, uri.toString()).apply()
    }

    private fun getLastChooseImageUri(): Uri {
        return Uri.parse(sharedPref.getString(PREF_IMAGE_URI, ""))
    }
}

interface AccessFile {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

    }
}