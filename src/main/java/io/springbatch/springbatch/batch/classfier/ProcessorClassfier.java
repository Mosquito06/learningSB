package io.springbatch.springbatch.batch.classfier;


import io.springbatch.springbatch.batch.domain.ApiReqeustVO;
import io.springbatch.springbatch.batch.domain.ProductVO;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

import java.util.HashMap;
import java.util.Map;

public class ProcessorClassfier<C, T> implements Classifier<C, T>
{
    private Map<String, ItemProcessor<ProductVO, ApiReqeustVO>> processorMap = new HashMap<>();

    public void setProcessorMap(Map<String, ItemProcessor<ProductVO, ApiReqeustVO>> processorMap)
    {
        this.processorMap = processorMap;
    }

    @Override
    public T classify(C classfiable)
    {
        return (T) processorMap.get(((ProductVO) classfiable).getType());
    }
}
