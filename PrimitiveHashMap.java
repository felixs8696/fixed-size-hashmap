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
  private static final int ONE_BIT_SHIFT_AMT = 1;
  private static final int TWO_BIT_SHIFT_AMT = 2;
  private static final int FOUR_BIT_SHIFT_AMT = 4;
  private static final int EIGHT_BIT_SHIFT_AMT = 8;
  private static final int SIXTEEN_BIT_SHIFT_AMT = 16;
  /**
  * Max Array Size to avoid Java heap space and VM limit errors (If still doesn't work, increase default JVM mem size).
  */
  private static final int MAX_ARRAY_SIZE = 1<<29;
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
  * Creates an empty fixed HashMap Object. Because it is a fixed
  * size hashmap, no items can be set. Only use is for explicitly
  * sized hashmaps to be generated from the contructor() method.
  */
  public PrimitiveHashMap() {
    this.itemCount = 0;
    this.capacity = 0;
    this.hashMapsize = 0;
    this.hashMap = new Object[0];
  }

  /**
  * Creates a fixed PrimitiveHashMap with the given item capacity and
  * a fixed hashmap size at the smallest power of 2 greater than 'n'.
  * @param size - int value to determine the fixed size of the
  * hashmap and amount of items that can be added
  */
  private PrimitiveHashMap(final int size) {
    this.itemCount = 0;
    this.capacity = size;
    this.hashMapsize = roundUpToNextPowerOfTwo(size);
    this.hashMap = new Object[hashMapsize];
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
  * CREDIT: http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/java/util/HashMap.java#HashMap.indexFor%28int,int%29
  * Alternative method to find the index at which to put the item
  * in the hashmap: bitwise AND is faster than modulo.
  * Issue: only works for hashmaps with a size that is a power of 2
  * @param hashcode - integer representing some hashcode of an object
  * @return boolean value indicating success / failure of the operation.
  */
  private int indexFor(final int hashcode) {
    return hashcode & (this.hashMapsize - 1);
  }

  /**
  * CREDIT: http://graphics.stanford.edu/~seander/bithacks.html#RoundUpPowerOf2
  * Solves the issue in indexFor() and rounds the given size up to
  * the nearest power of 2.
  * @param x - number to be rounded up to next power of 2 (given size of hashmap)
  * @return next power of 2 above given x
  */
  private int roundUpToNextPowerOfTwo(int x) {
    if (x > MAX_ARRAY_SIZE) {
      return MAX_ARRAY_SIZE;
    }
    x--;
    x |= x >> ONE_BIT_SHIFT_AMT;
    x |= x >> TWO_BIT_SHIFT_AMT;
    x |= x >> FOUR_BIT_SHIFT_AMT;
    x |= x >> EIGHT_BIT_SHIFT_AMT;
    x |= x >> SIXTEEN_BIT_SHIFT_AMT;
    x++;
    return x;
  }

  /**
  * Stores the given key/value pair in the hash map.
  * @param key - String key associated with the specified value
  * @param value - Object to be stored in the Hashmap linked to the key
  * @return boolean value indicating success / failure of the operation.
  */
  public final boolean set(final String key, final ValObj value) {
    if (this.itemCount >= this.capacity) {
      return false;
    }
    int hashcode = key.hashCode();
    int hashIndex = indexFor(hashcode);
    Node<ValObj> newNode = new Node<ValObj>(key, value);
    if (this.hashMap[hashIndex] == null) {
      this.hashMap[hashIndex] = new LinkedList();
    }
    @SuppressWarnings("unchecked")
    LinkedList linkedListAtIndex = (LinkedList) this.hashMap[hashIndex];
    linkedListAtIndex.add(newNode);
    this.itemCount += 1;
    return true;
  }

  /**
  * Retrieves the value associated with the given key, or null if no value is set.
  * @param key - String key used to retrieve a value from the HashMap
  * @return value associated with the given key, or null if no value is set.
  */
  public final ValObj get(final String key) {
    int hashcode = key.hashCode();
    int hashIndex = indexFor(hashcode);
    @SuppressWarnings("unchecked")
    LinkedList linkedListAtIndex = (LinkedList) this.hashMap[hashIndex];
    if (linkedListAtIndex == null) {
      return null;
    }
    @SuppressWarnings("unchecked")
    Node<ValObj> nodeForKey = linkedListAtIndex.get(key);
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
    LinkedList linkedListAtIndex = (LinkedList) this.hashMap[hashIndex];
    if (linkedListAtIndex == null) {
      return null;
    }
    @SuppressWarnings("unchecked")
    Node<ValObj> deletedNode = linkedListAtIndex.deleteNode(key);
    if (deletedNode != null) {
      return deletedNode.getValue();
    }
    return null;
  }

  /**
  * Returns a float value representing the load factor
  * (`(items in hash map)/(size of hash map)`) of the structure.
  * @return load factor of the HashMap
  */
  public final float load() {
    @SuppressWarnings("unchecked")
    float loadFactor = (float) itemCount / (float) capacity;
    return loadFactor;
  }

  /**
  * Used to print a string representation of the HashMap
  * (for debugging and testing)
  */
  void printString() {
    for (int i = 0; i < hashMap.length; i++) {
      LinkedList linkedListAtIndex = (LinkedList) hashMap[i];
      if (linkedListAtIndex != null && linkedListAtIndex.getHead() != null) {
        System.out.println("[" + i + ": "
        + linkedListAtIndex.printString() + "]");
      } else {
        System.out.println("[" + i + ": NULL]");
      }
    }
  }

  /**
  * Used to get the size of the LinkedList in the HashMap bucket
  * associated with the given key (for debugging and testing).
  * @return size of the retrieved LinkedList or 0 if it does not exist.
  */
  int linkedListSize(String key) {
    int hashcode = key.hashCode();
    int hashIndex = indexFor(hashcode);
    LinkedList linkedListAtIndex = (LinkedList) hashMap[hashIndex];
    if (linkedListAtIndex == null) {
      return 0;
    }
    return linkedListAtIndex.size();
  }

  class LinkedList {
    private Node head;
    private Node last;
    private int size;

    /**
    * Creates an empty LinkedList datastructure of nodes to be
    * used to populate the buckets of the HashMap array.
    */
    LinkedList() {
      this.head = null;
      this.last = null;
      this.size = 0;
    }

    /**
    * Stores the given node in the LinkedList.
    * @param a - Node to be added to the end of the LinkedList
    * @return the added node
    */
    Node add(final Node a) {
      if (head == null) {
        this.head = a;
        this.last = a;
        this.size += 1;
      } else {
        Node insertNode = get(a.getKey());
        if (insertNode == null) {
          a.setPrev(last);
          this.last.setNext(a);
          this.last = a;
          this.size += 1;
        } else {
          insertNode.setValue(a.getValue());
        }
      }
      return a;
    }

    /**
    * Retrieves the Node in the LinkedList associated with the
    * given key or null if none exists.
    * @param key - String key used to find a node in the LinkedList
    * @return  node associated with the given key, or null if no value is set.
    */
    Node get(final String key) {
      Node pointer = head;
      while (pointer != null && pointer.getKey() != key && !pointer.getKey().equals(key)) {
        pointer = pointer.getNext();
      }
      return pointer;
    }

    /**
    * Deletes the Node in the LinkedList associated with the given key.
    * @param key - String key used to find a node in the LinkedList
    * @return deleted node on success or null if the key has no value.
    */
    Node deleteNode(final String key) {
      Node deleteNode = get(key);
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
        this.last = beforeNode;
      } else {
        this.head = null;
        this.last = null;
        return deleteNode;
      }
      this.size -= 1;
      return deleteNode;
    }

    int size() {
      return this.size;
    }

    Node getHead() {
      return this.head;
    }

    Node getLast() {
      return this.last;
    }

    /**
    * Used to return a string representation of the LinkedList
    * (for debugging and testing)
    * @return a string representation of the LinkedList
    */
    String printString() {
      String result = "";
      Node pointer = head;
      while (pointer != null) {
        result += "[key: " + pointer.getKey() +
        ", value: " + pointer.getValue() + "]";
        if (pointer.getNext() != null) {
          result += ", ";
        }
        pointer = pointer.getNext();
      }
      return result;
    }
  }

  /**
  * A datastructure used to store the objects (ValObj) for the hashmap
  * with comparable primitive attributes for efficient retrieval.
  * @param <ValObj> - the type of the objects stored in the hashmap
  */
  class Node<ValObj> {
    private String key;
    private ValObj value;
    private Node prev;
    private Node next;

    /**
    * Creates a Node object containing a key and arbitrary value object.
    * @param inKey - String value that the value of this
    * node is associated to in the HashMap
    * @param inValue - Value storing the object in this node.
    */
    Node(final String inKey, final ValObj inValue) {
      this.key = inKey;
      this.value = inValue;
    }

    ValObj getValue() {
      return this.value;
    }

    String getKey() {
      return this.key;
    }

    Node getPrev() {
      return this.prev;
    }

    Node getNext() {
      return this.next;
    }

    void setValue(final ValObj val) {
      this.value = val;
    }

    void setKey(final String newKey) {
      this.key = newKey;
    }

    void setPrev(final Node newPrev) {
      this.prev = newPrev;
    }

    void setNext(final Node newNext) {
      this.next = newNext;
    }
  }
}
