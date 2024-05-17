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

@Service
@RequiredArgsConstructor
public class LinkAliasService {

    private final LinkAliasRepository linkAliasRepository;
    private final ShortLinkCreater shortLinkCreater;

    @Transactional
    public String createShortLink(LinkRequest linkRequest){
        var baseLink = linkAliasRepository.findByBaseLink(linkRequest.getUrl()).orElse(LinkAlias.builder()
                .baseLink(linkRequest.getUrl())
                .shortLink(shortLinkCreater.createShortLink(linkRequest.getUrl()))
                .build());
        if(!linkRequest.getTTL().equals(baseLink.getTtl())){
            baseLink.setTtl(linkRequest.getTTL());
        }
        baseLink.setCreateTime(Instant.now());
        linkAliasRepository.save(baseLink);
        return shortLinkCreater.getBase()+baseLink.getShortLink();
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
        var baseLink = linkAliasRepository.findByBaseLink(linkRequest.getUrl()).orElseGet(
                ()->{
                    String uri = linkRequest.getPrettyUrl();
                    if(linkAliasRepository.existsByShortLink(uri)){
                         throw  new LinkIsResolvedException(linkRequest.getPrettyUrl());
                    }
                    return  LinkAlias.builder()
                            .baseLink(linkRequest.getUrl())
                            .shortLink(uri)
                            .build();
        });
        if(!linkRequest.getTTL().equals(baseLink.getTtl())){
            baseLink.setTtl(linkRequest.getTTL());
        }
        baseLink.setCreateTime(Instant.now());
        linkAliasRepository.save(baseLink);
        return shortLinkCreater.getBase()+ baseLink.getShortLink();
    }
}
