# Primitive Fixed Size HashMap
*A fixed-size hash map that associates string keys with arbitrary data objects using only primitive types.*
Avg Time Complexities:
`Search: O(1)` `Insertion: O(1)` `Deletion: O(1)`

## Important Info:
1. **Fixed-Size**: The maximum amount of items that can be added to the hashmap is fixed by the constructor and the size of the internal hashmap array is also fixed. This means no resizing or rehashing of items.

2. **Internal HashMap Array**: The number of 'buckets' in the internal hashmap array is fixed at the smallest power of 2 greater than 'n' to optimize setting hash indices with bit operations.

3. **No Error Handling for Overloading**: If user tries to set more items than can fit in the HashMap, the set function returns false, but no error message is displayed.

4. **HashMap Keys**: user defined Strings

5. **HashMap Values**: arbitrary user defined Objects

## Implementation
1. Creates a hashmap with a capacity of 'n' items and fixes the hashmap size at the smallest power of 2 greater than 'n'. (to optimize setting hash indices with bit operations)

2. Used java [CheckStyle](http://checkstyle.sourceforge.net/) for style checking
