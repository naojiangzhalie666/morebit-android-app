package com.zjzy.morebit.interfaces;


import com.zjzy.morebit.pojo.UserIncomeDetail;

public interface EarningBalanceCallback {
    void getBalance(UserIncomeDetail earnings);
    void refreshComplete();
}
