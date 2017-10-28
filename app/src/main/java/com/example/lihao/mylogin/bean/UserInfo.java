package com.example.lihao.mylogin.bean;

/**
 * Created by lihao on 17-10-22.
 */

public class UserInfo {


    /**
     * uid : 123456
     * nickname : 1
     * u_icon : public/static/uploads/user.png
     * u_age : 0
     * rank_level : 1
     * user_type : 5
     * create_time : 2017-10-05
     * address : 123
     * job : 123
     * sex : 男
     * visitorSum : 0
     */

    private int uid;
    private String nickname;
    private String u_icon;
    private int u_age;
    private int rank_level;
    private int user_type;
    private long create_time;
    private String address;
    private int job;
    private String sex;
    private int visitorSum;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getU_icon() {
        return u_icon;
    }

    public void setU_icon(String u_icon) {
        this.u_icon = u_icon;
    }

    public int getU_age() {
        return u_age;
    }

    public void setU_age(int u_age) {
        this.u_age = u_age;
    }

    public int getRank_level() {
        return rank_level;
    }

    public void setRank_level(int rank_level) {
        this.rank_level = rank_level;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getJob() {
        return job;
    }

    public void setJob(int job) {
        this.job = job;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getVisitorSum() {
        return visitorSum;
    }

    public void setVisitorSum(int visitorSum) {
        this.visitorSum = visitorSum;
    }
}
