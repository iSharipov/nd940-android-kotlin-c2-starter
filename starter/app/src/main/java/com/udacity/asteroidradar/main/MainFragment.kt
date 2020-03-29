package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.helper.DataHelper

class MainFragment : Fragment() {

    private lateinit var fragmentMainBinding: FragmentMainBinding

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(DataHelper.getInstance(context!!))).get(MainViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpUI(fragmentMainBinding)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMainBinding = FragmentMainBinding.inflate(inflater)
        fragmentMainBinding.lifecycleOwner = this

        fragmentMainBinding.viewModel = viewModel

        setHasOptionsMenu(true)

        return fragmentMainBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    private fun setUpUI(binding: FragmentMainBinding) {
        viewModel.getAsteroids().observe(viewLifecycleOwner, Observer {
            binding.asteroidRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = AsteroidsAdapter(it.getAsteroids())
            }
        })
        viewModel.getPictureOfDay().observe(viewLifecycleOwner, Observer {
            if (it != null) Picasso.with(context).load(it.url).into(binding.imageOfTheDay)

        })
        viewModel.fetchAsteroids()
        viewModel.fetchPictureOfDay()
    }
}
