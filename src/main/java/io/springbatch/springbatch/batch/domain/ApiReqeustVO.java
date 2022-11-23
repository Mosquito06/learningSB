package io.springbatch.springbatch.batch.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiReqeustVO
{
    private long id;
    private ProductVO productVO;
    private ApiResponseVO responseVO;
}
