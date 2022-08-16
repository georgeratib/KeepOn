package com.social.keepon

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var emailTil : TextInputLayout
    private lateinit var passwordTil : TextInputLayout
    private lateinit var Login_Btn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailTil = findViewById(R.id.Login_input_layout_Email)
        passwordTil = findViewById(R.id.Login_input_layout_Password)
        Login_Btn = findViewById(R.id.Login_Btn_Login)
        Login_Btn.setOnClickListener{
            loginUser()
        }

    }

     private fun loginUser(){
         val email = emailTil.editText!!.text.toString().trim().toLowerCase()
         val password  = passwordTil.editText!!.text.toString()

         when{
             TextUtils.isEmpty(email) -> emailTil.error = "Email is Required"
             TextUtils.isEmpty(password) -> passwordTil.error = "Password is Required"

             else -> {
                 val progressDialog = ProgressDialog(this)
                 progressDialog.setTitle("Login")
                 progressDialog.setMessage("Please Wait, This May Take a while...")
                 progressDialog.setCanceledOnTouchOutside(false)
                 progressDialog.show()


                 val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
                 mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                     if (task.isSuccessful){
                         progressDialog.dismiss()

                         var intent = Intent(this, MainActivity::class.java)
                         startActivity(intent)
                         finish()
                     }

                     else{
                         var message = task.exception!!.toString()
                         Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                         FirebaseAuth.getInstance().signOut()
                         progressDialog.dismiss()
                     }
                 }
             }
         }



     }



}