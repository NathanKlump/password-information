/* global passwordInformationPortletSbReactAppUrlRoot */
/* global passwordInformationPortletSbJwtToken */

import {CasFetch} from '../../casFetch/CasFetch.js';

export const get_lastChange = async is_demo => {
  let jwtToken = passwordInformationPortletSbJwtToken == null ? '' : passwordInformationPortletSbJwtToken;
  if (jwtToken == '') {
    let casFetch = new CasFetch(
        passwordInformationPortletSbReactAppUrlRoot + '/PasswordInformationPortlet-sb/casified/v1/PasswordInformation',
        {
          method: 'GET',
          credentials: 'include',
          headers: {
            Accept: '*' // <---- this header needs work. when just application/json, Java CAS client can't find saved URL
          }
        }
    );
    return casFetch.fetchJson();
  } else {
    let casFetch = new CasFetch(
        passwordInformationPortletSbReactAppUrlRoot + '/PasswordInformationPortlet-sb/casified/v1/PasswordInformation',
        {
          method: 'GET',
          credentials: 'include',
          headers: {
            Accept: 'application/json',
            Authorization: 'Bearer ' + passwordInformationPortletSbJwtToken
          }
        }
    );
    return casFetch.fetchJson();
  }
}
