package pl.mikron.sensors.sensors

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.acos

@Singleton
class Accelerometer @Inject constructor(
  @ApplicationContext private val context: Context
) {

  private val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager

  fun observe(): Flowable<SensorData> = Flowable.combineLatest(
    observeSensor(Sensor.TYPE_ACCELEROMETER),
    observeSensor(Sensor.TYPE_MAGNETIC_FIELD)
  )
  { accData, magneticData ->
    var orientationText = ""
    var azimuth = 0f
    var angle = 0f
    val r = FloatArray(9)
    val i = FloatArray(9)
    if (SensorManager.getRotationMatrix(r, i, accData.values, magneticData.values)) {
      val orientation = FloatArray(3)
      SensorManager.getOrientation(r, orientation)
      orientationText = orientation.asList().joinToString()
      azimuth = orientation.getOrNull(0) ?: 0f
      angle = acos(r[8])
    }
    return@combineLatest SensorData(
      accData = accData.values.asList().joinToString(),
      orientation = orientationText,
      azimuth = azimuth,
      angle = angle
    )
  }

  private fun observeSensor(type: Int): Flowable<SensorEvent> = Flowable.create({ emitter ->

    val sensor = sensorManager.getDefaultSensor(type)

    if (sensor == null) {
      emitter.onError(Throwable("Sensors not available for type $type"))
      return@create
    }

    sensorManager
      .registerListener(object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent) =
          emitter.onNext(event)

        override fun onAccuracyChanged(sensor: Sensor, point: Int) {
          /* No op */
        }

      }, sensor, 100)
  }, BackpressureStrategy.BUFFER)
}
