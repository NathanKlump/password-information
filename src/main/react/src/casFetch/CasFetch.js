import {CasFetchModel} from './CasFetchModel.js';
import {FetchResponseModel} from './FetchResponseModel.js';

export class CasFetch {
  constructor(url, params) {
    this.url = url;
    this.params = params;
  }

  fetchJson() {
    return this.fetch('json');
  }

  fetchBodyRead() {
    return this.fetch('bodyRead');
  }

  fetch(dataType) {
    return new Promise(
      async (resolve, reject) => {
        let fetchResponseModel = await this.performFetchCall();
        if (fetchResponseModel.e != null) {
          fetchResponseModel = await this.performFetchCall();
        }
        if (fetchResponseModel.e != null) {
          reject(fetchResponseModel.e);
        }
        let casFetchModel = await this.processSuccessfulFetch(fetchResponseModel, dataType);
        resolve(casFetchModel.data);
      }
    );
  }

  async performFetchCall() {
    let fetchResponseModel = await fetch(this.url, this.params)
        .then(
          response => {
            return new FetchResponseModel(response, null);
          }
        ).catch(
          e => {
            return new FetchResponseModel(null, e);
          }
        );
    return fetchResponseModel;
  }

  async processSuccessfulFetch(fetchResponseModel, dataType) {
    if (fetchResponseModel.response.ok) {
      let casFetchModel = await this.processOkResponse(fetchResponseModel.response, dataType);
      return casFetchModel;
    }
    return new CasFetchModel(null, new Error('Response not OK'));
  }

  async processOkResponse(response, dataType) {
    if (response.url.includes('/cas/login')) {
      let casRootUrl = response.url.split('?')[0];
      let changeEntirePageUrl = casRootUrl + '?service=' + window.location.href;
      window.location.href = changeEntirePageUrl;
    } else if (response.url.includes(this.url)) {
      let casFetchModel = await this.awaitResponseBasedOnDataType(response, dataType);
      return casFetchModel;
    } else {
      return new CasFetchModel(null, new Error('Response not OK'));
    }
  }

  async awaitResponseBasedOnDataType(response, dataType) {
    if (dataType == 'json') {
      let jsonData = await response.json();
      return new CasFetchModel(jsonData, null);
    } else if (dataType == 'bodyRead') {
      let bodyReadData = await response.body.getReader().read();
      return new CasFetchModel(bodyReadData, null);
    }
  }
};