export type ProjectAssumptionResponse = {
  concurrentUsers: number;
  headroom: number;
  requestsPerUserPerSecond: number;
  apiCallsPerRequest: number;
  dbRatioPerRequest: number;
  searchRatioPerRequest: number;
  cacheRatioPerRequest: number;
  kafkaRatioPerRequest: number;
  logBytesPerRequest: number;
  authRatio: number;
};
