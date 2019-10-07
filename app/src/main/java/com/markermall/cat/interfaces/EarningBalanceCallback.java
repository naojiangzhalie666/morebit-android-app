package com.markermall.cat.interfaces;


import com.markermall.cat.pojo.DayEarnings;

public interface EarningBalanceCallback {
    void getBalance(DayEarnings earnings);
    void refreshComplete();
}
