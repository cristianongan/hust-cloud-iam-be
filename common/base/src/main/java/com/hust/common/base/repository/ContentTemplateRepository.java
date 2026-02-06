package com.hust.common.base.repository;

import com.hust.common.base.constants.CacheConstants;
import com.hust.common.base.model.ContentTemplate;
import com.hust.common.base.repository.extend.ContentTemplateRepositoryExtend;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentTemplateRepository extends JpaRepository<ContentTemplate, Long>, ContentTemplateRepositoryExtend {
    @Cacheable(cacheNames = CacheConstants.CONTENT_TEMPLATE.FIND_BY_TEMPLATE_CODE,
            key = "{#templateCode, #status}",
            unless = "#result == null")
    ContentTemplate findByTemplateCodeAndStatus(String templateCode, int status);
}
