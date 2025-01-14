package com.px.common.file.constants;

/**
 * 文件类型常量
 * 常量名称规则： __为目录/ _为单词分割-
 */
public interface AttachmentTypePathConstant {
    /**
     * 车辆停泊记录-系统录入
     */
    String CAR_LOT_RECORD__SYSTEM = "car-lot-record/system";
    /**
     * 车辆停泊记录-臻识
     */
    String CAR_LOT_RECORD__ZS = "car-lot-record/zs";
    /**
     * 车辆停泊记录-嘉视成
     */
    String CAR_LOT_RECORD__JSC = "car-lot-record/jsc";

    /**
     * 用户-头像
     */
    String USER__AVATAR = "user/avatar";
    /**
     * 用户-身份证反面
     */
    String USER__ID_CARD_BACK = "user/id-card-back";
    /**
     * 用户-身份证正面
     */
    String USER__ID_CARD_FRONT = "user/id-card-front";
    // TODO: 其他图片类型自己添加
}
