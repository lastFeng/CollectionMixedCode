package com.springboot.o2o.enums;

/**
 * Created by wst on 2019/5/7.
 */
public enum ProductStateEnum {
    /**
     * 图片为空
     */
    NOT_IMG(0, "请添加商品图"),

    /**
     * 操作成功
     */
    SUCCESS(1, "操作成功"),

    /**
     * 内部错误
     */
    INNER_ERROR(-1001, "内部系统错误");

    private int state;
    private String stateInfo;

    ProductStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /**
     * 通过状态来查找对应枚举
     */
    public static ProductStateEnum stateOf(int state) {
        for (ProductStateEnum stateEnum : ProductStateEnum.values()) {
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
