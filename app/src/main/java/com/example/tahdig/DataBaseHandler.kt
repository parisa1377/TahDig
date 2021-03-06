package com.example.tahdig

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import android.content.Context
import android.location.Address

const val DATABASE_NAME = "TahDigDB"

class DatabaseHandler(var context:Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1)
{
    private val rest = "Restaurant"
    private val loggedperson = "Loggedperson"
    private val addr = "Address"
    private val user = "User"
    private val reqnew = "newreq"
    private val loggedres = "LoggedRestaurants"

    override fun onCreate(db: SQLiteDatabase)
    {


        db.execSQL("DROP TABLE IF EXISTS " + rest);
        db.execSQL("DROP TABLE IF EXISTS " + reqnew);
        db.execSQL("DROP TABLE IF EXISTS " + loggedres);

        val Loggedperson = "CREATE TABLE Loggedperson " +
                "(username VARCHAR(16) PRIMARY KEY," +
                "password VARCHAR(16))"

        db.execSQL(Loggedperson)

        val createTableAddress = "CREATE TABLE Address " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "city VARCHAR(50)," +
                "street VARCHAR(50)," +
                "alley VARCHAR(50)," +
                "number VARCHAR(50))"

        db.execSQL(createTableAddress)

        val createTableUser = "CREATE TABLE User " +
                "(username VARCHAR(16) PRIMARY KEY," +
                "name VARCHAR(50)," +
                "password VARCHAR(16)," +
                "addressID INTEGER," +
                "FOREIGN KEY (addressID) REFERENCES Address(id))"

        db.execSQL(createTableUser)

        val createTableRestaurant = "CREATE TABLE Restaurant " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(50)," +
                "ownerUsername VARCHAR(16)," +
                "businessLicenseNumber VARCHAR(10)," +
                "phoneNumber VARCHAR(12)," +
                "addressID INTEGER," +
                "menu VARCHAR(100)," +
                "FOREIGN KEY (addressID) REFERENCES Address(id)," +
                "FOREIGN KEY (ownerUsername) REFERENCES User(username))"

        db.execSQL(createTableRestaurant)

        val createTablenewreq = "CREATE TABLE newreq " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(50)," +
                "ownerUsername VARCHAR(16)," +
                "businessLicenseNumber VARCHAR(10)," +
                "phoneNumber VARCHAR(12)," +
                "addressID INTEGER," +
                "FOREIGN KEY (addressID) REFERENCES Address(id)," +
                "FOREIGN KEY (ownerUsername) REFERENCES User(username))"

        db.execSQL(createTablenewreq)

        val createTableLoggedRestaurants = "CREATE TABLE LoggedRestaurants " +
                "(id INTEGER PRIMARY KEY," +
                "name VARCHAR(50)," +
                "menu VARCHAR(100))"

        db.execSQL(createTableLoggedRestaurants)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // on upgrade drop older tables

        db.execSQL("DROP TABLE IF EXISTS " + rest);
        db.execSQL("DROP TABLE IF EXISTS " + reqnew);
        db.execSQL("DROP TABLE IF EXISTS " + loggedres);

        // create new tables
        onCreate(db);
    }

    fun cleartables() {
        val db = this.writableDatabase
        db.delete("Address",null,null)
        db.delete("User",null,null)
        db.delete("Loggedperson",null,null)
        db.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='Address';")
        db.close()
    }

