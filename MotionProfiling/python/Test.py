t = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h']
odd = []
even = []

for index, item in enumerate(t, start=0):
    if index % 2 == 0:
        even.append(t[index])
    else:
        odd.append(t[index])

#for i in enumerate(t):
 #   if i % 2 == 0:
  #      even.append(t[i])
   # else: 
    #    odd.append(t[i])

print(odd)
print(even)