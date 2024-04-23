package kz.zhansaya.solvaMCR.components;

import kz.zhansaya.solvaMCR.feigns.AlphaVantageFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlphaVantageService {

    private final AlphaVantageFeignClient alphaVantageFeignClient;

    public String fetchDailyFXData(String fromSymbol, String toSymbol) {
        try {
            String function = "FX_DAILY";
            String apiKey = "VZSHMVH4IA4W0442";
            return alphaVantageFeignClient.fetchDailyFXData(function, fromSymbol, toSymbol, apiKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch daily FX data: " + e.getMessage());
        }
    }
}
