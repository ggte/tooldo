/**
 *
 */
package net.escritoriodigital.unicamp.redefor.utils.common;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import net.objectlab.kit.datecalc.common.DateCalculator;
import net.objectlab.kit.datecalc.common.DefaultHolidayCalendar;
import net.objectlab.kit.datecalc.common.HolidayCalendar;
import net.objectlab.kit.datecalc.common.HolidayHandlerType;
import net.objectlab.kit.datecalc.joda.LocalDateKitCalculatorsFactory;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

/**
 * @author andre
 *
 */
public class DateUtils implements Serializable {

	private static final long serialVersionUID = -1320565560797343478L;

	/**
	 * Adiciona uma quantidade de dias corridos a uma data
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDaysToDate(Date date, int days) {
		DateTime dt = new DateTime(date).plusDays(days);
		return dt.toDate();
	}

	/**
	 * Retorna a quantidade de dias corridos entre duas datas
	 * @param start
	 * @param end
	 * @return
	 */
	public static Integer getDaysBetween(Date start, Date end) {
		DateTime dtStart = new DateTime(start);
		DateTime dtEnd = new DateTime(end);

		Days d = Days.daysBetween(dtStart, dtEnd);

		return d.getDays();
	}

	/**
	 * Adiciona dias úteis a data enviada como parâmetro, com base nos feriados e finais de semana
	 * @param date
	 * @param days
	 * @param holidays
	 * @return
	 */
	public static Date addBusinessDaysToDate(Date date, int days, Set<LocalDate> holidays) {

		DateTime dateTime = new DateTime(date);

		LocalDate localDate = new LocalDate(dateTime);

		final HolidayCalendar<LocalDate> calendar = new DefaultHolidayCalendar<LocalDate>(holidays, localDate, localDate.plusYears(2));

		LocalDateKitCalculatorsFactory.getDefaultInstance().registerHolidays("BR", calendar);

		DateCalculator<LocalDate> cal = LocalDateKitCalculatorsFactory.getDefaultInstance().getDateCalculator("BR", HolidayHandlerType.FORWARD);
		cal.setStartDate(localDate);

		LocalDate current = cal.moveByBusinessDays(days).getCurrentBusinessDate();

		return current.toDateTime(dateTime).toDate();
	}

	/**
	 * Conta o número de dias úteis entre duas datas
	 * @param start
	 * @param end
	 * @return
	 */
	public static Integer getWorkDaysBetween(Date start, Date end, Set<LocalDate> holidays) {

		DateTime dtStart = new DateTime(start);
		DateTime dtEnd = new DateTime(end);

		Integer result = 0;

		LocalDate localStart = new LocalDate(dtStart);
		LocalDate localEnd = new LocalDate(dtEnd);

		if(dtStart.compareTo(dtEnd) < 0) {

			final HolidayCalendar<LocalDate> calendar = new DefaultHolidayCalendar<LocalDate>(holidays, localStart, localEnd);

			LocalDateKitCalculatorsFactory.getDefaultInstance().registerHolidays("BR", calendar);

			DateCalculator<LocalDate> cal = LocalDateKitCalculatorsFactory.getDefaultInstance().getDateCalculator("BR", HolidayHandlerType.FORWARD);
			cal.setStartDate(localStart);

			LocalDate current = cal.getCurrentBusinessDate();

			while(current.compareTo(localEnd) < 0) {

				current = cal.moveByDays(1).getCurrentBusinessDate();

				result++;
				if(result > 1000) break;
			}

		} else if(dtStart.compareTo(dtEnd) > 0) {

			final HolidayCalendar<LocalDate> calendar = new DefaultHolidayCalendar<LocalDate>(holidays, localEnd, localStart);

			LocalDateKitCalculatorsFactory.getDefaultInstance().registerHolidays("BR", calendar);

			DateCalculator<LocalDate> cal = LocalDateKitCalculatorsFactory.getDefaultInstance().getDateCalculator("BR", HolidayHandlerType.FORWARD);
			cal.setStartDate(localEnd);

			LocalDate current = cal.getCurrentBusinessDate();

			while(current.compareTo(localStart) < 0) {

				current = cal.moveByDays(1).getCurrentBusinessDate();

				result--;
				if(result < -1000) break;
			}
		}

		return result;
	}

}
