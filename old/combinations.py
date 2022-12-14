# -*- coding: utf-8 -*-
"""Combinations.ipynb

Automatically generated by Colaboratory.

Original file is located at
    https://colab.research.google.com/drive/1HWlhqn718KpG5Hq9TjxNMH-2QQ6W_bhV
"""

import itertools

min_exp_hash = 1
max_exp_hash = 32
numbers = [i for i in range(1, 9) ]

k = 4
all_combinations = [p for p in itertools.product(numbers, repeat=k)]

sum_list = [ sum(c) for c in all_combinations ]

for x in range(0, len(all_combinations) ):
  t = [str(d) for d in all_combinations[x] ]
  all_combinations[x] = t

for hash in range(min_exp_hash, max_exp_hash +1):
  count = 0
  #print(f"Checking hash value {hash}")
  for i in range(0, len(sum_list) ):
    if( sum_list[i] == hash ):
      #print("-".join( all_combinations[i] ))
      count += 1
  print(f"Expected hash: {hash}   Total possible combinations: {count}")

