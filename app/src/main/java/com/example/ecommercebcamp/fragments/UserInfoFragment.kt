package com.example.ecommercebcamp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ecommercebcamp.R
import com.example.ecommercebcamp.databinding.FragmentHomeBinding
import com.example.ecommercebcamp.databinding.FragmentUserInfoBinding

class UserInfoFragment : Fragment() {

    private var _binding: FragmentUserInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE)

        binding.btnSave.setOnClickListener {
            val name = binding.tvUserName.text.toString()
            val gender = when (binding.rgGender.checkedRadioButtonId){
                R.id.rb_male -> "male"
                R.id.rb_female -> "female"
                else -> "other"
            }

            sharedPref.edit()
                .putString("userName", name)
                .putString("userGender", gender)
                .apply()

            findNavController().navigate(R.id.action_userInfoFragment_to_homeFragment)

        }
    }

}