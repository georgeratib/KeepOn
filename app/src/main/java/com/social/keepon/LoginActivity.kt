package com.social.keepon

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Patterns
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {
    private lateinit var emailTil: TextInputLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var passwordTil: TextInputLayout
    private lateinit var Login_Btn: Button
    private lateinit var Register_Tv: TextView
    private var doubleBackToExit = false
    override fun onBackPressed() {
        if (doubleBackToExit) {
            super.onBackPressed()
            return
        }
        doubleBackToExit = true
        Toast.makeText(this, "Press Again To Exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({
            doubleBackToExit = false

        }, 2000)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
        emailTil = findViewById(R.id.Login_input_layout_Email)
        passwordTil = findViewById(R.id.Login_input_layout_Password)
        Login_Btn = findViewById(R.id.Login_Btn_Login)
        Register_Tv = findViewById(R.id.Login_Tv_Register)
        Login_Btn.setOnClickListener {
            login()
        }
        Register_Tv.setOnClickListener {
            openRegisterActivity()
        }

    }


    private fun validateEmail(): Boolean {
        val email = emailTil.editText!!.text.toString().trim()
        if (email.isEmpty()) {
            emailTil.error = "Field Can't be Empty"
            return false

        }
        // ملاحظة 79
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTil.error = "Invalid Email"
            return false
        }

        emailTil.error = null
        return true
    }

    private fun validatePassword(): Boolean {
        val password = passwordTil.editText!!.text.toString().trim()


        if (password.isEmpty()) {
            passwordTil.error = "Field Can't be Empty"
            return false
        }

        if (password.length < 6) {
            passwordTil.error = "Password is too short (Min: 6 Character)"
        }


        // ملاحظة 111 & 112
        passwordTil.error = null

        return true
    }

    fun login() {
        Register_Tv.isEnabled= false
        if (!validateEmail() or !validatePassword())
            return
        findViewById<ProgressBar>(R.id.login_progress_bar).visibility = ViewGroup.VISIBLE
        auth.signInWithEmailAndPassword(
            emailTil.editText!!.text.toString(),
            passwordTil.editText!!.text.toString()

        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser!!.isEmailVerified) {
                        checkGenderSavedOrNot()
                        Register_Tv.isEnabled = true
                    } else {
                        findViewById<ProgressBar>(R.id.login_progress_bar).visibility =
                            ViewGroup.INVISIBLE
                        AlertDialog.Builder(this)
                            .setTitle("Email Verification")
                            .setMessage("Please Verify Your Email Address.\n A Verification Link has been sent to Your Email Address")
                            .setPositiveButton(
                                "OK",
                                DialogInterface.OnClickListener { dialogInterface, i ->
                                    dialogInterface.dismiss()

                                })
                            .setCancelable(false)
                            .create()
                            .show()

                    }
                }
            }
            .addOnFailureListener {
                Register_Tv.isEnabled = true
                AlertDialog.Builder(this)
                    .setTitle("Attention")
                    .setMessage("${it.message}")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()

                    })
                    .setCancelable(false)
                    .create()
                    .show()
                findViewById<ProgressBar>(R.id.login_progress_bar).visibility = ViewGroup.INVISIBLE


            }


    }

    private fun checkGenderSavedOrNot() {
        var user = auth.currentUser!!
        var username = user.displayName!!
        databaseRef.child("users")
            .child(user.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var userGender = snapshot.child("gender").value.toString()
                    if (userGender == "none") {
                        val intent = Intent(this@LoginActivity, GenderSelectionActivity::class.java)
                        intent.putExtra("name", username)
                        intent.putExtra("uid", user.uid)
                        startActivity(intent)
                        finish()
                    } else {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        Toast.makeText(this@LoginActivity, "Welcome To KeepOn", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    fun openRegisterActivity() {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }

    private fun userForgetPassword() {
        val forgotDialog = ForgotPassword()
        //forgotDialog.show(supportFragmentManager,"Forgot Password Dialog")
    }

    override fun onStart() {

        super.onStart()
        var user = auth.currentUser
        if (user!=null&&user.isEmailVerified){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}