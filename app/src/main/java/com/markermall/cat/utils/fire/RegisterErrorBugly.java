package com.markermall.cat.utils.fire;

/**
 * Created by fengrs on 2018/7/7.
 */

public class RegisterErrorBugly extends Exception {

    public RegisterErrorBugly(String errorText) {
        super(errorText);
    }
}
