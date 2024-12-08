package com.afrinacapstone.ecocraft.ui.profile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afrinacapstone.ecocraft.R

class EditPasswordActivity : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var oldPasswordInput: EditText
    private lateinit var newPasswordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_password)

        backButton = findViewById(R.id.back_button)
        oldPasswordInput = findViewById(R.id.old_password_input)
        newPasswordInput = findViewById(R.id.new_password_input)
        confirmPasswordInput = findViewById(R.id.confirm_password_input)
        saveButton = findViewById(R.id.button_save_password)

        backButton.setOnClickListener {
            onBackPressed()
        }

        saveButton.setOnClickListener {
            val oldPassword = oldPasswordInput.text.toString()
            val newPassword = newPasswordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            if (oldPassword.isEmpty()) {
                showToast("Password lama tidak boleh kosong")
            } else if (newPassword.isEmpty()) {
                showToast("Password baru tidak boleh kosong")
            } else if (confirmPassword.isEmpty()) {
                showToast("Konfirmasi password tidak boleh kosong")
            } else if (newPassword != confirmPassword) {
                showToast("Password baru dan konfirmasi password tidak cocok")
            } else {
                changePassword(oldPassword, newPassword)
            }
        }
    }

    private fun changePassword(oldPassword: String, newPassword: String) {
        showToast("Password berhasil diubah!")
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
