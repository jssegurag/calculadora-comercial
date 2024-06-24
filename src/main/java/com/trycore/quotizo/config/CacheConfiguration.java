package com.trycore.quotizo.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.trycore.quotizo.domain.Users.class.getName());
            createCache(cm, com.trycore.quotizo.domain.Users.class.getName() + ".budgets");
            createCache(cm, com.trycore.quotizo.domain.Users.class.getName() + ".financialParameters");
            createCache(cm, com.trycore.quotizo.domain.Users.class.getName() + ".userRoles");
            createCache(cm, com.trycore.quotizo.domain.Users.class.getName() + ".budgetAuthorizeds");
            createCache(cm, com.trycore.quotizo.domain.Users.class.getName() + ".assignedTos");
            createCache(cm, com.trycore.quotizo.domain.Users.class.getName() + ".approvedBies");
            createCache(cm, com.trycore.quotizo.domain.UserRole.class.getName());
            createCache(cm, com.trycore.quotizo.domain.UserRole.class.getName() + ".permissions");
            createCache(cm, com.trycore.quotizo.domain.UserRole.class.getName() + ".budgets");
            createCache(cm, com.trycore.quotizo.domain.UserRole.class.getName() + ".financialParameters");
            createCache(cm, com.trycore.quotizo.domain.UserRole.class.getName() + ".users");
            createCache(cm, com.trycore.quotizo.domain.Permission.class.getName());
            createCache(cm, com.trycore.quotizo.domain.Permission.class.getName() + ".permissions");
            createCache(cm, com.trycore.quotizo.domain.Position.class.getName());
            createCache(cm, com.trycore.quotizo.domain.Resource.class.getName());
            createCache(cm, com.trycore.quotizo.domain.Resource.class.getName() + ".resourceAllocations");
            createCache(cm, com.trycore.quotizo.domain.Budget.class.getName());
            createCache(cm, com.trycore.quotizo.domain.Budget.class.getName() + ".resourceAllocations");
            createCache(cm, com.trycore.quotizo.domain.Budget.class.getName() + ".budgetComments");
            createCache(cm, com.trycore.quotizo.domain.Budget.class.getName() + ".authorizeds");
            createCache(cm, com.trycore.quotizo.domain.Budget.class.getName() + ".roleAuthorizeds");
            createCache(cm, com.trycore.quotizo.domain.BudgetTemplate.class.getName());
            createCache(cm, com.trycore.quotizo.domain.BudgetTemplate.class.getName() + ".resourceAllocations");
            createCache(cm, com.trycore.quotizo.domain.ResourceAllocation.class.getName());
            createCache(cm, com.trycore.quotizo.domain.FinancialParameter.class.getName());
            createCache(cm, com.trycore.quotizo.domain.FinancialParameter.class.getName() + ".roleAuthorizeds");
            createCache(cm, com.trycore.quotizo.domain.FinancialParameterType.class.getName());
            createCache(cm, com.trycore.quotizo.domain.FinancialParameterType.class.getName() + ".financialParameters");
            createCache(cm, com.trycore.quotizo.domain.DroolsRuleFile.class.getName());
            createCache(cm, com.trycore.quotizo.domain.DroolsRuleFile.class.getName() + ".ruleAssignments");
            createCache(cm, com.trycore.quotizo.domain.Country.class.getName());
            createCache(cm, com.trycore.quotizo.domain.Country.class.getName() + ".financialParameters");
            createCache(cm, com.trycore.quotizo.domain.Country.class.getName() + ".budgets");
            createCache(cm, com.trycore.quotizo.domain.Country.class.getName() + ".budgetTemplates");
            createCache(cm, com.trycore.quotizo.domain.BudgetComment.class.getName());
            createCache(cm, com.trycore.quotizo.domain.RuleAssignment.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
