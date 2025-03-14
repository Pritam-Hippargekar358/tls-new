package com.relationship.interview;

import java.io.IOException;

public class Child extends Parent{

    public Child() {
        System.out.println("Inside child constructor");
    }

    @Override
    public void testDisplay() throws IOException{
        System.out.println("Inside child testDisplay");
        throw new IOException("");
    }
}
