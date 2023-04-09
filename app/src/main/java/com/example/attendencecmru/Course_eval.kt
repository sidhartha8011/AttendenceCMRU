package com.example.attendencecmru
import androidx.activity.OnBackPressedCallback
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import javax.security.auth.Subject

class Course_eval : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_course_eval, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val imageView:ImageView=view.findViewById(R.id.qrdisplay)
                if (imageView.visibility==View.VISIBLE) {
                    // Perform your task here, for example, save the data and then exit the fragment
                    // ...
                    imageView.visibility=View.GONE

                    val tableLayout = view.findViewById<TableLayout>(R.id.table_layout)
                    tableLayout.visibility=View.VISIBLE

                    // Call onBackPressed() to trigger the default behavior
                   // isEnabled = false

                } else {
                    // If the condition is not true, allow the back button to behave as usual
                //    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        })

        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Course_eval.REQUEST_LOCATION_PERMISSION
            )
        }

        val db = Firebase.firestore
        val auth = FirebaseAuth.getInstance()


        val tf2: TextView = view.findViewById(R.id.middletf)
        val tf3: TextView = view.findViewById(R.id.righttf)

        val parentActivity = activity as TW
        val category = parentActivity.getMyVariable()


        if (category == 1) {
            var counter = 1

            db.collection("attendance")
                .document(auth.currentUser!!.uid)
                .collection("subjects")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        // Get the attendance data for the subject

                        val subjectName = document.getString("subject_name")
                        val attended = document.getLong("attended")
                        val total = document.getLong("total")

                        val id = document.id.toString()

                        val tableLayout = view.findViewById<TableLayout>(R.id.table_layout)
                        val layoutParams = TableRow.LayoutParams(0, 90)

                        layoutParams.bottomMargin = 10

                        val tableRow = TableRow(context)
                        layoutParams.weight = 2F

                        tableRow.layoutParams = layoutParams

                        val textView1 = TextView(context)
                        textView1.id = counter
                        counter++
                        textView1.layoutParams = layoutParams

                        tableRow.addView(textView1)

                        val textView2 = TextView(context)
                        textView2.id = counter
                        counter++
                        layoutParams.weight = 1F

                        textView2.layoutParams = layoutParams

                        tableRow.addView(textView2)


                        val textView3 = TextView(context)
                        textView3.id = counter
                        counter++
                        layoutParams.weight = 2F
                        textView3.layoutParams = layoutParams

                        textView1.textSize = 18F
                        textView1.gravity = Gravity.CENTER
                        textView1.setTypeface(null, Typeface.BOLD)
                        textView1.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            )
                        )
                        textView1.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.grey
                            )
                        )
                        textView1.setPadding(0, 2, 0, 2)
                        textView1.setAllCaps(true)

                        textView2.textSize = 18F
                        textView2.gravity = Gravity.CENTER
                        textView2.setTypeface(null, Typeface.BOLD)
                        textView2.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            )
                        )
                        textView2.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.grey
                            )
                        )
                        textView2.setPadding(0, 2, 0, 2)
                        textView2.setAllCaps(true)

                        textView3.textSize = 18F
                        textView3.gravity = Gravity.CENTER
                        textView3.setTypeface(null, Typeface.BOLD)
                        textView3.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            )
                        )
                        textView3.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.grey
                            )
                        )
                        textView3.setPadding(0, 2, 0, 2)
                        textView3.setAllCaps(true)


                        tableRow.addView(textView3)

                        textView1.text = subjectName.toString()
                        textView2.text = attended.toString() + "/" + total.toString()
                        if (total.toString() == "0")
                            textView3.text = "0"
                        else {

                            val percentage = (attended!!.toFloat() / total!!.toFloat()) * 100
                            textView3.text = percentage.toString()
                        }



                        tableLayout.addView(tableRow)


                    }
                }
                .addOnFailureListener {
                    // Handle any errors that occur during the query
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()

                }

            val scanqr:FloatingActionButton=view.findViewById(R.id.fab)

            scanqr.visibility=View.VISIBLE


        } else if (category == 2) {

            Log.d("enter1", "ernrew")

            tf3.text = "Semester"
            tf2.text = "Total"

            val qr:ImageView=view.findViewById(R.id.qr)

            val layoutParams1 = TableRow.LayoutParams(0,    138)
            layoutParams1.weight=1F
            qr.layoutParams=layoutParams1


            var counter = 100

            val teacherId = auth.currentUser!!.uid

            val teacherAttendanceRef = db.collection("total_teacher_attendance")
                .document(teacherId)
                .collection("subjects")

            teacherAttendanceRef.get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {


                        // Get the attendance data for the subject

                        val subjectName = document.getString("name_of_subject")
                        val semester = document.getString("semester")
                        val total = document.getLong("total_classes")


                        val id = document.id.toString()

                        val tableLayout = view.findViewById<TableLayout>(R.id.table_layout)
                        val layoutParams = TableRow.LayoutParams(0, 90)

                        layoutParams.bottomMargin = 10

                        val tableRow = TableRow(context)
                        layoutParams.weight = 2F

                        tableRow.layoutParams = layoutParams

                        val textView1 = TextView(context)
                        textView1.id = counter
                        counter++
                        textView1.layoutParams = layoutParams

                        tableRow.addView(textView1)

                        val textView2 = TextView(context)
                        textView2.id = counter
                        counter++
                        layoutParams.weight = 1F

                        textView2.layoutParams = layoutParams

                        tableRow.addView(textView2)


                        val textView3 = TextView(context)
                        textView3.id = counter
                        counter++
                        layoutParams.weight = 2F
                        textView3.layoutParams = layoutParams

                        textView1.textSize = 15F
                        textView1.gravity = Gravity.CENTER
                        textView1.setTypeface(null, Typeface.BOLD)
                        textView1.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            )
                        )
                        textView1.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.grey
                            )
                        )
                        textView1.setPadding(0, 2, 0, 2)
                        textView1.setAllCaps(true)

                        textView2.textSize = 15F
                        textView2.gravity = Gravity.CENTER
                        textView2.setTypeface(null, Typeface.BOLD)
                        textView2.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            )
                        )
                        textView2.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.grey
                            )
                        )
                        textView2.setPadding(0, 2, 0, 2)
                        textView2.setAllCaps(true)

                        textView3.textSize = 15F
                        textView3.gravity = Gravity.CENTER
                        textView3.setTypeface(null, Typeface.BOLD)
                        textView3.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            )
                        )
                        textView3.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.grey
                            )
                        )
                        textView3.setPadding(0, 2, 0, 2)
                        textView3.setAllCaps(true)


                        tableRow.addView(textView3)

                        val imageButton = ImageButton(context)
                        layoutParams.weight=1F
                        imageButton.layoutParams=layoutParams

                        imageButton.scaleType=ImageView.ScaleType.CENTER_INSIDE
                        imageButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey))
                        imageButton.setPadding(0, 2, 0, 2)
                        imageButton.setImageResource(R.drawable.ic_baseline_qr_code_24)


                        tableRow.addView(imageButton)


                        textView1.text = subjectName.toString()
                        textView3.text = semester.toString()
                        textView2.text = total.toString()

                        tableLayout.addView(tableRow)

                        imageButton.setOnClickListener {
                            tableLayout.visibility=View.GONE
                            val qrimage:ImageView=view.findViewById(R.id.qrdisplay)
                            qrimage.visibility=View.VISIBLE



                            val text = "${document.id}"
                            val location = getLocation()

                            // Combine the text and location
                            val combinedText = "$text\n$location"

                            // Generate the QR code
                            val width = 500
                            val height = 500
                            val qrCode = generateQRCode(combinedText, width, height)

                            val qrCodeImageView:ImageView= view.findViewById(R.id.qrdisplay)
                            qrCodeImageView.setImageBitmap(qrCode )

                        }


                    }


                }
                .addOnFailureListener {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }


        }
        }

    private fun getLocation(): String {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Check if location permission is granted

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Course_eval.REQUEST_LOCATION_PERMISSION
            )
            return "Location permission not granted"
        }
        // Get the last known location from the location manager
        val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        // Set up location listener to get the current location
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Do something with the new location
                locationManager.removeUpdates(this)
            }

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }

        // Request location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)

        // Return the location string
        return if (lastKnownLocation != null) {
            "Latitude: ${lastKnownLocation.latitude}, Longitude: ${lastKnownLocation.longitude}"
        } else {
            Toast.makeText(context, "ReLogin location not available rigth now invalid QR", Toast.LENGTH_LONG).show()
            "Location not available"
        }
    }

    private fun generateQRCode(text: String, width: Int, height: Int): Bitmap {
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }

        return bitmap
    }
    companion object {
        const val REQUEST_LOCATION_PERMISSION = 100
    }






}


