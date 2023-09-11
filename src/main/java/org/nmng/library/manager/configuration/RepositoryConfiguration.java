package org.nmng.library.manager.configuration;

import org.nmng.library.manager.dao.Impl.CustomRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.nmng.library.manager.dao", repositoryBaseClass = CustomRepositoryImpl.class)
public class RepositoryConfiguration {
}
