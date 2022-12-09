package com.example.projo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import org.json.JSONObject

class activity_signup : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        //find ids
        val name=findViewById<EditText>(R.id.name)
        val email=findViewById<EditText>(R.id.email)
        val password=findViewById<EditText>(R.id.password)
        val confirm=findViewById<EditText>(R.id.confirm_password)
        val phone=findViewById<EditText>(R.id.phone)
        val signup=findViewById<Button>(R.id.signup)
        val progress=findViewById<ProgressBar>(R.id.progress_bar)
        //hide the progress bar
        progress.visibility= View.GONE//on opening the signup,the bar should be gone
        //just paste
        //loopj
        signup.setOnClickListener {
            //start the progress bar
            progress.visibility=View.VISIBLE//show bar
            val client=AsyncHttpClient(true,80,443)
            val body= JSONObject()
            //Access the details inserted by user-values from edit text
            //put the details in body of json object
            body.put("Name",name.text.toString())
            body.put("Email",email.text.toString())
            body.put("Password",password.text.toString())
            body.put("Confirm",confirm.text.toString())
            body.put("Phone",phone.text.toString())

            val con_body = StringEntity(body.toString())
            //https://0chieng.pythonanywhere.com/signup
            client.get(this,"https://0chieng.pythonanywhere.com/signup",con_body,
            "application/json",
            object: JsonHttpResponseHandler(){
                //create a function for on success
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONObject?
                ) {
                    //check if status code is correct(200)
                    if (statusCode==200){
                        Toast.makeText(applicationContext,"you are registered successfully",
                        Toast.LENGTH_LONG).show()
                        val i=Intent(applicationContext,signin::class.java)
                        startActivity(i)
                    }//end if
                    else{
                        Toast.makeText(applicationContext,"please try again",
                            Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(statusCode: Int, headers: Array<out Header>?,
                    throwable: Throwable?,
                    errorResponse: JSONObject?
                ) {
                    progress.visibility=View.GONE
                    Toast.makeText(applicationContext,"something went wrong",
                        Toast.LENGTH_LONG).show()
                }
            })
        }

    }
}