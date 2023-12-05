package com.example.mipt5;

public class Currency {

    private String code;
    private String rate;

    public Currency(String code, String rate) {
        this.code = code;
        this.rate = rate;
    }

    public String getCode() {
        return code;
    }

    public String getRate() {
        return rate;
    }
}
