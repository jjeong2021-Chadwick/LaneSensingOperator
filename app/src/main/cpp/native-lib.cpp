#include <jni.h>
#include <string>
#include <iostream>
#include <opencv2/opencv.hpp>
#include <opencv2/features2d.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/objdetect/objdetect.hpp>
#include <opencv2/xfeatures2d/nonfree.hpp>
#include <opencv2/xfeatures2d.hpp>

using namespace std;
using namespace cv;
using namespace cv::xfeatures2d;

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_smombie_1warning_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_smombie_1warning_MainActivity_ConvertRGBtoGray(JNIEnv *env, jobject instance,
                                                                jlong matAddrInput,
                                                                jlong matAddrResult) {

    Mat &input = *(Mat *)matAddrInput;
    Mat &result = *(Mat *)matAddrResult;

    cvtColor(input, input, COLOR_RGBA2GRAY);

    Ptr<SURF> detector = SURF::create();

    vector<KeyPoint> kps_db;
    detector->detect(input, kps_db);
    //drawKeypoints(input, kps_db, result, Scalar::all(-1), DrawMatchesFlags::DRAW_RICH_KEYPOINTS);
}
