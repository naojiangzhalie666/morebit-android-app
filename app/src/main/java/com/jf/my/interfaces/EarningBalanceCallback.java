package com.jf.my.interfaces;


import com.jf.my.pojo.DayEarnings;

public interface EarningBalanceCallback {
    void getBalance(DayEarnings earnings);
    void refreshComplete();
}
