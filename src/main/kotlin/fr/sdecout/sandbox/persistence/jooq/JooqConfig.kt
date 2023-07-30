package fr.sdecout.sandbox.persistence.jooq

import org.jooq.conf.RenderNameCase.LOWER
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("jooq")
@Configuration
class JooqConfig {

    @Bean
    fun configurationCustomizer() = DefaultConfigurationCustomizer { it.settings().withRenderNameCase(LOWER) }

}
