package com.example.tahdig

import android.content.Intent
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent1 = Intent(this,login_signup2::class.java)
        val intent2 = Intent(this,admin_main::class.java)
        val intent3 = Intent(this,seller_main::class.java)

        About_us_button.setOnClickListener {
            val db1 = DatabaseHandler(this)
            val result_res =  db1.findRestaurants("w1")
            Toast.makeText(this, "name:"+result_res[0].name, Toast.LENGTH_SHORT).show()

        }

        start_button.setOnClickListener {
            val context = this
            val intent11 = intent
            val map_data : HashMap<String, String>
            if (getIntent().getSerializableExtra("data_array") != null)
                map_data = getIntent().getSerializableExtra("data_array")as HashMap<String, String>
            else
                map_data  = HashMap()


            val db1 = DatabaseHandler(context)
            val db  = db1.readableDatabase
            val num = DatabaseUtils.queryNumEntries(db, "Loggedperson")
            if (num != 0.toLong())
            {
                val query = "Select * from Loggedperson"
                val result = db.rawQuery(query, null)
                result.moveToFirst()
                val username1 = result.getString(result.getColumnIndex("username"))
                val password1 = result.getString(result.getColumnIndex("password"))

                Toast.makeText(this, "size:$num", Toast.LENGTH_SHORT).show()

                //restaurant num
                val data = db1.findRestaurants(username1)

                if (data.size == 0 )
                    map_data.put("res_no","0")
                else
                    map_data.put("res_no","1")

                //restaurant num

                result.close()
                if( username1.equals("admin") && password1.equals("1234")) {

                        intent2.putExtra("data_array",map_data)
                    startActivity(intent2)

                }
                else {

                        intent3.putExtra("data_array",map_data)
                    startActivity(intent3)
                }
            }
            else
            {
                map_data.put("res_no","0")
                    intent1.putExtra("data_array",map_data)
                startActivity(intent1)
            }

        }
    }
}