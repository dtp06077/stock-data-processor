package com.openapi.demo.transfer;

public class JsonTransfer {

    public static String getExePriceMessage(String approvalKey, String trKey) {
        return String.format(
                "{\"header\":{" +
                        "\"approval_key\":\"%s\"," +
                        "\"custtype\":\"P\"," +
                        "\"tr_type\":\"1\"," +
                        "\"content-type\":\"utf-8\"" +
                        "}," +
                        "\"body\":{" +
                        "\"input\":{" +
                        "\"tr_id\":\"H0STCNT0\"," +
                        "\"tr_key\":\"%s\"" +
                        "}" +
                        "}}",
                approvalKey, trKey
        );
    }
}
