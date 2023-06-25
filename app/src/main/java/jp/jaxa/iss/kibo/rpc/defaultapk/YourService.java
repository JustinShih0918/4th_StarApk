package jp.jaxa.iss.kibo.rpc.defaultapk;


import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;


import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {

    double angle = Math.sqrt(2) / 2;

    // Point, Astrobee can arrive
    Point P_Goal = new Point(11.143, -6.7607, 4.9654);
    Point P1 = new Point(11.2746, -9.92284, 5.2988);
    Point P2 = new Point(10.612, -9.0709, 4.48);
    Point P3 = new Point(10.71, -7.7, 4.48);
    Point P4 = new Point(10.51, -6.7185, 5.1804);
    Point P5 = new Point(11.114, -7.9756, 5.3393);
    Point P6 = new Point(11.355, -8.9929, 4.7818);
    Point P7 = new Point(11.369,-8.5518,4.48);
    Point A = new Point(10.51, -9.81 , 4.58);            //有改過
    Point B = new Point(10.58,-9.81,5.25);               //有改過
    Point C = new Point(10.78, -9.07, 5.34);
    Point D = new Point(10.58, -8, 5.34);
    Point E = new Point(10.58, -7.71, 4.48);
    Point F = new Point(10.55, -7.4, 5);


    //new
    Point OneToTwo = new Point(10.9,-9.5,5.3);
    Point OneToThree = new Point(10.79,-8.08,4.86);
    Point TwoToThree_1 = new Point(10.65,-8.5,4.9);
    Point TwoToThree_2 = new Point(10.68,-8.13,4.89);
    Point TwoToFour = new Point(10.52,-8.56,4.85);
    Point TwoToFive = new Point(10.95,-8.25,5.33);
    Point TwoToSix = new Point(10.7,-9.14,4.83);
    Point ThreeToFour = new Point(10.71,-7.32,5);
    Point ThreeToFive_1 = new Point(10.6,-7.75,4.9);
    Point ThreeToFive_2 = new Point(10.6,-7.75,5.31);
    Point ThreeToSix_1 = new Point(10.83,-8.11,4.87);
    Point FourToSix_1 = new Point(10.88,-7.54,4.91);
    Point FiveToSix = new Point(11.2,-8.24,5.3);



    Quaternion Q_Goal = new Quaternion(0f, 0f, (float)-angle, (float)angle);
    Quaternion Q1 = new Quaternion(0f, 0f, (float)-angle, (float)angle);
    Quaternion Q2 = new Quaternion(0.5f, 0.5f, -0.5f, 0.5f);
    Quaternion Q3 = new Quaternion(0f, (float)angle, 0, (float)angle);
    Quaternion Q4 = new Quaternion(0f, 0f, -1f, 0f);
    Quaternion Q5 = new Quaternion(-0.5f, -0.5f, -0.5f, 0.5f);
    Quaternion Q6 = new Quaternion(0, 0, 0, 1);
    Quaternion Q7 = new Quaternion(0, (float)angle, 0, (float)angle);

    // Target, Astrobee can't arrive
    Point P_target1 = new Point(11.2625, -10.58, 5.3625);
    Point P_target2 = new Point(10.513384, -9.085172, 3.76203);
    Point P_target3 = new Point(10.6031, -7.71007, 3.76093);
    Point P_target4 = new Point(9.866984, -6.673972, 5.09531);
    Point P_target5 = new Point(11.102, -8.0304, 5.9076);
    Point P_target6 = new Point(12.023, -8.989, 4.8305);
    Point P_qrcode = new Point(11.381944, -8.566172, 3.76203);

    Quaternion Q_target1 = new Quaternion((float)angle, 0f, 0f, (float)angle);
    Quaternion Q_target2 = new Quaternion(0f, 0f, 0f, 1f);
    Quaternion Q_target3 = new Quaternion((float)angle, 0f, 0f, (float)angle);
    Quaternion Q_target4 = new Quaternion(-0.5f, 0.5f, -0.5f, 0.5f);
    Quaternion Q_target5 = new Quaternion(1f, 0f, 0f, 0f);
    Quaternion Q_target6 = new Quaternion(0.5f, 0.5f, -0.5f, -0.5f);
    Quaternion Q_qrcode = new Quaternion(0f, 0f, 0f, 1f);

    // Adjust Point   0, 0.0572(左), 0.1111(下), 面對
//    Point AP1 = new Point(11.2625, -9.92284, 5.3625);   // (-0.0572, 0, 0.1111)
//    Point AP2 = new Point(10.513384, -9.085172, 4.48);  // (-0.0572, -0.1111, 0)
//    Point AP3 = new Point(10.6031, -7.71007, 4.48);     // (0.1111, -0.0572, 0)
//    Point AP4 = new Point(10.51, -6.673972, 5.09531);   // (0, 0.0572, 0.1111)
//    Point AP5 = new Point(11.102, -8.0304, 5.3393);     // (-0.0572, 0.1111, 0)
//    Point AP6 = new Point(11.355, -8.989, 4.8305);      // (0, -0.0572, 0.1111)
//    Point AP7 = new Point(11.381944, -8.566172,4.48);   // (0, -0.0572, 0.1111)


    //New AP   //Don't need Point
    Point AP1 = new Point(11.2053,-9.72284,5.4736);   // (-0.0572, 0, 0.1111)
    Point AP2 = new Point(10.456184,-9.196272,4.68);  // (-0.0572, -0.1111, 0)
    Point AP3 = new Point(10.7142,-7.76727,4.68);     // (0.1111, -0.0572, 0)
    Point AP4 = new Point(10.71,-6.616772,5.20641);   // (0, 0.0572, 0.1111)
    // AP5 打不準
    Point AP5 = new Point(11.0448,-7.9193,5.3393);    // (-0.0572, 0.1111, 0)
    Point AP6 = new Point(11.155,-9.0462,4.9416);     // (0, -0.0572, 0.1111)
    Point AP7 = new Point(11.381944, -8.623372,4.5911); // (0, -0.0572, 0.1111)


    List<Integer> active_targets = new ArrayList<>();
    double score[] = {0, 30, 20, 40, 20, 30, 30, 50};

    Long TravelTime[][] = {
            /*0*/ {1000000L, 53328L, 31640L, 68144L, 69840L, 59360L, 45920L, 51920L, 1000000L},
            /*1*/ {53328L, 1000000L, 34984L, 46608L, 43960L, 32896L, 21104L, 27104L, 53016L},
            /*2*/ {31640L, 35040L, 1000000L, 50952L, 49480L, 41208L, 30520L, 36520L, 50504L},
            /*3*/ {68144L, 46704L, 50976L, 1000000L, 40280L, 54088L, 43696L, 49696L, 23789L},
            /*4*/ {69840L, 39256L, 49552L, 40880L, 1000000L, 29040L, 53088L, 59088L, 16244L},  // 4-6 有改
            /*5*/ {59360L, 30248L, 50736L, 52528L, 29192L, 1000000L, 39184L, 45184L, 34864L},
            /*6*/ {45920L, 24120L, 28272L, 40152L, 56048L, 34832L, 1000000L, 6000L, 46760L},  // 6-4  有改
            /*7*/ {45920L, 24120L, 28272L, 40152L, 56048L, 34832L, 6000L, 1000000L, 46760L},
    };

    String qr = "";

    int currentStep = 0;
    boolean hasScan = false;
    boolean stillGo = true;

    @Override
    protected void runPlan1() {
        api.startMission();


        for (int phase = 1; phase < 10; phase ++){
            stillGo = mainMethod();
            if (!stillGo) {
                capture("out" + phase);
                break;
            }
        }

        api.notifyGoingToGoal();
        move(currentStep, 8);
        api.reportMissionCompletion(qr);
    }



    public Boolean mainMethod(){
        active_targets = api.getActiveTargets();
        boolean canGo = true;
        if (active_targets.size() == 1) {
            canGo = target_1();

        } else if (active_targets.size() == 2) {
            canGo = target_2();

        } else if (active_targets.size() == 3){
            canGo = target_3();

        } else {
            canGo = false;
        }
        return canGo;
    }

    public boolean target_1(){
        if (active_targets.get(0) == 6 && !hasScan){
            if (TravelTime[currentStep][7] < api.getTimeRemaining().get(0) && TravelTime[currentStep][7] + TravelTime[7][8] < api.getTimeRemaining().get(1)) {
                move(currentStep, 7);
                currentStep = 6;
//            if (currentStep == 6 && hasScan) {
//                //掃QRcode
//                hasScan = false;
//            }
                return true;
            }else {
                return false;
            }
        } else {
            if (TravelTime[currentStep][active_targets.get(0)] < api.getTimeRemaining().get(0) && TravelTime[currentStep][active_targets.get(0)] + TravelTime[active_targets.get(0)][8] < api.getTimeRemaining().get(1)) {
                move(currentStep, active_targets.get(0));
                currentStep = active_targets.get(0);
//            if (currentStep == 6 && hasScan) {
//                //掃QRcode
//                hasScan = false;
//            }
                return true;
            }else {
                return false;
            }
        }

    }

    public boolean target_2(){
        if (active_targets.contains(6) && !hasScan){
            active_targets.add(7);
            return  target_3();
        } else {
            double cp = 0;
            for (int i = 1; i < 7; i++) {
                if (i != active_targets.get(0) && i != active_targets.get(1)) {
                    cp += (TravelTime[currentStep][active_targets.get(1)] + TravelTime[active_targets.get(1)][active_targets.get(0)] - TravelTime[currentStep][active_targets.get(0)] + TravelTime[active_targets.get(0)][active_targets.get(1)] + TravelTime[active_targets.get(0)][i] - TravelTime[active_targets.get(1)][i]) * score[i];
                }
            }
            if (cp < 0) {
                if (TravelTime[currentStep][active_targets.get(1)] + TravelTime[active_targets.get(1)][active_targets.get(0)] + TravelTime[active_targets.get(0)][8] < api.getTimeRemaining().get(1)) {
                    move(currentStep, active_targets.get(1));
                    move(active_targets.get(1), active_targets.get(0));
                    currentStep = active_targets.get(0);
                    return true;

                } else if (TravelTime[currentStep][active_targets.get(0)] + TravelTime[active_targets.get(0)][active_targets.get(1)] + TravelTime[active_targets.get(1)][8] < api.getTimeRemaining().get(1)) {
                    move(currentStep, active_targets.get(0));
                    move(active_targets.get(0), active_targets.get(1));
                    currentStep = active_targets.get(1);
                    return true;

                } else if (TravelTime[currentStep][active_targets.get(1)] + TravelTime[active_targets.get(1)][8] < api.getTimeRemaining().get(1) && TravelTime[currentStep][active_targets.get(0)] + TravelTime[active_targets.get(0)][8] < api.getTimeRemaining().get(1)) {
                    if (score[active_targets.get(1)] > score[active_targets.get(0)]) {
                        move(currentStep, active_targets.get(1));
                        return true;

                    } else {
                        move(currentStep, active_targets.get(0));
                        return true;
                    }

                } else if (TravelTime[currentStep][active_targets.get(1)] + TravelTime[active_targets.get(1)][8]< api.getTimeRemaining().get(1)) {
                    move(currentStep, active_targets.get(1));
                    return true;

                } else if (TravelTime[currentStep][active_targets.get(0)] + TravelTime[active_targets.get(0)][8]< api.getTimeRemaining().get(1)) {
                    move(currentStep, active_targets.get(0));
                    return true;

                } else {
                    return false;
                }
            } else if (cp > 0) {
                if (TravelTime[currentStep][active_targets.get(0)] + TravelTime[active_targets.get(0)][active_targets.get(1)] + TravelTime[active_targets.get(1)][8] < api.getTimeRemaining().get(1)) {
                    move(currentStep, active_targets.get(0));
                    move(active_targets.get(0), active_targets.get(1));
                    currentStep = active_targets.get(1);
                    return true;

                } else if (TravelTime[currentStep][active_targets.get(1)] + TravelTime[active_targets.get(1)][active_targets.get(0)] + TravelTime[active_targets.get(0)][8] < api.getTimeRemaining().get(1)) {
                    move(currentStep, active_targets.get(1));
                    move(active_targets.get(1), active_targets.get(0));
                    currentStep = active_targets.get(0);
                    return true;

                } else if (TravelTime[currentStep][active_targets.get(1)] + TravelTime[active_targets.get(1)][8] < api.getTimeRemaining().get(1) && TravelTime[currentStep][active_targets.get(0)] + TravelTime[active_targets.get(0)][8] < api.getTimeRemaining().get(1)) {
                    if (score[active_targets.get(1)] > score[active_targets.get(0)]) {
                        move(currentStep, active_targets.get(1));
                        return true;
                    } else {
                        move(currentStep, active_targets.get(0));
                        return true;
                    }

                } else if (TravelTime[currentStep][active_targets.get(0)] + TravelTime[active_targets.get(0)][8]< api.getTimeRemaining().get(1)) {
                    move(currentStep, active_targets.get(0));
                    return true;

                } else if (TravelTime[currentStep][active_targets.get(1)] + TravelTime[active_targets.get(1)][8]< api.getTimeRemaining().get(1)) {
                    move(currentStep, active_targets.get(1));
                    return true;

                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean target_3(){
        boolean isDone = false;

        if (active_targets.get(2) == 6 && !hasScan){
            active_targets.set(2, 7);

            List<ThreeJudge> goThree = new ArrayList<>();
            List<ThreeJudge> goTwo = new ArrayList<>();
            List<ThreeJudge> goOne = new ArrayList<>();
            ThreeJudge a = new ThreeJudge(active_targets.get(0), active_targets.get(1), active_targets.get(2), TravelTime[currentStep][active_targets.get(0)]+TravelTime[active_targets.get(0)][active_targets.get(1)]+TravelTime[active_targets.get(1)][active_targets.get(2)]);
            ThreeJudge b = new ThreeJudge(active_targets.get(0), active_targets.get(2), active_targets.get(1), TravelTime[currentStep][active_targets.get(0)]+TravelTime[active_targets.get(0)][active_targets.get(2)]+TravelTime[active_targets.get(2)][active_targets.get(1)]);
            ThreeJudge c = new ThreeJudge(active_targets.get(1), active_targets.get(0), active_targets.get(2), TravelTime[currentStep][active_targets.get(1)]+TravelTime[active_targets.get(1)][active_targets.get(0)]+TravelTime[active_targets.get(0)][active_targets.get(2)]);
            ThreeJudge d = new ThreeJudge(active_targets.get(1), active_targets.get(2), active_targets.get(0), TravelTime[currentStep][active_targets.get(1)]+TravelTime[active_targets.get(1)][active_targets.get(2)]+TravelTime[active_targets.get(2)][active_targets.get(0)]);
            ThreeJudge e = new ThreeJudge(active_targets.get(2), active_targets.get(0), active_targets.get(1), TravelTime[currentStep][active_targets.get(2)]+TravelTime[active_targets.get(2)][active_targets.get(0)]+TravelTime[active_targets.get(0)][active_targets.get(1)]);
            ThreeJudge f = new ThreeJudge(active_targets.get(2), active_targets.get(1), active_targets.get(0), TravelTime[currentStep][active_targets.get(2)]+TravelTime[active_targets.get(2)][active_targets.get(1)]+TravelTime[active_targets.get(1)][active_targets.get(0)]);

            goThree.add(a);
            goThree.add(b);
            goThree.add(c);
            goThree.add(d);
            goThree.add(e);
            goThree.add(f);


            // 判斷時間內來不來得及走3個點後走到終點
            for(int i=5; i>=0; i--){
                if(goThree.get(i).time >= api.getTimeRemaining().get(0) || goThree.get(i).time + TravelTime[goThree.get(i).pt3][8] > api.getTimeRemaining().get(1)){
                    goThree.remove(i);
                }
            }

            // 來得及走 3 個點
            if (goThree.size() > 0){

                goThree.sort(new ThreeJudgeComparator());
                move(currentStep, goThree.get(0).pt1);
                move(goThree.get(0).pt1, goThree.get(0).pt2);
                move(goThree.get(0).pt2, goThree.get(0).pt3);
                currentStep = goThree.get(0).pt3;
                isDone = true;

                return true;


            } else {
                //  choose 2 from 3，時間內只能從 phase 中的 3 個點挑 2 個出來走
                a.time = TravelTime[currentStep][active_targets.get(0)]+TravelTime[active_targets.get(0)][active_targets.get(1)];
                b.time = TravelTime[currentStep][active_targets.get(0)]+TravelTime[active_targets.get(0)][active_targets.get(2)];
                c.time = TravelTime[currentStep][active_targets.get(1)]+TravelTime[active_targets.get(1)][active_targets.get(0)];
                d.time = TravelTime[currentStep][active_targets.get(1)]+TravelTime[active_targets.get(1)][active_targets.get(2)];
                e.time = TravelTime[currentStep][active_targets.get(2)]+TravelTime[active_targets.get(2)][active_targets.get(0)];
                f.time = TravelTime[currentStep][active_targets.get(2)]+TravelTime[active_targets.get(2)][active_targets.get(1)];

                goTwo.add(a);
                goTwo.add(b);
                goTwo.add(c);
                goTwo.add(d);
                goTwo.add(e);
                goTwo.add(f);




                for(int i=5; i>=0; i--){
                    if (goTwo.get(i).time + TravelTime[goTwo.get(i).pt2][8] > api.getTimeRemaining().get(1)){
                        goTwo.remove(i);
                    }
                }

                if(goTwo.size()>0){
                    List<ThreeJudge> ninty = new ArrayList<>();
                    List<ThreeJudge> eighty = new ArrayList<>();
                    List<ThreeJudge> seventy = new ArrayList<>();
                    List<ThreeJudge> sixty = new ArrayList<>();
                    List<ThreeJudge> fifty = new ArrayList<>();
                    List<ThreeJudge> forty = new ArrayList<>();

                    for (int i=0; i<goTwo.size(); i++){
                        if (score[goTwo.get(i).pt1] + score[goTwo.get(i).pt2] == 90){
                            ninty.add(goTwo.get(i));
                        } else if (score[goTwo.get(i).pt1] + score[goTwo.get(i).pt2] == 80){
                            eighty.add(goTwo.get(i));
                        } else if (score[goTwo.get(i).pt1] + score[goTwo.get(i).pt2] == 70){
                            seventy.add(goTwo.get(i));
                        } else if (score[goTwo.get(i).pt1] + score[goTwo.get(i).pt2] == 60){
                            sixty.add(goTwo.get(i));
                        } else if (score[goTwo.get(i).pt1] + score[goTwo.get(i).pt2] == 50){
                            fifty.add(goTwo.get(i));
                        } else if (score[goTwo.get(i).pt1] + score[goTwo.get(i).pt2] == 40){
                            forty.add(goTwo.get(i));
                        }
                    }

                    if (ninty.size() > 0){
                        ninty.sort(new ThreeJudgeComparator());

                        for (int i=0; i<7; i++){
                            if (i != active_targets.get(0) && i != active_targets.get(1) && i != active_targets.get(2)){
                                if (120000 + TravelTime[ninty.get(0).pt2][i] + TravelTime[i][8] < api.getTimeRemaining().get(1)){
                                    move(currentStep, ninty.get(0).pt1);
                                    move(ninty.get(0).pt1, ninty.get(0).pt2);
                                    currentStep = ninty.get(0).pt2;
                                    isDone = true;
                                    if (!hasScan && TravelTime[currentStep][7] < api.getTimeRemaining().get(0) && TravelTime[6][8] < api.getTimeRemaining().get(1)-api.getTimeRemaining().get(0)){
                                        move(currentStep, 7);
                                        while (api.getTimeRemaining().get(0) < 119000){
                                            waiting(10);
                                        }
                                    } else {
                                        while (api.getTimeRemaining().get(0) < 119000){
                                            waiting(10);
                                        }
                                    }
                                    return true;
                                }
                            }
                        }

                        move(currentStep, ninty.get(0).pt1);
                        move(ninty.get(0).pt1, ninty.get(0).pt2);
                        currentStep = ninty.get(0).pt2;
                        isDone = true;
                        return false;

                    } else if (eighty.size() > 0){
                        eighty.sort(new ThreeJudgeComparator());

                        for (int i=0; i<7; i++) {
                            if (i != active_targets.get(0) && i != active_targets.get(1) && i != active_targets.get(2)) {
                                if (120000 + TravelTime[eighty.get(0).pt2][i] + TravelTime[i][8] < api.getTimeRemaining().get(1)) {
                                    move(currentStep, eighty.get(0).pt1);
                                    move(eighty.get(0).pt1, eighty.get(0).pt2);
                                    currentStep = eighty.get(0).pt2;
                                    isDone = true;
                                    if (!hasScan && TravelTime[currentStep][7] < api.getTimeRemaining().get(0) && TravelTime[6][8] < api.getTimeRemaining().get(1)-api.getTimeRemaining().get(0)){
                                        move(currentStep, 7);
                                        while (api.getTimeRemaining().get(0) < 119000){
                                            waiting(10);
                                        }
                                    } else {
                                        while (api.getTimeRemaining().get(0) < 119000){
                                            waiting(10);
                                        }
                                    }
                                    return true;
                                }
                            }
                        }

                        move(currentStep, eighty.get(0).pt1);
                        move(eighty.get(0).pt1, eighty.get(0).pt2);
                        currentStep = eighty.get(0).pt2;
                        isDone = true;
                        return false;
                    } else if (seventy.size() > 0){
                        seventy.sort(new ThreeJudgeComparator());

                        for (int i=0; i<7; i++){
                            if (i != active_targets.get(0) && i != active_targets.get(1) && i != active_targets.get(2)){
                                if (120000 + TravelTime[seventy.get(0).pt2][i] + TravelTime[i][8] < api.getTimeRemaining().get(1)){
                                    move(currentStep, seventy.get(0).pt1);
                                    move(seventy.get(0).pt1, seventy.get(0).pt2);
                                    currentStep = seventy.get(0).pt2;
                                    isDone = true;
                                    if (!hasScan && TravelTime[currentStep][7] < api.getTimeRemaining().get(0) && TravelTime[6][8] < api.getTimeRemaining().get(1)-api.getTimeRemaining().get(0)){
                                        move(currentStep, 7);
                                        while (api.getTimeRemaining().get(0) < 119000){
                                            waiting(10);
                                        }
                                    } else {
                                        while (api.getTimeRemaining().get(0) < 119000){
                                            waiting(10);
                                        }
                                    }
                                    return true;
                                }
                            }
                        }

                        move(currentStep, seventy.get(0).pt1);
                        move(seventy.get(0).pt1, seventy.get(0).pt2);
                        currentStep = seventy.get(0).pt2;
                        isDone = true;
                        return false;

                    } else if (sixty.size() > 0){
                        sixty.sort(new ThreeJudgeComparator());

                        for (int i=0; i<7; i++) {
                            if (i != active_targets.get(0) && i != active_targets.get(1) && i != active_targets.get(2)) {
                                if (120000 + TravelTime[sixty.get(0).pt2][i] + TravelTime[i][8] < api.getTimeRemaining().get(1)) {
                                    move(currentStep, sixty.get(0).pt1);
                                    move(sixty.get(0).pt1, sixty.get(0).pt2);
                                    currentStep = sixty.get(0).pt2;
                                    isDone = true;
                                    if (!hasScan && TravelTime[currentStep][7] < api.getTimeRemaining().get(0) && TravelTime[6][8] < api.getTimeRemaining().get(1)-api.getTimeRemaining().get(0)){
                                        move(currentStep, 7);
                                        while (api.getTimeRemaining().get(0) < 119000){
                                            waiting(10);
                                        }
                                    } else {
                                        while (api.getTimeRemaining().get(0) < 119000){
                                            waiting(10);
                                        }
                                    }
                                    return true;
                                }
                            }
                        }

                        move(currentStep, sixty.get(0).pt1);
                        move(sixty.get(0).pt1, sixty.get(0).pt2);
                        currentStep = sixty.get(0).pt2;
                        isDone = true;
                        return false;
                    } else if (fifty.size() > 0){
                        fifty.sort(new ThreeJudgeComparator());

                        for (int i=0; i<7; i++) {
                            if (i != active_targets.get(0) && i != active_targets.get(1) && i != active_targets.get(2)) {
                                if (120000 + TravelTime[fifty.get(0).pt2][i] + TravelTime[i][8] < api.getTimeRemaining().get(1)) {
                                    move(currentStep, fifty.get(0).pt1);
                                    move(fifty.get(0).pt1, fifty.get(0).pt2);
                                    currentStep = fifty.get(0).pt2;
                                    isDone = true;
                                    if (!hasScan && TravelTime[currentStep][7] < api.getTimeRemaining().get(0) && TravelTime[6][8] < api.getTimeRemaining().get(1)-api.getTimeRemaining().get(0)){
                                        move(currentStep, 7);
                                        while (api.getTimeRemaining().get(0) < 119000){
                                            waiting(10);
                                        }
                                    } else {
                                        while (api.getTimeRemaining().get(0) < 119000){
                                            waiting(10);
                                        }
                                    }
                                    return true;
                                }
                            }
                        }

                        move(currentStep, fifty.get(0).pt1);
                        move(fifty.get(0).pt1, fifty.get(0).pt2);
                        currentStep = fifty.get(0).pt2;
                        isDone = true;
                        return false;
                    } else if (forty.size() > 0){
                        forty.sort(new ThreeJudgeComparator());

                        for (int i=0; i<7; i++) {
                            if (i != active_targets.get(0) && i != active_targets.get(1) && i != active_targets.get(2)) {
                                if (120000 + TravelTime[forty.get(0).pt2][i] + TravelTime[i][8] < api.getTimeRemaining().get(1)) {
                                    move(currentStep, forty.get(0).pt1);
                                    move(forty.get(0).pt1, forty.get(0).pt2);
                                    currentStep = forty.get(0).pt2;
                                    isDone = true;
                                    if (!hasScan && TravelTime[currentStep][7] < api.getTimeRemaining().get(0) && TravelTime[6][8] < api.getTimeRemaining().get(1)-api.getTimeRemaining().get(0)){
                                        move(currentStep, 7);
                                        while (api.getTimeRemaining().get(0) < 119000){
                                            waiting(10);
                                        }
                                    } else {
                                        while (api.getTimeRemaining().get(0) < 119000){
                                            waiting(10);
                                        }
                                    }
                                    return true;
                                }
                            }
                        }

                        move(currentStep, forty.get(0).pt1);
                        move(forty.get(0).pt1, forty.get(0).pt2);
                        currentStep = forty.get(0).pt2;
                        isDone = true;
                        return false;
                    }
                } else {
                    // choose 1 from 3
                    // 這裡沒有加上時間

                    a.time = TravelTime[currentStep][active_targets.get(0)];
                    c.time = TravelTime[currentStep][active_targets.get(1)];
                    e.time = TravelTime[currentStep][active_targets.get(2)];

                    goOne.add(a);
                    goOne.add(c);
                    goOne.add(e);

                    for(int i=2; i>=0; i--){
                        if (goOne.get(i).time + TravelTime[goOne.get(i).pt1][8] > api.getTimeRemaining().get(1)){  // && 120000 + TravelTime[goTwo.get(i).pt2][8] > api.getTimeRemaining().get(1)){
                            goOne.remove(i);
                        }
                    }

                    if (goOne.size() > 0){
                        List<ThreeJudge> forty = new ArrayList<>();
                        List<ThreeJudge> thirty = new ArrayList<>();
                        List<ThreeJudge> twenty = new ArrayList<>();

                        for (int i=0; i<2; i++){
                            if (score[active_targets.get(i)] == 40){
                                forty.add(goOne.get(i));
                            } else if (score[active_targets.get(i)] == 30){
                                thirty.add(goOne.get(i));
                            } else if (score[active_targets.get(i)] == 20){
                                twenty.add(goOne.get(i));
                            }
                        }
                        if (forty.size() > 0){
                            forty.sort(new ThreeJudgeComparator());
                            move(currentStep, forty.get(0).pt1);
                            currentStep = forty.get(0).pt1;
                            isDone = true;
                            return false;

                        } else if (thirty.size() > 0){
                            thirty.sort(new ThreeJudgeComparator());
                            move(currentStep, thirty.get(0).pt1);
                            currentStep = thirty.get(0).pt1;
                            isDone = true;
                            return false;

                        } else {
                            twenty.sort(new ThreeJudgeComparator());
                            move(currentStep, twenty.get(0).pt1);
                            currentStep = twenty.get(0).pt1;
                            isDone = true;
                            return false;

                        }
                    }
                    else {
                        return false;
                    }
                }
            }
        }

        if (!isDone){
            List<ThreeJudge> goThree = new ArrayList<>();
            List<ThreeJudge> goTwo = new ArrayList<>();
            List<ThreeJudge> goOne = new ArrayList<>();
            ThreeJudge a = new ThreeJudge(active_targets.get(0), active_targets.get(1), active_targets.get(2), TravelTime[currentStep][active_targets.get(0)]+TravelTime[active_targets.get(0)][active_targets.get(1)]+TravelTime[active_targets.get(1)][active_targets.get(2)]);
            ThreeJudge b = new ThreeJudge(active_targets.get(0), active_targets.get(2), active_targets.get(1), TravelTime[currentStep][active_targets.get(0)]+TravelTime[active_targets.get(0)][active_targets.get(2)]+TravelTime[active_targets.get(2)][active_targets.get(1)]);
            ThreeJudge c = new ThreeJudge(active_targets.get(1), active_targets.get(0), active_targets.get(2), TravelTime[currentStep][active_targets.get(1)]+TravelTime[active_targets.get(1)][active_targets.get(0)]+TravelTime[active_targets.get(0)][active_targets.get(2)]);
            ThreeJudge d = new ThreeJudge(active_targets.get(1), active_targets.get(2), active_targets.get(0), TravelTime[currentStep][active_targets.get(1)]+TravelTime[active_targets.get(1)][active_targets.get(2)]+TravelTime[active_targets.get(2)][active_targets.get(0)]);
            ThreeJudge e = new ThreeJudge(active_targets.get(2), active_targets.get(0), active_targets.get(1), TravelTime[currentStep][active_targets.get(2)]+TravelTime[active_targets.get(2)][active_targets.get(0)]+TravelTime[active_targets.get(0)][active_targets.get(1)]);
            ThreeJudge f = new ThreeJudge(active_targets.get(2), active_targets.get(1), active_targets.get(0), TravelTime[currentStep][active_targets.get(2)]+TravelTime[active_targets.get(2)][active_targets.get(1)]+TravelTime[active_targets.get(1)][active_targets.get(0)]);

            goThree.add(a);
            goThree.add(b);
            goThree.add(c);
            goThree.add(d);
            goThree.add(e);
            goThree.add(f);


            // 判斷時間內來不來得及走3個點後走到終點
            for(int i=5; i>=0; i--){
                if(goThree.get(i).time >= api.getTimeRemaining().get(0) || goThree.get(i).time + TravelTime[goThree.get(i).pt3][8] > api.getTimeRemaining().get(1)){
                    goThree.remove(i);
                }
            }

            // 來得及走 3 個點
            if (goThree.size() > 0){

                goThree.sort(new ThreeJudgeComparator());
                move(currentStep, goThree.get(0).pt1);
                move(goThree.get(0).pt1, goThree.get(0).pt2);
                move(goThree.get(0).pt2, goThree.get(0).pt3);
                currentStep = goThree.get(0).pt3;

                return true;


            } else {
                //  choose 2 from 3，時間內只能從 phase 中的 3 個點挑 2 個出來走
                a.time = TravelTime[currentStep][active_targets.get(0)]+TravelTime[active_targets.get(0)][active_targets.get(1)];
                b.time = TravelTime[currentStep][active_targets.get(0)]+TravelTime[active_targets.get(0)][active_targets.get(2)];
                c.time = TravelTime[currentStep][active_targets.get(1)]+TravelTime[active_targets.get(1)][active_targets.get(0)];
                d.time = TravelTime[currentStep][active_targets.get(1)]+TravelTime[active_targets.get(1)][active_targets.get(2)];
                e.time = TravelTime[currentStep][active_targets.get(2)]+TravelTime[active_targets.get(2)][active_targets.get(0)];
                f.time = TravelTime[currentStep][active_targets.get(2)]+TravelTime[active_targets.get(2)][active_targets.get(1)];

                goTwo.add(a);
                goTwo.add(b);
                goTwo.add(c);
                goTwo.add(d);
                goTwo.add(e);
                goTwo.add(f);




                for(int i=5; i>=0; i--){
                    if (goTwo.get(i).time + TravelTime[goTwo.get(i).pt2][8] > api.getTimeRemaining().get(1)){
                        goTwo.remove(i);
                    }
                }

                if(goTwo.size()>0){
                    List<ThreeJudge> seventy = new ArrayList<>();
                    List<ThreeJudge> sixty = new ArrayList<>();
                    List<ThreeJudge> fifty = new ArrayList<>();
                    List<ThreeJudge> forty = new ArrayList<>();

                    for (int i=0; i<goTwo.size(); i++){
                        if (score[goTwo.get(i).pt1] + score[goTwo.get(i).pt2] == 70){
                            seventy.add(goTwo.get(i));
                        } else if (score[goTwo.get(i).pt1] + score[goTwo.get(i).pt2] == 60){
                            sixty.add(goTwo.get(i));
                        } else if (score[goTwo.get(i).pt1] + score[goTwo.get(i).pt2] == 50){
                            fifty.add(goTwo.get(i));
                        } else if (score[goTwo.get(i).pt1] + score[goTwo.get(i).pt2] == 40){
                            forty.add(goTwo.get(i));
                        }
                    }

                    if (seventy.size() > 0){
                        seventy.sort(new ThreeJudgeComparator());

                        for (int i=0; i<7; i++){
                            if (i != active_targets.get(0) && i != active_targets.get(1) && i != active_targets.get(2)){
                                if (120000 + TravelTime[seventy.get(0).pt2][i] + TravelTime[i][8] < api.getTimeRemaining().get(1)){
                                    move(currentStep, seventy.get(0).pt1);
                                    move(seventy.get(0).pt1, seventy.get(0).pt2);
                                    currentStep = seventy.get(0).pt2;
                                    while (api.getTimeRemaining().get(0) < 119000){
                                        waiting(10);
                                    }
                                    return true;
                                }
                            }
                        }

                        move(currentStep, seventy.get(0).pt1);
                        move(seventy.get(0).pt1, seventy.get(0).pt2);
                        currentStep = seventy.get(0).pt2;
                        return false;

                    } else if (sixty.size() > 0){
                        sixty.sort(new ThreeJudgeComparator());

                        for (int i=0; i<7; i++) {
                            if (i != active_targets.get(0) && i != active_targets.get(1) && i != active_targets.get(2)) {
                                if (120000 + TravelTime[sixty.get(0).pt2][i] + TravelTime[i][8] < api.getTimeRemaining().get(1)) {
                                    move(currentStep, sixty.get(0).pt1);
                                    move(sixty.get(0).pt1, sixty.get(0).pt2);
                                    currentStep = sixty.get(0).pt2;
                                    while (api.getTimeRemaining().get(0) < 119000){
                                        waiting(10);
                                    }
                                    return true;
                                }
                            }
                        }

                        move(currentStep, sixty.get(0).pt1);
                        move(sixty.get(0).pt1, sixty.get(0).pt2);
                        currentStep = sixty.get(0).pt2;
                        return false;
                    } else if (fifty.size() > 0){
                        fifty.sort(new ThreeJudgeComparator());

                        for (int i=0; i<7; i++) {
                            if (i != active_targets.get(0) && i != active_targets.get(1) && i != active_targets.get(2)) {
                                if (120000 + TravelTime[fifty.get(0).pt2][i] + TravelTime[i][8] < api.getTimeRemaining().get(1)) {
                                    move(currentStep, fifty.get(0).pt1);
                                    move(fifty.get(0).pt1, fifty.get(0).pt2);
                                    currentStep = fifty.get(0).pt2;
                                    while (api.getTimeRemaining().get(0) < 119000){
                                        waiting(10);
                                    }
                                    return true;
                                }
                            }
                        }

                        move(currentStep, fifty.get(0).pt1);
                        move(fifty.get(0).pt1, fifty.get(0).pt2);
                        currentStep = fifty.get(0).pt2;
                        return false;
                    } else if (forty.size() > 0){
                        forty.sort(new ThreeJudgeComparator());

                        for (int i=0; i<7; i++) {
                            if (i != active_targets.get(0) && i != active_targets.get(1) && i != active_targets.get(2)) {
                                if (120000 + TravelTime[forty.get(0).pt2][i] + TravelTime[i][8] < api.getTimeRemaining().get(1)) {
                                    move(currentStep, forty.get(0).pt1);
                                    move(forty.get(0).pt1, forty.get(0).pt2);
                                    currentStep = forty.get(0).pt2;
                                    while (api.getTimeRemaining().get(0) < 119000){
                                        waiting(10);
                                    }
                                    return true;
                                }
                            }
                        }

                        move(currentStep, forty.get(0).pt1);
                        move(forty.get(0).pt1, forty.get(0).pt2);
                        currentStep = forty.get(0).pt2;
                        return false;
                    }
                } else {
                    // choose 1 from 3
                    // 這裡沒有加上時間

                    a.time = TravelTime[currentStep][active_targets.get(0)];
                    c.time = TravelTime[currentStep][active_targets.get(1)];
                    e.time = TravelTime[currentStep][active_targets.get(2)];

                    goOne.add(a);
                    goOne.add(c);
                    goOne.add(e);

                    for(int i=2; i>=0; i--){
                        if (goOne.get(i).time + TravelTime[goOne.get(i).pt1][8] > api.getTimeRemaining().get(1)){  // && 120000 + TravelTime[goTwo.get(i).pt2][8] > api.getTimeRemaining().get(1)){
                            goOne.remove(i);
                        }
                    }

                    if (goOne.size() > 0){
                        List<ThreeJudge> forty = new ArrayList<>();
                        List<ThreeJudge> thirty = new ArrayList<>();
                        List<ThreeJudge> twenty = new ArrayList<>();

                        for (int i=0; i<2; i++){
                            if (score[active_targets.get(i)] == 40){
                                forty.add(goOne.get(i));
                            } else if (score[active_targets.get(i)] == 30){
                                thirty.add(goOne.get(i));
                            } else if (score[active_targets.get(i)] == 20){
                                twenty.add(goOne.get(i));
                            }
                        }
                        if (forty.size() > 0){
                            forty.sort(new ThreeJudgeComparator());
                            move(currentStep, forty.get(0).pt1);
                            currentStep = forty.get(0).pt1;

                            return false;

                        } else if (thirty.size() > 0){
                            thirty.sort(new ThreeJudgeComparator());
                            move(currentStep, thirty.get(0).pt1);
                            currentStep = thirty.get(0).pt1;

                            return false;

                        } else {
                            twenty.sort(new ThreeJudgeComparator());
                            move(currentStep, twenty.get(0).pt1);
                            currentStep = twenty.get(0).pt1;

                            return false;

                        }
                    }
                    else {
                        return false;
                    }
                }
            }
        }

        return false;
    }

    class ThreeJudge{
        public int pt1;
        public int pt2;
        public int pt3;
        public double time;

        ThreeJudge(int pt1, int pt2, int pt3, double time){
            this.pt1 = pt1;
            this.pt2 = pt2;
            this.pt3 = pt3;
            this.time = time;
        }
    }

    class ThreeJudgeComparator implements  Comparator<ThreeJudge>{
        public int compare(ThreeJudge a, ThreeJudge b){
            if(a.time<b.time) return -1;
            else if(a.time>b.time) return  1;
            else return 0;
        }
    }

    public void doScan(){
        if(!hasScan){
            Mat usage = api.getMatNavCam();
            api.saveMatImage(usage,"use");
            qr = Qrcode(usage,350, 400, 700, 280, 1200, 1200);
            if(qr!=""){
                hasScan = true;
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
            hasScan=false;
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
            default:
                answer = "NO_PROBLEM";
                break;
        }
        return answer;

    }
    public void capture(String a) {
        api.saveMatImage(api.getMatNavCam(), a);
    }


    private void LaserAndTakePhoto(int id) {
        Result result;

        result = api.laserControl(true);

        if (result == null){
            if (id == 1){
                api.moveTo(new Point(11.2053,-9.82284,5.4736), Q1, false);
                result = api.laserControl(true);
                api.saveMatImage(api.getMatNavCam(), "P" + id);
                api.takeTargetSnapshot(id);
                api.laserControl(false);
                api.moveTo(AP1, Q1, false);
            } else if (id == 2){
                api.moveTo(new Point(10.456184,-9.196272,4.58), Q2, false);
                result = api.laserControl(true);
                api.saveMatImage(api.getMatNavCam(), "P" + id);
                api.takeTargetSnapshot(id);
                api.laserControl(false);
                api.moveTo(AP2, Q2, false);
            } else if (id == 3){
                api.moveTo(new Point(10.7142,-7.76727,4.58), Q3, false);
                result = api.laserControl(true);
                api.saveMatImage(api.getMatNavCam(), "P" + id);
                api.takeTargetSnapshot(id);
                api.laserControl(false);
                api.moveTo(AP3, Q3, false);
            } else if (id == 4){
                api.moveTo(new Point(10.61,-6.616772,5.20641), Q4, false);
                result = api.laserControl(true);
                api.saveMatImage(api.getMatNavCam(), "P" + id);
                api.takeTargetSnapshot(id);
                api.laserControl(false);
                api.moveTo(AP4, Q4, false);
            } else if (id == 5){
                api.moveTo(new Point(11.0448,-7.9193,5.4393), Q5, false);
                result = api.laserControl(true);
                api.saveMatImage(api.getMatNavCam(), "P" + id);
                api.takeTargetSnapshot(id);
                api.laserControl(false);
                api.moveTo(AP5, Q5, false);
            } else if (id == 6){
                api.moveTo(new Point(11.255,-9.0462,4.9416), Q6, false);
                result = api.laserControl(true);
                api.saveMatImage(api.getMatNavCam(), "P" + id);
                api.takeTargetSnapshot(id);
                api.laserControl(false);
                api.moveTo(AP6, Q6, false);
            }
        } else {
            api.laserControl(true);
            api.saveMatImage(api.getMatNavCam(), "P" + id);
            api.takeTargetSnapshot(id);
            api.laserControl(false);
        }
    }

    private void waiting(int sec) {
        try {
            Thread.sleep(sec);
        } catch (Exception ignored) {
        }
    }

    private void move(int start, int end){
        Result result;

        if (start == 0 && end == 1){
            api.moveTo(A, Q6, false);
            api.moveTo(B, Q1, false);
            api.moveTo(AP1, Q1, false);
            LaserAndTakePhoto(1);
        } else if (start == 0 && end == 2){
            api.moveTo(A, Q6, false);
            api.moveTo(AP2, Q2, false);
            LaserAndTakePhoto(2);
        } else if (start == 0 && end == 3){
            api.moveTo(A, Q6, false);
            api.moveTo(TwoToThree_1, Q3, false);
            api.moveTo(TwoToThree_2, Q3, false);
            api.moveTo(AP3, Q3, false);
            LaserAndTakePhoto(3);
        } else if (start == 0 && end == 4){
            api.moveTo(A, Q6, false);
            api.moveTo(TwoToFour, Q4, false);
            api.moveTo(AP4, Q4, false);
            LaserAndTakePhoto(4);
        } else if (start == 0 && end == 5){
            api.moveTo(A, Q6, false);
            api.moveTo(new Point(10.95,-8.24,5.3), Q5, false);
            api.moveTo(AP5, Q5, false);
            LaserAndTakePhoto(5);
        } else if (start == 0 && end == 6){
            api.moveTo(A, Q6, false);
            api.moveTo(new Point(10.71,-9.57,4.64), Q6, false);
            api.moveTo(AP6, Q6, false);
            LaserAndTakePhoto(6);
        } else if (start == 0 && end == 7){
            api.moveTo(A, Q6, false);
            api.moveTo(new Point(10.71,-9.57,4.64), Q6, false);
            api.moveTo(AP6, Q7, false);
            doScan();
            if (active_targets.contains(6) || active_targets.contains(7)){
                api.moveTo(AP6, Q6, false);
                LaserAndTakePhoto(6);
            }
        } else if (start == 1 && end == 2){
            api.moveTo(OneToTwo, Q2, false);
            api.moveTo(AP2, Q2, false);
            LaserAndTakePhoto(2);
        } else if (start == 1 && end == 3){
            api.moveTo(OneToThree, Q3, false);
            api.moveTo(AP3, Q3, false);
            LaserAndTakePhoto(3);
        } else if (start == 1 && end == 4){
            api.moveTo(AP4, Q4, false);
            LaserAndTakePhoto(4);
        } else if (start == 1 && end == 5){
            api.moveTo(AP5, Q5, false);
            LaserAndTakePhoto(5);
        } else if (start == 1 && end == 6){
            api.moveTo(AP6, Q6, false);
            LaserAndTakePhoto(6);
        } else if (start == 1 && end == 7){
            api.moveTo(AP6, Q7, false);
            doScan();
            if (active_targets.contains(6) || active_targets.contains(7)){
                api.moveTo(AP6, Q6, false);
                LaserAndTakePhoto(6);
            }
        } else if (start == 1 && end == 8){           //goal
            api.moveTo(new Point(11.16,-7.52,5.35), Q1, false);
            api.moveTo(P_Goal, Q_Goal, false);
        } else if (start == 2 && end == 1){
            api.moveTo(OneToTwo, Q1, false);
            api.moveTo(AP1, Q1, false);
            LaserAndTakePhoto(1);
        } else if (start == 2 && end == 3){
            api.moveTo(TwoToThree_1, Q3, false);
            api.moveTo(TwoToThree_2, Q3, false);
            api.moveTo(AP3, Q3, false);
            LaserAndTakePhoto(3);
        } else if (start == 2 && end == 4){
            api.moveTo(TwoToFour, Q2, false);
            api.moveTo(AP4, Q4, false);
            LaserAndTakePhoto(4);
        } else if (start == 2 && end == 5){
            api.moveTo(TwoToFive, Q5, false);
            api.moveTo(AP5, Q5, false);
            LaserAndTakePhoto(5);
        } else if (start == 2 && end == 6){
            api.moveTo(TwoToSix, Q6, false);
            api.moveTo(AP6, Q6, false);
            LaserAndTakePhoto(6);
        } else if (start == 2 && end == 7){
            api.moveTo(TwoToSix, Q6, false);
            api.moveTo(AP6, Q7, false);
            doScan();
            if (active_targets.contains(6) || active_targets.contains(7)){
                api.moveTo(AP6, Q6, false);
                LaserAndTakePhoto(6);
            }
        } else if (start == 2 && end == 8){           //goal
            api.moveTo(new Point(10.63,-8.56,4.87), Q2, false);
            api.moveTo(P_Goal, Q_Goal, false);
        } else if (start == 3 && end == 1){
            api.moveTo(OneToThree, Q3, false);
            api.moveTo(AP1, Q1, false);
            LaserAndTakePhoto(1);
        } else if (start == 3 && end == 2){
            api.moveTo(TwoToThree_2, Q2, false);
            api.moveTo(TwoToThree_1, Q2, false);
            api.moveTo(AP2, Q2, false);
            LaserAndTakePhoto(2);
        } else if (start == 3 && end == 4){
            api.moveTo(ThreeToFour, Q4, false);
            api.moveTo(AP4, Q4, false);
            LaserAndTakePhoto(4);
        } else if (start == 3 && end == 5){
            api.moveTo(ThreeToFive_1, Q3, false);
            api.moveTo(ThreeToFive_2, Q5, false);
            api.moveTo(AP5, Q5, false);
            LaserAndTakePhoto(5);
        } else if (start == 3 && end == 6){
            api.moveTo(ThreeToSix_1, Q6, false);
            api.moveTo(AP6, Q6, false);
            LaserAndTakePhoto(6);
        } else if (start == 3 && end == 7){
            api.moveTo(ThreeToSix_1, Q6, false);
            api.moveTo(AP6, Q7, false);
            doScan();
            if (active_targets.contains(6) || active_targets.contains(7)){
                api.moveTo(AP6, Q6, false);
                LaserAndTakePhoto(6);
            }
        } else if (start == 3 && end == 8){           //goal
            api.moveTo(P_Goal, Q_Goal, false);
        } else if (start == 4 && end == 1){
            api.moveTo(AP1, Q1, false);
            LaserAndTakePhoto(1);
        } else if (start == 4 && end == 2){
            api.moveTo(TwoToFour, Q4, false);
            api.moveTo(AP2, Q2, false);
            LaserAndTakePhoto(2);
        } else if (start == 4 && end == 3){
            api.moveTo(ThreeToFour, Q3, false);
            api.moveTo(AP3, Q3, false);
            LaserAndTakePhoto(3);
        } else if (start == 4 && end == 5){
            api.moveTo(AP5, Q5, false);
            LaserAndTakePhoto(5);
        } else if (start == 4 && end == 6){
            api.moveTo(FourToSix_1, Q6, false);
            api.moveTo(AP6, Q6, false);
            LaserAndTakePhoto(6);
        } else if (start == 4 && end == 7){
            api.moveTo(FourToSix_1, Q6, false);
            api.moveTo(AP6, Q7, false);
            doScan();
            if (active_targets.contains(6) || active_targets.contains(7)){
                api.moveTo(AP6, Q6, false);
                LaserAndTakePhoto(6);
            }
        } else if (start == 4 && end == 8){           //goal
            api.moveTo(P_Goal, Q_Goal, false);
        } else if (start == 5 && end == 1){
            api.moveTo(AP1, Q1, false);
            LaserAndTakePhoto(1);
        } else if (start == 5 && end == 2){
            api.moveTo(TwoToFive, Q2, false);
            api.moveTo(AP2, Q2, false);
            LaserAndTakePhoto(2);
        } else if (start == 5 && end == 3){
            api.moveTo(ThreeToFive_2, Q3, false);
            api.moveTo(ThreeToFive_1, Q3, false);
            api.moveTo(AP3, Q3, false);
            LaserAndTakePhoto(3);
        } else if (start == 5 && end == 4){
            api.moveTo(AP4, Q4, false);
            LaserAndTakePhoto(4);
        } else if (start == 5 && end == 6){
            api.moveTo(FiveToSix, Q6, false);
            api.moveTo(AP6, Q6, false);
            LaserAndTakePhoto(6);
        } else if (start == 5 && end == 7){
            api.moveTo(FiveToSix, Q6, false);
            api.moveTo(AP6, Q7, false);
            doScan();
            if (active_targets.contains(6) || active_targets.contains(7)){
                api.moveTo(AP6, Q6, false);
                LaserAndTakePhoto(6);
            }
        } else if (start == 5 && end == 8){           //goal                    //如果錯了要重抓
            api.moveTo(new Point(11.12, -7.52, 5.3393), Q5, false);
            api.moveTo(P_Goal, Q_Goal, false);
        } else if (start == 6 && end == 1){
            api.moveTo(AP1, Q1, false);
            LaserAndTakePhoto(1);
        } else if (start == 6 && end == 2){
            api.moveTo(TwoToSix, Q2, false);
            api.moveTo(AP2, Q2, false);
            LaserAndTakePhoto(2);
        } else if (start == 6 && end == 3){
            api.moveTo(ThreeToSix_1, Q3, false);
            api.moveTo(AP3, Q3, false);
            LaserAndTakePhoto(3);
        } else if (start == 6 && end == 4){
            api.moveTo(FourToSix_1, Q6, false);
            api.moveTo(AP4, Q4, false);
            LaserAndTakePhoto(4);
        } else if (start == 6 && end == 5){
            api.moveTo(FiveToSix, Q5, false);
            api.moveTo(AP5, Q5, false);
            LaserAndTakePhoto(5);
        } else if (start == 6 && end == 7){
            if (!hasScan){
                api.moveTo(AP6, Q7, false);
                doScan();
            }
        } else if (start == 6 && end == 8){           //goal
            api.moveTo(new Point(11.15,-7.54,4.9), Q6, false);
            api.moveTo(P_Goal, Q_Goal, false);
        } else if (start == 7 && end == 1){
            api.moveTo(AP1, Q1, false);
            LaserAndTakePhoto(1);
        } else if (start == 7 && end == 2){
            api.moveTo(TwoToSix, Q2, false);
            api.moveTo(AP2, Q2, false);
            LaserAndTakePhoto(2);
        } else if (start == 7 && end == 3){
            api.moveTo(ThreeToSix_1, Q3, false);
            api.moveTo(AP3, Q3, false);
            LaserAndTakePhoto(3);
        } else if (start == 7 && end == 4){
            api.moveTo(FourToSix_1, Q6, false);
            api.moveTo(AP4, Q4, false);
            LaserAndTakePhoto(4);
        } else if (start == 7 && end == 5){
            api.moveTo(FiveToSix, Q5, false);
            api.moveTo(AP5, Q5, false);
            LaserAndTakePhoto(5);
        } else if (start == 7 && end == 6){
            api.moveTo(AP6,  Q6, false);
            LaserAndTakePhoto(6);
        } else if (start == 7 && end == 8){           //goal
            api.moveTo(new Point(11.15,-7.54,4.9), Q6, false);
            api.moveTo(P_Goal, Q_Goal, false);
        }
    }
}