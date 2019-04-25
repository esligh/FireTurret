package com.esli.fireturret;


/**
 * Created by lisic on 2019/4/21.
 */

public class TestInstance {
    private static TestInstance sInstance;
    private int age;
    private String name;
    public static TestInstance getInstance()
    {
        if(sInstance == null){
            sInstance = new TestInstance();
        }
        return sInstance;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
