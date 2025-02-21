package com.example.cineverse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // Facebook CallbackManager
        callbackManager = CallbackManager.Factory.create()

        // Google Sign-In button
        view.findViewById<ImageButton>(R.id.btnGoogle).setOnClickListener {
            signInWithGoogle()
        }

        // Facebook Sign-In button
        view.findViewById<ImageButton>(R.id.btnFacebook).setOnClickListener {
            signInWithFacebook()
        }

        // Skip to HomeFragment
        view.findViewById<TextView>(R.id.tvSkip).setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

        view.findViewById<Button>(R.id.btnSignIn).setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
        view.findViewById<TextView>(R.id.tvSignUp).setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        view.findViewById<TextView>(R.id.tvForgotPassword).setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        return view
    }



    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleGoogleSignInResult(task)
        } else {
            Log.e("LoginFragment", "Google Sign-In failed: ${result.resultCode}")
            Toast.makeText(context, "Google girişindən imtina edildi.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            Log.e("LoginFragment", "Google Sign-In Xətası: ${e.statusCode}")
            Toast.makeText(context, "Google girişində xəta baş verdi.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                Log.e("LoginFragment", "Firebase ilə giriş uğursuz oldu: ${task.exception}")
                Toast.makeText(context, "Firebase girişində xəta: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Toast.makeText(context, "Facebook girişindən imtina edildi.", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(context, "Facebook girişində xəta: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                Toast.makeText(context, "Facebook girişində xəta: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
