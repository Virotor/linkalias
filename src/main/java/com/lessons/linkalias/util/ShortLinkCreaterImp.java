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
        return baseURL+Long.toHexString(seed);
    }

    private synchronized Long getSeed(){
        return Instant.now().getEpochSecond();
    }
}
