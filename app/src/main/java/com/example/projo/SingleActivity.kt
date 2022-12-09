package com.example.projo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import org.json.JSONObject

class SingleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)

        //access shared prefferences

        val prefs: SharedPreferences = getSharedPreferences(
            "store",
            Context.MODE_PRIVATE
        )

        //access the saved product_name from preferences and put in the TextView
        val id = prefs.getString("room_id", "")
        val room_id = findViewById(R.id.room_id) as TextView
        room_id.text = id

        //access the saved product_desc from prefferences and put in the TextView
        val name = prefs.getString("room_name", "")
        val room_name = findViewById(R.id.room_name) as TextView
        room_name.text = name

        //access the saved product_cost from prefferences and put in the TextView
        val desc = prefs.getString("room_desc", "")
        val room_desc = findViewById(R.id.room_desc) as TextView
        room_desc.text = desc

        val persons = prefs.getString("num_of_persons", "")
        val num_persons = findViewById(R.id.num_of_persons) as TextView
        num_persons.text = persons

        val availability = prefs.getString("availability", "")
        val status = findViewById(R.id.availability) as TextView
        status.text = availability

        val cost = prefs.getString("cost", "")
        val room_cost = findViewById(R.id.cost) as TextView
        room_cost.text = cost

        //access the saved image from prefferences and put in the ImageView Using Glide
        val image_url = prefs.getString("image_url", "")
        val image = findViewById(R.id.image_url) as ImageView
        Glide.with(applicationContext).load(image_url)
            .apply(RequestOptions().centerCrop())
            .into(image)

            ///find ids for making reservations
            val name_r = findViewById<EditText>(R.id.name)
            val user_id = findViewById<EditText>(R.id.user_id)
            val email = findViewById<EditText>(R.id.email)
            val start_t = findViewById<EditText>(R.id.start_time)
            val date = findViewById<EditText>(R.id.date)
            val end_t = findViewById<EditText>(R.id.end_time)
            val phone = findViewById<EditText>(R.id.phone)
            val r_id = findViewById<EditText>(R.id.R_id)
            val reserve = findViewById<Button>(R.id.make_r)
            var progress = findViewById<ProgressBar>(R.id.progress_bar)
            //hide the progress bar
            progress.visibility = View.GONE//on opening the single activity,the bar should be gone
            //just paste
            //loopj
            reserve.setOnClickListener {
                progress.visibility = View.VISIBLE
                val client = AsyncHttpClient(true, 80, 443)
                val body = JSONObject()
                body.put("name", name_r.text.toString())
                body.put("user_id", user_id.text.toString())
                body.put("email", email.text.toString())
                body.put("start_time", start_t.text.toString())
                body.put("date", date.text.toString())
                body.put("end_time", end_t.text.toString())
                body.put("room_id", r_id.text.toString())
                body.put("phone", phone.text.toString())

                val con_body = StringEntity(body.toString())
                //https://0chieng.pythonanywhere.com/signup
                client.post(this, "https://0chieng.pythonanywhere.com/reservations", con_body,
                    "application/json",
                    object : JsonHttpResponseHandler() {
                        //create a function for on success
                        override fun onSuccess(
                            statusCode: Int,
                            headers: Array<out Header>?,
                            response: JSONObject?
                        ) {
                            //check if status code is correct(200)
                            if (statusCode == 202) {
                                progress.visibility = View.GONE
                                Toast.makeText(
                                    applicationContext, "reservation successful",
                                    Toast.LENGTH_LONG
                                ).show()
                            }//end if
                            else {
                                progress.visibility = View.GONE
                                Toast.makeText(
                                    applicationContext, "please try again",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(
                            statusCode: Int,
                            headers: Array<out Header>?,
                            throwable: Throwable?,
                            errorResponse: JSONObject?
                        ) {
                            progress.visibility = View.GONE
                            Toast.makeText(
                                applicationContext, "something went wrong",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            }
        val view = findViewById<Button>(R.id.view_reservations)
        view.setOnClickListener {
            val i = Intent(applicationContext, Admin::class.java)
            startActivity(i)

        }
    }
}
