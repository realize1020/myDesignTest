package com.example.design.test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 原生Java HTTP客户端Demo
 * 演示POST请求和body体传参
 */
public class HttpClientDemo {
    
    /**
     * 发送POST请求（JSON格式）
     */
    public static String sendPostJson(String urlStr, String jsonBody) throws IOException {
        System.out.println("发送POST请求（JSON格式）");
        System.out.println("URL: " + urlStr);
        System.out.println("请求体: " + jsonBody);
        
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        try {
            // 设置请求属性
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            
            // 发送请求体
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // 获取响应
            int responseCode = connection.getResponseCode();
            System.out.println("响应码: " + responseCode);
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readResponse(connection);
            } else {
                return "请求失败，响应码: " + responseCode + "，错误信息: " + readErrorResponse(connection);
            }
            
        } finally {
            connection.disconnect();
        }
    }
    
    /**
     * 发送POST请求（表单格式）
     */
    public static String sendPostForm(String urlStr, String formData) throws IOException {
        System.out.println("发送POST请求（表单格式）");
        System.out.println("URL: " + urlStr);
        System.out.println("表单数据: " + formData);
        
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        try {
            // 设置请求属性
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            
            // 发送请求体
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = formData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // 获取响应
            int responseCode = connection.getResponseCode();
            System.out.println("响应码: " + responseCode);
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readResponse(connection);
            } else {
                return "请求失败，响应码: " + responseCode + "，错误信息: " + readErrorResponse(connection);
            }
            
        } finally {
            connection.disconnect();
        }
    }
    
    /**
     * 发送POST请求（自定义Content-Type）
     */
    public static String sendPostCustom(String urlStr, String body, String contentType) throws IOException {
        System.out.println("发送POST请求（自定义Content-Type）");
        System.out.println("URL: " + urlStr);
        System.out.println("请求体: " + body);
        System.out.println("Content-Type: " + contentType);
        
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        try {
            // 设置请求属性
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", contentType);
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            
            // 发送请求体
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // 获取响应
            int responseCode = connection.getResponseCode();
            System.out.println("响应码: " + responseCode);
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readResponse(connection);
            } else {
                return "请求失败，响应码: " + responseCode + "，错误信息: " + readErrorResponse(connection);
            }
            
        } finally {
            connection.disconnect();
        }
    }
    
    /**
     * 发送POST请求（带请求头）
     */
    public static String sendPostWithHeaders(String urlStr, String body, String contentType, 
                                           String authorization) throws IOException {
        System.out.println("发送POST请求（带请求头）");
        System.out.println("URL: " + urlStr);
        System.out.println("请求体: " + body);
        
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        try {
            // 设置请求属性
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("User-Agent", "Java-HTTP-Client/1.0");
            
            if (authorization != null && !authorization.isEmpty()) {
                connection.setRequestProperty("Authorization", authorization);
            }
            
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            
            // 发送请求体
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // 获取响应
            int responseCode = connection.getResponseCode();
            System.out.println("响应码: " + responseCode);
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readResponse(connection);
            } else {
                return "请求失败，响应码: " + responseCode + "，错误信息: " + readErrorResponse(connection);
            }
            
        } finally {
            connection.disconnect();
        }
    }
    
    /**
     * 读取成功响应
     */
    private static String readResponse(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        System.out.println("响应内容: " + response.toString());
        return response.toString();
    }
    
    /**
     * 读取错误响应
     */
    private static String readErrorResponse(HttpURLConnection connection) throws IOException {
        StringBuilder errorResponse = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                errorResponse.append(responseLine.trim());
            }
        }
        return errorResponse.toString();
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        try {
            // 测试JSON POST请求
            String jsonUrl = "https://httpbin.org/post";
            String jsonBody = "{\"name\":\"张三\",\"age\":25,\"city\":\"北京\"}";
            
            System.out.println("=== 测试JSON POST请求 ===");
            String jsonResponse = sendPostJson(jsonUrl, jsonBody);
            System.out.println("JSON响应结果: " + jsonResponse);
            System.out.println();
            
            // 测试表单POST请求
            String formUrl = "https://httpbin.org/post";
            String formData = "username=testuser&password=123456&email=test@example.com";
            
            System.out.println("=== 测试表单POST请求 ===");
            String formResponse = sendPostForm(formUrl, formData);
            System.out.println("表单响应结果: " + formResponse);
            System.out.println();
            
            // 测试自定义Content-Type
            String customUrl = "https://httpbin.org/post";
            String xmlBody = "<user><name>李四</name><age>30</age></user>";
            
            System.out.println("=== 测试XML POST请求 ===");
            String xmlResponse = sendPostCustom(customUrl, xmlBody, "application/xml");
            System.out.println("XML响应结果: " + xmlResponse);
            System.out.println();
            
            // 测试带认证头的请求
            String authUrl = "https://httpbin.org/post";
            String authBody = "{\"message\":\"Hello World\"}";
            
            System.out.println("=== 测试带认证头的POST请求 ===");
            String authResponse = sendPostWithHeaders(authUrl, authBody, "application/json", 
                                                    "Bearer your-token-here");
            System.out.println("认证请求响应结果: " + authResponse);
            
        } catch (IOException e) {
            System.err.println("请求发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
}