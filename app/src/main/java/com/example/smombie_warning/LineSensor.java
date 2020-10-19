package com.example.smombie_warning;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

class LineSensor {
    static int threshold;

    static Mat ROIMask(Mat original, Mat binary) {
        Mat dilated = new Mat();
        Mat reversed = new Mat();
        Mat result = new Mat();

        Imgproc.dilate(binary, dilated, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(7, 7)));
        Core.bitwise_not(binary, reversed);
        Core.bitwise_and(dilated, reversed, reversed);

        reversed = SaturationMask(original, reversed);

//        Imgproc.dilate(reversed, dilated, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
//        Core.bitwise_or(binary, dilated, result);
//        Imgproc.erode(result, result, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));

        Imgproc.dilate(reversed, dilated, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(15, 15)));
        Core.bitwise_and(binary, dilated, result);
        //Imgproc.erode(result, result, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3, 3)));

        return result;
    }

    static Mat SaturationMask(Mat input, Mat mask) {
        Mat hsv = new Mat();
        Mat result = new Mat();
        ArrayList<Mat> hsv_split = new ArrayList<>(3);

        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);
        Imgproc.cvtColor(mask, mask, Imgproc.COLOR_GRAY2RGB);
        Core.bitwise_and(hsv, mask, hsv);

        Core.split(hsv, hsv_split);
        Imgproc.threshold(hsv_split.get(1), result, threshold,
                255, Imgproc.THRESH_TOZERO_INV);

        return result;
    }
}
