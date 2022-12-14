export class FetchResponseModel {
  constructor(response, e) {
    this.response = response;
    this.e = e;
  }

  toString() {
    console.log('response: ' + this.response);
    console.log('e: ' + this.e);
  }
};