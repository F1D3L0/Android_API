package com.example.projo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import org.json.JSONObject

class signin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        // find ids for different views
        //create variable to hold the views

        val email=findViewById<EditText>(R.id.email)
        val progress=findViewById<ProgressBar>(R.id.progress_bar)
        val password=findViewById<EditText>(R.id.password)
        val submit=findViewById<Button>(R.id.signin)
        progress.visibility= View.GONE// on opening sign in hide progress bar
//        submit?.setOnClickListener { Toast.makeText(this@MainActivity,
//        "you have signed in",Toast.LENGTH_LONG).show() }
        //find signup
        val signup=findViewById<TextView>(R.id.register)
        signup.setOnClickListener {
            var i = Intent(applicationContext,activity_signup::class.java)
            startActivity(i)
        }
        //sign in
        submit.setOnClickListener {
            //start the progress bar
            progress.visibility= View.VISIBLE//show progressbar
            val client= AsyncHttpClient(true,80,443)
            val body= JSONObject()
            //Access the details inserted by user-values from edit text
            //put the details in body of json object
            body.put("Email",email.text.toString())
            body.put("Password",password.text.toString())

            val con_body = StringEntity(body.toString())
            //https://0chieng.pythonanywhere.com/signup
            client.post(this,"http://0chieng.pythonanywhere.com/signin",con_body,
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
                            Toast.makeText(applicationContext,"you are signed in successfully",
                                Toast.LENGTH_LONG).show()
                            val i= Intent(applicationContext,MainActivity::class.java)
                            startActivity(i)
                        }//end if
                        else{
                            Toast.makeText(applicationContext,"please try again",
                                Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        throwable: Throwable?,
                        errorResponse: JSONObject?
                    ) {
                        progress.visibility= View.GONE
                        Toast.makeText(applicationContext,"something went wrong",
                            Toast.LENGTH_LONG).show()
                    }
                })
        }
    }
}