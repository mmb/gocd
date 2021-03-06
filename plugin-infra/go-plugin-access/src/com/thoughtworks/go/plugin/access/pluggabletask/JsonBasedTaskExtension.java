/*************************GO-LICENSE-START*********************************
 * Copyright 2014 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************GO-LICENSE-END***********************************/

package com.thoughtworks.go.plugin.access.pluggabletask;

import com.thoughtworks.go.plugin.access.PluginRequestHelper;
import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;
import com.thoughtworks.go.plugin.api.task.Task;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.infra.Action;
import com.thoughtworks.go.plugin.infra.ActionWithReturn;
import com.thoughtworks.go.plugin.infra.PluginManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class JsonBasedTaskExtension implements TaskExtensionContract {
    public final static String TASK_EXTENSION = "task";
    public final static String CONFIGURATION_REQUEST = "configuration";
    public final static String VALIDATION_REQUEST = "validate";
    public final static String EXECUTION_REQUEST = "execute";
    public final static String TASK_VIEW_REQUEST = "view";

    private final HashMap<String, JsonBasedTaskExtensionHandler> handlerHashMap;
    private final PluginRequestHelper pluginRequestHelper;
    private PluginManager pluginManager;
    private List<String> supportedVersions = new ArrayList<String>();

    JsonBasedTaskExtension(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
        supportedVersions.add(JsonBasedTaskExtensionHandler_V1.VERSION);
        handlerHashMap = new HashMap<String, JsonBasedTaskExtensionHandler>();
        handlerHashMap.put(JsonBasedTaskExtensionHandler_V1.VERSION, new JsonBasedTaskExtensionHandler_V1());
        pluginRequestHelper = new PluginRequestHelper(pluginManager, supportedVersions, TASK_EXTENSION);
    }

    @Override
    public ExecutionResult execute(String pluginId, ActionWithReturn<Task, ExecutionResult> actionWithReturn) {
        JsonBasedPluggableTask task = new JsonBasedPluggableTask(pluginId, pluginRequestHelper, handlerHashMap);
        return actionWithReturn.execute(task, pluginManager.getPluginDescriptorFor(pluginId));
    }

    @Override
    public void doOnTask(String pluginId, Action<Task> action) {
        JsonBasedPluggableTask task = new JsonBasedPluggableTask(pluginId, pluginRequestHelper, handlerHashMap);
        action.execute(task, pluginManager.getPluginDescriptorFor(pluginId));
    }

    @Override
    public ValidationResult validate(String pluginId, TaskConfig taskConfig) {
        JsonBasedPluggableTask task = new JsonBasedPluggableTask(pluginId, pluginRequestHelper, handlerHashMap);
        return task.validate(taskConfig);
    }
}
