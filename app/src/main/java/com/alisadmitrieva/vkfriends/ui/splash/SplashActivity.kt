package com.alisadmitrieva.vkfriends.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.alisadmitrieva.vkfriends.R
import com.alisadmitrieva.vkfriends.ui.login.LoginActivity
import com.alisadmitrieva.vkfriends.ui.friends.FriendsActivity
import com.alisadmitrieva.vkfriends.utils.Config
import com.alisadmitrieva.vkfriends.utils.getToken

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.NoActionBar);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            run {
                if (getToken() != null) {
                    startActivity(Intent(this, FriendsActivity::class.java))
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
            finish()
        }, Config.SHOW_SPLASH_DELAY_MILLIS)

    }

}
