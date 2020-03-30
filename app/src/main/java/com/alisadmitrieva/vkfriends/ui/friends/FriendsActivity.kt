package com.alisadmitrieva.vkfriends.ui.friends

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.CookieManager
import com.alisadmitrieva.vkfriends.R
import com.alisadmitrieva.vkfriends.State
import com.alisadmitrieva.vkfriends.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_friends.*

class FriendsActivity : AppCompatActivity() {

    private lateinit var viewModel: FriendsListViewModel
    private lateinit var friendsListAdapter: FriendsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        viewModel = ViewModelProviders.of(this)
            .get(FriendsListViewModel::class.java)
        initAdapter()
        initState()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemLogout -> {
                logout()
            }
        }
        return true
    }

    private fun logout() {
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().removeAllCookies(null)
        } else {
            CookieManager.getInstance().removeAllCookie()
        }
        startActivity(Intent(this, LoginActivity::class.java))
        this.finish()
    }

    private fun initAdapter() {
        friendsListAdapter =
            FriendsListAdapter { viewModel.retry() }
        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.adapter = friendsListAdapter
        viewModel.friendsList.observe(this, Observer {
            friendsListAdapter.submitList(it)
        })
    }

    private fun initState() {
        errorMessage.setOnClickListener { viewModel.retry() }
        viewModel.getState().observe(this, Observer { state ->
            progressBar.visibility =
                if (viewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
            errorMessage.visibility =
                if (viewModel.listIsEmpty() && state == State.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                friendsListAdapter.setState(state ?: State.DONE)
            }
        })
    }

}