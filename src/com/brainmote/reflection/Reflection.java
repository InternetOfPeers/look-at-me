package com.brainmote.reflection;

import java.lang.reflect.InvocationTargetException;

public class Reflection {

	/**
	 * Invoca un metodo sull'istanza dell'oggetto passato. Il metodo viene
	 * invocato senza parametri e viene restituito l'oggetto di ritorno previsto
	 * dal metodo. ATTENZIONE: poiché questo metodo restiutisce null in caso di
	 * errore, non usarlo con metodi per i quali null è un valore di ritorno
	 * ammissibile, nel qual caso non si potrebbe distinguere un errore dalla
	 * corretta esecuzione
	 * 
	 * @param instance
	 *            istanza dell'oggetto sul quale invocare il metodo
	 * @param methodName
	 *            nome del metodo da invocare
	 * @return l'oggetto di ritorno previsto dal metodo invocato. Viene
	 *         restituito null in caso di errore, quindi
	 */
	public static Object invokeMethod_NoParameters_ReturnObject(Object instance, String methodName) {
		try {
			return instance.getClass().getMethod(methodName, (Class<Object>[]) null).invoke(instance, (Object[]) null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

}
