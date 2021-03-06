package com.example.tahdig

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.PointerIcon
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_admin_main.*
import kotlinx.android.synthetic.main.activity_login_signup2.*

class admin_main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        val intent1 = Intent(this,request_list::class.java)
        val intent1_3 = intent
        val map_data :HashMap<String,String> = intent1_3.getSerializableExtra("data_array") as HashMap<String, String>

        //check number of req
        val db =  DatabaseHandler(this)
        val ReqList = db.readDatareq()
        db.close()
        var req_num = ReqList.size
        //check number of req
        if (req_num == 0){
            req_title.text = "There is no unchecked request"

        }
        else{
            req_title.text = req_title.text.toString() + req_num.toString() + "\nClick for more detail"
            req_title.setOnClickListener {
                intent1.putExtra("data_array",map_data)
                startActivity(intent1)
            }
        }


        admintitle_toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_logout -> {
                    onAlertDialog(admintitle_toolbar)
                    return@setOnMenuItemClickListener true
                }

            }
            return@setOnMenuItemClickListener false
        }

    }
    fun onAlertDialog(view: View) {
        val intent1_3 = intent
        val map_data :HashMap<String,String> = intent1_3.getSerializableExtra("data_array") as HashMap<String, String>

        val intent1 = Intent(this, login_signup2::class.java)

        //Instantiate builder variable
        val builder = AlertDialog.Builder(view.context)

        // set title
        builder.setTitle("Alert")

        //set content area
        builder.setMessage("Are you sure you want to logOut ?")

        //set negative button
        builder.setPositiveButton(
            "Yes"
        ) { dialog, id ->
            // User clicked Update Now button
            val context = this
            val db = DatabaseHandler(context)
            db.deletefromLoggedperson()
            db.close()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            intent1.putExtra("data_array",map_data)
            startActivity(intent1)

        }

        //set positive button
        builder.setNegativeButton(
            "No"
        ) { dialog, id ->
            // User cancelled the dialog
        }
        builder.show()
    }
}