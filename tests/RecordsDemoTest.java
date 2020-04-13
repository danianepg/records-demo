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
