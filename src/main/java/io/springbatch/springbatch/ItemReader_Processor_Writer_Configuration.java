package io.springbatch.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class ItemReader_Processor_Writer_Configuration
{
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob()
    {
        return jobBuilderFactory.get("itemJob")
                .start( Step1() )
                .next( Step2() )
                .build();
    }

    @Bean
    public Step Step1()
    {
        return stepBuilderFactory.get("Step1")
                .<Customer, Customer>chunk(3)
                .reader( itemReader()  )
                .processor( itemProcessor() )
                .writer( itemWriter() )
                .build();
    }

    @Bean
    public ItemReader<Customer> itemReader()
    {
        return new CustomItemReader(Arrays.asList(new Customer("user1"), new Customer("user2"), new Customer("user3")));
    }

    @Bean
    public ItemProcessor<? super Customer,? extends Customer> itemProcessor()
    {
        return new CustomItemProcessor();
    }

    @Bean
    public ItemWriter<? super Customer> itemWriter()
    {
        return new CustomItemWriter();
    }

    public Step Step2()
    {
        return stepBuilderFactory.get("Step2")
                .tasklet(new Tasklet()
                {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception
                    {
                        System.out.println(" >> step2 was executed ");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
}
