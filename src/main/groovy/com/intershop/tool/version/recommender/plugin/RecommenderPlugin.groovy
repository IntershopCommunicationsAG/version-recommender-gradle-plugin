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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.reporting.ReportingExtension
import org.gradle.api.tasks.SourceSet

/**
 * Plugin implementation
 */
class RecommenderPlugin implements Plugin<Project> {

    /**
     * Applies the extension and calls the
     * task initialization for this plugin
     *
     * @param project
     */
    void apply (Project project) {
        project.logger.debug('Apply RecommenderPlugin for {}', project.name)
        configureRecommendTask(project)
        configureUpdateTask(project)
        configureStoreTask(project)
    }

    /**
     * Configures tasks
     *
     * @param project
     * @param task
     */
    private void configureRecommendTask(Project project) {

        Task task = project.tasks.findByName(RecommendTask.TASK_NAME)
        if(! task) {
            task = project.getTasks().create(RecommendTask.TASK_NAME, RecommendTask)
            task.group = RecommenderExtension.TASK_GROUP
            task.description = RecommendTask.TASK_DESCRIPTION
        }
        RecommenderExtension extension = project.extensions.findByType(RecommenderExtension) ?: project.extensions.create(RecommenderExtension.EXTENSION_NAME, RecommenderExtension, project)
        task.conventionMapping.propertiesFile  = { extension.propertiesFile }
        task.conventionMapping.strategy = { extension.strategy }
        task.conventionMapping.exceptions = { extension.exceptions }
        task.conventionMapping.versionCache = { extension.versionCache }
    }

    /**
     * Configures tasks
     *
     * @param project
     * @param task
     */
    private void configureUpdateTask(Project project) {

        Task task = project.tasks.findByName(UpdateTask.TASK_NAME)
        if(! task) {
            task = project.getTasks().create(UpdateTask.TASK_NAME, UpdateTask)
            task.group = RecommenderExtension.TASK_GROUP
            task.description = UpdateTask.TASK_DESCRIPTION
        }
        RecommenderExtension extension = project.extensions.findByType(RecommenderExtension) ?: project.extensions.create(RecommenderExtension.EXTENSION_NAME, RecommenderExtension, project)
        task.conventionMapping.propertiesFile  = { extension.propertiesFile }
        task.conventionMapping.strategy = { extension.strategy }
        task.conventionMapping.exceptions = { extension.exceptions }
        task.conventionMapping.versionCache = { extension.versionCache }
    }

    /**
     * Configures tasks
     *
     * @param project
     * @param task
     */
    private void configureStoreTask(Project project) {

        Task task = project.tasks.findByName(StoreTask.TASK_NAME)
        if(! task) {
            task = project.getTasks().create(StoreTask.TASK_NAME, StoreTask)
            task.group = RecommenderExtension.TASK_GROUP
            task.description = StoreTask.TASK_DESCRIPTION
        }
        RecommenderExtension extension = project.extensions.findByType(RecommenderExtension) ?: project.extensions.create(RecommenderExtension.EXTENSION_NAME, RecommenderExtension, project)
        task.conventionMapping.propertiesFile  = { extension.propertiesFile }
        task.conventionMapping.strategy = { extension.strategy }
        task.conventionMapping.exceptions = { extension.exceptions }
        task.conventionMapping.versionCache = { extension.versionCache }
    }
}
