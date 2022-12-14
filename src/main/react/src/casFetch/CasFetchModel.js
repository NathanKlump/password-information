export class CasFetchModel {
  constructor(data, e) {
    this.data = data;
    this.e = e;
  }

  toString() {
    console.log('data: ' + this.data);
    console.log('e: ' + this.e);
  }
};