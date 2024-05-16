package com.lessons.linkalias.util;

import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.temporal.TemporalAmount;

public class ShortLinkCreaterImp implements  ShortLinkCreater{

    @Value("${short.link}")
    private String baseURL;

    @Override
    public String createShortLink(String baseLink) {
        long seed = getSeed();
        StringBuilder stringBuilder = new StringBuilder(baseURL);
        while(seed > 0){
            stringBuilder.append('A' + seed/26);
            seed /=26;
        }
        return  stringBuilder.toString();
    }

    private synchronized Long getSeed(){
        return Instant.now().getEpochSecond();
    }
}
