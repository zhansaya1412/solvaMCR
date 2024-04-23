package kz.zhansaya.solvaMCR.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "kz.zhansaya.solvaMCR.feigns")
public class FeignClientConfiguration {
}
