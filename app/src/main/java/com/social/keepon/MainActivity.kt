package com.social.keepon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
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

        //com

    }

    private lateinit var logoutbtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


      //logoutbtn = findViewById(R.id.main_btn_logout)
//       // logoutbtn.setOnClickListener{
//         //   FirebaseAuth.getInstance().signOut()
//           // startActivity(Intent(this, LoginActivity::class.java))
//            //finish()
//        }


    }
}