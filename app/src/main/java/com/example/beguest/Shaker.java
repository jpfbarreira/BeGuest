package com.example.beguest;


import static android.content.Context.SENSOR_SERVICE;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.Nullable;

import com.google.protobuf.StringValue;

public class Shaker {
    private int shakeScore = 0;
    private SensorManager sensorManager;
    private Sensor sensor;

    float [] history = new float[2];

    public void Accelerometer(Context context) {

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float xChange = history[0] - event.values[0];
            float yChange = history[1] - event.values[1];

            history[0] = event.values[0];
            history[1] = event.values[1];

            if (xChange >3){
                shakeScore += 1;
                Log.d("Direction", "Left to Right");
                Log.d("Score", String.valueOf(shakeScore));
            }

            if (xChange <-3){
                shakeScore += 1;
                Log.d("Direction", "Right to Left");
                Log.d("Score", String.valueOf(shakeScore));
            }

            if (yChange >2){
                shakeScore += 2;
                Log.d("Direction", "Down to Up");
                Log.d("Score", String.valueOf(shakeScore));
            }

            if (yChange <-2){
                shakeScore += 2;

                Log.d("Direction", "Up to Down");
                Log.d("Score", String.valueOf(shakeScore));
            }


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    public int getShakeScore() {
        return shakeScore;
    }

    public void resetShakeScore(){
        shakeScore = 0;
    }

    public void stopShaker(){
        sensorManager.unregisterListener(sensorEventListener);
        sensorManager = null;

    }

    /* Para adicionar o shaker é só fazer isto numa activity/class
     //SHAKER PART
            Shaker shaker = new Shaker();
            shaker.Accelerometer(getApplicationContext());
        //
    */
}
