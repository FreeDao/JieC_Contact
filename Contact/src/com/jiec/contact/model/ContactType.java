
package com.jiec.contact.model;

public class ContactType {

    // 客户
    public static final int sCustomer = 0;

    // 骚扰
    public static final int sHarass = 1;

    // 广告
    public static final int sAdv = 2;

    // 政府
    public static final int sGovernment = 3;

    // 银行
    public static final int sBank = 4;

    // 其他
    public static final int sOthers = 5;

    public static String getTypeName(int type) {
        String name = "客户";
        if (type == sHarass) {
            name = "骚扰";
        } else if (type == sAdv) {
            name = "广告";
        } else if (type == sGovernment) {
            name = "政府";
        } else if (type == sBank) {
            name = "银行";
        } else if (type == sOthers) {
            name = "其他";
        }
        return name;
    }
}
