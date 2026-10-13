/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BEARER = "bearerAuth";
    private static final String NOTION_DOCS_URL =
            "https://app.notion.com/p/382385babde6810d98e3c84dc27990f8";
    private static final String LINKEDIN_URL =
            "https://www.linkedin.com/in/anshul-negi-55aa14345/";

    @Bean
    public OpenAPI hotelBookingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Property Booking API")
                        .version("1.0")
                        .description("""
                                ## Demo logins (pre-filled in Auth → login)

                                | Role | Email | Password |
                                | --- | --- | --- |
                                | Guest | guest@demo.com | Demo@123 |
                                | Hotel Manager (admin) | manager@demo.com | Demo@123 |

                                Admin role = HOTEL_MANAGER (manages /admin/* hotels and rooms). No separate super-admin.

                                ---

                                ## Try it in 60 seconds

                                ### Step 1 — Search hotels (no login)

                                Open **2. Hotels (public)** → **GET /hotels/search** → **Try it out** → **Execute**

                                Delhi + dates are pre-filled.

                                ### Step 2 — Login as guest

                                Open **1. Auth** → **POST /auth/login** → **Execute** → copy accessToken from the response

                                Guest credentials are pre-filled.

                                ### Step 3 — Authorize

                                Click **Authorize** (top right) → paste Bearer token or just the token → **Authorize** → **Close**

                                ### Step 4 — Create a booking

                                Open **3. Bookings** → **POST /bookings/init** → **Execute**

                                Sample hotelId and roomId are pre-filled (from search results).

                                ### Step 5 — Admin APIs (optional)

                                Login as manager@demo.com → **Authorize** → **4. Admin — Hotels** → **GET /admin/hotels** → **Execute**

                                ---

                                Full walkthrough: see **Documentation** link (Notion) at the top of this page.

                                **Author:** [Anshul Negi (LinkedIn)](%s) · [GitHub @NegiCoder](https://github.com/NegiCoder)

                                Cold start ~30–60s on free Azure tier. H2 demo DB re-seeds on each wake.
                                """.formatted(LINKEDIN_URL)))
                .externalDocs(new ExternalDocumentation()
                        .description("Full documentation (Notion)")
                        .url(NOTION_DOCS_URL))
                .components(new Components()
                        .addSecuritySchemes(BEARER, new SecurityScheme()
                                .name(BEARER)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("POST /auth/login → copy accessToken from response → Authorize here")));
    }
}
