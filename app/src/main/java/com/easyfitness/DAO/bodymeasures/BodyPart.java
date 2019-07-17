package com.easyfitness.DAO.bodymeasures;

import com.easyfitness.R;

/* 데이터베이스 객체(object) */
public class BodyPart {
    public static final int ABDOMINAUX = 0; // 복근
    public static final int ADDUCTEURS = 1; // 내전근 (다리)
    public static final int BICEPS = 2;     // 이두근 (팔)
    public static final int TRICEPS = 3;    // 삼두근 (팔)
    public static final int DELTOIDS = 4;   // 삼각근 (가슴)
    public static final int MOLLETS = 5;    // 종아리 (다리)
    public static final int PECTORAUX = 6;  // 흉근 (가슴)
    public static final int DORSEAUX = 7;   // 등 (가슴)
    public static final int QUADRICEPS = 8; // 넓적다리 앞쪽의 근육 (다리)
    public static final int ISCHIOJAMBIERS = 9; // 다리 근육 (다리)
    public static final int LEFTARM = 10;   // 왼쪽 팔 (팔)
    public static final int RIGHTARM = 11;  // 오른쪽 팔 (팔)
    public static final int LEFTTHIGH = 12; // 왼쪽 허벅지 (다리)
    public static final int RIGHTTHIGH = 13;// 오른쪽 허벅지 (다리)
    public static final int LEFTCALVES = 14;// 왼쪽 종아리 (다리)
    public static final int RIGHTCALVES = 15; // 오른쪽 종아리 (다리)
    public static final int WAIST = 16;     // 허리
    public static final int NECK = 17;      // 목
    public static final int BEHIND = 18;    // 엉덩이
    public static final int WEIGHT = 19;    // 몸무게
    public static final int FAT = 20;       // 체지방
    public static final int BONES = 21;     // 뼈
    public static final int WATER = 22;     // 체수분
    public static final int MUSCLES = 23;   // 근육

    private int id;    // ID는 long 타입
    private BodyMeasure mLastMeasure;   // 최근 측정값

    public BodyPart(int id) {
        // ID를 받아들이면
        // BodyPart 클래스 내의 id 설정
        super();
        this.id = id;
        this.mLastMeasure = null;
    }

    public BodyPart(int id, BodyMeasure lastMeasure) {
        // ID랑 최근 측정값 받아들이면
        // BodyPart 클래스 내의 id랑 lastMeasure 설정
        super();
        this.id = id;
        this.mLastMeasure = lastMeasure;
    }

    private static int getBodyResourceID(int pBodyID) {
        // pBodyID = line 7 ~ 30에서 정의한 몸의 각 부분의 ID값
        // 몸 부분 ID 받아들이면 어떤 부위인지 이름 리턴
        switch (pBodyID) {
            case ABDOMINAUX: // 복근
                return R.string.abdominaux;
            case ADDUCTEURS: // 내전근 (다리)
                return R.string.adducteurs;
            case BICEPS: // 이두근 (팔)
                return R.string.biceps;
            case TRICEPS: // 삼두근 (팔)
                return R.string.triceps;
            case DELTOIDS: // 삼각근 (가슴)
                return R.string.deltoids;
            case MOLLETS: // 종아리 (다리)
                return R.string.mollets;
            case PECTORAUX: // 흉근 (가슴)
                return R.string.pectoraux;
            case DORSEAUX: // 등 (가슴)
                return R.string.dorseaux;
            case QUADRICEPS: // 넓적다리 앞쪽의 근육 (다리)
                return R.string.quadriceps;
            case ISCHIOJAMBIERS: // 다리 근육 (다리)
                return R.string.ischio_jambiers;
            case LEFTARM: // 왼쪽 팔 (팔)
                return R.string.left_arm;
            case RIGHTARM: // 오른쪽 팔 (팔)
                return R.string.right_arm;
            case LEFTTHIGH: // 왼쪽 허벅지 (다리)
                return R.string.left_thigh;
            case RIGHTTHIGH: // 오른쪽 허벅지 (다리)
                return R.string.right_thigh;
            case LEFTCALVES: // 왼쪽 종아리 (다리)
                return R.string.left_calves;
            case RIGHTCALVES: // 오른쪽 종아리 (다리)
                return R.string.right_calves;
            case WAIST: // 허리
                return R.string.waist;
            case NECK: // 목
                return R.string.neck;
            case BEHIND: // 엉덩이
                return R.string.behind;
            case WEIGHT: // 몸무게 (kg)
                return R.string.weightLabel;
            case FAT: // 체지방 (%)
                return R.string.fatLabel;
            case BONES: // 뼈 (%)
                return R.string.bonesLabel;
            case WATER: // 체수분 (%)
                return R.string.waterLabel;
            case MUSCLES: // 근육
                return R.string.musclesLabel;
        }

        return 0;
    }

    private static int getBodyLogoID(int pBodyID) {
        switch (pBodyID) {
            case ABDOMINAUX: // 복근
                return R.drawable.ic_chest;
            case ADDUCTEURS: // 내전근 (다리)
                return R.drawable.ic_leg;
            case BICEPS: // 이두근 (팔)
                return R.drawable.ic_arm;
            case TRICEPS: // 삼두근 (팔)
                return R.drawable.ic_arm;
            case DELTOIDS: // 삼각근 (가슴)
                return R.drawable.ic_chest;
            case MOLLETS: // 종아리 (다리)
                return R.drawable.ic_leg;
            case PECTORAUX: // 흉근 (가슴)
                return R.drawable.ic_chest_measure;
            case DORSEAUX: // 등 (가슴)
                return R.drawable.ic_chest;
            case QUADRICEPS: // 넓적다리 앞쪽의 근육 (다리)
                return R.drawable.ic_leg;
            case ISCHIOJAMBIERS: // 다리 근육 (다리)
                return R.drawable.ic_leg;
            case LEFTARM: // 왼쪽 팔 (팔)
                return R.drawable.ic_arm_measure;
            case RIGHTARM: // 오른쪽 팔 (팔)
                return R.drawable.ic_arm_measure;
            case LEFTTHIGH: // 왼쪽 허벅지 (다리)
                return R.drawable.ic_tight_measure;
            case RIGHTTHIGH: // 오른쪽 허벅지 (다리)
                return R.drawable.ic_tight_measure;
            case LEFTCALVES: // 왼쪽 종아리 (다리)
                return R.drawable.ic_calve_measure;
            case RIGHTCALVES: // 오른쪽 종아리 (다리)
                return R.drawable.ic_calve_measure;
            case WAIST: // 허리
                return R.drawable.ic_waist_measure;
            case NECK: // 목
                return R.drawable.ic_neck;
            case BEHIND: // 엉덩이
                return R.drawable.ic_buttock_measure;
        }

        return 0;
    }

    public long getId() {
        return id;
    }
    // ID 리턴


    public int getResourceNameID() {
        return getBodyResourceID((int) id);
    }
    // 몸 부위의 ID 리턴


    public int getResourceLogoID() {
        return getBodyLogoID((int) id);
    }
    // Body logo ID 리턴


    public BodyMeasure getLastMeasure() {
        return this.mLastMeasure;
    }
    // 최근 측정값 리턴


    public void setLastMeasure(BodyMeasure lastmeasure) {
        this.mLastMeasure = lastmeasure;
    }
    // 최근 측정값 설정
}
