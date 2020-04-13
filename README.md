# Java 14 Records :: did we live to see getters and setters die?

## Is it the end of the line for getters, setters and Lombok project?


![](https://miro.medium.com/max/6317/0*fU0S3kOcMHJtaLcv)

Photo by  [Mike Kenneally](https://unsplash.com/@asthetik?utm_source=medium&utm_medium=referral)  on  [Unsplash](https://unsplash.com/?utm_source=medium&utm_medium=referral)

If you try to teach Java to a  _millennium_  or to someone coming from another less verbose language, you can often face resistance regarding the writing of getters, setters, toString, equals and hashCode methods and the need of so much boilerplate code.

Vintage developers will not bother about it. We simply got used to instructing the IDE to generate them for us.

But let’s face it: we don’t particularly  _like_ them. We just _got used_  to them. We accepted the need for these methods, but to be honest: it could be different.

Java 14 has launched  **records**: a preview feature for a new type that dismisses the necessity of getters, setters, toString, equals and hashCode methods. It is possible to create an object with a few lines of code.

## How does it work?

Since it is yet a preview feature, we should enable preview features on our compiler, IDE and/or Maven.

In summary records have the following characteristics:

-   They are final and immutable.
-   They can implement interfaces.
-   They can have static members.
-   They can define validations.
-   They can define default values.
-   They accept generics.

I have tested it and the project is available on my  [GitHub](https://github.com/danianepg/records-demo).

![](https://miro.medium.com/max/831/1*yToge12v7yoO9aUTr4Eo0Q.png)

All passed! \o/

## Final and Immutable

Observe that records do not have the classic getters and setters and therefore, it is impossible to modify their values once they are assigned.

Note on the lines 34 to 38 below that the reading of attributes, is done without the “get” prefix. We use the attribute’s name straight forward.

```java
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Objects;

import org.junit.Test;

import com.danianepg.previewfeature.data.NationalHoliday;
import com.danianepg.previewfeature.data.records.CelebrationGenericRecord;
import com.danianepg.previewfeature.data.records.SpecialDate;

/**
 * Test class to verify the records' functionalities.
 * @author Daniane P. Gomes
 *
 */
public class RecordsDemoTest {

	private String name = "My Bday";
	private Integer day = 20;
	private Month month = Month.OCTOBER;
	private LocalDateTime created = LocalDateTime.now();
	
	private String country = "Brazil";
	private String nationalHolidayName = "Independence Day";
	private Integer nationalHolidayDay = 7;
	private Month nationalHolidayMonth = Month.SEPTEMBER;

	@Test
	public void specialDateRecord_ok() {
		
		SpecialDate myBday = new SpecialDate(name, day, month, created);
		boolean isNameEquals = myBday.name().equals(name);
		boolean isDayEquals = myBday.day() == day;
		boolean isMonthEquals = myBday.month() == month;
		boolean isCreatedEquals = myBday.created().equals(created);

		assertTrue(isNameEquals && isDayEquals && isMonthEquals && isCreatedEquals);
	}

	@Test
	public void specialDateRecord_equals_ok() {
		SpecialDate myBday = new SpecialDate(name, day, month, created);
		SpecialDate myBdayCopy = new SpecialDate(name, day, month, created);
		assertTrue(myBday.equals(myBdayCopy));
	}

	@Test
	public void specialDateRecord_alternativeConstructor() {
		SpecialDate myBday = new SpecialDate(name, day, month);
		boolean isCreatedNotNull = !Objects.isNull(myBday.created());
		assertTrue(isCreatedNotNull);
	}


	@Test(expected = IllegalArgumentException.class)
	public void specialDateRecord_exceptionWhenDayOutOfTheRange() {
		try {
			new SpecialDate(name, 32, month);
		} catch (IllegalArgumentException e) {
			System.out.println("Message exception: "+e.getMessage());
			throw e;
		}
	}

	@Test
	public void celebrationGenericRecord_ok() {

		CelebrationGenericRecord<NationalHoliday> dateGenericClassic = new CelebrationGenericRecord<>(
				new NationalHoliday(country), nationalHolidayName, nationalHolidayDay, nationalHolidayMonth);
		
		boolean isInstanceOfNationalHoliday = dateGenericClassic.contents() instanceof NationalHoliday;
		boolean isCountryEquals = false;

		if (isInstanceOfNationalHoliday) {
			NationalHoliday dateClassic = (NationalHoliday) dateGenericClassic.contents();
			isCountryEquals = dateClassic.getCountry().equals(country);
		}
		
		assertTrue(isInstanceOfNationalHoliday && isCountryEquals);

	}

}
```

## Interfaces, Static Members, Validations, Default values

Records are compatible with our classic interfaces. Check the record “SpecialDate” that implements the interface “CelebrationInterface” and therefore overwrites the method “totalDates()”. Observe lines 13 and 48.

For an extra static member check line 18.

For a validation check line 28.

For a default value, check the alternative constructor on line 41, which assigns the current date and time for the created object, even if it is not informed during the record’s object creation.

```java
package com.danianepg.previewfeature.data.records;

import java.time.LocalDateTime;
import java.time.Month;

import com.danianepg.previewfeature.interfaces.CelebrationInterface;

/**
 * An example of a record implementing an interface, validating data, passing default values and using extra static attributes and methods.
 * @author Daniane P. Gomes
 *
 */
public record SpecialDate(String name, Integer day, Month month, LocalDateTime created) implements CelebrationInterface {

	/**
	 * Additional static members
	 */
	private static int totalDates;

	/**
	 * Define validations for the attributtes
	 * @param name
	 * @param day
	 * @param month
	 * @param created
	 */
	public SpecialDate {
		if (day < 1 || day > 31) {
			throw new IllegalArgumentException("Day must be on the interval 1-31.");
		}

		totalDates++;
	}

	/**
	 * Additional constructor, to assign a default value to attribute "created"
	 * @param name
	 * @param day
	 * @param month
	 */
	public SpecialDate(String name, Integer day, Month month) {
		this(name, day, month, LocalDateTime.now());
	}

	/**
	 * Additional public method to retrieve 
	 */
	public int totalDates() {
		return totalDates;
	}

}
```

## Generics

Records are flexible and accept generics! The definition can be found below and the and example on how to use it is available on the test “celebrationGenericRecord_ok()” of class “[RecordsDemoTest](https://github.com/danianepg/records-demo/blob/master/tests/RecordsDemoTest.java)”.

```java
package com.danianepg.previewfeature.data.records;

import java.time.Month;

/**
 * Generic record
 * @author Daniane P. Gomes
 *
 * @param <T>
 */
public record CelebrationGenericRecord<T>(T contents, String name, Integer day, Month month) {
}
```

## Why is this useful?

Short answer: to carrier data.

Long answer: now we can declare a simple data transfer object with a few lines of code and without the need to satisfy all the ceremonies that we are used to having in Java, meaning CLEAN code!

Since records are compatible with other Java’s important features, I am really excited to start to use. It gives me much satisfaction to remove extra code on my projects. <o>

## Conclusion

Well then… is it the end of the line for our beloved-hateful pet methods getters and setters? Hmmm… Apparently not quite yet.

Since records are immutable they cannot simply replace our classic classes. Also, another concern to be addressed is how compatible are they with frameworks such as Hibernate and Spring?

They will, however, help us to make code  **cleaner** and  **smaller** and in some cases, they could even eliminate the need of external libraries such as Lombok, as stated Ben Evans for  [Java Magazine](https://blogs.oracle.com/javamagazine/records-come-to-java):

> “This will help many applications make domain classes clearer and smaller. It will also help teams eliminate many hand-coded implementations of the underlying pattern and reduce or remove the need for libraries like Lombok.”

It is important to keep in mind though, that this is a preview feature and as the [documentation reminds](https://docs.oracle.com/en/java/javase/14/docs/api/java.base/java/lang/Record.html):

> “Preview features may be removed in a future release, or upgraded to permanent features of the Java language.”

I hope they keep it. Fingers crossed.

## References

[**JEP 359: Records (Preview)**](https://openjdk.java.net/jeps/359 "https://openjdk.java.net/jeps/359") 

[**Record (Java SE 14 & JDK 14)**](https://docs.oracle.com/en/java/javase/14/docs/api/java.base/java/lang/Record.html "https://docs.oracle.com/en/java/javase/14/docs/api/java.base/java/lang/Record.html") 

[**Records Come to Java**](https://blogs.oracle.com/javamagazine/records-come-to-java "https://blogs.oracle.com/javamagazine/records-come-to-java")

Originally posted on [my Medium page](https://medium.com/@danianepg/java-14-records-did-we-live-to-see-getters-and-setters-die-b23f4cb0495a?source=friends_link&sk=717c3ed5f0b19e52d399339194f8346b).
