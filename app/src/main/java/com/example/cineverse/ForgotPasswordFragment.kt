package com.example.cineverse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.cineverse.ForgotPasswordFragmentDirections
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordFragment : Fragment() {

    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var btnReset: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forgot_password, container, false)

        auth = FirebaseAuth.getInstance()

        emailInputLayout = view.findViewById(R.id.emailInputLayout)
        btnReset = view.findViewById(R.id.btnReset)

        btnReset.setOnClickListener {
            val eEmail = emailInputLayout.editText?.text.toString()

            if (eEmail.isNotEmpty() && isValidEmail(eEmail)) {
                auth.sendPasswordResetEmail(eEmail)
                    .addOnSuccessListener {
                        Toast.makeText(context, "E-poçtunuzu yoxlayın!", Toast.LENGTH_SHORT).show()
                        val action =
                            ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToResetPasswordFragment(
                                eEmail
                            )
                        findNavController().navigate(action)
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Xəta: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(context, "Etibarlı e-poçt ünvanı daxil edin", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    // E-poçt formatını yoxlamaq üçün funksiya
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
