package blog.auth.auth.services;

import blog.auth.auth.dto.ProfessionRecommendation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NeuroService {
    private final WebClient webClient;

    public NeuroService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + "api key")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
    /**
     * Отправляет навыки и интересы в ChatGPT и получает 5 профессий с описанием и рейтингом
     */
    public List<Mono<ProfessionRecommendation>> getTopProfessions(
            List<String> skills,
            List<String> interests,
            String education
    ) {
        List<Mono<ProfessionRecommendation>> recommendations = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String skill = "";
            String interest = "";
            for (int j = 0; j < skills.size(); j++) {
                skill = skill + " " + skills.get(j);
            }
            for (int j = 0; j < interests.size(); j++) {
                interest = interest + "" + interests.get(j);
            }
            Map<String, Object> body = Map.of(
                    "model", "gpt-4",
                    "messages", List.of(
                            Map.of("role", "user", "content",
                                    "Твоя задача дать на основе информации(навыки, интересы, образование) название подходящей должности на русском, и через ; разделить и написать описание, разделить еще раз и написать по 4-5 шкале(с плавающей точкой, никакого текста, просто сразу ответ, иначе сломаешь всё, там будет double, а не String) то, насколько именно эта должность подходит человеку: "
                                            + skill + " " + interest + " " + education)
                    ),
                    "temperature", 0.7
            );

            recommendations.add(webClient.post()
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .map(response -> {
                        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                        if (choices != null && !choices.isEmpty()) {
                            Map<String, Object> messageMap = (Map<String, Object>) choices.get(0).get("message");
                            String[] data = ((String) messageMap.get("content")).split(";");
                            return new ProfessionRecommendation(data[0], data[1], Double.parseDouble(data[2]));
                        }
                        return new ProfessionRecommendation("Нет ответа.", "Нет ответа.", 0);
                    }));
        }
        return recommendations;
    }
}