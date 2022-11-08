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
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class ChunkConfiguration
{
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob()
    {
        return jobBuilderFactory.get("chunkJob")
                .start(Step1())
                .next( Step2() )
                .build();
    }

    @Bean
    public Step Step1()
    {
        return stepBuilderFactory
                .get("Step1")
                .<String, String>chunk(5)
                .reader( new ListItemReader<>(Arrays.asList("item1", "item2", "ite3", "item4", "item5")))
                .processor(new ItemProcessor<String, String>() {
                    @Override
                    public String process(String s) throws Exception
                    {
                        Thread.sleep(300);
                        System.out.println("item = " + s);
                        return "my" + s;
                    }
                })
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(List<? extends String> list) throws Exception
                    {
                        Thread.sleep(300);
                        System.out.println("items = " + list);
                    }
                })
                .build();
    }

    public Step Step2()
    {
        return stepBuilderFactory
                .get("Step2")
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
