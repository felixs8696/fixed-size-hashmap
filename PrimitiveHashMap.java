/**
* PrimitiveHashMap - A fixed-size hash map that associates string keys
* with arbitrary data objects using only primitive types.
* @author Felix Su
*/

/**
* A fixed-size hash map that associates string keys with arbitrary
* data objects using only primitive types.
* @param <ValObj> - the Object type to be stored in the hashmap
*/
public class PrimitiveHashMap<ValObj> {
  /**
  * Amount of items currently stored in the hashmap.
  */
  private int itemCount;
  /**
  * Limit on the amount of items that can be stored in the hashmap.
  */
  private int capacity;
  /**
  * Amount of buckets in the internal array of the hashmap.
  */
  private int hashMapsize;
  /**
  * Internal array datastructure of the hashmap.
  */
  private Object[] hashMap;
  /**
  * Bit shift amount to roundUpToNextPowerOfTwo() for 2 bit numbers.
  */
  private static final int TWO_BIT_SHIFT_AMT = 1;
  /**
  * Bit shift amount to roundUpToNextPowerOfTwo() for 4 bit numbers.
  */
  private static final int FOUR_BIT_SHIFT_AMT = 2;
  /**
  * Bit shift amount to roundUpToNextPowerOfTwo() for 8 bit numbers.
  */
  private static final int EIGHT_BIT_SHIFT_AMT = 4;
  /**
  * Bit shift amount to roundUpToNextPowerOfTwo() for 16 bit numbers.
  */
  private static final int SIXTEEN_BIT_SHIFT_AMT = 8;
  /**
  * Bit shift amount to roundUpToNextPowerOfTwo() for 32 bit numbers.
  */
  private static final int THIRTYTWO_BIT_SHIFT_AMT = 16;

  /**
  * Creates an empty fixed HashMap Object. Because it is a fixed
  * size hashmap, no items can be set. Only use is for explicitly
  * sized hashmaps to be generated from the contructor() method.
  */
  public PrimitiveHashMap() {
    itemCount = 0;
    capacity = 0;
    hashMapsize = 0;
    hashMap = new Object[0];
  }

  /**
  * Creates a fixed PrimitiveHashMap with the given item capacity and
  * a fixed hashmap size at the smallest power of 2 greater than 'n'.
  * @param size - int value to determine the fixed size of the
  * hashmap and amount of items that can be added
  */
  private PrimitiveHashMap(final int size) {
    itemCount = 0;
    capacity = size;
    hashMapsize = roundUpToNextPowerOfTwo(size);
    hashMap = new Object[hashMapsize];
  }

  /**
  * Creates and returns a new PrimitiveHashMap with pre-allocated
  * space for the given number of objects.
  * @param size - int value to determine the fixed size of the
  * hashmap and amount of items that can be added
  * @return instance of this PrimitiveHashMap class with
  * pre-allocated space for the given number of objects.
  */
  public final PrimitiveHashMap constructor(final int size) {
    return new PrimitiveHashMap(size);
  }

  /**
  * Alternative method to find the index at which to put the item
  * in the hashmap: bitwise AND is faster than modulo.
  * Issue: only works for hashmaps with a size that is a power of
  * 2 (but for this implementation, size can be user defined)
  * @param hashcode - integer representing some hashcode of an
  * object
  * @return boolean value indicating success / failure of the
  * operation.
  */
  private int indexFor(final int hashcode) {
    return hashcode & (hashMapsize - 1);
  }

  /**
  * Solves the issue in indexFor() and rounds the given size up to
  * the nearest power of 2.
  * Issue: Causes load factor to be inaccurate
  * @param x - number to be rounded up to next power of 2 (given
  * size of hashmap)
  * @return next power of 2 above given x
  */
  private int roundUpToNextPowerOfTwo(int x) {
    x--;
    x |= x >> TWO_BIT_SHIFT_AMT;
    x |= x >> FOUR_BIT_SHIFT_AMT;
    x |= x >> EIGHT_BIT_SHIFT_AMT;
    x |= x >> SIXTEEN_BIT_SHIFT_AMT;
    x |= x >> THIRTYTWO_BIT_SHIFT_AMT;
    x++;
    return x;
  }

  /**
  * Stores the given key/value pair in the hash map.
  * @param key - String key associated with the specified
  * value, used during HashMap retrieval
  * @param value - Object to be stored in the Hashmap linked
  * to the key
  * @return boolean value indicating success / failure of the
  * operation.
  */
  public final boolean set(final String key, final ValObj value) {
    if (itemCount >= capacity) {
      return false;
    }
    int hashcode = key.hashCode();
    int hashIndex = indexFor(hashcode);
    Node<ValObj> newNode = new Node<ValObj>(key, value, hashcode);
    if (hashMap[hashIndex] == null) {
      hashMap[hashIndex] = new LinkedList();
    }
    @SuppressWarnings("unchecked")
    LinkedList linkedListAtIndex = (LinkedList) hashMap[hashIndex];
    linkedListAtIndex.add(newNode);
    itemCount += 1;
    return true;
  }

