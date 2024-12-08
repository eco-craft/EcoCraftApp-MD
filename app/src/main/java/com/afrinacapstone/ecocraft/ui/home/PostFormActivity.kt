package com.afrinacapstone.ecocraft.ui.home

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.afrinacapstone.ecocraft.R

class PostFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_post_form)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton: ImageView = findViewById(R.id.back_button)
        val galleryButton: Button = findViewById(R.id.button_gallery)
        val cameraButton: Button = findViewById(R.id.button_camera)
        val titleInput: EditText = findViewById(R.id.title_input)
        val materialInput: EditText = findViewById(R.id.material_input)
        val descriptionInput: EditText = findViewById(R.id.description_input)
        val saveButton: Button = findViewById(R.id.button_save)

        backButton.setOnClickListener {
            onBackPressed()
        }

        galleryButton.setOnClickListener {
        }

        cameraButton.setOnClickListener {
        }

        saveButton.setOnClickListener {
            val title = titleInput.text.toString()
            val materials = materialInput.text.toString()
            val description = descriptionInput.text.toString()
        }
    }
}
