package com.example.design.mybatisSource;

import org.apache.ibatis.reflection.property.PropertyTokenizer;

public class PropertyTokenizerTest {
    public static void main(String[] args) {
        PropertyTokenizer propertyTokenizer =new PropertyTokenizer("student[sId].name");
        System.out.println(propertyTokenizer.getName());
        System.out.println(propertyTokenizer.getIndexedName());
        System.out.println(propertyTokenizer.getIndex());
        System.out.println(propertyTokenizer.getChildren());
    }
}
