package com.apad.graffiti

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.apad.graffiti.com.apad.graffiti.PostsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.internal.artificialFrame
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


//import statements. We need all of these libraries. They automatically come with Android. Nothing to install


//To see how this page looks, look at content_main.xml file

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        //these 3 lines are automatically there. They load the layout UI (from the .xml files)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //This brings the email and password from the login page. If there is no login both are null
        var email = intent.getStringExtra("Email")

        // This changes the text if a user has logged in or not by looking to see if the information is null
        // Pressing on the button if a user is logged in will logout the user.
        if (email != null) {
            btn_login_3.setText("Logout")
        } else {
            btn_login_3.setText("Login")
        }

        setSupportActionBar(findViewById(R.id.toolbar))
        InitializeDropDown() //setup dropdown menu

        //What to do when the circle add button is pressed
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            // If a user is not logged in then they are directed to the login page and if the
            //user is logged in then they are directed allowed to go to the AddPost page
            if (email != null) {
                val intent = Intent(this, AddPostActivity::class.java)
                intent.putExtra("Email", email)
                startActivity(intent)
            } else {
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
            }
        }

        val jsonText = ChooseDropDown("All")

        //val jsonText = "{\"posts\":[{\"art\":\"https://apad-streetart.herokuapp.com/static/art/b1eeb4a91bf9cf1d.png\",\"category\":\"Tag\",\"content\":\"fsdfsdf\",\"date_posted\":\"Sat, 08 Aug 2020 02:07:36 GMT\",\"location\":\"30.2734, -97.7835\",\"tag\":\"jose\",\"title\":\"This is a test\"},{\"art\":\"https://apad-streetart.herokuapp.com/static/art/cf0b7a69a23df676.png\",\"category\":\"Throw-up\",\"content\":\"qweweqw\",\"date_posted\":\"Sat, 08 Aug 2020 02:00:26 GMT\",\"location\":\"30.2734, -97.7835\",\"tag\":\"qweqw\",\"title\":\"This is a test\"},{\"art\":\"https://apad-streetart.herokuapp.com/static/art/a57da187bb3d25f5.jpg\",\"category\":\"Wildstyle\",\"content\":\"qweqweweq\",\"date_posted\":\"Fri, 07 Aug 2020 00:19:27 GMT\",\"location\":\"30.2734, -97.7835\",\"tag\":\"@joserocks\",\"title\":\"This is a test\"},{\"art\":\"https://apad-streetart.herokuapp.com/static/art/915da586212afc8a.jpg\",\"category\":\"Wildstyle\",\"content\":\"qweqweweq\",\"date_posted\":\"Fri, 07 Aug 2020 00:17:53 GMT\",\"location\":\"30.2834, -97.7835\",\"tag\":\"@joserocks\",\"title\":\"This is a test\"},{\"art\":\"https://apad-streetart.herokuapp.com/static/art/17a25b283893ce39.jpeg\",\"category\":\"Tag\",\"content\":123123,\"date_posted\":\"Thu, 06 Aug 2020 21:29:01 GMT\",\"location\":\"30.2534, -97.9835\",\"tag\":\"@joserocks\",\"title\":\"This is a test\"},{\"art\":\"https://apad-streetart.herokuapp.com/static/art/a705b9c8b322db0c.png\",\"category\":\" Sticker (slap)\",\"content\":\"zxczcxcz\",\"date_posted\":\"Sat, 01 Aug 2020 00:48:18 GMT\",\"location\":\"30.2534, -97.8835\",\"tag\":\"@joserocks\",\"title\":\"heyyyyy\"},{\"art\":\"https://apad-streetart.herokuapp.com/static/art/55bc2d5fed093ea8.png\",\"category\":\" Poster (paste-up)\",\"content\":\"zxvxvcxv\",\"date_posted\":\"Sat, 01 Aug 2020 00:43:20 GMT\",\"location\":\"30.2534, -97.7835\",\"tag\":\"vzxvzcvzvxvzx\",\"title\":\"hello\"},{\"art\":\"https://apad-streetart.herokuapp.com/static/art/40ab896edc38dcce.png\",\"category\":\"Throw-up\",\"content\":\"cxczxc\",\"date_posted\":\"Sat, 01 Aug 2020 00:43:02 GMT\",\"location\":\"30.2050,-97.7748\",\"tag\":\"zxcxzcz\",\"title\":\"This is a test\"},{\"art\":\"https://apad-streetart.herokuapp.com/static/art/59781031c883be87.png\",\"category\":\"Heaven\",\"content\":\"wqewqe\",\"date_posted\":\"Fri, 31 Jul 2020 20:14:42 GMT\",\"location\":\"30.2543, -97.7670\",\"tag\":\"@joserocks\",\"title\":\"This is a test\"},{\"art\":\"https://apad-streetart.herokuapp.com/static/art/c4f53a73ae90897c.png\",\"category\":\"Wildstyle\",\"content\":\"this is content\",\"date_posted\":\"Fri, 31 Jul 2020 19:28:05 GMT\",\"location\":\"30.2729, -97.7444\",\"tag\":\"@qqweqwewq , wejwjejwe,test2\",\"title\":\"hello\"},{\"art\":\"https://apad-streetart.herokuapp.com/static/art/2fb8ff74d49aa349.jpeg\",\"category\":\"Wildstyle\",\"content\":\"adsad\",\"date_posted\":\"Fri, 31 Jul 2020 04:44:14 GMT\",\"location\":\"30.2655, -97.7091\",\"tag\":\"@joserocks\",\"title\":\"This is a test\"}]}"

        //this is an example Json string.


        btn_login_3.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btn_login_3.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    //function that accepts a json string and turns it into a list of posts (remember a post is a list)
    fun TurnJsonIntoWidget(jsonText : String) : ArrayList<Array<String>>
    {
        val result : ArrayList<Array<String>> = arrayListOf() //create the array we will return

        val jsonObj = JSONObject(jsonText.substring(jsonText.indexOf("{"), jsonText.lastIndexOf("}") + 1))
        val postsJson = jsonObj.getJSONArray("posts") //read the JSON and get the posts
        for (i in 0..postsJson!!.length() - 1) //for every post in the JSON file
        {
            val postList : Array<String> = arrayOf("", "", "", "", "", "", "", "") //create an empty post
            val text = postsJson.getJSONObject(i).getString("content") //get the content from json file
            val location = postsJson.getJSONObject(i).getString("location")//get location from json file
            val art = postsJson.getJSONObject(i).getString("art")
            val title = postsJson.getJSONObject(i).getString("title")
            val date_posted = postsJson.getJSONObject(i).getString("date_posted")
            val user = postsJson.getJSONObject(i).getString("user")
            val cate = postsJson.getJSONObject(i).getString("category")
            val tag = postsJson.getJSONObject(i).getString("tag")

            postList[0] = text
            postList[1] = location
            postList[2] = art
            postList[3] = title
            postList[4] = date_posted
            postList[5] = cate
            postList[6] = tag
            postList[7] = user
            //add stuff to list (post)

            result.add(postList) //add the post to the list of posts
        }
        return result
    }


    fun InitializeDropDown()
    {
        val dropdown = findViewById<Spinner>(R.id.dropdown_menu)
        val items = arrayOf("All", "Blockbuster", "Heaven", "Poster", "Stencil", "Sticker", "Tag", "Throw-up", "Wildstyle")
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, items)
        dropdown.adapter = adapter

        dropdown.onItemSelectedListener = this
    }

    fun GetJson(jsonText: String){
        val posts : ArrayList<Array<String>> = TurnJsonIntoWidget(jsonText)
        val postsListView : ListView = findViewById<ListView>(R.id.postsListView); //get the ListView widget from the content_main.xml file using the id
        val adapter : PostsAdapter? = PostsAdapter(this, posts) //create an Adapter (see PostsAdapter file)
        postsListView.adapter = adapter //attach the adapter to our ListView Widget

        //what happens when we click on a post
        postsListView.setOnItemClickListener { parent, view, position, id ->
            val content : Array<String> = postsListView.adapter.getItem(position) as Array<String> //get the data of the post we clicked on
            val location : String =  content[1] //get the location of the post we clicked on
            val uris = Uri.parse("https://www.google.com/maps/search/"+location)
            val intents = Intent(Intent.ACTION_VIEW, uris)
            startActivity(intents) //open the web browser of Google Maps for the location
        }
    }

    fun ChooseDropDown(UserCategory: String):String {
        var jsonFile = ""
        Thread({
            var URL = URL("https://apad-streetart.herokuapp.com/api/viewCategories/" + UserCategory)
            var reader = BufferedReader(InputStreamReader(URL.openStream()))
            var line = reader.readLine()
            while (line != null) {
                jsonFile += line
                line = reader.readLine()
            }
            reader.close()

            runOnUiThread(
                object : Runnable {
                    override fun run() {
                        GetJson(jsonFile)
                    }
                }
            )
        }).start()
        return jsonFile
    }
    //These functions were automatically created

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId)
        {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val items = arrayOf("All", "Blockbuster", "Heaven", "Poster", "Stencil", "Sticker", "Tag", "Throw-up", "Wildstyle")
        ChooseDropDown(items[p2])}
}