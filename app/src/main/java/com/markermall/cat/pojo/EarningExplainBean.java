package com.markermall.cat.pojo;

import java.io.Serializable;

/**
 * @Author: fengrs
 * @Description:
 **/
public class EarningExplainBean implements Serializable {

    /**
     * consumptionStatement : 月消费预估收入说明~
     * lastMonthStatement : 上月消费结算预估收入说明~
     * payNumStatement : 付款笔数说明~
     * settlementStatement : 结算预付收入说明~
     * sysKey : SYSTEM_INCOME_DESCRIPTION
     * thisMonthStatement : 本月消费结算预估收入说明~
     * transactionStatement : 成交预付收入说明~
     */

    private String consumptionStatement;
    private String lastMonthStatement;
    private String payNumStatement;
    private String settlementStatement;
    private String sysKey;
    private String thisMonthStatement;
    private String transactionStatement;

    public String getConsumptionStatement() {
        return consumptionStatement;
    }

    public void setConsumptionStatement(String consumptionStatement) {
        this.consumptionStatement = consumptionStatement;
    }

    public String getLastMonthStatement() {
        return lastMonthStatement;
    }

    public void setLastMonthStatement(String lastMonthStatement) {
        this.lastMonthStatement = lastMonthStatement;
    }

    public String getPayNumStatement() {
        return payNumStatement;
    }

    public void setPayNumStatement(String payNumStatement) {
        this.payNumStatement = payNumStatement;
    }

    public String getSettlementStatement() {
        return settlementStatement;
    }

    public void setSettlementStatement(String settlementStatement) {
        this.settlementStatement = settlementStatement;
    }

    public String getSysKey() {
        return sysKey;
    }

    public void setSysKey(String sysKey) {
        this.sysKey = sysKey;
    }

    public String getThisMonthStatement() {
        return thisMonthStatement;
    }

    public void setThisMonthStatement(String thisMonthStatement) {
        this.thisMonthStatement = thisMonthStatement;
    }

    public String getTransactionStatement() {
        return transactionStatement;
    }

    public void setTransactionStatement(String transactionStatement) {
        this.transactionStatement = transactionStatement;
    }
}
