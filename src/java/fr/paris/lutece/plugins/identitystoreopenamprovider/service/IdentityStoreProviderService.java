/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.identitystoreopenamprovider.service;

import java.util.HashMap;
import java.util.Map;

import fr.paris.lutece.plugins.identitystore.web.exception.IdentityNotFoundException;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.AttributeDto;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.IdentityDto;
import fr.paris.lutece.plugins.identitystore.web.service.IdentityService;
import fr.paris.lutece.plugins.mylutece.modules.openam.service.IIdentityProviderService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * 
 * IdentityStoreProviderService
 * 
 */
public class IdentityStoreProviderService implements IIdentityProviderService
{

    private static final String PROPERTY_IDENTITY_STORE_APPLICATION_CODE = "identitystoreopenamprovider.identityStoreApplicationCode";
    private static final String CLIENT_APP_CODE = AppPropertiesService.getProperty( PROPERTY_IDENTITY_STORE_APPLICATION_CODE );
    private static final String BEAN_IDENTITY_SERVICE = "identitystoreopenamprovider.identitystore.identityService";
    private static final String LUTECE_USER_ATTRIBUTE_PREFIX = "idx.";

    @Override
    public Map<String, String> getIdentityInformations( String strName )
    {
        Map<String, String> UserInformations = new HashMap<String, String>( );
        IdentityService identityStoreService = SpringContextService.getBean( BEAN_IDENTITY_SERVICE );

        try
        {
            IdentityDto identityDto = identityStoreService.getIdentityByConnectionId( strName, CLIENT_APP_CODE );

            if ( identityDto != null && identityDto.getAttributes( ) != null )
            {
                for ( Map.Entry<String, AttributeDto> entry : identityDto.getAttributes( ).entrySet( ) )
                {
                    UserInformations.put( LUTECE_USER_ATTRIBUTE_PREFIX + entry.getKey( ), entry.getValue( ).getValue( ) );

                }
            }

        }
        catch( IdentityNotFoundException infe )
        {
            AppLogService.error( "error during loading identity for guid " + strName,infe );
        }

        return UserInformations;
    }
}
