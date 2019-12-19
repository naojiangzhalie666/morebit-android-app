package com.zjzy.morebit.pojo.event;

/**
 * Created by haiping.liu on 2019-12-19.
 */
public class MyMoreCoinEvent {
    private  Long coin;
    public MyMoreCoinEvent(Long coin){
        this.coin = coin;

    }

    public Long getCoin() {
        return coin;
    }

    public void setCoin(Long coin) {
        this.coin = coin;
    }
}
