package com.example.youtubeexplorer

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.youtubeexplorer.adapter.ViewPagerAdapter
import com.example.youtubeexplorer.fragment.DetailsFragment
import com.example.youtubeexplorer.fragment.VideosFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private lateinit var toolbar: MaterialToolbar

    private val videosFragment = VideosFragment()
    private val detailsFragment = DetailsFragment()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        viewPager.adapter = ViewPagerAdapter(this, videosFragment, detailsFragment)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position == 0)
                tab.text = "Videos"
            else
                tab.text = "Details"
        }.attach()


        toolbar.setOnMenuItemClickListener {

            when (it.itemId) {

                R.id.menu_refresh -> {
                    recreate()
                    true
                }

                R.id.menu_settings ->{
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu?.findItem(R.id.menu_search)

        val searchView = searchItem?.actionView as? SearchView ?: return true

        searchView.queryHint = "Search videos"

        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    videosFragment.filterVideos(
                        newText ?: ""
                    )

                    return true
                }
            })

        return true
    }
}