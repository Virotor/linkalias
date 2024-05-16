package com.lessons.linkalias.repository;

import com.lessons.linkalias.model.LinkAlias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinkAliasRepository extends JpaRepository<LinkAlias, Long> {

    Boolean existsByBaseLink(String baseLink);

    Optional<LinkAlias> findByBaseLink(String baseLink);

    Optional<LinkAlias> findByShortLink(String shortLink);
}
