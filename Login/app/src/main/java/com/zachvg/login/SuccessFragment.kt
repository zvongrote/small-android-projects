package com.zachvg.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zachvg.login.databinding.FragmentSuccessBinding

class SuccessFragment : Fragment() {

    private var binding: FragmentSuccessBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSuccessBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.backButton?.setOnClickListener {
            findNavController().navigate(R.id.action_successFragment_to_loginFragment)
        }
    }

    override fun onResume() {
        super.onResume()

        runSuccessfulLoginAnimations()
    }


    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

    private fun runSuccessfulLoginAnimations() {

        val animationTime = 1000L

        val loginAnimator = ObjectAnimator.ofFloat(binding?.loginTextView, "alpha", 1f).apply {
            duration = animationTime
        }

        val successfulAnimator =
            ObjectAnimator.ofFloat(binding?.successfulTextView, "alpha", 1f).apply {
                duration = animationTime
            }

        val buttonAnimator = ObjectAnimator.ofFloat(binding?.backButton, "alpha", .9f).apply {
            duration = animationTime
        }

        AnimatorSet().apply {
            playSequentially(loginAnimator, successfulAnimator, buttonAnimator)
            start()
        }
    }

}