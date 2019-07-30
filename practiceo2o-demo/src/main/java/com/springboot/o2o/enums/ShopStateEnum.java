package com.springboot.o2o.enums;

/**
 * Created by wst on 2019/5/7.
 */
public enum ShopStateEnum {
    /**
     * 审核
     */
    CHECK(0, "审核中"),
    /**
     * 下线
     */
    OFFLINE(-1, "非法店铺"),
    /**
     * 操作成功
     */
    SUCCESS(1, "操作成功"),
    /**
     * 通过
     */
    PASS(2, "通过成功"),
    /**
     * 内部错误
     */
    INNER_ERROR(-1001, "内部系统错误"),

    /**
     * 店铺为空
     */
    NULL_SHOP(-1003, "店铺为空");

    private int state;
    private String stateInfo;

    ShopStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /**
     * 通过状态来查找对应枚举
     */
    public static ShopStateEnum stateOf(int state) {
        for (ShopStateEnum stateEnum : ShopStateEnum.values()) {
            if (stateEnum.getState() == state) {
                return stateEnum;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
