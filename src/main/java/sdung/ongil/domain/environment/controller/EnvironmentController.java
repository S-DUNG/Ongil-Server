package sdung.ongil.domain.environment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sdung.ongil.domain.environment.dto.SafetyResponseDto;
import sdung.ongil.domain.environment.dto.WeatherResponseDto;
import sdung.ongil.domain.environment.service.EnvironmentService;

@RestController
@RequestMapping("/environment")
@RequiredArgsConstructor
public class EnvironmentController {
    private final EnvironmentService environmentService;

    @GetMapping("/weather")
    public WeatherResponseDto getWeather(@RequestParam double lat, @RequestParam double lng) {
        return environmentService.getWeather(lat, lng);
    }

    @GetMapping("/safety")
    public SafetyResponseDto getSafety(@RequestParam double lat, @RequestParam double lng) {
        return environmentService.getSafety(lat, lng);
    }
}
