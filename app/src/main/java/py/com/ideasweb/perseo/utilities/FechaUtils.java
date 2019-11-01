package py.com.ideasweb.perseo.utilities;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class FechaUtils {

	private Locale _currentLocale;

	public static Integer getDia(Date fecha) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		Integer dia_ = calendar.get(Calendar.DAY_OF_MONTH);
		return dia_;
	}


	/*Retorna 1 Domingo y 7 Sabado*/
	public static Integer getDiaSemana(Date fecha) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		Integer dia_ = calendar.get(Calendar.DAY_OF_WEEK);
		return dia_;
	}

	public static String getDiaSemanaName(Date fecha) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		return new SimpleDateFormat("EEEE", new Locale("es", "ES")).format(calendar.getTime());
	}

	public static Integer getMes(Date fecha) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		int mes_ = calendar.get(Calendar.MONTH) + 1;
		return mes_;
	}

	public static String getMesName(Date fecha) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		return  new SimpleDateFormat("MMMM", new Locale("es", "ES")).format(calendar.getTime());

	}


	public static String getHora(Date fecha) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		return  new SimpleDateFormat("HH:mm").format(calendar.getTime());

	}


	public static Integer getAnio(Date fecha) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		int anio_ = calendar.get(Calendar.YEAR);
		return anio_;
	}

	public static Boolean getSabadosDomingo(Date fecha){

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(fecha);
		if (gc.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || gc.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			return true;
		}else {
			return false;
		}


	}

	public Calendar diaHabilSiguiente(Date fecha, int dia) throws Exception {
		_currentLocale = new Locale("es", "PY");
		Calendar calFin = Calendar.getInstance(_currentLocale);
		try {

			/**
			 * Define el LOCALE
			 */
			Locale locale = Locale.getDefault();
			locale = new Locale("es", "PY");
			Locale.setDefault(locale);

			calFin.setTime(fecha);
			calFin.add(Calendar.DATE, dia);

			boolean b = true;

			while (b) {
				// Validación de sábado o domingo.
				if (calFin.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					b = true;
					// Esta instrucción incrementa la fecha por un día y no
					// importa si está al fin de mes o al fin de año, siempre lo
					// hace bien.
					calFin.add(Calendar.DATE, 1);
				} else {
					b = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return calFin;
	}

	/**
	 * Devuelve el tiempo transcurrido entre 2 fechas
	 *
	 * @return long
	 * @param
	 *            campo Los valores de campo pueden ser: - 1: Días
	 *            transcurridos. - 2: Meses transcurridos. - 3: Años
	 *            transcurridos - 4: Horas transcurridas - 5: Segundos
	 *            transcurridos
	 */
	public static long getTiempoTranscurrido(Date hasta, Date desde, int campo) throws Exception {
		long milisegundos = desde.getTime();
		long milisegundosactuales = hasta.getTime();
		long diferencia = milisegundosactuales - milisegundos;

		// if (diferencia < 0){
		// diferencia = diferencia * -1;
		// }

		switch (campo) {
			//dias
		case 1: {
			diferencia /= 1000;
			diferencia /= 60;
			diferencia /= 60;
			diferencia /= 24;
			return diferencia;
		}
		case 2: {
			diferencia /= 1000;
			diferencia /= 60;
			diferencia /= 60;
			diferencia /= 24;
			diferencia /= 30;
			return diferencia;
		}
		case 3: {
			diferencia /= 1000;
			diferencia /= 60;
			diferencia /= 60;
			diferencia /= 24;
			diferencia /= 365;
			return diferencia;
		}
		case 4: {
			diferencia /= 1000;
			diferencia /= 60;
			diferencia /= 60;
			return diferencia;
		}
		case 5: {
			diferencia /= 1000;
			diferencia /= 60;
			return diferencia;
		}
		case 6: {
			diferencia /= 1000;
			return diferencia;
		}
		}
		return diferencia;
	}

	public static void main(String[] args) {

	}

	public static int edad(Date ahora, Date nacimiento) {
		return getEdad(ahora, nacimiento);
	}

	public static Date getDate(String src, String format) {
		Date d = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			d = new Date(sdf.parse(src).getTime());
		} catch (ParseException e) {
			return null;
		}
		return d;
	}

	/**
	 * Obtiene la edad
	 *
	 * @return
	 */
	public static int getEdad(Date ahora, Date nacimiento) {
		GregorianCalendar nacimientoCal = new GregorianCalendar();
		nacimientoCal.setTimeInMillis(nacimiento.getTime());
		GregorianCalendar ahoraCal = new GregorianCalendar();
		ahoraCal.setTimeInMillis(ahora.getTime());
		GregorianCalendar x = new GregorianCalendar();
		x.setTimeInMillis(ahoraCal.getTimeInMillis() - nacimientoCal.getTimeInMillis());
		Double ed = x.getTimeInMillis() / 1000.0 / 60.0 / 60.0 / 24.0 / 365.25;
		int edad = ed.intValue();
		return edad;
	}

	public static final String obtenerDifEntreHoras(Timestamp desde, Timestamp hasta) {
		String value = "";

		long diferenciaMils = hasta.getTime() - desde.getTime();

		// obtenemos los segundos
		long segundos = diferenciaMils / 1000;

		// obtenemos las horas
		long horas = segundos / 3600;

		// restamos las horas para continuar con minutos
		segundos -= horas * 3600;

		// igual que el paso anterior
		long minutos = segundos / 60;
		segundos -= minutos * 60;
		value = horas + ":" + minutos + ":" + segundos;

		return value;
	}

	//
	public static Boolean isBirthDay(Date fecha, Date today){
		if(getMes(fecha) == getMes(today)){
			// mismo mes
			if(getDia(fecha) == getDia(today)){
				// mismo dia
				return true;
			}
		}

		return false;
	}

	public static Date convertUtilToSql(java.util.Date uDate) {

		Date sDate = new Date(uDate.getTime());

		return sDate;

	}

}
