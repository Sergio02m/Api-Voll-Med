package med.voll.api.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    // Metodo para agregar una autorización en el encabezado de nuestras peticiones

    // Tenemos que agregar un método del tipo vean, colocar la anotación bean para ejecutar ese
    // método de forma automática dentro del contexto de Spring framework.
    // Así como colocar la anotación de requerimientos de seguridad dentro de todos los controladores
    // que vayan a tener acceso a ese token.

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                .addSecuritySchemes("bearer-key",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }

    @Bean
    public void message(){
        System.out.println("bearer is working");
    }


}
