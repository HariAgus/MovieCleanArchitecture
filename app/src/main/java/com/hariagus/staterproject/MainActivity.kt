package com.hariagus.staterproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hariagus.staterproject.databinding.ActivityMainBinding
import com.hariagus.staterproject.home.HomeFragment
import com.hariagus.staterproject.search.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_search -> {
                    navigateChangeFragment(SearchFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_home -> {
                    navigateChangeFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_favorite -> {
                    moveToFavoriteFragment()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
        openFragment()
    }

    private fun openFragment() {
        binding.navView.selectedItemId = R.id.nav_search
    }

    private val classNameFavorite: String
        get() = "com.hariagus.favorite.favorite.FavoriteFragment"

    private fun moveToFavoriteFragment() {
        val fragment = instantiateFragment(classNameFavorite)
        if (fragment != null) {
            navigateChangeFragment(fragment)
        }
    }

    private fun instantiateFragment(classNameFavorite: String): Fragment? {
        return try {
            Class.forName(classNameFavorite).newInstance() as Fragment
        } catch (e: Exception) {
            Toast.makeText(this, "Module not Found", Toast.LENGTH_SHORT).show()
            null
        }
    }

    private fun navigateChangeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}