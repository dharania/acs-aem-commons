/*
 * Copyright 2017 Adobe.
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
 * limitations under the License.
 */
package com.adobe.acs.commons.mcp.model;

import com.adobe.acs.commons.mcp.FieldComponent;
import com.adobe.acs.commons.mcp.FormField;
import java.util.stream.Stream;
import org.apache.sling.api.scripting.SlingScriptHelper;

/**
 * Provisions for path fields
 * Accepts the following options:
 * base=[path] -- Root of tree shown to user
 * multiple    -- If added it indicates the user can make multiple selections and values are stored in a multi-value field
 */
public abstract class PathfieldComponent extends FieldComponent {

    @Override
    public void init(String name, FormField field, SlingScriptHelper sling) {
        super.init(name, field, sling);
        setResourceType("/libs/granite/ui/components/coral/foundation/form/pathbrowser");
        Stream.of(field.options())
                .filter(s->s.equalsIgnoreCase("multiple"))
                .findFirst().ifPresent(o->getComponentMetadata().put("pickerMultiselect", true));
        Stream.of(field.options())
                .filter(s->s.startsWith("base="))
                .findFirst().ifPresent(o->{
                    String[] parts = o.split("=");
                    getComponentMetadata().put("rootPath", parts[1]);
        });
        getComponentMetadata().put("predicate", "nosystem");
    }
    
    
    public static class AssetSelectComponent extends PathfieldComponent {
        @Override
        public void init(String name, FormField field, SlingScriptHelper sling) {
            super.init(name, field, sling);
            getComponentMetadata().put("predicate", "hierarchy");
        }
    }
    public static class PageSelectComponent extends PathfieldComponent {
        @Override
        public void init(String name, FormField field, SlingScriptHelper sling) {
            super.init(name, field, sling);
            getComponentMetadata().put("predicate", "hierarchyNotFile");
        }
    }
    public static class FolderSelectComponent extends PathfieldComponent {
        @Override
        public void init(String name, FormField field, SlingScriptHelper sling) {
            super.init(name, field, sling);
            getComponentMetadata().put("predicate", "folder");
        }
    }
}
