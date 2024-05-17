package com.lessons.linkalias.component;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Short link API",
                description = "Short link", version = "1.0.0",
                contact = @Contact(
                        name = "Kabarikha Victor",
                        email = "victor@gmail.dev",
                        url = "http://t.me/kabavolan"
                )
        )
)
public class OpenApiConfig {

}