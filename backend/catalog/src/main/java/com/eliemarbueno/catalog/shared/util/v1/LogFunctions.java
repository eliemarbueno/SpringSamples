package com.eliemarbueno.catalog.shared.util.v1;

public class LogFunctions {

	public static String getMethodWithClass() {
		    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
	        StackTraceElement caller = stackTrace[2]; //Considering Method as third Index
	        
	        return caller.getClassName() + '.' + caller.getMethodName(); 
	}
	
	public static String getMethod(int elementLevel) {
		if (elementLevel < 0) {
			return "";
		}

		var e = safeGetSourcePoint(Thread.currentThread(), elementLevel);
		return e != null ? e.getMethodName() + ": " : "";
	}

	public static String getMethod() {
		int i = 4;
		while (true) {
			String methodName = getMethod(i);
			if (!methodName.equals("getMethod") && !methodName.equals("doFilterInternal")) {
				return methodName;
			}
			i++;
		}
	}

	public static String getErrorMessageSlim(Exception e) {
		StackTraceElement[] stackTrace = e.getStackTrace();
        StackTraceElement caller = stackTrace[0]; //considering last place throws exception
        
        String className = caller.getClassName();
        String methodName = caller.getMethodName();
        String fileName = caller.getFileName();
        int lineNumber = caller.getLineNumber();
        
        return String.format("[Class: %s, Method: %s, File: %s, Line: %d] %s", className, methodName, fileName, lineNumber, e.getMessage());
	}
	
	public static String getErrorMessage(Exception ex) {
		StringBuilder message = new StringBuilder();
		int i = 5;
		message.append(getMethod(i)).append("\n\n");
		message.append("Exception: ");
		appendIfNotNull(message, "\nClass: ", ex.getClass());
		appendIfNotNull(message, "\nMessage: ", ex.getMessage());
		appendIfNotNull(message, "\nLocalizedMessage: ", ex.getLocalizedMessage());
		appendIfNotNull(message, "\nCause: ", ex.getCause());
		appendIfNotNull(message, "\nSuppressed: ", (Object) ex.getSuppressed());
		appendIfNotNull(message, "\nStackTrace: ", (Object) ex.getStackTrace());

		message.append("Getting ").append(i).append(" possible levels of source error:");
		for (int x = 0; x < i; x++) {
			var e = safeGetSourcePoint(ex, x);
			if (e != null) {
				message.append("\n\nError happened on: ");
				message.append("\nClassLoaderName: ").append(e.getClassLoaderName());
				message.append("\nClassName: ").append(e.getClassName());
				message.append("\nMethodName: ").append(e.getMethodName());
				message.append("\nLineNumber: ").append(e.getLineNumber());
			}
		}
		return message.toString();
	}

	private static StackTraceElement safeGetSourcePoint(Thread thread, int ref) {
		try {
			StackTraceElement[] stackTrace = thread.getStackTrace();
			return ref >= 0 && ref < stackTrace.length ? stackTrace[ref] : null;
		} catch (Exception e) {
			return null;
		}
	}

	private static StackTraceElement safeGetSourcePoint(Throwable throwable, int ref) {
		try {
			StackTraceElement[] stackTrace = throwable.getStackTrace();
			return ref >= 0 && ref < stackTrace.length ? stackTrace[ref] : null;
		} catch (Exception e) {
			return null;
		}
	}

	private static void appendIfNotNull(StringBuilder sb, String label, Object obj) {
		if (obj != null) {
			sb.append(label).append(obj);
		}
	}
}
