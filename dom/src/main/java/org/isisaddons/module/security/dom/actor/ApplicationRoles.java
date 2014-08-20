/*
 *  Copyright 2014 Jeroen van der Wal
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.isisaddons.module.security.dom.actor;

import java.util.List;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.query.QueryDefault;

@Named("Application Roles")
@DomainService
public class ApplicationRoles extends AbstractFactoryAndRepository {

    @ActionSemantics(Of.SAFE)
    public ApplicationRole findByName(final String name) {
        return uniqueMatch(new QueryDefault<ApplicationRole>(ApplicationRole.class, "findByName", "name", name));
    }

    @ActionSemantics(Of.NON_IDEMPOTENT)
    public ApplicationRole newRole(
            final @Named("Name") String name) {
        ApplicationRole role = newTransientInstance(ApplicationRole.class);
        role.setName(name);
        persist(role);
        return role;
    }

    @Prototype
    @ActionSemantics(Of.SAFE)
    public List<ApplicationRole> allRoles() {
        return allInstances(ApplicationRole.class);
    }


    @Programmatic // not part of metamodel
    public List<ApplicationRole> autoComplete(final String name) {
        return allMatches(
                new QueryDefault<ApplicationRole>(ApplicationRole.class,
                        "findByNameContaining",
                        "name", name));
    }

}
