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

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.InheritanceStrategy;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.util.TitleBuffer;

@javax.jdo.annotations.PersistenceCapable(
        identityType = IdentityType.DATASTORE, table = "IsisSecurityApplicationRole")
@javax.jdo.annotations.Inheritance(
        strategy = InheritanceStrategy.NEW_TABLE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy = IdGeneratorStrategy.NATIVE, column = "id")
@javax.jdo.annotations.Uniques({
        @javax.jdo.annotations.Unique(
                name = "IsisSecurityApplicationRole_name_UNQ", members = { "name" })
})
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.module.security.dom.actor.ApplicationRole "
                        + "WHERE name == :name"),
        @javax.jdo.annotations.Query(
                name = "findByNameContaining", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.isisaddons.module.security.dom.actor.ApplicationRole "
                        + "WHERE name.indexOf(:name) >= 0")
})
@AutoComplete(repository=ApplicationRoles.class, action="autoComplete")
@ObjectType("IsisSecurityApplicationRole")
@Bookmarkable
public class ApplicationRole implements Comparable<ApplicationRole>, Actor {

    //region > identification
    /**
     * having a title() method (rather than using @Title annotation) is necessary as a workaround to be able to use
     * wrapperFactory#unwrap(...) method, which is otherwise broken in Isis 1.6.0
     */
    public String title() {
        final TitleBuffer buf = new TitleBuffer();
        buf.append(getName());
        return buf.toString();
    }
    //endregion

    //region > name (property,)
    private String name;

    @javax.jdo.annotations.Column(allowsNull="false")
    @Disabled
    @MemberOrder(sequence = "1")
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
    @MemberOrder(name="name", sequence = "1")
    @ActionSemantics(ActionSemantics.Of.IDEMPOTENT)
    public ApplicationRole updateName(final String name) {
        setName(name);
        return this;
    }

    public String default0UpdateName() {
        return getName();
    }

    //endregion

    //region > roles (collection, not persisted, not visible)

    @Programmatic
    public SortedSet<ApplicationRole> getRoles() {
        return new TreeSet(Collections.singleton(this));
    }

    //endregion

    //region > compareTo

    @Override
    public int compareTo(final ApplicationRole o) {
        return ObjectContracts.compare(this, o, "name");
    }
    //endregion

}
