package com.amo.chartserver.view;

import lombok.*;

import java.util.Map;

/**
 * 请求视图
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestView {

    private String key;

    @NonNull
    private String url;

    private Object[] params;

}
