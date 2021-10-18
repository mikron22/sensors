package pl.mikron.sensors.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import pl.mikron.sensors.R
import pl.mikron.sensors.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private val viewModel: MainViewModel
      by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this

    viewModel.observeAccelerometer()
  }
}