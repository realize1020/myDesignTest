package com.example.design.multiThread.皮带秤煤炭;

import lombok.Data;

@Data
public class LivePoint {

    private String name;

    private float single;

    public LivePoint(String name, float single) {
        this.name = name;
        this.single = single;
    }
}
