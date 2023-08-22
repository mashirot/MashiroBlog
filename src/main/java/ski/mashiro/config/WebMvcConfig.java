package ski.mashiro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author MashiroT
 * @since 2022-08-22
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${cors.allow-origin-patterns}")
    private String originPatterns;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(originPatterns)
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowCredentials(true);
    }
}
