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
class StoreTask extends BaseRecommenderTask {

    final static String TASK_NAME = 'storeDependencies'
    final static String TASK_DESCRIPTION = 'write new versions of dependencies'

    @TaskAction
    void storeDependencies() {
        getRecommender().store(getConfiguration().getDependencies(), getStrategy(), getExceptions());
    }
}
