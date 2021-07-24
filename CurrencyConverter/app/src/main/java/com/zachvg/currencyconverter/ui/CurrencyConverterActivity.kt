package com.zachvg.currencyconverter.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.zachvg.currencyconverter.R
import com.zachvg.currencyconverter.databinding.ActivityCurrencyConverterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CurrencyConverterActivity : AppCompatActivity() {

    private val viewModel: CurrencyConverterViewModel by viewModels()

    private lateinit var binding: ActivityCurrencyConverterBinding

    private lateinit var baseSpinnerAdapter: ArrayAdapter<String>
    private lateinit var targetSpinnerAdapter: ArrayAdapter<String>

    private var eventJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCurrencyConverterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        baseSpinnerAdapter = ArrayAdapter(this, R.layout.currency_spinner_item)
        targetSpinnerAdapter = ArrayAdapter(this, R.layout.currency_spinner_item)

        binding.baseCurrencySpinner.adapter = baseSpinnerAdapter
        binding.targetCurrencySpinner.adapter = targetSpinnerAdapter

        // Observers
        viewModel.currencyDisplayStrings.observe(this) {
            // Update the spinners
            baseSpinnerAdapter.clear()
            baseSpinnerAdapter.addAll(it)

            targetSpinnerAdapter.clear()
            targetSpinnerAdapter.addAll(it)
        }

        viewModel.conversionResults.observe(this) {
            binding.amountFromLabel.text = getString(R.string.conversion_display, it.first)
            binding.amountToLabel.text = it.second
        }

        viewModel.currenciesLoaded.observe(this) { loaded ->
            if (loaded) {
                transitionFromLoadingToMain()
            }
        }

        // Click listeners
        binding.convertButton.setOnClickListener { viewModel.onConvertClick() }

        binding.swapButton.setOnClickListener { viewModel.onSwapClick() }

        // Handle events from the View Model
        eventJob = lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is CurrencyConverterViewModel.Event.Convert -> {
                        convert()
                    }
                    is CurrencyConverterViewModel.Event.InvalidAmount -> {
                        showInvalidAmountMessage()
                    }
                    is CurrencyConverterViewModel.Event.SwapCurrencies -> {
                        swapCurrencies()
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()

        eventJob?.cancel()
    }

    private fun transitionFromLoadingToMain() {
        // Fade out the loading views and fade in all the main views

        val fadeDuration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        with(binding) {
            // Make views visible
            fromLabel.visibility = View.VISIBLE
            toLabel.visibility = View.VISIBLE
            baseCurrencySpinner.visibility = View.VISIBLE
            swapButton.visibility = View.VISIBLE
            targetCurrencySpinner.visibility = View.VISIBLE
            amountInputLayout.visibility = View.VISIBLE
            amountInput.visibility = View.VISIBLE
            convertButton.visibility = View.VISIBLE
            amountFromLabel.visibility = View.VISIBLE
            amountToLabel.visibility = View.VISIBLE

            // Fade out animators
            val fadeOutSet = AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(loadingCurrenciesLabel, "alpha", 0f),
                    ObjectAnimator.ofFloat(loadingCurrenciesProgressBar, "alpha", 0f)
                )

                duration = fadeDuration

                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        loadingCurrenciesLabel.visibility = View.GONE
                        loadingCurrenciesProgressBar.visibility = View.GONE
                    }
                })
            }

            // Fade in animators
            val fadeInSet = AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(fromLabel, "alpha", 1f),
                    ObjectAnimator.ofFloat(toLabel, "alpha", 1f),
                    ObjectAnimator.ofFloat(baseCurrencySpinner, "alpha", 1f),
                    ObjectAnimator.ofFloat(swapButton, "alpha", 1f),
                    ObjectAnimator.ofFloat(targetCurrencySpinner, "alpha", 1f),
                    ObjectAnimator.ofFloat(amountInputLayout, "alpha", 1f),
                    ObjectAnimator.ofFloat(amountInput, "alpha", 1f),
                    ObjectAnimator.ofFloat(convertButton, "alpha", 1f),
                    ObjectAnimator.ofFloat(amountFromLabel, "alpha", 1f),
                    ObjectAnimator.ofFloat(amountToLabel, "alpha", 1f),
                )
            }

            // Total animation
            AnimatorSet().apply {
                play(fadeOutSet).before(fadeInSet)
                start()
            }
        }
    }

    private fun convert() {
        // Get the currency code from the base and target currencies
        val baseCode = binding.baseCurrencySpinner.selectedItem.toString().substring(0, 3)
        val targetCode = binding.targetCurrencySpinner.selectedItem.toString().substring(0, 3)

        val amount = binding.amountInput.text?.toString()

        viewModel.convert(baseCode, targetCode, amount)
    }

    private fun swapCurrencies() = with(binding) {
        // Get the currencies that are currently selected
        val baseCurrencyPosition = baseCurrencySpinner.selectedItemPosition
        val targetCurrencyPosition = targetCurrencySpinner.selectedItemPosition

        // Swap
        baseCurrencySpinner.setSelection(targetCurrencyPosition)
        targetCurrencySpinner.setSelection(baseCurrencyPosition)
    }

    private fun showInvalidAmountMessage() = Snackbar.make(
        binding.root,
        getString(R.string.invalid_amount_message),
        Snackbar.LENGTH_SHORT
    ).show()
}