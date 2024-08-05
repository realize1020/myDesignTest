package com.example.design.single;

public enum EnumSingleton {


    INSTANCE;


    private String id;


    private String username;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public String getInfo(){
        return "id = "+id+", username = "+username;
    }


}


class test{
    public static void main(String[] args) {
        EnumSingleton enumSingleton = EnumSingleton.INSTANCE;
        enumSingleton.setId("123");
        enumSingleton.setUsername("lucy");
        System.out.println(enumSingleton.getInfo());

        EnumSingleton enumSingleton2 = EnumSingleton.INSTANCE;
        System.out.println(enumSingleton.hashCode());
        System.out.println(enumSingleton2.hashCode());
    }
}
