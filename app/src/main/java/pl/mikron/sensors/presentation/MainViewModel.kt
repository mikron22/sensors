package pl.mikron.sensors.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import pl.mikron.sensors.sensors.Accelerometer
import pl.mikron.sensors.sensors.SensorData
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val accelerometer: Accelerometer
) : ViewModel() {

  private val tag = MainViewModel::class.simpleName

  private val disposables = CompositeDisposable()

  private val _sensorData: MutableLiveData<SensorData> =
    MutableLiveData()

  val sensorData: LiveData<SensorData> =
    _sensorData

  override fun onCleared() {
    super.onCleared()
    disposables.dispose()
  }

  internal fun observeAccelerometer() {
    disposables.add(
      accelerometer
        .observe()
        .subscribe(
          _sensorData::postValue
        ) { Log.e(tag, "An error occurred: $it") }
    )
  }
}
