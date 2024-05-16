package com.lessons.linkalias.service;

import com.lessons.linkalias.dto.LinkRequest;
import com.lessons.linkalias.exceptions.TTLException;
import com.lessons.linkalias.model.LinkAlias;
import com.lessons.linkalias.repository.LinkAliasRepository;
import com.lessons.linkalias.util.ShortLinkCreater;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Period;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class LinkAliasService {

    private final LinkAliasRepository linkAliasRepository;
    private  final ShortLinkCreater shortLinkCreater;

    @Transactional
    public String createShortLink(LinkRequest linkRequest){
        var getBase = linkAliasRepository.findByBaseLink(linkRequest.getUrl());
        return getBase.map(LinkAlias::getShortLink).orElseGet(()->{
            var shortLink = shortLinkCreater.createShortLink(linkRequest.getUrl());
            linkAliasRepository.save(LinkAlias.builder()
                    .baseLink(linkRequest.getUrl())
                    .ttl(linkRequest.getTTL())
                    .shortLink(shortLink)
                    .createTime(Instant.now())
                    .build()
            );
            return  shortLink;
        });
    }

    public String getRedirect(String link) {
        var aliasLink = linkAliasRepository.findByShortLink(link).orElseThrow(IllegalArgumentException::new);
        var currentTime = Instant.now();
        if(aliasLink.getCreateTime().plusMillis(aliasLink.getTtl()).compareTo(currentTime) < 0 && aliasLink.getTtl() != 0){
            linkAliasRepository.delete(aliasLink);
            linkAliasRepository.flush();
            throw  new TTLException(aliasLink.getBaseLink());
        }
        return aliasLink.getBaseLink();
    }
}
