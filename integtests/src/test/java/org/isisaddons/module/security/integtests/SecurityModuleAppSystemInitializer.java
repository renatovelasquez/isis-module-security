/*
 *  Copyright 2014 Dan Haywood
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
package org.isisaddons.module.security.integtests;

import org.apache.isis.core.commons.config.IsisConfiguration;
import org.apache.isis.core.integtestsupport.IsisSystemForTest;
import org.apache.isis.objectstore.jdo.datanucleus.DataNucleusPersistenceMechanismInstaller;
import org.apache.isis.objectstore.jdo.datanucleus.IsisConfigurationForJdoIntegTests;

/**
 * Holds an instance of an {@link IsisSystemForTest} as a {@link ThreadLocal} on the current thread,
 * initialized with ToDo app's domain services. 
 */
public class SecurityModuleAppSystemInitializer {
    
    private SecurityModuleAppSystemInitializer(){}

    public static IsisSystemForTest initIsft() {
        IsisSystemForTest isft = IsisSystemForTest.getElseNull();
        if(isft == null) {
            isft = new ExampleSecuredEntitiesAppSystemBuilder().build().setUpSystem();
            IsisSystemForTest.set(isft);
        }
        return isft;
    }

    private static class ExampleSecuredEntitiesAppSystemBuilder extends IsisSystemForTest.Builder {

        public ExampleSecuredEntitiesAppSystemBuilder() {
            withLoggingAt(org.apache.log4j.Level.INFO);
            with(testConfiguration());
            with(new DataNucleusPersistenceMechanismInstaller());

            // services annotated with @DomainService
            withServicesIn(
                    "org.isisaddons.module.security"
            );

            withServices( /* nothing extra */);
        }

        private static IsisConfiguration testConfiguration() {
            final IsisConfigurationForJdoIntegTests testConfiguration = new IsisConfigurationForJdoIntegTests();
            testConfiguration.addRegisterEntitiesPackagePrefix("org.isisaddons.module.security");
            return testConfiguration;
        }
    }

}
