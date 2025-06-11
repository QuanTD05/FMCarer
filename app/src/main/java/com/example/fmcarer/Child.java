package com.example.fmcarer;
public class Child {
    private String childId;
    private String userId;
    private String name;
    private String birthday;
    private String gender;

    public Child() {}

    public Child(String childId, String userId, String name, String birthday, String gender) {
        this.childId = childId;
        this.userId = userId;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
    }

    public String getChildId() { return childId; }
    public void setChildId(String childId) { this.childId = childId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}
