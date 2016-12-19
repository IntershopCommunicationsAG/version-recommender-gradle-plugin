/*
 * Copyright 2015 Intershop Communications AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.intershop.tool.version.recommender.plugin

import com.intershop.tool.version.recommender.Recommender
import com.intershop.tool.version.recommender.VersionProvider
import com.intershop.tool.version.recommender.resolver.CachedVersionResolver
import com.intershop.tool.version.recommender.resolver.VersionResolver

import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*
import org.gradle.api.GradleException

/**
 * Task for api validation
 */
@Slf4j
class BaseRecommenderTask extends DefaultTask {

    @InputFile
    File propertiesFile

    @Input
    String strategy

    @Input
    Map<String, String> exceptions

    def File versionCache
    def ConfigurationCollection configurations = null
    def VersionProvider versionProvider = null
    def VersionResolver versionResolver = null
    def Recommender recommender = null

    ConfigurationCollection getConfiguration()
    {
        if (configurations == null)
        {
            project.logger.debug('Using version of file {} at project {}', getPropertiesFile(), project.name)
            configurations = new ConfigurationCollection(project, getVersionProvider());
        }
        return configurations;
    }

    VersionResolver getVersionResolver()
    {
        if (versionResolver == null)
        {
            versionResolver = new CachedVersionResolver(new RepositoryVersionProvider(project), getVersionCache());
        }
        return versionResolver;
    }

    VersionProvider getVersionProvider()
    {
        if (versionProvider == null)
        {
            versionProvider = new NebulaVersionProperties(getPropertiesFile());
        }
        return versionProvider;
    }

    Recommender getRecommender()
    {
        if (recommender == null)
        {
            recommender = new Recommender(getVersionResolver(), getVersionProvider());
        }
        return recommender;
    }
}
