package com.apad.graffiti

import android.content.Intent
import com.apad.graffiti.MainActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity() {
    // global variable for loginState. If loginState is true then user has logged in successfully
    var loginState:Boolean = false
    fun login(email: String, password: String) {
        // Instantiate the RequestQueue, url, and stringRes
        val queue = Volley.newRequestQueue(this)
        val url = "https://apad-streetart.herokuapp.com/api/login"
        var stringRes = ""
        var string_data: String = "{\"email\": \"$email\", \"password\": \"$password\"}"
        Log.i("string", string_data)        //creating the Json Request
        val jsonRequest = object: JsonObjectRequest(Request.Method.POST, url, JSONObject(string_data), Response.Listener {
                response -> try {
                // getting the response back from volley and converting it into a string for conditional
                val volleyRes =  response.optString("status")
                stringRes =volleyRes.toString()
                // Creating a String for loginState for troubleshooting purposes
                var loginString = loginState.toString()
                Log.i("Sent", stringRes)
                Log.i("Before", loginString)
                val state = "logged"
                // Checking if the stringRes contains the correct string to show the account exists if not then the login failed
                if (stringRes.contains(state)) {
                    Log.i("loginsuccess", "yeah it works")
                    Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_SHORT).show()
                    loginState = true
                } else {
                    Log.i("loginfail", "No")
                    loginState = false
                    Toast.makeText(applicationContext, "Login Unsuccessful. Please check email and password", Toast.LENGTH_SHORT).show()                }
                // Creating a String for loginState for troubleshooting purposes
                loginString = loginState.toString()
                Log.i("After", loginString)
            } catch(e: JSONException) {
            e.printStackTrace()
            }
            }, Response.ErrorListener { error ->
                Log.i("This is the error", "Error :" + error.toString())
            })
        {
            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        queue.add(jsonRequest)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)        // setting global variable to false to make user logs in
        loginState = false        // gets information  from the editable Text
        var etEmail = findViewById<EditText>(R.id.et_email)
        var etPassword = findViewById<EditText>(R.id.et_password)        // Login Button
        btn_submit.setOnClickListener {
            // gets the information from the editable text variable and does some validation.
            // after validation is complete then it performs the login function
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if ( email !="" && password !="") {
                    login(email, password)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please fill out all fields",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }        // Next button
        btn_next.setOnClickListener {
            // allows you to login and go to the main page with a logged in user
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val successText = "You are logged in"
            val failText = "You did not login"
            val duration = Toast.LENGTH_SHORT            // Creating a String for loginState for troubleshooting purposes
            var loginString =loginState.toString()
            Log.i("Button press", loginString)
            val correct_login = loginState
            if (correct_login) {
                Toast.makeText(applicationContext, successText, duration).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("Email", email)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, failText, duration).show()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }        }    }
}