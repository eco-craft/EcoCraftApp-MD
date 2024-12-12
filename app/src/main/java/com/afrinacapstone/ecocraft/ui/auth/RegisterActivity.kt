package com.afrinacapstone.ecocraft.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextWatcher
import android.util.TypedValue
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.afrinacapstone.ecocraft.R
import com.afrinacapstone.ecocraft.databinding.ActivityRegisterBinding
import com.afrinacapstone.ecocraft.util.Resource
import com.afrinacapstone.ecocraft.util.loadingDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.truncate

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loadingDialog = loadingDialog(this)

        viewModel.registerUiState.observe(this) {
            when (it) {
                is Resource.Loading -> loadingDialog.show()

                is Resource.Success -> {
                    loadingDialog.dismiss()

                    binding.apply {
                        edtEmail.text?.clear()
                        edtPassword.text?.clear()
                        edtName.text?.clear()
                    }

                    showDialog(
                        getString(R.string.registration_successful),
                        getString(R.string.your_account_has_been_created_successfully_please_login_to_continue),
                        onPositiveButtonClick = {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    )
                }

                is Resource.Error -> {
                    loadingDialog.dismiss()
                    showDialog(
                        title = getString(R.string.something_went_wrong),
                        message = it.message.toString()
                    )
                }

                else -> loadingDialog.dismiss()
            }
        }

        binding.setupViews()
    }

    private fun ActivityRegisterBinding.setupViews() {
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

        txtSignInHere.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonRegister.setOnClickListener {
            var formValid = true

            if (edtEmail.text.toString().isEmpty()) {
                edtEmail.error = getString(R.string.field_is_required, "Email")
                formValid = false
            }

            if (edtPassword.text.toString().isEmpty()) {
                edtPassword.error = getString(R.string.field_is_required, "Password")
                formValid = false
            }

            if (edtName.text.toString().isEmpty()) {
                edtName.error = getString(R.string.field_is_required, "Name")
                formValid = false
            }


            if (formValid)
                viewModel.register(
                    email = edtEmail.text.toString().trim(),
                    password = edtPassword.text.toString().trim(),
                    name = edtName.text.toString().trim(),
                )

        }
    }

    private fun showDialog(
        title: String,
        message: String,
        onPositiveButtonClick: (() -> Unit)? = null
    ) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                onPositiveButtonClick?.invoke()
                dialog.dismiss()
            }
            .show()
    }

}
