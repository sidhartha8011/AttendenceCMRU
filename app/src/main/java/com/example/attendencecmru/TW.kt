package com.example.attendencecmru

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout

class TW : AppCompatActivity() {
    private lateinit var tabbedlayout:TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: FragmentPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tw)

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

    }
}