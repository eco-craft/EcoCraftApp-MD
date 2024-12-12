package com.afrinacapstone.ecocraft

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.afrinacapstone.ecocraft.databinding.ActivityMainBinding
import com.afrinacapstone.ecocraft.util.gone
import com.afrinacapstone.ecocraft.util.saveImageAndGetPath
import com.afrinacapstone.ecocraft.util.visible
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.UUID

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var imageFromCameraUri: Uri? = null
    private var imagePath: String? = null

    private lateinit var navController: NavController

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNavigation

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_home, R.id.nav_detection, R.id.nav_profile -> navView.visible()
                else -> navView.gone()
            }

        }
    }

    fun takePictureFromCamera() {
        createImageFile().also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this, "${packageName}.fileprovider", it
            )
            imageFromCameraUri = photoURI
            getImagePhotoResult.launch(photoURI)
        }
    }

    private fun createImageFile(): File {
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/EcoCraft")
        return File.createTempFile(
            UUID.randomUUID().toString(), ".jpg", storageDir
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            val path = absolutePath
            imagePath = path

            mainViewModel.setImagePathFromCamera(path)
        }
    }

    fun openGalleryPicker() {
        val i = Intent(
            Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        getImageGaleryPickerResult.launch(i)
    }

    private var getImagePhotoResult = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) {
        if (it) {
            mainViewModel.setImageGalleryData(
                ImageData(imageFromCameraUri, true, imagePath)
            )
        }
    }

    private var getImageGaleryPickerResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult? ->
        if (result?.resultCode == RESULT_OK) {
            val uri = result.data?.data!!
            saveImageAndGetPath(uri) { path ->
                mainViewModel.setImageGalleryData(ImageData(uri, true, path))
            }
        }
    }
}
