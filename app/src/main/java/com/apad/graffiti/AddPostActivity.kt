package com.apad.graffiti

//import java.awt.PageAttributes.MediaType
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.android.synthetic.main.activity_add_post.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream

private const val PERMISSION_REQUEST =10
class AddPostActivity : AppCompatActivity() {

    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private  var  locationGps: Location? = null
    private  var locationNetwork: Location? = null
    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null
    val url = "https://apad-streetart.herokuapp.com/api/newPost"
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            imageView.setImageURI(image_uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        disableView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(permissions))
              {
               enableView()
             }
            else{
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        } else {
            enableView()
        }


        //button click for image capture

        camera.setOnClickListener {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              if (checkSelfPermission(Manifest.permission.CAMERA)
                  == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                  == PackageManager.PERMISSION_DENIED){
                  val permission = arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                  requestPermissions(permission,PERMISSION_CODE)
              }
              else{
                openCamera()
              }
          }
        }
        var etpostTitle = findViewById<EditText>(R.id.post_title)
        var etpostTags = findViewById<EditText>(R.id.post_tags)
        var etpostContent = findViewById<EditText>(R.id.post_content)
        var email = intent.getStringExtra("Email")
        val spinner: Spinner = findViewById(R.id.spinner_category)
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.categories_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)


        button_submit.setOnClickListener {
            val postTitle = etpostTitle.text.toString()
            val postTags = etpostTags.text.toString()
            val postContent = etpostContent.text.toString()
            val postLocation =  findViewById<TextView>(R.id.location_text).text.toString()
            val postCategory = spinner.getSelectedItem().toString()


                val postImage = imageToString(
                    MediaStore.Images.Media.getBitmap(
                        contentResolver,
                        image_uri
                    ))
                var string_data: String =
                    "{ \"title\": \"$postTitle\", \"tags\": \"$postTags\", \"content\": \"$postContent\"" +
                            ", \"location\": \"$postLocation\", \"category\": \"$postCategory\", \"user\": \"$email\",\"image\": \"$postImage\"}"
                //String Request initialized
                val jsonRequest = object : JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    JSONObject(string_data),
                    Response.Listener { response ->
                        try {
                            Log.i("Success", "It works")
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    },
                    Response.ErrorListener { error ->
                        Log.i("This is the error", "Error :" + error.toString())
                    }) {
                    override fun getBodyContentType(): String {
                        return "application/json"
                    }


                }
                queue.add(jsonRequest)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
        fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
           when(requestCode){
               PERMISSION_CODE -> {
                   if (grantResults.size > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                       openCamera()

                   }
                   else {
                       Toast.makeText( this,"Permission denied",Toast.LENGTH_SHORT).show()
                   }
               }
           }
        }

    }

    private fun enableView() {
        Toast.makeText(this,"done",Toast.LENGTH_SHORT).show()
        get_location.isEnabled =true
        get_location.alpha = 1f
        get_location.setOnClickListener { getLocation()  }
    }

    private fun disableView() {
        get_location.isEnabled =false
        get_location.alpha = 0.5f
    }
    @SuppressLint("MissingPermission")
    private fun getLocation(){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if(hasGps || hasNetwork){
            if(hasGps){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,0f, object : LocationListener {
                    override fun onLocationChanged(p0: Location) {
                        if (p0!=null){
                            locationGps = p0
                            Log.i("hello",locationGps.toString())
                        }
                    }
                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                    }
                    override fun onProviderEnabled(provider: String) {
                    }
                })
                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if(localGpsLocation!=null){
                    locationGps=localGpsLocation
                }
            }

        }
            if(hasNetwork){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,0f, object : LocationListener {


                    override fun onLocationChanged(p0: Location) {
                        if (p0!=null){
                            locationNetwork = p0
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                    }

                    override fun onProviderEnabled(provider: String) {

                    }
                })
                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if(localNetworkLocation!=null){
                    locationNetwork=localNetworkLocation
                }
            }
        Log.i("Gps",locationGps.toString())
        Log.i("netowrk",locationNetwork.toString())
        if(locationGps!=null){

            Log.i("lattitude",locationGps!!.latitude.toString())
            location_text.text = locationGps!!.latitude.toString()+""+locationGps!!.longitude.toString()
            Log.i("ffull string",location_text.text.toString())



        }

    }
    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri )
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE)
    }


    private fun imageToString(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val bitmap = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(bitmap,Base64.DEFAULT)
    }
    private fun checkPermission(permissionArray: Array<String>):Boolean {
        var allSucess =true
        for (i in permissionArray.indices){
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED)
                allSucess = false
        }
        return allSucess
    }
}
