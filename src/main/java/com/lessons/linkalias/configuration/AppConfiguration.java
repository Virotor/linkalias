package com.lessons.linkalias.configuration;

import com.lessons.linkalias.util.ShortLinkCreater;
import com.lessons.linkalias.util.ShortLinkCreaterImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public ShortLinkCreater getShortLinkCreater(){

        return  new ShortLinkCreaterImp();
    }

}
