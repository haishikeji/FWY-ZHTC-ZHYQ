package com.px.common.enums;

public enum ProjectState {
    INPROGRESS(0, "进行中")
    , NORMAL(1, "正常")
    , ABNORMAL(2, "异常");

    private final long code;
    private final String info;

    ProjectState(long code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public long getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