  /**
  * Retrieves the value associated with the given key, or null if
  * no value is set.
  * @param key - String key used to retrieve a value from the
  * HashMap
  * @return value associated with the given key, or null if no
  * value is set.
  */
  public final ValObj get(final String key) {
    int hashcode = key.hashCode();
    int hashIndex = indexFor(hashcode);
    @SuppressWarnings("unchecked")
    LinkedList linkedListAtIndex = (LinkedList) hashMap[hashIndex];
    if (linkedListAtIndex == null) {
      return null;
    }
    @SuppressWarnings("unchecked")
    Node<ValObj> nodeForKey = linkedListAtIndex.get(key, hashcode);
    if (nodeForKey != null) {
      return nodeForKey.getValue();
    }
    return null;
  }

  /**
  * Deletes the value associated with the given key.
  * @param key - String key used to retrieve a value from the HashMap
  * @return deleted value on success or null if the key has no value.
  */
  public final ValObj delete(final String key) {
    int hashcode = key.hashCode();
    int hashIndex = indexFor(hashcode);
    @SuppressWarnings("unchecked")
    LinkedList linkedListAtIndex = (LinkedList) hashMap[hashIndex];
    if (linkedListAtIndex == null) {
      return null;
    }
    @SuppressWarnings("unchecked")
    Node<ValObj> deletedNode = linkedListAtIndex.deleteNode(key, hashcode);
    if (deletedNode != null) {
      return deletedNode.getValue();
    }
    return null;
  }

  /**
  * Returns a float value representing the load factor
  * (`(items in hash map)/(size of hash map)`) of the
  * data structure. Since the size of the data structure
  * is fixed, this should never be greater than 1.
  * @return load factor of the HashMap
  */
  public final float load() {
    @SuppressWarnings("unchecked")
    float loadFactor = (float) itemCount / (float) capacity;
    return loadFactor;
  }

  /**
  * Used to print a string representation of the HashMap
  * Implementation (for debugging and testing)
  */
  // public void printString() {
  //   for (int i = 0; i < hashMap.length; i++) {
  //     LinkedList linkedListAtIndex = (LinkedList) hashMap[i];
  //     if (linkedListAtIndex.getHead() != null) {
  //       System.out.println("[" + i + ": "
  //       + linkedListAtIndex.printString() + "]");
  //     } else {
  //       System.out.println("[" + i + ": NULL]");
  //     }
  //   }
  // }

  /**
  * Used to get the size of the LinkedList in the HashMap bucket
  * associated with the given key (for debugging and testing).
  * Uncomment to run testDuplicateValues() in PrimitiveHashMapTest.java
  */
  // public int linkedListSize(String key) {
  //   int hashcode = key.hashCode();
  //   int hashIndex = indexFor(hashcode);
  //   LinkedList linkedListAtIndex = (LinkedList) hashMap[hashIndex];
  //   if (linkedListAtIndex == null) {
  //     return 0;
  //   }
  //   return linkedListAtIndex.size();
  // }

  class LinkedList {
    /**
    * The first node in this LinkedList.
    */
    private Node head;
    /**
    * The last node in this LinkedList.
    */
    private Node last;
    /**
    * The size node in this LinkedList.
    */
    private int size;

    /**
    * Creates an empty LinkedList datastructure of nodes to be
    * used to populate the buckets of the HashMap array.
    */
    LinkedList() {
      head = null;
      last = null;
      size = 0;
    }

    /**
    * Stores the given node in the LinkedList.
    * @param a - Node to be added to the end of the LinkedList
    * @return the added node
    */
    Node add(final Node a) {
      if (head == null) {
        head = a;
        last = a;
        size += 1;
      } else {
        Node insertNode = get(a.getKey(), a.getHashCode());
        if (insertNode == null) {
          a.setPrev(last);
          last.setNext(a);
          last = a;
          size += 1;
        } else {
          insertNode.setHashCode(a.getHashCode());
          insertNode.setValue(a.getValue());
          insertNode.setKey(a.getKey());
        }
      }
      return a;
    }

    /**
    * Retrieves the Node in the LinkedList associated with the
    * given key and hashcode or null if none exists.
    * @param key - String key used to find a node in the LinkedList
    * @param hashCode - int used to represent the hashcode
    * of the key as an extra check for the right node
    * @return  node associated with the given key, or null if
    * no value is set.
    */
    Node get(final String key, final int hashCode) {
      Node pointer = head;
      while (pointer != null && pointer.getKey() != key
      && pointer.getHashCode() != hashCode) {
        pointer = pointer.getNext();
      }
      return pointer;
    }

