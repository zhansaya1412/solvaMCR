package kz.zhansaya.solvaMCR.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "alphaVantageClient", url = "${feign.client.alphaVantageClient.url}")
public interface AlphaVantageFeignClient {

    @GetMapping("/query")
    String fetchDailyFXData(@RequestParam("function") String function,
                            @RequestParam("from_symbol") String fromSymbol,
                            @RequestParam("to_symbol") String toSymbol,
                            @RequestParam("apikey") String apiKey);
}
