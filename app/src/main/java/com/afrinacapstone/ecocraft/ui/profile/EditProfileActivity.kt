package com.afrinacapstone.ecocraft.ui.profile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afrinacapstone.ecocraft.R

class EditProfileActivity : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var profileImage: ImageView
    private lateinit var editProfilePicture: ImageView
    private lateinit var nameInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var addressInput: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        backButton = findViewById(R.id.back_button)
        profileImage = findViewById(R.id.profile_image)
        editProfilePicture = findViewById(R.id.edit_profile_picture)
        nameInput = findViewById(R.id.name_input)
        phoneInput = findViewById(R.id.phone_input)
        emailInput = findViewById(R.id.email_input)
        addressInput = findViewById(R.id.address_input)
        saveButton = findViewById(R.id.button_save_profile)

        backButton.setOnClickListener {
            onBackPressed()
        }

        editProfilePicture.setOnClickListener {
        }

        saveButton.setOnClickListener {
            val name = nameInput.text.toString()
            val phone = phoneInput.text.toString()
            val email = emailInput.text.toString()
            val address = addressInput.text.toString()

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
                showToast("Please fill out all fields")
            } else {
                updateProfile(name, phone, email, address)
            }
        }
    }

    private fun updateProfile(name: String, phone: String, email: String, address: String) {
        showToast("Profile updated successfully!")
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
