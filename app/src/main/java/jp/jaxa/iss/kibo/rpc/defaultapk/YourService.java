package jp.jaxa.iss.kibo.rpc.defaultapk;


import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;

import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {

    double angle = Math.sqrt(2)/2;

    // Point, Astrobee can arrive
    // A - B - P1 - P6 - P7 - C - P2 - D - P5 - P4 - F - P3 - PG
    Point P_Goal = new Point(11.143, -6.7607, 4.9654);
    Point P1 = new Point(11.2746, -9.92284, 5.2988);
    Point P2 = new Point(10.612, -9.0709, 4.48);
    Point P3 = new Point(10.71, -7.7, 4.48);
    Point P4 = new Point(10.51, -6.7185, 5.1804);
    Point P5 = new Point(11.114, -7.9756, 5.3393);
    Point P6 = new Point(11.355, -8.9929, 4.7818);
    Point P7 = new Point(11.369,-8.5518,4.48);
    Point A = new Point(10.51, -9.81 , 4.58);
    Point B = new Point(10.58,-9.81,5.25);
    Point C = new Point(10.78, -9.07, 5.34);
    Point D = new Point(10.58, -8, 5.34);
    Point E = new Point(10.58, -7.71, 4.48);
    Point F = new Point(10.55, -7.4, 5);

    Point OneToTwo = new Point(10.9,-9.5,5.3);
    Point OneToThree = new Point(10.79,-8.08,4.86);
    Point TwoToThree_1 = new Point(10.65,-8.5,4.9);
    Point TwoToThree_2 = new Point(10.68,-8.13,4.89);
    Point TwoToFour = new Point(10.52,-8.56,4.85);
    Point TwoToFive = new Point(10.95,-8.25,5.33);
    Point TwoToSix = new Point(10.7,-9.14,4.83);
    Point TwoToSeven_1 = new Point(10.68,-9.02,4.835);
    Point TwoToSeven_2 = new Point(11.14,-8.71,4.835);
    Point ThreeToFour = new Point(10.71,-7.32,5);
    Point ThreeToFive_1 = new Point(10.6,-7.75,4.9);
    Point ThreeToFive_2 = new Point(10.6,-7.75,5.31);
    Point ThreeToSix_1 = new Point(10.83,-8.11,4.87);
    Point ThreeToSeven_1 = new Point(11.02,-8.1,4.85);
    Point ThreeToSeven_2 = new Point(11.369,-8.5518,4.85);
    Point FourToSix_1 = new Point(10.88,-7.54,4.91);
    Point FourToSix_2 = new Point(11.01,-8.25,4.91);
    Point FourToSeven_1 = new Point(10.89,-7.56,4.91);
    Point FourToSeven_2 = new Point(11.369,-8.5518,4.85);
    Point FiveToSix = new Point(11.2,-8.24,5.3);
    Point FiveToSeven_1 = new Point(11.27,-8.25,5.3);
    Point FiveToSeven_2 = new Point(11.369,-8.5518,4.85);

    Quaternion Q_Goal = new Quaternion(0f, 0f, (float)-angle, (float)angle);
    Quaternion Q1 = new Quaternion(0f, 0f, (float)-angle, (float)angle);
    Quaternion Q2 = new Quaternion(0.5f, 0.5f, -0.5f, 0.5f);
    Quaternion Q3 = new Quaternion(0f, (float)angle, 0, (float)angle);
    Quaternion Q4 = new Quaternion(0f, 0f, -1f, 0f);
    Quaternion Q5 = new Quaternion(-0.5f, -0.5f, -0.5f, 0.5f);
    Quaternion Q6 = new Quaternion(0, 0, 0, 1);
    Quaternion Q7 = new Quaternion(0, (float)angle, 0, (float)angle);


    // Adjust Point (Laser fixed)
    Point AP1 = new Point(11.2053,-9.72284,5.4736);   // (-0.0572, 0, 0.1111)
    Point AP2 = new Point(10.456184,-9.196272,4.68);  // (-0.0572, -0.1111, 0)
    Point AP3 = new Point(10.7142,-7.76727,4.68);     // (0.1111, -0.0572, 0)
    Point AP4 = new Point(10.71,-6.616772,5.20641);   // (0, 0.0572, 0.1111)
    Point AP5 = new Point(11.0448,-7.9193,5.3393);    // (-0.0572, 0.1111, 0)
    Point AP6 = new Point(11.155,-9.0462,4.9416);     // (0, -0.0572, 0.1111)
    Point AP7 = new Point(11.381944, -8.623372,4.5911); // (0, -0.0572, 0.1111)


    List<betweenDistance> DistanceCompare = new ArrayList<>();
    List<Integer> active_targets = new ArrayList<>();
    List<Long> time = new ArrayList<>();
    double [][]TravelTime = {

            {1000000, 53328, 31640, 68144, 69840, 59360, 45920, 1000000, 1000000},
            {53328, 1000000, 34984, 46608, 43960, 32896, 21104, 1000000, 53016},
            {31640, 35040, 1000000, 50952, 49480, 41208, 30520, 1000000, 50504},
            {68144, 46704, 50976, 1000000, 40280, 54088, 43696, 1000000, 23789},
            {69840, 39256, 49552, 40880, 1000000, 29040, 61216, 1000000, 16244},
            {59360, 30248, 50736, 52528, 29192, 1000000, 39184, 1000000, 34864},
            {45920, 24120, 28272, 40152, 62072, 34832, 1000000, 1000000, 46760},
            {1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000},
            {100,3.06,2.56,1.13,0.52,1.26,2.29,100,100}

    };

    String qr = "";

    double Distance[][] = {
            {100, 1.5, 0.88, 2.44, 3.82, 2.83, 1.29, 100,100},
            {1.5, 100, 1.53, 3, 2.96, 2.13, 1.07, 1.6,3.06},
            {0.88, 1.53, 100, 1.67, 2.52, 1.71, 1.16, 1.37,2.56},
            {2.44, 3, 1.67, 100, 1.29, 1.37, 1.59, 1.57,1.13},
            {3.82, 2.96, 2.52, 1.29, 100, 1.25, 2.48, 2.43,0.52},
            {2.83, 2.13, 1.71, 1.37, 1.25, 100, 1.38, 1.4,1.26},
            {1.29, 1.07, 1.16, 1.59, 2.48, 1.38, 100, 0.53,2.29},
            {100, 1.6, 1.37, 1.57, 2.43, 1.4, 0.53, 100,100},
            {100,3.06,2.56,1.13,0.52,1.26,2.29,100,100}
    };


    int currentStep = 0;
    int times = 0;
    int phase = 1;
    double speed;
    boolean hasScan = true;


    @Override
    protected void runPlan1(){

        api.startMission();


        while (phase < 10) {



            active_targets = api.getActiveTargets();
            time = api.getTimeRemaining();
            if (active_targets.size() == 1) {
                if (TravelTime[currentStep][active_targets.get(0)] < time.get(0)) {
                    if (TravelTime[currentStep][active_targets.get(0)] + TravelTime[active_targets.get(0)][8] < time.get(1)) {
                        move(currentStep, active_targets.get(0));
                        currentStep = active_targets.get(0);
                        if (currentStep == 6 && hasScan) {
                            api.moveTo(AP6, Q7, false);

                            try{
                                capture("Start to scan");
                                qr = Qrcode(api.getMatNavCam(),370,400,650,150,800,800);
                                api.saveMatImage(api.getMatNavCam(),qr+" I am qr 2");
                            }
                            catch(Exception ingore){
                                api.saveMatImage(api.getMatNavCam(),"Nav");
                            }
                            hasScan = false;
                        }
                        phase++;
                    } else {
                        move(currentStep, 8);
                        api.reportMissionCompletion(qr);
                    }
                } else if (TravelTime[currentStep][active_targets.get(0)] > time.get(0)) {
                    if (TravelTime[currentStep][8] < time.get(1) - time.get(0)) {
                        try {
                            Thread.sleep(time.get(0));
                        } catch (Exception ignored) {
                        }
                        phase++;
                    } else {
                        move(currentStep, 8);
                        api.reportMissionCompletion(qr);
                    }
                }
            } else if (active_targets.size() == 2) {
                if (TravelTime[currentStep][active_targets.get(1)] < TravelTime[currentStep][active_targets.get(0)] && (TravelTime[currentStep][active_targets.get(1)] + TravelTime[active_targets.get(1)][active_targets.get(0)]) < time.get(0)) {
                    if (TravelTime[currentStep][active_targets.get(1)] + TravelTime[active_targets.get(1)][8] < time.get(1)) {
                        move(currentStep, active_targets.get(1));
                        currentStep = active_targets.get(1);
                        if (active_targets.get(1) == 6 && hasScan) {
                            try{
                                capture("Start to scan");
                                qr = Qrcode(api.getMatNavCam(),370,400,650,150,800,800);
                                api.saveMatImage(api.getMatNavCam(),qr+" I am qr 2");
                            }
                            catch(Exception ingore){
                                api.saveMatImage(api.getMatNavCam(),"Nav");
                            }
                            hasScan = false;
                        }
                        time = api.getTimeRemaining();
                        if (TravelTime[currentStep][active_targets.get(0)] + TravelTime[active_targets.get(0)][8] < time.get(1)) {
                            if (TravelTime[currentStep][active_targets.get(0)] < time.get(0)) {
                                move(currentStep, active_targets.get(0));
                                if (active_targets.get(0) == 6 && hasScan) {
                                    try{
                                        capture("Start to scan");
                                        qr = Qrcode(api.getMatNavCam(),370,400,650,150,800,800);
                                        api.saveMatImage(api.getMatNavCam(),qr+" I am qr 2");
                                    }
                                    catch(Exception ingore){
                                        api.saveMatImage(api.getMatNavCam(),"Nav");
                                    }
                                    hasScan = false;
                                }
                                currentStep = active_targets.get(0);
                                phase++;
                            } else if (TravelTime[currentStep][8] < time.get(1) - time.get(0)) {
                                try {
                                    Thread.sleep(time.get(0));
                                } catch (Exception ignored) {
                                }
                                phase++;
                            } else {
                                move(currentStep, 8);
                                api.reportMissionCompletion(qr);
                            }
                        }
                    } else {
                        move(currentStep, 8);
                        api.reportMissionCompletion(qr);
                    }
                } else if (TravelTime[currentStep][active_targets.get(1)] > TravelTime[currentStep][active_targets.get(0)] && (TravelTime[currentStep][active_targets.get(0)] + TravelTime[active_targets.get(1)][active_targets.get(0)]) < time.get(0)) {
                    if (TravelTime[currentStep][active_targets.get(0)] + TravelTime[active_targets.get(0)][8] < time.get(1)) {
                        move(currentStep, active_targets.get(0));
                        currentStep = active_targets.get(0);
                        if (active_targets.get(0) == 6 && hasScan) {
                            try{
                                capture("Start to scan");
                                qr = Qrcode(api.getMatNavCam(),370,400,650,150,800,800);
                                api.saveMatImage(api.getMatNavCam(),qr+" I am qr 2");
                            }
                            catch(Exception ingore){
                                api.saveMatImage(api.getMatNavCam(),"Nav");
                            }
                            hasScan = false;
                        }
                        time = api.getTimeRemaining();
                        if (TravelTime[currentStep][active_targets.get(1)] + TravelTime[active_targets.get(1)][8] < time.get(1)) {
                            if (TravelTime[currentStep][active_targets.get(1)] < time.get(0)) {
                                move(currentStep, active_targets.get(1));
                                if (active_targets.get(1) == 6 && hasScan) {
                                    try{
                                        capture("Start to scan");
                                        qr = Qrcode(api.getMatNavCam(),370,400,650,150,800,800);
                                        api.saveMatImage(api.getMatNavCam(),qr+" I am qr 2");
                                    }
                                    catch(Exception ingore){
                                        api.saveMatImage(api.getMatNavCam(),"Nav");
                                    }
                                    hasScan = false;
                                }
                                currentStep = active_targets.get(1);
                                phase++;
                            } else if (TravelTime[currentStep][8] < time.get(1) - time.get(0)) {
                                try {
                                    Thread.sleep(time.get(0));
                                } catch (Exception ignored) {
                                }
                                phase++;
                            } else {
                                move(currentStep, 8);
                                api.reportMissionCompletion(qr);
                            }
                        }
                    } else {
                        move(currentStep, 8);
                        api.reportMissionCompletion(qr);
                    }
                } else if (TravelTime[currentStep][active_targets.get(1)] < TravelTime[currentStep][active_targets.get(0)] && (TravelTime[currentStep][active_targets.get(1)]) < time.get(0)) {
                    if (TravelTime[currentStep][active_targets.get(1)] + TravelTime[active_targets.get(1)][8] < time.get(1)) {
                        move(currentStep, active_targets.get(1));
                        if (active_targets.get(1) == 6 && hasScan) {
                            try{
                                capture("Start to scan");
                                qr = Qrcode(api.getMatNavCam(),370,400,650,150,800,800);
                                api.saveMatImage(api.getMatNavCam(),qr+" I am qr 2");
                            }
                            catch(Exception ingore){
                                api.saveMatImage(api.getMatNavCam(),"Nav");
                            }
                            hasScan = false;
                        }
                        currentStep = active_targets.get(1);
                        time = api.getTimeRemaining();
                        if (TravelTime[currentStep][8] < time.get(1) - time.get(0)) {
                            try {
                                Thread.sleep(time.get(0));
                            } catch (Exception ignored) {
                            }
                            phase++;
                        } else {
                            move(currentStep, 8);
                            api.reportMissionCompletion(qr);
                        }
                    } else {
                        move(currentStep, 8);
                        api.reportMissionCompletion(qr);
                    }
                } else if (TravelTime[currentStep][active_targets.get(1)] > TravelTime[currentStep][active_targets.get(0)] && (TravelTime[currentStep][active_targets.get(0)]) < time.get(0)) {
                    if (TravelTime[currentStep][active_targets.get(0)] + TravelTime[active_targets.get(0)][8] < time.get(1)) {
                        move(currentStep, active_targets.get(0));
                        if (active_targets.get(0) == 6 && hasScan) {
                            try{
                                capture("Start to scan");
                                qr = Qrcode(api.getMatNavCam(),370,400,650,150,800,800);
                                api.saveMatImage(api.getMatNavCam(),qr+" I am qr 2");
                            }
                            catch(Exception ingore){
                                api.saveMatImage(api.getMatNavCam(),"Nav");
                            }
                            hasScan = false;
                        }
                        currentStep = active_targets.get(0);
                        time = api.getTimeRemaining();
                        if (TravelTime[currentStep][8] < time.get(1) - time.get(0)) {
                            try {
                                Thread.sleep(time.get(0));
                            } catch (Exception ignored) {
                            }
                            phase++;
                        } else {
                            move(currentStep, 8);
                            api.reportMissionCompletion(qr);
                        }
                    } else {
                        move(currentStep, 8);
                        api.reportMissionCompletion(qr);
                    }
                }
            } else if (active_targets.size() == 3) {
                betweenDistance a = new betweenDistance(active_targets.get(0), active_targets.get(1), active_targets.get(2), TravelTime[currentStep][active_targets.get(0)] + TravelTime[active_targets.get(0)][active_targets.get(1)] + TravelTime[active_targets.get(1)][active_targets.get(2)]);
                DistanceCompare.add(a);
                betweenDistance b = new betweenDistance(active_targets.get(0), active_targets.get(2), active_targets.get(1), TravelTime[currentStep][active_targets.get(0)] + TravelTime[active_targets.get(0)][active_targets.get(2)] + TravelTime[active_targets.get(1)][active_targets.get(2)]);
                DistanceCompare.add(a);
                betweenDistance c = new betweenDistance(active_targets.get(1), active_targets.get(0), active_targets.get(2), TravelTime[currentStep][active_targets.get(1)] + TravelTime[active_targets.get(1)][active_targets.get(0)] + TravelTime[active_targets.get(0)][active_targets.get(2)]);
                DistanceCompare.add(a);
                betweenDistance d = new betweenDistance(active_targets.get(1), active_targets.get(2), active_targets.get(0), TravelTime[currentStep][active_targets.get(1)] + TravelTime[active_targets.get(1)][active_targets.get(2)] + TravelTime[active_targets.get(2)][active_targets.get(0)]);
                DistanceCompare.add(a);
                betweenDistance e = new betweenDistance(active_targets.get(2), active_targets.get(0), active_targets.get(1), TravelTime[currentStep][active_targets.get(2)] + TravelTime[active_targets.get(0)][active_targets.get(2)] + TravelTime[active_targets.get(0)][active_targets.get(1)]);
                DistanceCompare.add(a);
                betweenDistance f = new betweenDistance(active_targets.get(2), active_targets.get(1), active_targets.get(0), TravelTime[currentStep][active_targets.get(2)] + TravelTime[active_targets.get(2)][active_targets.get(1)] + TravelTime[active_targets.get(1)][active_targets.get(0)]);
                DistanceCompare.add(a);
                DistanceCompare.add(b);
                DistanceCompare.add(c);
                DistanceCompare.add(d);
                DistanceCompare.add(e);
                DistanceCompare.add(f);

                DistanceCompare.sort(new BetweenDistanceComparator());

                if (TravelTime[currentStep][DistanceCompare.get(0).pt1] + TravelTime[DistanceCompare.get(0).pt1][8] < time.get(1)) {
                    move(currentStep, DistanceCompare.get(0).pt1);
                    if (DistanceCompare.get(0).pt1 == 6 && hasScan) {
                        try{
                            capture("Start to scan");
                            qr = Qrcode(api.getMatNavCam(),370,400,650,150,800,800);
                            api.saveMatImage(api.getMatNavCam(),qr+" I am qr 2");
                        }
                        catch(Exception ingore){
                            api.saveMatImage(api.getMatNavCam(),"Nav");
                        }
                        hasScan = false;
                    }
                    currentStep = DistanceCompare.get(0).pt1;
                    time = api.getTimeRemaining();

                    if (TravelTime[currentStep][DistanceCompare.get(0).pt2] < time.get(0)) {
                        capture(Long.toString(api.getTimeRemaining().get(0)));
                        if (TravelTime[currentStep][DistanceCompare.get(0).pt2] + TravelTime[DistanceCompare.get(0).pt2][8] < time.get(1)) {
                            move(currentStep, DistanceCompare.get(0).pt2);
                            if (DistanceCompare.get(0).pt2 == 6 && hasScan) {
                                try{
                                    capture("Start to scan");
                                    qr = Qrcode(api.getMatNavCam(),370,400,650,150,800,800);
                                    api.saveMatImage(api.getMatNavCam(),qr+" I am qr 2");
                                }
                                catch(Exception ingore){
                                    api.saveMatImage(api.getMatNavCam(),"Nav");
                                }
                                hasScan = false;
                            }
                            currentStep = DistanceCompare.get(0).pt2;
                            time = api.getTimeRemaining();
                            if (TravelTime[currentStep][DistanceCompare.get(0).pt3] < time.get(0)) {
                                if (TravelTime[currentStep][DistanceCompare.get(0).pt3] + TravelTime[DistanceCompare.get(0).pt3][8] < time.get(1)) {
                                    move(currentStep, DistanceCompare.get(0).pt3);
                                    if (DistanceCompare.get(0).pt3 == 6 && hasScan) {
                                        try{
                                            capture("Start to scan");
                                            qr = Qrcode(api.getMatNavCam(),370,400,650,150,800,800);
                                            api.saveMatImage(api.getMatNavCam(),qr+" I am qr 2");
                                        }
                                        catch(Exception ingore){
                                            api.saveMatImage(api.getMatNavCam(),"Nav");
                                        }
                                        hasScan = false;
                                    }
                                    currentStep = DistanceCompare.get(0).pt3;
                                    phase++;
                                } else if (TravelTime[currentStep][8] < time.get(1) - time.get(0)) {
                                    try {
                                        Thread.sleep(time.get(0));
                                    } catch (Exception ignored) {
                                    }
                                    phase++;
                                } else {
                                    move(currentStep, 8);
                                    api.reportMissionCompletion(qr);
                                }
                            } else if (TravelTime[currentStep][8] < time.get(1) - time.get(0)) {
                                try {
                                    Thread.sleep(time.get(0));
                                } catch (Exception ignored) {
                                }
                                phase++;
                            } else {
                                move(currentStep, 8);
                                api.reportMissionCompletion(qr);
                            }
                        }
                    } else if (TravelTime[currentStep][8] < time.get(1) - time.get(0)) {
                        try {
                            Thread.sleep(time.get(0));
                        } catch (Exception ignored) {
                        }
                        phase++;
                    } else {
                        move(currentStep, 8);
                        api.reportMissionCompletion(qr);
                    }
                } else {
                    move(currentStep, 8);
                    api.reportMissionCompletion(qr);
                }
            }
        }

    }

    public String Qrcode(Mat image, int width,int height,int startX,int startY,int bigW,int bigH){
        QRCodeDetector qrCodeDetector = new QRCodeDetector();
        api.saveMatImage(image,"I am the one you use to scan");


        Mat imgCut = new Mat(width,height, CvType.CV_8UC3);
        Mat imgROI = new Mat(image,new Rect(startX,startY,width,height));
        imgROI.copyTo(imgCut);

        api.saveMatImage(imgCut,"cutImage");

        Mat bigImg = new Mat();
        Imgproc.resize(imgCut,bigImg,new Size(bigW,bigH));
        api.saveMatImage(bigImg,"I am bigger");

        Mat points = new Mat();
        Mat straight_qrcode = new Mat();
        String data = qrCodeDetector.detectAndDecode(bigImg, points, straight_qrcode);

        if (data.length() > 0) {
            System.out.println("Decoded Data :" + data);
            capture(data+" I am data");
        }
        else{
            capture("scan fail");
        }

        String answer = "";

        switch (data){
            case "JEM":
                answer = "STAY_AT_JEM";
                break;
            case "COLUMBUS":
                answer = "GO_TO_COLUMBUS";
                break;
            case "RACK1":
                answer = "CHECK_RACK_1";
                break;
            case "ASTROBEE":
                answer = "I_AM_HERE";
                break;
            case "INTBALL":
                answer = "LOOKING_FORWARD_TO_SEE_YOU";
                break;
            case "BLANK":
                answer = "NO_PROBLEM";
                break;

        }
        return answer;

    }


    public void capture(String a){
        api.saveMatImage(api.getMatNavCam(),a);
    }


    class betweenDistance{
        public int pt1;
        public int pt2;
        public int pt3;
        public double time;

        betweenDistance(int pt1, int pt2, int pt3, double time){
            this.pt1 = pt1;
            this.pt2 = pt2;
            this.pt3 = pt3;
            this.time = time;
        }
    }

    class BetweenDistanceComparator implements Comparator<betweenDistance> {
        public int compare(betweenDistance a, betweenDistance b) {
            if (a.time < b.time) return -1;
            else if(a.time > b.time) return 1;
            else return 0;
        }
    }

    private void LaserAndTakePhoto(int id){
        //aim();
        api.laserControl(true);
        waiting();
        api.saveMatImage(api.getMatNavCam(), "P" + id);
        waiting();
        api.takeTargetSnapshot(id);
        api.laserControl(false);
    }

    private void waiting(){
        try {
            Thread.sleep(0);
        } catch (Exception ignored) {
        }
    }
    private void move(int start, int end){
        if (start == 0 && end == 1){
            api.moveTo(A, Q1, false);
            waiting();
            api.moveTo(B, Q1, false);
            waiting();
            api.moveTo(AP1, Q1, false);
            LaserAndTakePhoto(1);
        } else if (start == 0 && end == 2){
            api.moveTo(A, Q1, false);
            waiting();
            api.moveTo(AP2, Q2, false);
            waiting();
            LaserAndTakePhoto(2);
        } else if (start == 0 && end == 3){
            api.moveTo(A, Q3, false);
            waiting();
            api.moveTo(TwoToThree_1, Q3, false);
            waiting();
            api.moveTo(TwoToThree_2, Q3, false);
            waiting();
            api.moveTo(AP3, Q3, false);
            LaserAndTakePhoto(3);
        } else if (start == 0 && end == 4){
            api.moveTo(A, Q1, false);
            waiting();
            api.moveTo(TwoToFour, Q4, false);
            waiting();
            api.moveTo(AP4, Q4, false);
            LaserAndTakePhoto(4);
        } else if (start == 0 && end == 5){
            api.moveTo(A, Q1, false);
            waiting();
            api.moveTo(new Point(10.95,-8.24,5.3), Q5, false);
            waiting();
            api.moveTo(AP5, Q5, false);
            LaserAndTakePhoto(5);
        } else if (start == 0 && end == 6){
            api.moveTo(A, Q1, false);
            waiting();
            api.moveTo(new Point(10.71,-9.57,4.64), Q6, false);
            waiting();
            api.moveTo(AP6, Q6, false);
            waiting();
            LaserAndTakePhoto(6);
        } else if (start == 1 && end == 2){
            api.moveTo(OneToTwo, Q2, false);
            waiting();
            api.moveTo(AP2, Q2, false);
            waiting();
            LaserAndTakePhoto(2);
        } else if (start == 1 && end == 3){
            api.moveTo(OneToThree, Q3, false);
            waiting();
            api.moveTo(AP3, Q3, false);
            waiting();
            LaserAndTakePhoto(3);

        } else if (start == 1 && end == 4){
            api.moveTo(AP4, Q4, false);
            waiting();
            LaserAndTakePhoto(4);
        } else if (start == 1 && end == 5){
            api.moveTo(AP5, Q5, false);
            waiting();
            LaserAndTakePhoto(5);
        } else if (start == 1 && end == 6){
            api.moveTo(AP6, Q6, false);
            waiting();
            LaserAndTakePhoto(6);
        } else if (start == 1 && end == 7){
            api.moveTo(P7, Q7, false);
            waiting();
            api.moveTo(AP7, Q7, false);
            waiting();
            api.saveMatImage(api.getMatNavCam(), "QRcode");
            waiting();
            api.moveTo(P7, Q7, false);
        } else if (start == 1 && end == 8){           //goal
            api.moveTo(new Point(11.16,-7.52,5.35), Q1, false);
            waiting();
            api.notifyGoingToGoal();
            api.moveTo(P_Goal, Q_Goal, false);
        } else if (start == 2 && end == 1){
            api.moveTo(OneToTwo, Q1, false);
            waiting();
            api.moveTo(AP1, Q1, false);
            waiting();
            LaserAndTakePhoto(1);
        } else if (start == 2 && end == 3){
            api.moveTo(TwoToThree_1, Q3, false);
            waiting();
            api.moveTo(TwoToThree_2, Q3, false);
            waiting();
            api.moveTo(AP3, Q3, false);
            waiting();
            LaserAndTakePhoto(3);
        } else if (start == 2 && end == 4){
            api.moveTo(TwoToFour, Q4, false);
            waiting();
            api.moveTo(AP4, Q4, false);
            waiting();
            LaserAndTakePhoto(4);
        } else if (start == 2 && end == 5){
            api.moveTo(TwoToFive, Q5, false);
            waiting();
            api.moveTo(AP5, Q5, false);
            waiting();
            LaserAndTakePhoto(5);
        } else if (start == 2 && end == 6){
            api.moveTo(TwoToSix, Q6, false);
            waiting();
            api.moveTo(AP6, Q6, false);
            waiting();
            LaserAndTakePhoto(6);
        } else if (start == 2 && end == 7){
            api.moveTo(TwoToSeven_1, Q7, false);
            waiting();
            api.moveTo(TwoToSeven_2, Q7, false);
            waiting();
            api.moveTo(P7, Q7, false);
            waiting();
            api.moveTo(AP7, Q7, false);
            waiting();
            api.saveMatImage(api.getMatNavCam(), "QRcode");
            waiting();
            api.moveTo(P7, Q7, false);
        } else if (start == 2 && end == 8){           //goal
            api.moveTo(new Point(10.63,-8.56,4.87), Q2, false);
            waiting();
            api.notifyGoingToGoal();
            api.moveTo(P_Goal, Q_Goal, false);
            api.reportMissionCompletion("NO_PROBLEM");
        } else if (start == 3 && end == 1){
            api.moveTo(OneToThree, Q1, false);
            waiting();
            api.moveTo(AP1, Q1, false);
            waiting();
            LaserAndTakePhoto(1);
        } else if (start == 3 && end == 2){
            api.moveTo(TwoToThree_2, Q2, false);
            waiting();
            api.moveTo(TwoToThree_1, Q2, false);
            waiting();
            api.moveTo(AP2, Q2, false);
            waiting();
            LaserAndTakePhoto(2);
        } else if (start == 3 && end == 4){
            api.moveTo(ThreeToFour, Q4, false);
            waiting();
            api.moveTo(AP4, Q4, false);
            waiting();
            LaserAndTakePhoto(4);
        } else if (start == 3 && end == 5){
            api.moveTo(ThreeToFive_1, Q3, false);
            waiting();
            api.moveTo(ThreeToFive_2, Q5, false);
            waiting();
            api.moveTo(AP5, Q5, false);
            waiting();
            LaserAndTakePhoto(5);
        } else if (start == 3 && end == 6){
            api.moveTo(ThreeToSix_1, Q6, false);
            waiting();
            api.moveTo(AP6, Q6, false);
            waiting();
            LaserAndTakePhoto(6);
        } else if (start == 3 && end == 7){
            api.moveTo(ThreeToSeven_1, Q7, false);
            waiting();
            api.moveTo(ThreeToSeven_2, Q7, false);
            waiting();
            api.moveTo(P7, Q7, false);
            waiting();
            api.moveTo(AP7, Q7, false);
            waiting();
            api.saveMatImage(api.getMatNavCam(), "QRcode");
            waiting();
            api.moveTo(P7, Q7, false);
        } else if (start == 3 && end == 8){           //goal
            api.notifyGoingToGoal();
            api.moveTo(P_Goal, Q_Goal, false);
            api.reportMissionCompletion("NO_PROBLEM");
        } else if (start == 4 && end == 1){
            api.moveTo(AP1, Q1, false);
            waiting();
            LaserAndTakePhoto(1);
        } else if (start == 4 && end == 2){
            api.moveTo(TwoToFour, Q2, false);
            waiting();
            api.moveTo(AP2, Q2, false);
            waiting();
            LaserAndTakePhoto(2);
        } else if (start == 4 && end == 3){
            api.moveTo(ThreeToFour, Q3, false);
            waiting();
            api.moveTo(AP3, Q3, false);
            waiting();
            LaserAndTakePhoto(3);
        } else if (start == 4 && end == 5){
            api.moveTo(AP5, Q5, false);
            waiting();
            LaserAndTakePhoto(5);
        } else if (start == 4 && end == 6){
            api.moveTo(FourToSix_1, Q6, false);
            waiting();
            api.moveTo(FourToSix_2, Q6, false);
            waiting();
            api.moveTo(AP6, Q6, false);
            waiting();
            LaserAndTakePhoto(6);
        } else if (start == 4 && end == 7){
            api.moveTo(FourToSeven_1, Q7, false);
            waiting();
            api.moveTo(FourToSeven_2, Q7, false);
            waiting();
            api.moveTo(P7, Q7, false);
            waiting();
            api.moveTo(AP7, Q7, false);
            waiting();
            api.saveMatImage(api.getMatNavCam(), "QRcode");
            waiting();
            api.moveTo(P7, Q7, false);
        } else if (start == 4 && end == 8){           //goal
            api.notifyGoingToGoal();
            api.moveTo(P_Goal, Q_Goal, false);
            api.reportMissionCompletion("NO_PROBLEM");
        } else if (start == 5 && end == 1){
            api.moveTo(AP1, Q1, false);
            waiting();
            LaserAndTakePhoto(1);
        } else if (start == 5 && end == 2){
            api.moveTo(TwoToFive, Q2, false);
            waiting();

            api.moveTo(AP2, Q2, false);
            waiting();
            LaserAndTakePhoto(2);
        } else if (start == 5 && end == 3){
            api.moveTo(ThreeToFive_2, Q3, false);
            waiting();
            api.moveTo(ThreeToFive_1, Q3, false);
            waiting();
            api.moveTo(AP3, Q3, false);
            waiting();
            LaserAndTakePhoto(3);
        } else if (start == 5 && end == 4){
            api.moveTo(AP4, Q4, false);
            waiting();
            LaserAndTakePhoto(4);
        } else if (start == 5 && end == 6){
            api.moveTo(FiveToSix, Q6, false);
            waiting();
            api.moveTo(AP6, Q6, false);
            waiting();
            LaserAndTakePhoto(6);
        } else if (start == 5 && end == 7){
            api.moveTo(FiveToSeven_1, Q7, false);
            waiting();
            api.moveTo(FiveToSeven_2, Q7, false);
            waiting();
            api.moveTo(P7, Q7, false);
            waiting();
            api.moveTo(AP7, Q7, false);
            waiting();
            api.saveMatImage(api.getMatNavCam(), "QRcode");
            waiting();
            api.moveTo(P7, Q7, false);
        } else if (start == 5 && end == 8){           //goal
            api.moveTo(new Point(11.12, -7.52, 5.3393), Q5, false);
            api.notifyGoingToGoal();
            api.moveTo(P_Goal, Q_Goal, false);
            api.reportMissionCompletion("NO_PROBLEM");
        } else if (start == 6 && end == 1){
            api.moveTo(AP1, Q1, false);
            waiting();
            LaserAndTakePhoto(1);
        } else if (start == 6 && end == 2){
            api.moveTo(TwoToSix, Q2, false);
            waiting();
            api.moveTo(AP2, Q2, false);
            waiting();
            LaserAndTakePhoto(2);
        } else if (start == 6 && end == 3){
            api.moveTo(ThreeToSix_1, Q3, false);
            waiting();
            api.moveTo(AP3, Q3, false);
            waiting();
            LaserAndTakePhoto(3);
        } else if (start == 6 && end == 4){
            api.moveTo(FourToSix_2, Q4, false);
            waiting();
            api.moveTo(FourToSix_1, Q4, false);
            waiting();
            api.moveTo(AP4, Q4, false);
            waiting();
            LaserAndTakePhoto(4);
        } else if (start == 6 && end == 5){
            api.moveTo(FiveToSix, Q5, false);
            waiting();
            api.moveTo(AP5, Q5, false);
            waiting();
            LaserAndTakePhoto(5);
        } else if (start == 6 && end == 7){
            api.moveTo(P7, Q7, false);
            waiting();
            api.moveTo(AP7, Q7, false);
            waiting();
            api.saveMatImage(api.getMatNavCam(), "QRcode");
            waiting();
            api.moveTo(P7, Q7, false);
        } else if (start == 6 && end == 8){           //goal
            api.moveTo(new Point(11.15,-7.54,4.9), Q6, false);
            api.notifyGoingToGoal();
            api.moveTo(P_Goal, Q_Goal, false);
            api.reportMissionCompletion("NO_PROBLEM");
        } else if (start == 7 && end == 1){
            api.moveTo(P1, Q1, false);
            waiting();
            api.moveTo(AP1, Q1, false);
            waiting();
            LaserAndTakePhoto(1);
            waiting();
            api.moveTo(P1, Q1, false);
        } else if (start == 7 && end == 2){
            api.moveTo(TwoToSeven_2, Q2, false);
            waiting();
            api.moveTo(TwoToSeven_1, Q2, false);
            waiting();
            api.moveTo(P2, Q2, false);
            waiting();
            api.moveTo(AP2, Q2, false);
            waiting();
            LaserAndTakePhoto(2);
            waiting();
            api.moveTo(P2, Q2, false);
        } else if (start == 7 && end == 3){
            api.moveTo(ThreeToSeven_2, Q3, false);
            waiting();
            api.moveTo(ThreeToSeven_1, Q3, false);
            waiting();
            api.moveTo(P3, Q3, false);
            waiting();
            api.moveTo(AP3, Q3, false);
            waiting();
            LaserAndTakePhoto(3);
            waiting();
            api.moveTo(P3, Q3, false);
        } else if (start == 7 && end == 4){
            api.moveTo(FourToSeven_2, Q4, false);
            waiting();
            api.moveTo(FourToSeven_1, Q4, false);
            waiting();
            api.moveTo(P4, Q4, false);
            waiting();
            api.moveTo(AP4, Q4, false);
            waiting();
            LaserAndTakePhoto(4);
            waiting();
            api.moveTo(P4, Q4, false);
        } else if (start == 7 && end == 5){
            api.moveTo(FiveToSeven_2, Q5, false);
            waiting();
            api.moveTo(FiveToSeven_1, Q5, false);
            waiting();
            api.moveTo(P5, Q5, false);
            waiting();
            api.moveTo(AP5, Q5, false);
            waiting();
            LaserAndTakePhoto(5);
            waiting();
            api.moveTo(P5, Q5, false);
        } else if (start == 7 && end == 6){
            api.moveTo(P6, Q6, false);
            waiting();
            api.moveTo(AP6, Q6, false);
            waiting();
            LaserAndTakePhoto(6);
            waiting();
            api.moveTo(P6, Q6, false);
        } else if (start == 7 && end == 8){           //goal
            api.moveTo(FiveToSeven_2, Q_Goal, false);
            waiting();
            api.moveTo(FiveToSeven_1, Q_Goal, false);
            waiting();
            api.moveTo(P5, Q5, false);
            waiting();
            api.moveTo(new Point(11.12, -7.52, 5.3393), Q5, false);
            api.notifyGoingToGoal();
            api.moveTo(P_Goal, Q_Goal, false);
            api.reportMissionCompletion("NO_PROBLEM");
        }
    }

}

