package io.springbatch.springbatch.batch.chunk.writer;

import io.springbatch.springbatch.batch.domain.ApiReqeustVO;
import io.springbatch.springbatch.batch.domain.ApiResponseVO;
import io.springbatch.springbatch.service.AbstractApiService;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.util.List;

public class ApiItemWriter2 extends FlatFileItemWriter<ApiReqeustVO> {
    private final AbstractApiService apiService;

    public ApiItemWriter2(AbstractApiService apiService)
    {
        this.apiService = apiService;
    }

    @Override
    public void write(List<? extends ApiReqeustVO> list) throws Exception
    {
        ApiResponseVO responseVO = apiService.service(list);
        System.out.println("responseVO = " + responseVO );

        list.forEach( item -> item.setResponseVO( responseVO ));

        super.setResource(new FileSystemResource("C:\\GIT\\learningSB\\spring-batch\\src\\main\\resources\\product2.txt"));
        super.open( new ExecutionContext());
        super.setLineAggregator(new DelimitedLineAggregator<>());
        super.setAppendAllowed(true);
        super.write(list);
    }
}
