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

import java.util.Collections
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
class UpdateTask extends BaseRecommenderTask {

    final static String TASK_NAME = 'updateDependencies'
    final static String TASK_DESCRIPTION = 'use new versions of dependencies'

    def boolean enableUpdate = false

    @TaskAction
    void updateDependencies() {
        enableUpdate = true
    }

    String getNextVersion(String group, String artifact) {
        return getRecommender().getVersion(group, artifact, enableUpdate ? getStrategy() : "STICK", enableUpdate ? getExceptions() : Collections.emptyMap());
    }
}
