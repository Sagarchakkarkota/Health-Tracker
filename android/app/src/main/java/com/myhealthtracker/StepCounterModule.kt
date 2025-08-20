//package com.myhealthtracker
//
//import android.content.Context
//import android.hardware.Sensor
//import android.hardware.SensorEvent
//import android.hardware.SensorEventListener
//import android.hardware.SensorManager
//import com.facebook.react.bridge.Arguments
//import com.facebook.react.bridge.ReactApplicationContext
//import com.facebook.react.bridge.ReactContextBaseJavaModule
//import com.facebook.react.bridge.ReactMethod
//import com.facebook.react.bridge.WritableMap
//import com.facebook.react.modules.core.DeviceEventManagerModule
//
//class StepCounterModule(private val reactContext: ReactApplicationContext):
//    ReactContextBaseJavaModule(reactContext),SensorEventListener{
//private var sensorManager:SensorManager?=null
//    private var stepSensor:Sensor?=null
//
//
//    override fun getName():String{
//        return "StepCounter"
//    }
//
//    override fun initialize() {
//        super.initialize()
//        sensorManager=reactContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        stepSensor=sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
//    }
//    @ReactMethod
//    fun startSensor(){
//       stepSensor?.let {  sensorManager?.registerListener(this,it,SensorManager.SENSOR_DELAY_UI) }
//    }
//    override fun onSensorChanged(event: SensorEvent?) {
//        event?.let {
//             val stepCount:Int=it.values[0].toInt()
//            sendStepCountToJS(stepCount)
//        }
//    }
//
//    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//
//    }
//    @ReactMethod
//     fun stopSensor(){
//        sensorManager?.unregisterListener(this)
//    }
//    private fun sendStepCountToJS(stepCount:Int){
//     val params:WritableMap=Arguments.createMap()
//        params.putInt("stepCount",stepCount)
//        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java).emit("StepCounterUpdate",params)
//    }
//}
//
//
//
package com.myhealthtracker

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule

class StepCounterModule(private val reactContext:ReactApplicationContext):ReactContextBaseJavaModule(reactContext),SensorEventListener{
    private var sensorManager:SensorManager?=null
    private var sensor:Sensor?=null
    override fun getName(): String {
        return "StepCounter"
    }

    override fun initialize() {
        super.initialize()
       sensorManager=reactContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor=sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    }

    @ReactMethod
    fun startSensor(){
    sensor?.let {
    sensorManager?.registerListener(this,it,SensorManager.SENSOR_DELAY_UI)
    }
    }

    override fun onSensorChanged(event: SensorEvent?) {
         event?.let {val count:Int=it.values[0].toInt()
             sendCount(count)
         }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    @ReactMethod
    fun stopSensor(){
        sensorManager?.unregisterListener(this)
    }
    fun sendCount(count:Int){
        val params:WritableMap=Arguments.createMap()
        params.putInt("stepCount",count)
         reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java).emit("stepCountUpdate",params)
    }
}