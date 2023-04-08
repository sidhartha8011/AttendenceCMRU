package com.example.attendencecmru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TW : AppCompatActivity() {
    private lateinit var tabbedlayout:TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: FragmentPageAdapter

    private lateinit var mainref:CollectionReference
    private var category: Int=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tw)

        val db= Firebase.firestore
        val auth= FirebaseAuth.getInstance()

        category = intent.getIntExtra("category", 1)


        val nametf:TextView=findViewById(R.id.name)
        val categorytf:TextView=findViewById(R.id.category)

        val profileButton:ImageView=findViewById(R.id.profile)



        if(category==1){
            mainref = db.collection("students")
            categorytf.text="Student"
        }
        else if (category==2){
            mainref = db.collection("teachers")
            categorytf.text="Teacher"
        }

        tabbedlayout=findViewById(R.id.tablayout)
        viewPager2=findViewById(R.id.viewpager2)

        adapter= FragmentPageAdapter(supportFragmentManager,lifecycle)

        tabbedlayout.addTab(tabbedlayout.newTab().setText("Course Evaluation"))
        tabbedlayout.addTab(tabbedlayout.newTab().setText("Time Table"))

        viewPager2.adapter=adapter

        tabbedlayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem=tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabbedlayout.selectTab(tabbedlayout.getTabAt(position))
            }
        })


        val documentRef = mainref.document(auth.currentUser!!.uid)
        documentRef.get()
            .addOnSuccessListener {

                val data = it.data
                val name = data?.get("name")
                nametf.text=name.toString()
            }

        profileButton.setOnClickListener{
            val i= Intent(this,Profile::class.java)
            i.putExtra("category",category)
            startActivity(i)
        }
    }

    fun getMyVariable(): Int {
        return category // won't compile because myVariable is not in scope
    }
    fun getref(): CollectionReference{
        return mainref
    }
}