package com.openapi.demo.common;


public interface KisConstant {

    String REST_BASE_URL = "https://openapivts.koreainvestment.com:29443";
    String WS_BASE_URL = "ws://ops.koreainvestment.com:31000";

    String FHKUP03500100_PATH = "/uapi/domestic-stock/v1/quotations/inquire-daily-indexchartprice";
    String FHKST03030100_PATH = "/uapi/overseas-price/v1/quotations/inquire-daily-chartprice";
    String APPROVAL_PATH = "/oauth2/Approval";

    String[] itemCodeList = new String[]{"098120", "131100", "009520", "095570", "006840", "282330", "027410", "138930", "001465", "001460"};
}
