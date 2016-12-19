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

import java.util.Map
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

/**
 * This extension provides the container for all architecture report related configurations.
 */
class RecommenderExtension {

    /**
     * Extension name
     */
    final static String EXTENSION_NAME = 'versionRecommender'

    /**
     * Task group name
     */
    final static String TASK_GROUP = 'Dependency Management'

    private Project project

    /**
     * Initialize the extension.
     *
     * @param project
     */
    public RecommenderExtension(Project project) {

        this.project = project
    }

    File propertiesFile

    String strategy

    File versionCache

    Map exceptions = [:]
}
