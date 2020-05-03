package br.com.victorhgo.movieapp.presentation.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import br.com.victorhgo.movieapp.R
import br.com.victorhgo.movieapp.presentation.popular.PopularFragment
import br.com.victorhgo.movieapp.presentation.upcoming.UpcomingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        openFragment(UpcomingFragment.newInstance())
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_upcoming -> {
                    openFragment(UpcomingFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_popular -> {
                    openFragment(PopularFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
