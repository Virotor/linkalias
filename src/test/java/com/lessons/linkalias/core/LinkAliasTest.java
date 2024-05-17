package com.lessons.linkalias.core;

import com.lessons.linkalias.dto.LinkRequest;
import com.lessons.linkalias.dto.PrettyLinkRequest;
import com.lessons.linkalias.exceptions.LinkIsResolvedException;
import com.lessons.linkalias.exceptions.LinkNotFoundException;
import com.lessons.linkalias.exceptions.TTLException;
import com.lessons.linkalias.model.LinkAlias;
import com.lessons.linkalias.repository.LinkAliasRepository;
import com.lessons.linkalias.service.LinkAliasService;
import com.lessons.linkalias.util.ShortLinkCreater;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class LinkAliasTest {

    @Mock
    private LinkAliasRepository linkAliasRepository;

    @InjectMocks
    private LinkAliasService linkAliasService;

    @Mock
    private ShortLinkCreater shortLinkCreater ;


    @Test
    public  void testLinkAliasNew(){
        when(shortLinkCreater.createShortLink("testBase")).thenReturn("testShort");
        when(shortLinkCreater.getBase()).thenReturn("shortLink/");
        LinkAlias linkAlias = LinkAlias.builder()
                .baseLink("testBase")
                .shortLink("testShort")
                .createTime(Instant.EPOCH)
                .ttl(0L)
                .build();
        LinkRequest linkRequest = new LinkRequest("testBase", 0L);
        when(linkAliasRepository.findByBaseLink("testBase")).thenReturn(Optional.empty());
        when(linkAliasRepository.save(linkAlias)).thenReturn(linkAlias);
        assertThat(linkAliasService.createShortLink(linkRequest)).isEqualTo("shortLink/testShort");
    }

    @Test
    public  void testLinkAliasOldLink(){
        when(shortLinkCreater.createShortLink("testBase")).thenReturn("testShort");
        when(shortLinkCreater.getBase()).thenReturn("shortLink/");
        LinkAlias linkAlias = LinkAlias.builder()
                .baseLink("testBase")
                .shortLink("testShort")
                .createTime(Instant.EPOCH)
                .ttl(0L)
                .build();
        LinkRequest linkRequest = new LinkRequest("testBase", 0L);
        when(linkAliasRepository.findByBaseLink("testBase")).thenReturn(Optional.of(linkAlias));
        assertThat(linkAliasService.createShortLink(linkRequest)).isEqualTo("shortLink/testShort");
    }

    @Test
    public  void testLinkAliasCreatePretty(){
        when(shortLinkCreater.getBase()).thenReturn("prettyLink");
        PrettyLinkRequest linkRequest = new PrettyLinkRequest("testBase", 100L, "/prettyUrl");
        when(linkAliasRepository.findByBaseLink("testBase")).thenReturn(Optional.empty());
        when(linkAliasRepository.existsByShortLink("/prettyUrl")).thenReturn(false);
        assertThat(linkAliasService.createPrettyLink(linkRequest)).isEqualTo("prettyLink/prettyUrl");
    }

    @Test
    public  void testLinkAliasCreatePrettyLinkResolveException(){

        PrettyLinkRequest linkRequest = new PrettyLinkRequest("testBaseNew", 100L, "/prettyUrl");
        when(linkAliasRepository.existsByShortLink(linkRequest.getPrettyUrl())).thenReturn(true);
        when(linkAliasRepository.findByBaseLink("testBaseNew")).thenReturn(Optional.empty());
        assertThatThrownBy(()->linkAliasService.createPrettyLink(linkRequest)).isInstanceOf(LinkIsResolvedException.class);
    }

    @Test
    public  void testLinkAliasGetRedirect(){
        LinkAlias linkAlias = LinkAlias.builder()
                .baseLink("testBase")
                .shortLink("/redirect/prettyUrl")
                .createTime(Instant.EPOCH)
                .ttl(0L)
                .build();
        when(linkAliasRepository.findByShortLink("/redirect/prettyUrl")).thenReturn(Optional.of(linkAlias));
        assertThat(linkAliasService.getRedirect("/redirect/prettyUrl")).isEqualTo("testBase");
    }

    @Test
    public  void testLinkAliasGetRedirectTTLException(){
        LinkAlias linkAlias = LinkAlias.builder()
                .baseLink("testBase")
                .shortLink("/redirect/prettyUrl")
                .createTime(Instant.EPOCH)
                .ttl(100L)
                .build();
        when(linkAliasRepository.findByShortLink(linkAlias.getShortLink())).thenReturn(Optional.of(linkAlias));
        assertThatThrownBy(()->linkAliasService.getRedirect(linkAlias.getShortLink())).isInstanceOf(TTLException.class);
    }

    @Test
    public  void testLinkAliasGetRedirectLinkNotFoundException(){
        String shortLink = "testNotFound";
        when(linkAliasRepository.findByShortLink(shortLink)).thenReturn(Optional.empty());
        assertThatThrownBy(()->linkAliasService.getRedirect(shortLink)).isInstanceOf(LinkNotFoundException.class);}
}
