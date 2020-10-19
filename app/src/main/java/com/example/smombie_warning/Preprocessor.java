package com.example.smombie_warning;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;

class Preprocessor {

    private static ArrayList<Mat> rgba = new ArrayList<>(4);
    private static ArrayList<Mat> hist = new ArrayList<>(4);
    private static Mat ret = new Mat();
    private static Mat filter = new Mat();
    private static MatOfInt channel = new MatOfInt(0);
    private static MatOfInt histSize = new MatOfInt(25);
    private static MatOfFloat ranges = new MatOfFloat(0f, 256f);

    static Mat RGBBasedThreshold(Mat input) {
        if (ret == null) {
            ret = new Mat(input.rows(), input.cols(), input.type());
        }

        Core.split(input, rgba);

        MatOfDouble std = new MatOfDouble();
        MatOfDouble mean = new MatOfDouble();
        Core.meanStdDev(rgba.get(0), mean, std);
        Imgproc.threshold(rgba.get(0), rgba.get(0),
                mean.get(0, 0)[0] + 1.7f * std.get(0, 0)[0],
                255, Imgproc.THRESH_TOZERO);

        Core.meanStdDev(rgba.get(1), mean, std);
        Imgproc.threshold(rgba.get(1), rgba.get(1),
                mean.get(0, 0)[0] + 1.7f * std.get(0, 0)[0],
                255, Imgproc.THRESH_TOZERO);


        Core.meanStdDev(rgba.get(2), mean, std);
        Imgproc.threshold(rgba.get(2), rgba.get(2),
                mean.get(0, 0)[0] + 1.7f * std.get(0, 0)[0],
                255, Imgproc.THRESH_TOZERO);


        Core.bitwise_and(rgba.get(0), rgba.get(1), filter);
        Core.bitwise_and(filter, rgba.get(2), filter);

        ret.release();
        Core.bitwise_and(input, input, ret, filter);

        return ret; 
    }


    private static Size blurSize = new Size(3, 3);

    static Mat NoiseReduction(Mat input) {
        ret.release();
        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.GaussianBlur(input, input, blurSize, 5);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Imgproc.erode(input, ret, kernel);

        return ret;
    }
}
