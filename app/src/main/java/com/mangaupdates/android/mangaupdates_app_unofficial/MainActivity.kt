package com.mangaupdates.android.mangaupdates_app_unofficial

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.anupcowkur.reservoir.Reservoir
import com.mangaupdates.android.mangaupdates_app_unofficial.`feature_discover`.DiscoverFrament
import com.mangaupdates.android.mangaupdates_app_unofficial.feature_releases.ReleasesOverviewFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    lateinit var drawer : DrawerLayout
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Reservoir.init(this,2048)

        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(R.id.content_main, ReleasesOverviewFragment.newInstance()).commit()
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
       /* val searchItem = menu.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.setOnQueryTextListener(this)*/
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val current = supportFragmentManager.findFragmentById(R.id.content_main)

        if (id == R.id.nav_releases) {
            if (current is ReleasesOverviewFragment) {
                drawer.closeDrawer(GravityCompat.START)
            }else {
                supportFragmentManager.beginTransaction().replace(R.id.content_main, ReleasesOverviewFragment.newInstance()).commit()
            }
        } else if (id == R.id.nav_discover) {
            if (current is DiscoverFrament) {
                drawer.closeDrawer(GravityCompat.START)
            }else {
                supportFragmentManager.beginTransaction().replace(R.id.content_main, DiscoverFrament.newInstance()).commit()
            }
        } else if (id == R.id.nav_history) {

        } else {

        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }
}
