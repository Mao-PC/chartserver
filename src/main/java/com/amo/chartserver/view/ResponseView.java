package com.amo.chartserver.view;

import com.amo.chartserver.enums.ResponseStateEnum;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

/**
 * 返回给客户端的视图类封装
 */
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ResponseView implements Serializable {

    @NonNull
    private ResponseStateEnum state;

    private String msg;

    private Object data;

    public static ResponseView getSuccessView() {
        return new ResponseView(ResponseStateEnum.SUCCESS);
    }

    public static ResponseView getSuccessView(String msg) {
        return new ResponseView(ResponseStateEnum.SUCCESS, msg, null);
    }

    public static ResponseView getSuccessView(Object data) {
        return new ResponseView(ResponseStateEnum.SUCCESS, null, data);
    }

    public static ResponseView getSuccessView(String msg, Map<String, Object> data) {
        return new ResponseView(ResponseStateEnum.SUCCESS, msg, data);
    }

    public static ResponseView getFailedView() {
        return new ResponseView(ResponseStateEnum.FAILED);
    }

    public static ResponseView getFailedView(String msg) {
        return new ResponseView(ResponseStateEnum.FAILED, msg, null);
    }

    public static ResponseView getFailedView(Object data) {
        return new ResponseView(ResponseStateEnum.FAILED, null, data);
    }

    public static ResponseView getFailedView(String msg, Map<String, Object> data) {
        return new ResponseView(ResponseStateEnum.FAILED, msg, data);
    }
}
