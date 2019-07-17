package com.easyfitness.DAO.bodymeasures;

import java.util.Date;

/* 데이터베이스 객체(object) */
public class BodyMeasure {
    private long id;    // ID는 long 타입
    private Date mDate;
    private int mBodypart_id;
    private float mMeasure;
    private long mProfil_id;

    public BodyMeasure(long id, Date pDate, int pBodypart_id, float pMeasure, long pProfil_id) {
        // 넘겨받은 ID/날짜/BodypartID/측정값/프로필 ID를 BodyMeasure 클래스에 저장
        super();
        this.id = id;
        this.mDate = pDate;
        this.mBodypart_id = pBodypart_id;
        this.mMeasure = pMeasure;
        this.mProfil_id = pProfil_id;
    }

    public long getId() {
        return id;
    }   // ID 리턴

    public void setId(long id) {
        this.id = id;
    }   // ID 설정

    public Date getDate() {
        return mDate;
    }   // 날짜 리턴

    /**
     * @return long Body Part ID
     */
    public int getBodyPartID() {
        return mBodypart_id;
    }   // Bodypart ID 리턴

    public float getBodyMeasure() {
        return mMeasure;
    }   // 측정값 리턴

    public long getProfileID() {
        return mProfil_id;
    }   // 프로필 ID 리턴

}
