package io.springbatch.springbatch.service;


import io.springbatch.springbatch.batch.domain.ApiInfo;
import io.springbatch.springbatch.batch.domain.ApiReqeustVO;
import io.springbatch.springbatch.batch.domain.ApiResponseVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService1 extends AbstractApiService
{
    @Override
    protected ApiResponseVO doApiService(RestTemplate restTemplate, ApiInfo apiInfo)
    {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8081/api/product/1", apiInfo, String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();

        ApiResponseVO apiResponseVO = ApiResponseVO.builder().status(statusCodeValue).msg(responseEntity.getBody()).build();

        return apiResponseVO;
    }
}
