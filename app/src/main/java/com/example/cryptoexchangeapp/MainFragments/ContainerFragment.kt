package com.example.cryptoexchangeapp.MainFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptoexchangeapp.R
import com.example.cryptoexchangeapp.databinding.FragmentContainerBinding

class ContainerFragment : Fragment() {

    private lateinit var binding: FragmentContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFragment(MainFragment())
        bottomNavigationClick()
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout,fragment)
        transaction.commit()
    }

    private fun bottomNavigationClick(){
        val bottomNav = binding.bottomNavigationViewContainerFragment

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.main -> {loadFragment(MainFragment())}
                R.id.account -> {loadFragment(AccountFragment())}
                R.id.wallet -> {loadFragment(WalletFragment())}
                R.id.settings -> {loadFragment(SettingsFragment())}
            }
            true
        }
    }

}