package com.lessons.linkalias.util;

import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;

public class ShortLinkCreaterImp implements  ShortLinkCreater{

    @Value("${short.link}")
    private String endpointRout;

    @Value("${short.address}")
    private  String serverAddress;

    public String getBase(){
        return serverAddress + endpointRout;
    }

    @Override
    public String createShortLink(String baseLink) {
        long seed = getSeed();
        return Long.toHexString(seed);
    }

    private synchronized Long getSeed(){
        return Instant.now().getEpochSecond();
    }
}
