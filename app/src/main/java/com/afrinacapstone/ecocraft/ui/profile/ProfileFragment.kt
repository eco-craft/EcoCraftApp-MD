package com.afrinacapstone.ecocraft.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.afrinacapstone.ecocraft.R
import com.afrinacapstone.ecocraft.auth.LoginActivity
import com.afrinacapstone.ecocraft.ui.profile.EditProfileActivity

class ProfileFragment : Fragment() {

    private lateinit var profilePicture: ImageView
    private lateinit var profileName: TextView
    private lateinit var editProfileText: TextView
    private lateinit var editPasswordText: TextView
    private lateinit var logoutText: TextView
    private lateinit var editProfileLayout: LinearLayout
    private lateinit var editPasswordLayout: LinearLayout
    private lateinit var logoutLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        profilePicture = rootView.findViewById(R.id.profile_picture)
        profileName = rootView.findViewById(R.id.profile_name)
        editProfileText = rootView.findViewById(R.id.edit_profile_text)
        editPasswordText = rootView.findViewById(R.id.edit_password_text)
        logoutText = rootView.findViewById(R.id.logout_text)

        editProfileLayout = rootView.findViewById(R.id.edit_profile_layout)
        editPasswordLayout = rootView.findViewById(R.id.edit_password_layout)
        logoutLayout = rootView.findViewById(R.id.logout_layout)

        profileName.text = "Bachira"
        profilePicture.setImageResource(R.drawable.circle_background)

        editProfileLayout.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        editPasswordLayout.setOnClickListener {
            showToast("Edit Password clicked")
        }

        logoutLayout.setOnClickListener {
            logout()
        }

        return rootView
    }

    private fun logout() {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}
