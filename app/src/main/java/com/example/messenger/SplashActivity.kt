package com.example.messenger

import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

// #1

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val logo:ImageView = findViewById(R.id.iv_logo)
        logo.alpha = 0f

        requestPermissions(
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.CALL_PHONE,
            ),1
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0] == PERMISSION_GRANTED && grantResults[1] == PERMISSION_GRANTED){
            val logo:ImageView = findViewById(R.id.iv_logo)
            logo.animate().setDuration(3000).alpha(1f).withEndAction{

                if(FirebaseAuth.getInstance().currentUser != null){
                    startActivity(Intent(this,MainActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                    finish()
                }else{
                    startActivity(Intent(this,MainActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                    finish()
                }
            }
        }else{
            Toast.makeText(this,"PERMISSION NOT GRANTED",Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}