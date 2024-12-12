package com.afrinacapstone.ecocraft.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.TextViewCompat
import com.afrinacapstone.ecocraft.MainActivity
import com.afrinacapstone.ecocraft.R
import com.afrinacapstone.ecocraft.databinding.ActivityLoginBinding
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.loadingDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.truncate

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.setupViews()

        val loadingDialog = loadingDialog(this)

        viewModel.loginUiState.observe(this) {
            when (it) {
                is Resource.Loading -> loadingDialog.show()

                is Resource.Success -> {
                    Log.d("LoginActivity", "login success: ${it.data}")
                    loadingDialog.dismiss()

                    binding.apply {
                        edtEmail.text?.clear()
                        edtPassword.text?.clear()
                    }

                    viewModel.saveUserLoginStatus(true)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                is Resource.Error -> {
                    Log.d("LoginActivity", "login error: ${it.message}")
                    loadingDialog.dismiss()
                    showErrorDialog(getString(R.string.something_went_wrong), it.message.toString())
                }

                else -> loadingDialog.dismiss()
            }
        }
    }

    private fun ActivityLoginBinding.setupViews() {

        val maxTextScale = edtEmail.textSize
        val minTextScale = 0.2 * maxTextScale
        edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val paint = TextPaint(edtEmail.paint)
                val desiredTextWidth = StaticLayout.getDesiredWidth(s, paint)

                val ensureWiggleRoom = 0.95F
                val scaleFactor = edtEmail.width / desiredTextWidth
                val candidateTextSize = truncate(edtEmail.textSize * scaleFactor * ensureWiggleRoom)
                if (candidateTextSize > minTextScale && candidateTextSize < maxTextScale) {
                    edtEmail.setTextSize(TypedValue.COMPLEX_UNIT_PX, candidateTextSize)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnSignIn.setOnClickListener {
            var formValid = true

            if (edtEmail.text.toString().trim().isEmpty()) {
                edtEmail.error = getString(R.string.field_is_required, "Email")
                formValid = false
            }

            if (edtPassword.text.toString().trim().isEmpty()) {
                edtPassword.error = getString(R.string.field_is_required, "Password")
                formValid = false
            }

            if (formValid)
                viewModel.login(
                    email = edtEmail.text.toString().trim(),
                    password = edtPassword.text.toString().trim(),
                )


        }

        txtSignUpHere.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showErrorDialog(title: String, message: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}
