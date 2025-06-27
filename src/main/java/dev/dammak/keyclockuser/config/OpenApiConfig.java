//package dev.dammak.keyclockuser.config;
//
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
//import io.swagger.v3.oas.annotations.info.Contact;
//import io.swagger.v3.oas.annotations.info.Info;
//import io.swagger.v3.oas.annotations.info.License;
//import io.swagger.v3.oas.annotations.security.SecurityScheme;
//import io.swagger.v3.oas.annotations.servers.Server;
//import org.springframework.context.annotation.Configuration;
//
///**
// * OpenAPI configuration for API documentation.
// */
//@Configuration
//@OpenAPIDefinition(
//        info = @Info(
//                title = "User Service API",
//                version = "1.0.0",
//                description = "Enterprise User Management Microservice API",
//                contact = @Contact(
//                        name = "Architecture Team",
//                        email = "architecture@company.com"
//                ),
//                license = @License(
//                        name = "MIT License",
//                        url = "https://opensource.org/licenses/MIT"
//                )
//        ),
//        servers = {
//                @Server(url = "http://localhost:8080", description = "Development Server"),
//                @Server(url = "https://api.company.com", description = "Production Server")
//        }
//)
//@SecurityScheme(
//        name = "bearerAuth",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        scheme = "bearer"
//)
//public class OpenApiConfig {
//}