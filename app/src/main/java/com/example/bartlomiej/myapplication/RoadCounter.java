package com.example.bartlomiej.myapplication;

import java.util.Vector;

/**
 * Created by bartlomiej on 19.10.17.
 */

public class RoadCounter {

    public double road;
    public double velocity;
    private double i_minus_one_time;
    Vector<Float> accVec;
    Vector<Double> da_by_dtVector;
    Vector<Double> deltaTimeVector;
    public int signChangeCounter;
    public double da_by_dt;
    public double da_by_dtMean;
    float acceleration_i_minus_one;
    int buffor = 6000;

    public RoadCounter(){
        road = 0;
        velocity = 0;
        i_minus_one_time = 0;
        signChangeCounter = 1;
        da_by_dt = 0;
        da_by_dtMean = 0;
        acceleration_i_minus_one = 0;
        accVec = new Vector<Float>(buffor);
        da_by_dtVector = new Vector<Double>(buffor);
        deltaTimeVector = new Vector<Double>(buffor);
        for (int i=0; i<accVec.capacity(); i++){
            accVec.add(0.0f);
        }
        for (int i=0; i<da_by_dtVector.capacity(); i++){
            da_by_dtVector.add(0.0);
        }
        for (int i=0; i<deltaTimeVector.capacity(); i++){
            deltaTimeVector.add(0.0);
        }
    }

    public void reset()
    {
        road = 0;
        velocity = 0;
        i_minus_one_time = 0;
        signChangeCounter = 1;
        da_by_dt = 0;
        da_by_dtMean = 0;
        acceleration_i_minus_one = 0;
        deltaTimeVector.clear();
        da_by_dtVector.clear();
        accVec.clear();
        for (int i=0; i<accVec.capacity(); i++){
            accVec.add(0.0f);
        }
        for (int i=0; i<da_by_dtVector.capacity(); i++){
            da_by_dtVector.add(0.0);
        }
        for (int i=0; i<deltaTimeVector.capacity(); i++){
            deltaTimeVector.add(0.0);
        }
    }

    public void CountRoad(float[] acceleration, double i_time){
        if(acceleration[1]< 9.2 && acceleration[0]>2)
            return;
        double deltaTime = (i_time - i_minus_one_time)/1000;
        accChange(acceleration, deltaTime);
        velocity += Math.abs(acceleration[0]) * deltaTime;
        road  += velocity * deltaTime;
        i_minus_one_time = i_time;
    }

    private void accChange(float[] acceleration, double deletaTime){
        Boolean minusSign = false;
        Boolean plusSign = false;
        da_by_dt = Math.round(acceleration[0]/deletaTime);
        for(int i = accVec.capacity()-1; i >= 1; i--){
            accVec.set(i,accVec.elementAt(i-1));
        }
        accVec.set(0,acceleration[0]);
        for(int i = deltaTimeVector.capacity()-1; i >= 1; i--){
            deltaTimeVector.set(i, deltaTimeVector.elementAt(i-1));
        }
        deltaTimeVector.set(0,deletaTime);
            float accSum = 0;
            double deltaSum = 0;

            // wyciaganie sredniej
            for (float acc : accVec){
                accSum += acc;
                if (acc>0)
                    plusSign = true;
                if(acc<0)
                    minusSign = true;
            }
            for (double delta : deltaTimeVector){
                deltaSum += delta;
            }
            da_by_dtMean = Math.round(accSum/deltaSum);

            if(plusSign == true && minusSign == true && Math.abs(da_by_dtMean) > 35 && Math.abs(da_by_dtMean)< 160)
            {
                signChangeCounter++;
                deltaTimeVector.clear();
                da_by_dtVector.clear();
                accVec.clear();
                for (int i=0; i<accVec.capacity(); i++){
                    accVec.add(0.0f);
                }
                for (int i=0; i<da_by_dtVector.capacity(); i++){
                    da_by_dtVector.add(0.0);
                }
                for (int i=0; i<deltaTimeVector.capacity(); i++){
                    deltaTimeVector.add(0.0);
                }
            }

            if(!accVec.contains(0)){
                accVec.clear();
                for (int i=0; i<accVec.capacity(); i++){
                    accVec.add(0.0f);
                }
            }
            if(!da_by_dtVector.contains(0)){
                da_by_dtVector.clear();
                for (int i=0; i<da_by_dtVector.capacity(); i++){
                    da_by_dtVector.add(0.0);
                }
            }
            if(!deltaTimeVector.contains(0)){
                deltaTimeVector.clear();
                for (int i=0; i<deltaTimeVector.capacity(); i++){
                    deltaTimeVector.add(0.0);
                }
            }
    }

    public void resizeVectors(){
        accVec.setSize(buffor);
        da_by_dtVector.setSize(buffor);
        deltaTimeVector.setSize(buffor);
    }
}