    /**
    * Deletes the Node in the LinkedList associated with the given
    * key and hashcode.
    * @param key - String key used to find a node in the LinkedList
    * @param hashCode - int used to represent the hashcode of the
    * key as an extra check for the right node
    * @return deleted node on success or null if the key
    * has no value.
    */
    Node deleteNode(final String key, final int hashCode) {
      Node deleteNode = get(key, hashCode);
      if (deleteNode == null) {
        return null;
      }
      Node beforeNode = deleteNode.getPrev();
      Node afterNode = deleteNode.getNext();
      if (beforeNode != null && afterNode != null) {
        beforeNode.setNext(afterNode);
        afterNode.setPrev(beforeNode);
      } else if (beforeNode != null) {
        beforeNode.setNext(null);
        last = beforeNode;
      } else {
        head = null;
        last = null;
        return deleteNode;
      }
      size -= 1;
      return deleteNode;
    }

    /**
    * Returns the number of nodes in the LinkedList.
    * @return number of nodes in the LinkedList
    */
    int size() {
      return size;
    }

    /**
    * Returns the head Node of this LinkedList.
    * @return head Node of this LinkedList
    */
    Node getHead() {
      return head;
    }

    /**
    * Returns the last Node of this LinkedList.
    * @return last Node of this LinkedList
    */
    Node getLast() {
      return last;
    }

    /**
    * Used to print a string representation of the LinkedList
    * (for debugging and testing)
    */
    // String printString() {
    //   String result = "";
    //   Node pointer = head;
    //   while (pointer != null) {
    //     result += "[key: " + pointer.getKey() +
    //     ", value: " + pointer.getValue() + "]";
    //     if (pointer.getNext() != null) {
    //       result += ", ";
    //     }
    //     pointer = pointer.getNext();
    //   }
    //   return result;
    // }
  }

  /**
  * A datastructure used to store the objects (ValObj) for the hashmap
  * with comparable primitive attributes for efficient retrieval.
  * @param <ValObj> - the type of the objects stored in the hashmap
  */
  class Node<ValObj> {
    /**
    * The object (ValObj) stored in this node object.
    */
    private ValObj value;
    /**
    * The String key that the value of the node is associated to in.
    * the hashmap
    */
    private String key;
    /**
    * The hashcode of this node's key.
    */
    private int hashCode;
    /**
    * The preceding node of the LinkedList in which this node is stored.
    */
    private Node prev;
    /**
    * The following node of the LinkedList in which this node is stored.
    */
    private Node next;

    /**
    * Creates a Node object containing a key, arbitrary value
    * object, and hashcode that will be used to populate the
    * contents of the defined LinkedList structure.
    * @param inKey - String value that the value of this
    * node is associated to in the HashMap
    * @param inValue - Value storing the object in this node.
    * @param hash - Java hashcode of the key
    */
    Node(final String inKey, final ValObj inValue, final int hash) {
      key = inKey;
      value = inValue;
      hashCode = hash;
    }

    /**
    * Gets the value of this node.
    * @return the ValObj stored in this Node
    */
    ValObj getValue() {
      return value;
    }

    /**
    * Gets the key of this node.
    * @return the String key stored in this Node
    */
    String getKey() {
      return key;
    }

    /**
    * Gets the hashcode of this node's string.
    * @return the hashcode of the key stored in this Node
    */
    int getHashCode() {
      return hashCode;
    }

    /**
    * Gets the preceding node in this node's LinkedList.
    * @return the preceding node in this node's LinkedList
    */
    Node getPrev() {
      return prev;
    }

    /**
    * Gets the following node in this node's LinkedList.
    * @return the following node in this node's LinkedList
    */
    Node getNext() {
      return next;
    }

    /**
    * Sets the value of this node to the given ValObj.
    * @param val - the ValObj to set this Node's value to
    */
    void setValue(final ValObj val) {
      value = val;
    }

    /**
    * Sets the key of this node to the given String.
    * @param newKey - the String to set this Node's key to
    */
    void setKey(final String newKey) {
      key = newKey;
    }

    /**
    * Sets the hashcode of this node to the given hashcode.
    * @param newHash - the int to set this Node's hashCode to
    */
    void setHashCode(final int newHash) {
      hashCode = newHash;
    }

    /**
    * Sets the preceding node in this node's LinkedList to the given Node.
    * @param newPrev - the Node to set this Node's prev to
    */
    void setPrev(final Node newPrev) {
      prev = newPrev;
    }

    /**
    * Sets the following node in this node's LinkedList to the given Node.
    * @param newNext - the Node to set this Node's next to
    */
    void setNext(final Node newNext) {
      next = newNext;
    }
  }

  public static void main(String[] args) {
  }
}
