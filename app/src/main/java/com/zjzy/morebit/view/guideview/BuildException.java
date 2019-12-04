package com.zjzy.morebit.view.guideview;

/**
 * 遮罩系统运行异常的封装
 */
class BuildException extends RuntimeException {

    private static final long serialVersionUID = 6208777692136933357L;
    private final String mDetailMessage;

    public BuildException() {
        mDetailMessage = "General error.";
    }

    public BuildException(String detailMessage) {
        mDetailMessage = detailMessage;
    }

    @Override
    public String getMessage() {
        return "Build GuideFragment failed: " + mDetailMessage;
    }
}
