package com.lessons.linkalias.service;

import com.lessons.linkalias.dto.LinkRequest;
import com.lessons.linkalias.dto.PrettyLinkRequest;
import com.lessons.linkalias.exceptions.LinkIsResolvedException;
import com.lessons.linkalias.exceptions.LinkNotFoundException;
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
    private final ShortLinkCreater shortLinkCreater;

    @Transactional
    public String createShortLink(LinkRequest linkRequest){
        var getBase = linkAliasRepository.findByBaseLink(linkRequest.getUrl()).orElse(LinkAlias.builder()
                .baseLink(linkRequest.getUrl())
                .shortLink(shortLinkCreater.createShortLink(linkRequest.getUrl()))
                .build());
        if(!linkRequest.getTTL().equals(getBase.getTtl())){
            getBase.setTtl(linkRequest.getTTL());
        }
        getBase.setCreateTime(Instant.now());
        linkAliasRepository.save(getBase);
        return getBase.getShortLink();
    }

    public String getRedirect(String link) {
        var aliasLink = linkAliasRepository.findByShortLink(link).orElseThrow(()->new LinkNotFoundException(link));
        var currentTime = Instant.now();
        if(aliasLink.getCreateTime().plusMillis(aliasLink.getTtl()).compareTo(currentTime) < 0 && aliasLink.getTtl() != 0){
            linkAliasRepository.delete(aliasLink);
            linkAliasRepository.flush();
            throw  new TTLException(aliasLink.getBaseLink());
        }
        return aliasLink.getBaseLink();
    }

    public String createPrettyLink(PrettyLinkRequest linkRequest) {
        var getBase = linkAliasRepository.findByBaseLink(linkRequest.getUrl()).orElse(LinkAlias.builder()
                .baseLink(linkRequest.getUrl())
                .shortLink(linkRequest.getPrettyUrl())
                .build());
        if(linkAliasRepository.existsByBaseLink(linkRequest.getPrettyUrl()) && getBase.getBaseLink().equals(linkRequest.getUrl())){
            throw  new LinkIsResolvedException(linkRequest.getPrettyUrl());
        }
        if(!linkRequest.getTTL().equals(getBase.getTtl())){
            getBase.setTtl(linkRequest.getTTL());
        }
        getBase.setCreateTime(Instant.now());
        linkAliasRepository.save(getBase);
        return getBase.getShortLink();
    }
}
