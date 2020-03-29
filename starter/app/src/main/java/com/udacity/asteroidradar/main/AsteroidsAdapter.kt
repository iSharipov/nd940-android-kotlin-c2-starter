package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidsItemCardBinding

class AsteroidsAdapter(private val asteroids: List<Asteroid>, private val onAsteroidClickListener: OnAsteroidClickListener) :
    RecyclerView.Adapter<AsteroidsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        LayoutInflater.from(parent.context).also { layoutInflater ->
            AsteroidsItemCardBinding.inflate(layoutInflater).also { binding ->
                binding.onAsteroidClickListener = onAsteroidClickListener
                return ViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = asteroids.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        asteroids[position].also { holder.bindAsteroid(it) }
    }

    class ViewHolder(private val asteroidsItemCardBinding: AsteroidsItemCardBinding) : RecyclerView.ViewHolder(asteroidsItemCardBinding.root) {
        fun bindAsteroid(asteroid: Asteroid) {
            asteroidsItemCardBinding.asteroid = asteroid
            asteroidsItemCardBinding.executePendingBindings()
        }
    }
}

class OnAsteroidClickListener(private val listener: (asteroid: Asteroid) -> Unit) {
    fun onItemClicked(asteroid: Asteroid) = listener(asteroid)
}