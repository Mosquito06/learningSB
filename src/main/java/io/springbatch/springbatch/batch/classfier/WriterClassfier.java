package io.springbatch.springbatch.batch.classfier;


import io.springbatch.springbatch.batch.domain.ApiReqeustVO;
import io.springbatch.springbatch.batch.domain.ProductVO;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

import java.util.HashMap;
import java.util.Map;

public class WriterClassfier<C, T> implements Classifier<C, T>
{
    private Map<String, ItemWriter<ApiReqeustVO>> writerMap = new HashMap<>();

    public void setWriterMap(Map<String, ItemWriter< ApiReqeustVO>> writerMap)
    {
        this.writerMap = writerMap;
    }

    @Override
    public T classify(C classfiable)
    {
        return (T) writerMap.get(((ApiReqeustVO) classfiable).getProductVO().getType());
    }
}
