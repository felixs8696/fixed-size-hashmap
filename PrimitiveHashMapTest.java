import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class PrimitiveHashMapTest {
	private static final float EPSILON = .0001f;
	@Test
	public void testGetAndSet() {
		long startTime = System.nanoTime(); 

		PrimitiveHashMap<String> temp = new PrimitiveHashMap<String>();
		PrimitiveHashMap<String> testHashMap = temp.constructor(10);
		testHashMap.set("K", "Kleiner");
		testHashMap.set("P", "Perkins");
		testHashMap.set("C", "Caufield");
		testHashMap.set("B", "Byers");
		assertEquals("Kleiner", testHashMap.get("K"));
		assertEquals("Perkins", testHashMap.get("P"));
		assertEquals("Caufield", testHashMap.get("C"));
		assertEquals("Byers", testHashMap.get("B"));
		long endTime = System.nanoTime();

		double duration = (endTime - startTime)/1000000.0;
		System.out.println("testGetAndSet() took " + duration + "ms to complete");
	}

	@Test
	public void testFixedSizeContraint() {
		long startTime = System.nanoTime(); 

		PrimitiveHashMap<String> temp = new PrimitiveHashMap<String>();
		PrimitiveHashMap<String> testHashMap = temp.constructor(1);
		testHashMap.set("K", "Kleiner");
		assertFalse(testHashMap.set("P", "Perkins"));
		long endTime = System.nanoTime();

		double duration = (endTime - startTime)/1000000.0;
		System.out.println("testFixedSizeContraint() took " + duration + "ms to complete");
	}

	@Test
	public void testLargeSmallMapGet() {
		long startTime = System.nanoTime();
		PrimitiveHashMap<Integer> temp = new PrimitiveHashMap<Integer>();
		PrimitiveHashMap<Integer> largeMap = temp.constructor(100000);
		for (int i = 0; i < 100000; i++) {
			largeMap.set(String.valueOf(i), i);
		}
		long largeGetStartTime = System.nanoTime();
		assertEquals((Object) 1, largeMap.get("1"));
		long largeGetEndTime = System.nanoTime();
		double largeGetDuration = (largeGetEndTime - largeGetStartTime)/1000000.0;

		PrimitiveHashMap<Integer> smallMap = temp.constructor(10);
		for (int i = 0; i < 5; i++) {
			smallMap.set(String.valueOf(i), i);
		}
		long smallGetStartTime = System.nanoTime();
		assertEquals((Object) 1, smallMap.get("1"));
		long smallGetEndTime = System.nanoTime();
		double smallGetDuration = (smallGetEndTime - smallGetStartTime)/1000000.0;
		assertTrue(largeGetDuration-smallGetDuration < .5);

		long endTime = System.nanoTime();
		double duration = (endTime - startTime)/1000000.0;
		System.out.println("testLargeSmallMapGet() took " + duration + "ms to complete");
		System.out.println("testLargeSmallMapGet()-smallGet: " + smallGetDuration + "ms");
		System.out.println("testLargeSmallMapGet()-largeGet: " + largeGetDuration + "ms");
	}

	@Test
	public void testGetNull() {
		long startTime = System.nanoTime(); 

		PrimitiveHashMap<String> temp = new PrimitiveHashMap<String>();
		PrimitiveHashMap<String> testHashMap = temp.constructor(2);
		testHashMap.set("K", "Kleiner");
		assertNull(testHashMap.get("P"));
		long endTime = System.nanoTime();

		double duration = (endTime - startTime)/1000000.0;
		System.out.println("testGetNull() took " + duration + "ms to complete");
	}

	@Test
	public void testDeleteNull() {
		long startTime = System.nanoTime(); 

		PrimitiveHashMap<String> temp = new PrimitiveHashMap<String>();
		PrimitiveHashMap<String> testHashMap = temp.constructor(2);
		testHashMap.set("K", "Kleiner");
		assertNull(testHashMap.delete("P"));
		long endTime = System.nanoTime();

		double duration = (endTime - startTime)/1000000.0;
		System.out.println("testDeleteNull() took " + duration + "ms to complete");
	}

	@Test
	public void testNormalDelete() {
		long startTime = System.nanoTime(); 

		PrimitiveHashMap<String> temp = new PrimitiveHashMap<String>();
		PrimitiveHashMap<String> testHashMap = temp.constructor(2);
		testHashMap.set("K", "Kleiner");
		testHashMap.set("P", "Perkins");
		assertEquals("Perkins", testHashMap.get("P"));
		assertEquals("Perkins", testHashMap.delete("P"));
		assertNull(testHashMap.get("P"));
		long endTime = System.nanoTime();

		double duration = (endTime - startTime)/1000000.0;
		System.out.println("testNormalDelete() took " + duration + "ms to complete");
	}

	@Test
	public void testNormalLoadFactor() {
		long startTime = System.nanoTime();
		PrimitiveHashMap<Integer> temp = new PrimitiveHashMap<Integer>();
		PrimitiveHashMap<Integer> largeMap = temp.constructor(10);
		for (int i = 0; i < 5; i++) {
			largeMap.set(String.valueOf(i), i);
		}
		assertTrue(largeMap.load() <= 1);

		long endTime = System.nanoTime();
		double duration = (endTime - startTime)/1000000.0;
		System.out.println("testCorrectLoadFactor() took " + duration + "ms to complete");
	}

	@Test
	public void testExactLoadFactor() {
		long startTime = System.nanoTime();
		PrimitiveHashMap<Integer> temp = new PrimitiveHashMap<Integer>();
		PrimitiveHashMap<Integer> hashmap = temp.constructor(10);
		for (int i = 0; i < 5; i++) {
			hashmap.set(String.valueOf(i), i);
		}
		assertEquals(.5, hashmap.load(), EPSILON);

		long endTime = System.nanoTime();
		double duration = (endTime - startTime)/1000000.0;
		System.out.println("testExactLoadFactor() took " + duration + "ms to complete");
	}

	@Test
	public void testOverflowLoadFactor() {
		long startTime = System.nanoTime();
		PrimitiveHashMap<Integer> temp = new PrimitiveHashMap<Integer>();
		PrimitiveHashMap<Integer> largeMap = temp.constructor(10);
		for (int i = 0; i < 12; i++) {
			largeMap.set(String.valueOf(i), i);
		}
		assertTrue(largeMap.load() <= 1);

		long endTime = System.nanoTime();
		double duration = (endTime - startTime)/1000000.0;
		System.out.println("testOverflowLoadFactor() took " + duration + "ms to complete");
	}

	@Test
	public void testOverrideValues() {
		long startTime = System.nanoTime(); 

		PrimitiveHashMap<String> temp = new PrimitiveHashMap<String>();
		PrimitiveHashMap<String> testHashMap = temp.constructor(10);
		testHashMap.set("K", "Kleiner");
		testHashMap.set("B", "Byers");
		assertEquals("Kleiner", testHashMap.get("K"));
		assertEquals("Byers", testHashMap.get("B"));
		testHashMap.set("K", "Kevin");
		testHashMap.set("B", "Bacon");
		assertEquals("Kevin", testHashMap.get("K"));
		assertEquals("Bacon", testHashMap.get("B"));
		long endTime = System.nanoTime();

		double duration = (endTime - startTime)/1000000.0;
		System.out.println("testOverrideValues() took " + duration + "ms to complete");
	}

	//Uncomment linkedListSize() method in PrimitiveHashMap.java to successfully run this test.
	@Test
	public void testDuplicateValues() {
		long startTime = System.nanoTime(); 

		PrimitiveHashMap temp = new PrimitiveHashMap<String>();
		PrimitiveHashMap testHashMap = temp.constructor(10);
		testHashMap.set("K", "Kleiner");
		testHashMap.set("K", "Kleiner");
		testHashMap.set("K", "Kleiner");
		testHashMap.set("K", "Kleiner");
		assertEquals(1, testHashMap.linkedListSize("K"));
		long endTime = System.nanoTime();

		double duration = (endTime - startTime)/1000000.0;
		System.out.println("testOverrideValues() took " + duration + "ms to complete");
	}

	@Test
	public void testSuperLargeConstructor() {
		long startTime = System.nanoTime(); 

		PrimitiveHashMap temp = new PrimitiveHashMap<String>();
		PrimitiveHashMap testHashMap = temp.constructor((int) (Math.pow(2,31) + 1));
		testHashMap.set("K", "Kleiner");
		assertEquals("Kleiner", testHashMap.get("K"));
		long endTime = System.nanoTime();
		double duration = (endTime - startTime)/1000000.0;
		System.out.println("testSuperLargeConstructor() took " + duration + "ms to complete");
	}

	public static void main(String[] args) {
      Result result = JUnitCore.runClasses(PrimitiveHashMapTest.class);
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      System.out.println("All Tests Passed: " + result.wasSuccessful());
   }
}