    fun insertAddress(city:String,street:String,alley:String, number:Int): Long {

        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("city", city)
        cv.put("street", street)
        cv.put("alley", alley)
        cv.put("number", number)

        var result = db.insert("Address", null, cv)

        if (result ==-1.toLong())
            Toast.makeText(context, "Address insertion failed", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Address successfully inserted", Toast.LENGTH_SHORT).show()
        db.close()
        return result
    }

    fun insertUser(username:String,name:String,password:String,addressID:Int): Long {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("username", username)
        cv.put("name", name)
        cv.put("password", password)
        cv.put("addressID", addressID)

        var result = db.insert("User", null, cv)

        if (result ==-1.toLong())
            Toast.makeText(context, "user insertion failed", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "user successfully inserted", Toast.LENGTH_SHORT).show()
        db.close()
        return result
    }

    fun readDatauser(): MutableList<User> {
        val list: MutableList<User> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from User"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val user = User()
                user.username = result.getString(result.getColumnIndex("username"))
                user.name = result.getString(result.getColumnIndex("name"))
                user.password = result.getString(result.getColumnIndex("password"))
                user.addressID = result.getString(result.getColumnIndex("addressID")).toInt()
                list.add(user)
            }
            while (result.moveToNext())
        }
        result.close()
        return list
    }

    fun insertLoggedperson(username:String,password:String){
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("username", username)
        cv.put("password", password)

        var result = db.insert("Loggedperson", null, cv)
        db.close()
    }

    fun deletefromLoggedperson(){
        val db = this.writableDatabase
        db.delete("Loggedperson",null,null)
        db.close()
    }

    fun insertRestaurant(name:String,ownerUsername:String,businessLicenseNumber:String,phoneNumber:String,addressID:Int): Long {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("name", name)
        cv.put("ownerUsername", ownerUsername)
        cv.put("businessLicenseNumber", businessLicenseNumber)
        cv.put("phoneNumber", phoneNumber)
        cv.put("addressID", addressID)
        cv.put("menu","")

        var result = db.insert("Restaurant", null, cv)

        if (result ==-1.toLong())
            Toast.makeText(context, "Restaurant insertion failed", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Restaurant inserted successfully!", Toast.LENGTH_SHORT).show()

        db.close()

        return result
    }

      fun insertNewRequests(name1:String,ownerUsername1:String,businessLicenseNumber1:String,phoneNumber1:String,addressID1:Int): Long {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("name", name1)
        cv.put("ownerUsername", ownerUsername1)
        cv.put("businessLicenseNumber", businessLicenseNumber1)
        cv.put("phoneNumber", phoneNumber1)
        cv.put("addressID", addressID1)


        val result = db.insert("newreq", null, cv)

        if (result == (-1).toLong())
            Toast.makeText(context, "Request insertion failed", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Request inserted successfully!"+result.toString(), Toast.LENGTH_SHORT).show()
        db.close()
          return result
    }

    fun addMenu(ResID:Int, menu:String): Int {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("menu", menu)

        val whereArgs = arrayOf(ResID.toString())

        var result = db.update("Restaurant", cv, "RestaurantID = ?", whereArgs)

        if (result == -1)
            Toast.makeText(context, "Menu insertion failed", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Menu inserted successfully!", Toast.LENGTH_SHORT).show()
        db.close()
        return result
    }

    fun findRestaurants(ownerUsername1:String): MutableList<AbstractRestaurant> {
        val list: MutableList<AbstractRestaurant> = ArrayList()
        val db = this.readableDatabase

        val query = "SELECT * FROM Restaurant where ownerUsername = ?" ///////////
        val result = db.rawQuery(query, arrayOf(ownerUsername1))
        if (result.moveToFirst()) {
            do {
                val res = AbstractRestaurant()
                res.id = result.getInt(result.getColumnIndex("id"))
                res.name = result.getString(result.getColumnIndex("name"))
                if (result.getString(result.getColumnIndex("menu")) == null)
                    res.menu = ""
                else
                    res.menu = result.getString(result.getColumnIndex("menu"))
                list.add(res)
            }
            while (result.moveToNext())
        }
        db.close()
        result.close()
        return list
    }

    fun insertLoggedRestaurants(resID:Int,name:String,menu:String): Long {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("id", resID)
        cv.put("name", name)
        cv.put("menu", menu)

        var result = db.insert("LoggedRestaurants", null, cv)
        if (result ==-1.toLong())
            Toast.makeText(context, "LoggedRestaurants insertion failed", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "LoggedRestaurants inserted successfully!", Toast.LENGTH_SHORT).show()
        db.close()
        return result
    }

    fun deleteFromLoggedRestaurants(){
        val db = this.writableDatabase
        db.delete("LoggedRestaurants",null,null)
        db.close()
    }

    fun readDatareq(): MutableList<requestlist> {
        val list: MutableList<requestlist> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from newreq"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val user = requestlist()

                user.restaurant_name = result.getString(result.getColumnIndex("name"))
                user.businessLicenseNumber = result.getString(result.getColumnIndex("businessLicenseNumber"))
                user.id = result.getInt(result.getColumnIndex("id"))
                user.ownerUsername = result.getString(result.getColumnIndex("ownerUsername"))
                user.phoneNumber = result.getString(result.getColumnIndex("phoneNumber"))
                user.addressID = result.getString(result.getColumnIndex("addressID")).toInt()
                list.add(user)
            }
            while (result.moveToNext())
        }
        result.close()
        return list
    }

    fun DeleteFromNewRequests() {
        val db = this.writableDatabase
        db.delete("newreq",null,null)
        db.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='newreq';")
        db.close()
    }

}

class AbstractRestaurant {
    var id : Int = 0
    var name : String = ""
    var menu : String = ""
}

class requestlist {

    var id : Int = 0
    var restaurant_name : String = ""
    var ownerUsername : String = ""
    var businessLicenseNumber : String = ""
    var phoneNumber : String = ""
    var addressID : Int = 0
}

class User {
    var username : String = ""
    var password : String = ""
    var name : String = ""
    var addressID : Int = 0
}
