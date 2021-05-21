#!/usr/local/bin/python3

import requests
import time
import sys

urlMapping = {"normal": "http://localhost:4070/api/v1/resiliency/queue/Chat2/",
              "retry":  "http://localhost:4070/api/v1/resiliency/queue/Chat2?type=retry",
              "cache":  "http://localhost:4070/api/v1/resiliency/queue/Chat2?type=cache",
              "timeout":  "http://localhost:4070/api/v1/resiliency/queue/Chat2?type=timeout",
              "circuitbreaker":  "http://localhost:4070/api/v1/resiliency/queue/Chat2?type=circuitbreaker",
              "fallback":  "http://localhost:4070/api/v1/resiliency/queue/Chat2?type=fallback",
              "stacked":  "http://localhost:4070/api/v1/resiliency/queue/Chat2?type=stacked"}


key = sys.argv[1]

if key in urlMapping:
  url = urlMapping[key]
else:
  url = urlMapping["normal"]

print(url)
for x in range(1,350):
  start = time.time()
  resp = requests.get(url)
  end = time.time()

  print('{}: {} {} Execution Time: {}'.format(x,url,resp.status_code, end-start))
