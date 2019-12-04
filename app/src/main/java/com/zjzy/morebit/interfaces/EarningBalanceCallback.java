package com.zjzy.morebit.interfaces;


import com.zjzy.morebit.pojo.DayEarnings;

public interface EarningBalanceCallback {
    void getBalance(DayEarnings earnings);
    void refreshComplete();
}
