package io.springbatch.springbatch.batch.chunk.processor;

import io.springbatch.springbatch.batch.domain.ApiReqeustVO;
import io.springbatch.springbatch.batch.domain.ProductVO;
import org.springframework.batch.item.ItemProcessor;

public class ApiItemProcessor2 implements ItemProcessor<ProductVO, ApiReqeustVO>
{
    @Override
    public ApiReqeustVO process(ProductVO productVO) throws Exception
    {
        return ApiReqeustVO.builder()
                .id(productVO.getId())
                .productVO(productVO)
                .build();
    }
}
