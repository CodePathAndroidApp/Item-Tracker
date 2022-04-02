package com.example.itemtracker.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.*
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import com.example.itemtracker.MainActivity
import com.example.itemtracker.Post
import com.example.itemtracker.R
import com.parse.ParseFile
import com.parse.ParseUser
import java.io.File
import java.util.*

class ComposeFragment : Fragment()/*, LocationListener*/ {
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    lateinit var ivPreview: ImageView

    lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    var cityName = ""
    var stateName = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivPreview = view.findViewById(R.id.imageView)

        fun submitPost(description: String, lostlocation : String, user: ParseUser, file: File) {
            val post = Post()
            post.setDescription(description)
            post.setLostDescription(lostlocation)
            post.setUser(user)
            post.setImage(ParseFile(file))
            post.saveInBackground { exception ->
                if (exception != null) {
                    // Something has went wrong
                    Log.e(MainActivity.TAG, "Error while saving post")
                    exception.printStackTrace()
                } else {
                    Log.i(MainActivity.TAG, "Successfully saved post")
                }
            }
        }

        view.findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            val description = view.findViewById<EditText>(R.id.description).text.toString()
            val lostlocation = view.findViewById<TextView>(R.id.lostlocation).text.toString()
            val user = ParseUser.getCurrentUser()
            if (photoFile != null) {
                submitPost(description, lostlocation, user, photoFile!!)
            } else {
                Log.e(MainActivity.TAG, "Error submitting photo")
            }
        }

        // Returns the File for a photo stored on disk given the fileName
        fun getPhotoFileUri(fileName: String): File {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            val mediaStorageDir =
                File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MainActivity.TAG)

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d(MainActivity.TAG, "failed to create directory")
            }

            // Return the file target for the photo based on filename
            return File(mediaStorageDir.path + File.separator + fileName)
        }

        fun onLaunchCamera() {
            // create Intent to take a picture and return control to the calling application
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Create a File reference for future access
            photoFile = getPhotoFileUri(photoFileName)

            // wrap File object into a content provider
            // required for API >= 24
            // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
            if (photoFile != null) {
                val fileProvider: Uri =
                    FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider.Item_Tracker", photoFile!!)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

                // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                // So as long as the result is not null, it's safe to use the intent.

                if (intent.resolveActivity(requireContext().packageManager) != null) {
                    // Start the image capture intent to take photo
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
                }
            }
        }

        view.findViewById<Button>(R.id.btnTakePicture).setOnClickListener {
            onLaunchCamera()
        }

        /*
        fun locateMe() {
            locationManager = this.context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if ((ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(requireContext() as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        }

        view.findViewById<Button>(R.id.btnLocate).setOnClickListener {
            locateMe()
        }
         */
    }

    /*
    override fun onLocationChanged(location: Location) {
        try {
            val addresses: MutableList<Address>
            val geocoder = Geocoder(context, Locale.ENGLISH)
            addresses =
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (!CollectionUtils.isEmpty(addresses)) {
                val fetchedAddress = addresses[0]
                if (fetchedAddress.maxAddressLineIndex > -1) {
                    fetchedAddress.locality?.let {
                        cityName = it
                    }
                    fetchedAddress.adminArea?.let {
                        stateName = it
                    }
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        var location = "$cityName, $stateName"
        getView()?.findViewById<EditText>(R.id.lostlocation)?.setText(location)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(requireContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}