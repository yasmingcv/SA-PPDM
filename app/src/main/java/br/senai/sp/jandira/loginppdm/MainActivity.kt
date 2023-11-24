package br.senai.sp.jandira.loginppdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import br.senai.sp.jandira.loginppdm.screens.LoginScreen

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            LoginScreen(lifecycleScope)
        }

    }

}


