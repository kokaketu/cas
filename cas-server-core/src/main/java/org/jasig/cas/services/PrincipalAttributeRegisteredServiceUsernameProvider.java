/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.cas.services;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * Determines the username for this registered service based on a principal attribute.
 * If the attribute is not found, default principal id is returned.
 * @author Misagh Moayyed
 * @since 4.1
 */
public class PrincipalAttributeRegisteredServiceUsernameProvider implements RegisteredServiceUsernameAttributeProvider {

    private static final long serialVersionUID = -3546719400741715137L;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @NotNull
    private final String usernameAttribute;

    /**
     * Instantiates a new default registered service username provider.
     *
     * @param usernameAttribute the username attribute
     */
    public PrincipalAttributeRegisteredServiceUsernameProvider(@NotNull final String usernameAttribute) {
        this.usernameAttribute = usernameAttribute;
    }
    
    public String getUsernameAttribute() {
        return this.usernameAttribute;
    }

    @Override
    public String resolveUsername(final Principal principal, final Service service) {
        String principalId = principal.getId();
        
        if (principal.getAttributes().containsKey(this.usernameAttribute)) {
            principalId = principal.getAttributes().get(this.usernameAttribute).toString();
        } else {
            logger.warn("Principal [{}] did not have attribute [{}] among attributes [{}] so CAS cannot "
                    + "provide the user attribute the service expects. "
                    + "CAS will instead return the default principal id [{}]",
                    principalId,
                    this.usernameAttribute,
                    principal.getAttributes(),
                    principalId);
        }
        
        logger.debug("Principal id to return is [{}]. The default principal id is [{}].",
                principal.getId(), principalId);
        return principalId;
    }
    
    @Override
    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(null, ToStringStyle.SHORT_PREFIX_STYLE);
        toStringBuilder.append("usernameAttribute", this.usernameAttribute);
        return toStringBuilder.toString();
    }

}
