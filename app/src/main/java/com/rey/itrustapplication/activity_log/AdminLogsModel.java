package com.rey.itrustapplication.activity_log;

public class AdminLogsModel {

    String logsTitle, logsTime, logsDate;

    public AdminLogsModel(String logsTitle, String logsTime, String logsDate) {
        this.logsTitle = logsTitle;
        this.logsTime = logsTime;
        this.logsDate = logsDate;
    }

    public String getLogsTitle() {
        return logsTitle;
    }

    public String getLogsTime() {
        return logsTime;
    }

    public String getLogsDate() {
        return logsDate;
    }
}
