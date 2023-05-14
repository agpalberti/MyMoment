package com.agp.mymoment.config

import android.os.Build
import android.util.DisplayMetrics
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.agp.mymoment.config.DeviceConfig.Companion.setDpValues
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun GetDeviceConfig() {

    val configuration = LocalConfiguration.current


    setDpValues(configuration.screenHeightDp.dp,configuration.screenWidthDp.dp, LocalDensity.current.density )
}

class DeviceConfig{

    companion object{

        var screenHeight : Dp = 0.dp
        var screenWidth : Dp = 0.dp
        var dpi : Float = 0f

        fun setDpValues(height: Dp, width: Dp, dpi: Float){
            screenHeight = height
            screenWidth = width
            this.dpi = dpi
        }

        fun returnHeight():Dp{
            return screenHeight
        }

        fun returnWidth():Dp{
            return screenWidth
        }

        fun heightPercentage(targetPercentage : Int): Dp {
            return ((targetPercentage * screenHeight.toString().substringBefore('.').toInt())/100).dp
        }

        fun widthPercentage(targetPercentage : Int): Dp {
            return ((targetPercentage * screenWidth.toString().substringBefore('.').toInt())/100).dp
        }

        fun DPheightPercentage(initialDP : Dp, targetPercentage : Int):Dp{
            return ((targetPercentage * initialDP.toString().substringBefore('.').toInt())/100).dp
        }

        fun DPwidthPercentage(initialDP : Dp, targetPercentage : Int):Dp{
            return ((targetPercentage * initialDP.toString().substringBefore('.').toInt())/100).dp
        }

        fun dpToFloat(initialDP: Dp):Float{
            return ((initialDP.toString().substringBefore('.').toFloat() * 1)/ returnWidth().toString().substringBefore('.').toFloat())
        }

        //Cuanto corresponde 0.5f en big?
        fun tinyFloatToBig(initialFloat:Float):Float{
            return initialFloat * returnWidth().toString().substringBefore('.').toFloat()
        }

        fun dpToPx(initDP: Dp):Float{
            return (initDP.toString().substringBefore('.').toFloat() * (dpi / DisplayMetrics.DENSITY_DEFAULT))
        }

        fun pxToDp(px: Float): Dp{
            return (px / (dpi/ DisplayMetrics.DENSITY_DEFAULT)).dp
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun translateDate(postDate: String): String{

            val targetDate = LocalDateTime.parse(postDate, DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))

            val difference = Duration.between(targetDate, LocalDateTime.now()).toMillis()

            when {
                difference < 60000 -> {
                    return "hace " + (difference / 1000).toString() + "s"
                }
                difference < 3600000 -> {
                    return "hace " + (difference / 60000).toString() + "m"
                }

                difference < 86400000 -> {
                    return "hace " + (difference / 3600000).toString() + "h"
                }

                difference < 604800000 -> {
                    return "hace " + (difference / 86400000).toString() + "d"
                }
                difference < 2419200000  -> {
                    return "hace " + (difference / 604800000 ).toString() + " semanas"
                }
                difference < 31536000000  -> {
                    return "hace " + (difference / 2628000000 ).toString() + " meses"
                }
                else -> {
                    return "hace " + (difference / 31536000000).toString() + " años"
                }
            }
        }



    }

}