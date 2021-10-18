package pl.mikron.sensors.presentation.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import pl.mikron.sensors.R
import kotlin.math.PI

@BindingMethods(
  value = [
    BindingMethod(
      type = CompassView::class,
      attribute = "azimuth",
      method = "setAzimuth"
    ),
    BindingMethod(
      type = CompassView::class,
      attribute = "angle",
      method = "setAngle"
    )
  ]
)
class CompassView(context: Context, attributeSet: AttributeSet, defStyles: Int) :
  FrameLayout(context, attributeSet, defStyles) {

  constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0)

  init {
    inflate(context, R.layout.view_compass, this)
  }

  fun setAzimuth(azimuth: Float?) {
    azimuth ?: return

    val angle = azimuth.convertToAngle()

    val view = findViewById<ImageView>(R.id.arrowView)

    view.rotation = angle
  }

  fun setAngle(angle: Float?) {
    angle ?: return

    setBackgroundColor(getColorForAngle(angle))
  }

  private fun Float.convertToAngle() =
    -this * 180 / Math.PI.toInt() - 90

  private fun getColorForAngle(angle: Float) =
    when (angle > PI/2) {
      true -> Color.GREEN
      false -> Color.YELLOW
    }
}
