package com.markermall.cat.pojo;

import java.io.Serializable;

/**
 * Created by fengrs on 2018/8/3.
 */

public class BillDetaliBean implements Serializable {
    int last_month_settlement;
    int this_month_estimate;

    public int getLast_month_settlement() {
        return last_month_settlement;
    }

    public void setLast_month_settlement(int last_month_settlement) {
        this.last_month_settlement = last_month_settlement;
    }

    public int getThis_month_estimate() {
        return this_month_estimate;
    }

    public void setThis_month_estimate(int this_month_estimate) {
        this.this_month_estimate = this_month_estimate;
    }
}
