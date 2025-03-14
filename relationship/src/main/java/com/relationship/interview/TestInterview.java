package com.relationship.interview;

import java.io.IOException;

public class TestInterview {
    public static void main(String[] args) {
        Parent p = new Child();
        try {
            p.testDisplay();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
    }
}
