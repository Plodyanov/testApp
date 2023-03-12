package com.example.testapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.testapp.NavigationHomeDirections
import com.example.testapp.R
import com.example.testapp.databinding.FragmentHomeHostBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeHostFragment : Fragment() {


    private var _binding: FragmentHomeHostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeHostBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupNavigation() {
        val navController =
            (childFragmentManager.findFragmentById(R.id.home_host_fragment) as NavHostFragment).navController
        NavigationUI.setupWithNavController(binding.bottomNavigationBar, navController)

        binding.bottomNavigationBar.setOnItemSelectedListener { item ->
            navController.currentDestination
            when (item.itemId) {
                R.id.page1Fragment -> {
                    navController.navigate(NavigationHomeDirections.actionGlobalPage1Fragment())
                }
                R.id.profileFragment -> {
                    navController.navigate(NavigationHomeDirections.actionGlobalProfileFragment())
                }
            }
            onNavDestinationSelected(item, navController)
        }
    }
}