# Primitive Fixed Size HashMap
*A fixed-size hash map that associates string keys with arbitrary data objects using only primitive types.*

Time Complexities:

 Case | Search | Insertion | Space
 ---- | ------ | --------- | -----
Avg   |  O(1)  |    O(1)   | O(n)
Worst |  O(n)  |    O(n)   | O(n)

##Runnable Java Commands line Functions:
* Compile HashMap: 	`javac PrimitiveHashMap.java`
* Compile Tests: 	`javac PrimitiveHashMapTest.java`
* Run HashMap: 		`java PrimitiveHashMap`
* Run Tests: 		`java PrimitiveHashMapTest`

## Important Info:
1. **Fixed-Size**: The maximum amount of items that can be added to the hashmap is fixed by the constructor and the size of the internal hashmap array is also fixed. This means no resizing or rehashing of items.

2. **Internal HashMap Array**: The number of 'buckets' in the internal hashmap array is fixed at the smallest power of 2 greater than 'n' to optimize setting hash indices with bit operations.

3. **No Error Handling for Overloading**: If user tries to set more items than can fit in the HashMap, the set function returns false, but no error message is displayed.

4. **HashMap Keys**: user defined Strings

5. **HashMap Values**: arbitrary user defined Objects

## Implementation
1. Creates a hashmap with a capacity of 'n' items and fixes the hashmap size at the smallest power of 2 greater than 'n'. (to optimize setting hash indices with bit operations)

2. Used java [CheckStyle](http://checkstyle.sourceforge.net/) for style checking

3. Methods
	* **PrimitiveHashMap constructor(size)**: returns an instance of the class with a fixed amount space for the given number of objects.
	* **boolean set(key, value)**: stores the given key/value pair in the hash map.Returns true if there is enough allocated space for the object, false if not.
	* **get(key)**: return the object associated with the given key, or null if it does not exist.
	* **delete(key)**: delete the object associated with the given key, returning the it on success or null if the key has no value.
	* **float load()**: return a float value representing the number of items in the hash map/ size of hash map)`) of the data structure. Since the size of the dat structure is fixed, this should never be greater than 1.