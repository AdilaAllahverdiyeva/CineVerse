package com.example.cineverse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class ResetPasswordFragment : Fragment() {

    private lateinit var codeEditText: TextInputEditText
    private lateinit var btnNext: Button
    private lateinit var btnResend: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reset_password, container, false)

        codeEditText = view.findViewById(R.id.codeEditText)
        btnNext = view.findViewById(R.id.btnNext)
        btnResend = view.findViewById(R.id.btnResend)

        auth = FirebaseAuth.getInstance()

        // Emaili argumentdən almaq
        email = arguments?.getString("email") ?: ""

        btnNext.setOnClickListener {
            val enteredCode = codeEditText.text.toString()

            if (enteredCode.isEmpty()) {
                Toast.makeText(requireContext(), "Kod daxil edin!", Toast.LENGTH_SHORT).show()
            } else {
                verifyCode(enteredCode)
            }
        }

        btnResend.setOnClickListener {
            resendVerificationCode(email)
        }

        return view
    }

    private fun verifyCode(code: String) {
        auth.verifyPasswordResetCode(code)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("ResetPassword", "Kod uğurla təsdiqləndi.")
                    Toast.makeText(requireContext(), "Kod düzgün daxil edilib!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_resetPasswordFragment_to_homeFragment)
                } else {
                    val exception = task.exception
                    Log.e("ResetPassword", "Xəta: ${exception?.message}", exception)
                    if (exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(requireContext(), "Yanlış kod!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Xəta: ${exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    private fun resendVerificationCode(email: String) {
        if (email.isEmpty()) {
            Toast.makeText(requireContext(), "Email tapılmadı!", Toast.LENGTH_SHORT).show()
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Yeni kod e-poçt ünvanınıza göndərildi.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Kod göndərilməsində xəta: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
