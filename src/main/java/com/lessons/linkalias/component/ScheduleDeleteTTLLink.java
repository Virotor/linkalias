package com.lessons.linkalias.component;


import com.lessons.linkalias.repository.LinkAliasRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ScheduleDeleteTTLLink {

    private final LinkAliasRepository linkAliasRepository;

    @Scheduled(fixedRate = 60*1000)
    @Transactional
    public  void deleteTTLLink(){
        Instant currentTime = Instant.now();
        var links = linkAliasRepository.findAllByTtlGreaterThan(0L);
        for(var link : links ){
                Instant temp = link.getCreateTime().plusMillis(link.getTtl());
                if(temp.compareTo(currentTime) < 0){
                    linkAliasRepository.delete(link);
                }
        }
    }

}
