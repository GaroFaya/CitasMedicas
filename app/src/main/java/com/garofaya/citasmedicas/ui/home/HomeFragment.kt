package com.garofaya.citasmedicas.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.garofaya.citasmedicas.databinding.FragmentHomeBinding
import com.garofaya.citasmedicas.ui.chatbot.ChatBotFragment
import com.garofaya.citasmedicas.R


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflamos la vista principal del Home
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Aquí insertamos el ChatBot dentro del contenedor del HomeFragment
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_chatbot_container, ChatBotFragment())
            .commit()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
