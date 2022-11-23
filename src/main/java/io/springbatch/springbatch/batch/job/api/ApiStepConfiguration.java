package io.springbatch.springbatch.batch.job.api;

import io.springbatch.springbatch.batch.chunk.processor.ApiItemProcessor1;
import io.springbatch.springbatch.batch.chunk.processor.ApiItemProcessor2;
import io.springbatch.springbatch.batch.chunk.processor.ApiItemProcessor3;
import io.springbatch.springbatch.batch.chunk.writer.ApiItemWriter1;
import io.springbatch.springbatch.batch.chunk.writer.ApiItemWriter2;
import io.springbatch.springbatch.batch.chunk.writer.ApiItemWriter3;
import io.springbatch.springbatch.batch.classfier.ProcessorClassfier;
import io.springbatch.springbatch.batch.classfier.WriterClassfier;
import io.springbatch.springbatch.batch.domain.ApiReqeustVO;
import io.springbatch.springbatch.batch.domain.ProductVO;
import io.springbatch.springbatch.batch.partition.ProductPartitioner;
import io.springbatch.springbatch.service.AbstractApiService;
import io.springbatch.springbatch.service.ApiService1;
import io.springbatch.springbatch.service.ApiService2;
import io.springbatch.springbatch.service.ApiService3;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.build.Plugin;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ApiStepConfiguration
{
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private final ApiService1 apiService1;

    private final ApiService2 apiService2;

    private final ApiService3 apiService3;

    public int chunkSize = 10;

     @Bean
     public Step apiMasterStep() throws Exception
     {
         return stepBuilderFactory.get("apiMasterStep")
                 .partitioner( apiSlaveStep().getName(), partitioner() )
                 .step( apiSlaveStep() )
                 .gridSize( 3 )
                 .taskExecutor( taskExecuter() )
                 .build();
     }

     @Bean
     public TaskExecutor taskExecuter()
     {
         ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
         threadPoolTaskExecutor.setCorePoolSize(3);
         threadPoolTaskExecutor.setMaxPoolSize(6);
         threadPoolTaskExecutor.setThreadNamePrefix("api-thread");

         return threadPoolTaskExecutor;
     }


    @Bean
     public ProductPartitioner partitioner()
    {
        ProductPartitioner productPartitioner = new ProductPartitioner();
        productPartitioner.setDataSource(dataSource);

        return productPartitioner;
    }

    @Bean
    public Step apiSlaveStep() throws Exception
    {
        return stepBuilderFactory.get("apiSlaveStep")
                .<ProductVO, ProductVO>chunk( chunkSize )
                .reader( itemReader(null) )
                .processor( itemProcessor() )
                .writer( itemWriter() )
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<ProductVO> itemReader(@Value("#{stepExecutionContext['product']}") ProductVO productVO) throws Exception {

        JdbcPagingItemReader<ProductVO> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(dataSource);
        reader.setPageSize(chunkSize);
        reader.setRowMapper(new BeanPropertyRowMapper(ProductVO.class));

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, name, price, type");
        queryProvider.setFromClause("from product");
        queryProvider.setWhereClause("where type = :type");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("id", Order.DESCENDING);
        queryProvider.setSortKeys(sortKeys);

        reader.setParameterValues(QueryGenerator.getParameterForQuery("type", productVO.getType()));
        reader.setQueryProvider(queryProvider);
        reader.afterPropertiesSet();

        return reader;
    }

    @Bean
    public ItemProcessor itemProcessor()
    {
        ClassifierCompositeItemProcessor<ProductVO, ApiReqeustVO> processor = new ClassifierCompositeItemProcessor<ProductVO, ApiReqeustVO>();

        ProcessorClassfier<ProductVO, ItemProcessor<?, ? extends ApiReqeustVO>> classfier = new ProcessorClassfier();
        Map<String, ItemProcessor<ProductVO, ApiReqeustVO>> processorMap = new HashMap<>();

        processorMap.put("1", new ApiItemProcessor1() );
        processorMap.put("2", new ApiItemProcessor2() );
        processorMap.put("3", new ApiItemProcessor3() );

        classfier.setProcessorMap( processorMap );

        processor.setClassifier( classfier );

        return processor;
    }

    @Bean
    public ItemWriter itemWriter()
    {
        ClassifierCompositeItemWriter<ApiReqeustVO> writer = new ClassifierCompositeItemWriter<ApiReqeustVO>();

        WriterClassfier<ApiReqeustVO, ItemWriter<? super ApiReqeustVO>> classfier = new WriterClassfier();
        Map<String, ItemWriter<ApiReqeustVO>> writerMap = new HashMap<>();

        writerMap.put("1", new ApiItemWriter1( apiService1 ) ); //
        writerMap.put("2", new ApiItemWriter2( apiService2 ) );
        writerMap.put("3", new ApiItemWriter3( apiService3 ) );

        classfier.setWriterMap( writerMap );

        writer.setClassifier( classfier );

        return writer;
    }
}
